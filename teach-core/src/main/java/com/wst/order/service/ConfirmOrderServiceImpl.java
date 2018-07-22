/*
 * ConfigOrderServiceImpl.java Created on 2017年10月13日 下午3:05:36
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
package com.wst.order.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.constant.LessonCustomConstant.LessonCustomTypeEnmu;
import com.wst.constant.LessonPublicConstant.LessonPublicTypeEnmu;
import com.wst.constant.LessonPublicConstant.ShelfStatusEnum;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderSubTypeEnmu;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.constant.UserConstant.StatusEnum;
import com.wst.lesson.dao.CustomDao;
import com.wst.lesson.dao.PublicDao;
import com.wst.mem.dao.UserDao;
import com.wst.order.dao.OrderDao;
import com.wst.order.dao.PaymentDao;
import com.wst.order.helper.OrderHelper;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.service.ConfirmOrderService;

/**
 * 订单相关操作
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class ConfirmOrderServiceImpl implements ConfirmOrderService {

	@Resource
	private OrderDao orderDao;
	@Resource
	private PublicDao publicDao;
	@Resource
	private UserDao userDao;
	@Resource
	private CustomDao customDao;
	@Resource
	private PaymentDao paymentDao;
	@Resource
	private MssageService mssageService;

	@Override
	public ResultDTO<OrderDTO> createPublicLessonOrder(long publicLessonId, long userId, long amount) throws Exception {
		ResultDTO<OrderDTO> result = new ResultDTO<>();
		UserDTO user = userDao.getUserById(userId);
		if (user == null) {
			// 用户不存在
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User does not exist");
		}
		PublicDTO publicLesson = publicDao.getLessonById(publicLessonId);
		if (publicLesson == null) {
			// 课程不存在
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Curriculum does not exist");
		}
		if (publicLesson.getShelfStatus() != ShelfStatusEnum.UP.status) {
			// 课程已下架
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The course is off the shelf");
		}
		long lessonPrice = publicLesson.getDiscountPrice();
		if (lessonPrice != amount) {
			// 订单金额不正确
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The order amount is incorrect");
		}

		// 校验订单是否已创建
		OrderDTO orderDTO = orderDao.getByLessonIdUserId(userId, publicLessonId, 1);
		if (orderDTO != null && orderDTO.getOrderStatus() == OrderStatusEnum.HAS_CONFIRM.status) {
			// 已确认 未付款
			if (orderDTO.getPayStatus() == PayStatusEnum.NO_PAY.status) {
				return result.set(ResultCodeEnum.ERROR_HANDLE, "The course has been placed. Please pay it");
			} else if (orderDTO.getPayStatus() == PayStatusEnum.HAS_PAY.status
					&& orderDTO.getValidTime().after(new Date())) { // 已确认 已付款
																	// 且在有效期内
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Courses purchased");
			}
		}

		// 生成订单数据
		OrderDTO order = new OrderDTO();
		order.setOrderCode(OrderHelper.getOrderCode(OrderTypeEnmu.PUBLIC_CLASS_ORDER.type));
		// 订单名称==课程名称
		order.setOrderName(publicLesson.getLessonName());
		order.setOrderType(OrderTypeEnmu.PUBLIC_CLASS_ORDER.type);
		long lessonType = publicLesson.getLessonType();
		// 录播课
		if (lessonType == LessonPublicTypeEnmu.VIDEO_PLAY_CLASS.type) {
			order.setOrderSubType(OrderSubTypeEnmu.VIDEO_PLAY_CLASS.type);
		} else if (lessonType == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type) { // 直播课
			order.setOrderSubType(OrderSubTypeEnmu.LIVE_PLAY_CLASS.type);
		}
		order.setUserId(user.getUserId());
		order.setBusiId(publicLesson.getLessonId());
		// 原价
		order.setOrigPrice(publicLesson.getOrigPrice());
		// 折扣价
		order.setDiscountPrice(publicLesson.getDiscountPrice());
		// 线上
		order.setPayType(OrderPayTypeEnum.ON_PAY.type);
		// 已确认
		order.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);
		// 未付款
		order.setPayStatus(PayStatusEnum.NO_PAY.status);
		Calendar c = Calendar.getInstance();

		// 把日期往后增加n个月
		c.add(Calendar.MONTH, publicLesson.getValidDuration());
		order.setValidTime(c.getTime());
		orderDao.save(order);

		return result.set(ResultCodeEnum.SUCCESS, "", order);
	}

	@Override
	public ResultDTO<OrderDTO> createCustomLessonOrder(long lessonId, long payType, String notes) throws Exception {
		ResultDTO<OrderDTO> resultDTO = new ResultDTO<>();
		CustomDTO customDTO = customDao.getLessonById(lessonId);
		if (customDTO == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "课程不存在");
		}
		if (customDTO.getBuyTime() != null) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "课程已售");
		}
		Date date = new Date();
		if (customDTO.getEndTime().before(date)) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课程已结束");
		}
		UserDTO userDTO = userDao.getUserById(customDTO.getUserId());
		if (userDTO == null || userDTO.getStatus() != StatusEnum.HAS_EFFECTIVE.status) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "用户不合法");
		}

		OrderDTO orderDTO = orderDao.getByLessonIdUserId(customDTO.getUserId(), lessonId, 2);
		if (orderDTO != null) {
			if (orderDTO.getOrderStatus() == OrderStatusEnum.HAS_CONFIRM.status) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课程已下单");
			} else if (orderDTO.getOrderStatus() == OrderStatusEnum.HAS_CANCEL.status) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "订单已取消，请新建课程");
			} else if (orderDTO.getOrderStatus() == OrderStatusEnum.RETURN_MONEY.status) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课程已失效");
			}
		}

		OrderDTO order = new OrderDTO();
		order.setOrderCode(OrderHelper.getOrderCode(OrderTypeEnmu.CUSTOM_CLASS_ORDER.type));
		order.setOrderName(customDTO.getLessonName());
		order.setOrderType(OrderTypeEnmu.CUSTOM_CLASS_ORDER.type);
		order.setOrderSubType(customDTO.getLessonType() == LessonCustomTypeEnmu.CUSTOM_CLASS.type
				? OrderSubTypeEnmu.CUSTOM_CLASS.type : OrderSubTypeEnmu.INTERNSHIP_CLASS.type);
		order.setUserId(customDTO.getUserId());
		order.setBusiId(lessonId);
		order.setOrigPrice(customDTO.getLessonPrice());
		order.setDiscountPrice(customDTO.getLessonPrice());
		order.setPayType(payType);
		order.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);// 已确认
		order.setPayStatus(PayStatusEnum.NO_PAY.status);// 未付款
		order.setOrderNotes(notes);
		orderDao.save(order);

		// 线下支付创建支付记录
		/*
		 * PaymentDTO payment = new PaymentDTO();
		 * payment.setTradeSource(TradeSourceEnum.ON_PAY.type); // 线上 if
		 * (payType == OrderPayTypeEnum.DOWN_PAY.type) {
		 * payment.setTradeSource(TradeSourceEnum.DOWN_PAY.type); // 线下 }
		 * payment.setPayNo(OrderHelper.getTradeNo(9));
		 * payment.setOrderCode(order.getOrderCode());
		 * payment.setPayAmount(order.getDiscountPrice());
		 * payment.setTradeType(TradeTypeEnum.CONSUME.type); // 消费
		 * payment.setTradeStatus(TradeStatusEnum.PAY_IN.status);// 未支付
		 * paymentDao.save(payment);
		 */

		return resultDTO.set(ResultCodeEnum.SUCCESS, "", order);
	}

	@Override
	public ResultDTO<String> cancel(long userId, String orderCode) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		OrderDTO order = orderDao.getOrderByCode(orderCode);
		if (order == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Order does not exist");
		}

		if (order.getOrderType() != 1) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The order type is not an online order");
		}

		if (order.getOrderStatus() != 1 || order.getPayStatus() != 1) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The order status is not legal");
		}

		if (order.getUserId() != userId) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The order does not belong to this user");
		}

		orderDao.cancelOrder(orderCode);
		return result.set(ResultCodeEnum.SUCCESS, "success");
	}

}
