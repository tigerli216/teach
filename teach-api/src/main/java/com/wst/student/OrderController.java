/*
 * OrderController.java Created on 2017年10月11日 下午4:25:22
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.student;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.Globals;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderSubTypeEnmu;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.constant.RedisConstant;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.service.ConfirmOrderService;
import com.wst.service.order.service.OrderService;
import com.wst.service.order.service.PaymentService;
import com.wst.service.pay.dto.AppPaymentResp;
import com.wst.service.pay.service.PayResultService;

/**
 * 下单，支付控制器
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/API/student/order/")
public class OrderController {

	private static HiwiLog log = HiwiLogFactory.getLogger(OrderController.class);

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;
	@Reference(version = "0.0.1")
	private ConfirmOrderService confirmOrderService;
	@Reference(version = "0.0.1")
	private PayResultService payResultService;
	@Reference(version = "0.0.1")
	private OrderService orderService;
	@Reference(version = "0.0.1")
	private PaymentService paymentService;

	private Lock createLock = new ReentrantLock();// 创建订单锁
	private Lock payLock = new ReentrantLock();// 支付锁

	/**
	 * 确认订单(用于创建订单（下单，支付合并）)
	 * 
	 * 
	 * @param dto
	 * @param payAmount
	 *            订单金额（元）
	 * @param publicLessonId
	 *            网课课程id
	 * @param payType
	 *            支付方式 2-微信APP_Pay；3-微信JS_pay
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("confirmOrder")
	public ResultDTO<Map<String, Object>> confirmOrder(HttpServletRequest request, CommonParamsDTO dto,
			String payAmount, @RequestParam(defaultValue = "0") long publicLessonId,
			@RequestParam(defaultValue = "2") long payType) throws Exception {

		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		String limitKey = RedisConstant.LIMIT_ORDER_;
		boolean isNeedDelete = true; // 是否需要删除redis值
		try {
			if (publicLessonId <= 0) {
				// 请选择课程
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please choose the course", new HashMap<>());
			}
			long amountLong = Long.valueOf(AmountUtils.changeY2F(payAmount)); // 支付金额
			if (amountLong <= 0) {
				// 订单金额不正确
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The order amount is incorrect",
						new HashMap<>());
			}
			if (payType != 2 && payType != 3) {
				// 支付方式不正确
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Payment method is incorrect", new HashMap<>());
			}
			UserDTO user = UserHelper.getUserDTO(dto);
			if (user == null) {
				return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
			}
			limitKey += user.getUserId();
			try {
				createLock.lock();
				if (RedisClient.exists(limitKey)) {
					isNeedDelete = false;
					// 请勿频繁请求
					return result.set(ResultCodeEnum.ERROR_REQ_OFTEN, "Please do not request frequently",
							new HashMap<String, Object>());
				}
				RedisClient.set(limitKey, publicLessonId);

			} finally {
				createLock.unlock();
			}
			// 下单
			ResultDTO<OrderDTO> resultDTO = confirmOrderService.createPublicLessonOrder(publicLessonId,
					user.getUserId(), amountLong);
			if (!resultDTO.isSuccess()) {
				return result.set(resultDTO.getErrCode(), resultDTO.getErrDesc(), new HashMap<>());
			}
			OrderDTO orderDTO = resultDTO.getResult();
			// 支付
			String reqIp = Globals.getRealIP(request); // 客户端IP地址
			ResultDTO<AppPaymentResp> payResult = payResultService.prePayment(orderDTO.getOrderCode(), user, amountLong,
					payType, reqIp);

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("orderCode", orderDTO.getOrderCode());
			if (!payResult.isSuccess()) {
				return result.set(resultDTO.getErrCode(), resultDTO.getErrDesc(), resultMap);
			}

			AppPaymentResp appPaymentResp = payResult.getResult();
			resultMap.put("appid", appPaymentResp.getAppId());
			resultMap.put("noncestr", appPaymentResp.getNonceStr());
			resultMap.put("package", appPaymentResp.getPackageValue());
			resultMap.put("partnerid", appPaymentResp.getPartnerId());
			resultMap.put("prepayid", appPaymentResp.getPrepayId());
			resultMap.put("sign", appPaymentResp.getSign());
			resultMap.put("timestamp", appPaymentResp.getTimeStamp());

			return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
		} catch (Exception e) {
			log.error("=======确认订单接口异常==============", e);
			// 请稍后重试
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Please try again later", new HashMap<String, Object>());
		} finally {
			if (isNeedDelete) {
				// 删除频繁请求key
				RedisClient.del(limitKey);
			}
		}
	}

	/**
	 * 确认支付
	 * 
	 * @param dto
	 * @param orderCode
	 *            订单编码
	 * @param payAmount
	 *            支付金额
	 * @param payType
	 *            支付方式 2-微信APP_Pay；3-微信JS_pay；9-线下
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("confirmPay")
	public ResultDTO<Map<String, Object>> confirmPay(HttpServletRequest request, CommonParamsDTO dto, String orderCode,
			@RequestParam(defaultValue = "0") String payAmount, @RequestParam(defaultValue = "2") long payType)
			throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		String limitPayKey = RedisConstant.LIMIT_PAY_; // 限制频繁支付的key；
		boolean isDelPayKey = true; // 是否删除key
		try {
			if (StringUtils.isEmpty(orderCode)) {
				// 订单编码不能为空
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The order code cannot be empty",
						new HashMap<>());
			}
			long amoutLong = Long.parseLong(AmountUtils.changeY2F(payAmount));
			if (amoutLong <= 0) {
				// 支付金额不正确
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The pay amount is incorrect", new HashMap<>());
			}
			if (payType != 2 && payType != 3 && payType != 9) {
				// 支付方式不正确
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Payment method is incorrect", new HashMap<>());
			}
			UserDTO user = UserHelper.getUserDTO(dto);
			if (user == null) {
				return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
			}
			OrderDTO orderDTO = orderService.getOrderByCode(orderCode);
			if (orderDTO == null) {
				// 订单不存在
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Order does not exist");
			}
			if (orderDTO.getUserId() != user.getUserId()) {
				// 用户不合法
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal");
			}
			if (orderDTO.getOrderStatus() != OrderStatusEnum.HAS_CONFIRM.status
					|| orderDTO.getPayStatus() != PayStatusEnum.NO_PAY.status) {
				// 订单不合法
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Order illegal");
			}
			if (amoutLong != orderDTO.getDiscountPrice()) {
				// 支付金额不正确
				return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The amount of payment is incorrect");
			}
			try {
				payLock.lock();
				if (RedisClient.exists(limitPayKey)) {
					isDelPayKey = false;
					// 请勿频繁请求
					return result.set(ResultCodeEnum.ERROR_REQ_OFTEN, "Please do not request frequently",
							new HashMap<String, Object>());
				}
				RedisClient.set(limitPayKey, orderCode);
			} finally {
				payLock.unlock();
			}
			// 线下支付
			if (payType == 9) {
				if (orderDTO.getPayType() != OrderPayTypeEnum.DOWN_PAY.type
						|| orderDTO.getOrderType() != OrderTypeEnmu.CUSTOM_CLASS_ORDER.type
						|| (orderDTO.getOrderSubType() != OrderSubTypeEnmu.CUSTOM_CLASS.type
								&& orderDTO.getOrderSubType() != OrderSubTypeEnmu.INTERNSHIP_CLASS.type)) {
					// 非定制课订单，不能线下支付
					return result.set(ResultCodeEnum.ERROR_HANDLE, "Non customized orders can not be paid offline",
							new HashMap<String, Object>());
				}
				ResultDTO<String> payResult = payResultService.offlinePayHandle(orderCode, true);
				return result.set(payResult.getErrCode(), payResult.getErrDesc());
			}

			// 校验是否有需要关闭的支付记录
			ResultDTO<String> clodeOrderResult = payResultService.closePayment(orderDTO.getOrderCode());
			if (!clodeOrderResult.isSuccess()) {
				log.debug("关闭订单失败，订单号=" + orderCode + ",原因：" + clodeOrderResult.getErrDesc());
				return result.set(clodeOrderResult.getErrCode(), "Closing order failed", new HashMap<>());
			}

			String reqIp = Globals.getRealIP(request); // 客户端IP地址
			ResultDTO<AppPaymentResp> resultDTO = payResultService.prePayment(orderCode, user, amoutLong, payType,
					reqIp);
			if (!resultDTO.isSuccess()) {
				return result.set(resultDTO.getErrCode(), resultDTO.getErrDesc(), new HashMap<>());
			}
			AppPaymentResp appPaymentResp = resultDTO.getResult();
			log.debug("------预支付返回参数=" + JSONObject.toJSONString(appPaymentResp));
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("appid", appPaymentResp.getAppId());
			resultMap.put("noncestr", appPaymentResp.getNonceStr());
			resultMap.put("package", appPaymentResp.getPackageValue());
			resultMap.put("partnerid", appPaymentResp.getPartnerId());
			resultMap.put("prepayid", appPaymentResp.getPrepayId());
			resultMap.put("sign", appPaymentResp.getSign());
			resultMap.put("timestamp", appPaymentResp.getTimeStamp());
			return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
		} catch (Exception e) {
			log.error("=======确认支付接口异常==============", e);
			// 请稍后重试
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Please try again later", new HashMap<String, Object>());
		} finally {
			if (isDelPayKey) {
				RedisClient.del(limitPayKey);
			}
		}
	}

	/**
	 * 取消订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "cancel")
	public ResultDTO<String> cancel(CommonParamsDTO dto, String orderCode) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		// 参数校验
		if (StringUtils.isEmpty(orderCode)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The order encoding cannot be empty");
		}

		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "The user does not exist");
		}

		return result = confirmOrderService.cancel(user.getUserId(), orderCode);
	}

}
