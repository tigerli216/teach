/*
 * OrderServiceImpl.java Created on 2017年9月26日 下午4:48:57
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

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.wst.constant.PayConstant.TradeSourceEnum;
import com.wst.constant.PayConstant.TradeStatusEnum;
import com.wst.constant.PayConstant.TradeTypeEnum;
import com.wst.constant.RedisConstant;
import com.wst.constant.UserConstant.UserLevelTypeEnmu;
import com.wst.lesson.dao.CustomDao;
import com.wst.mem.dao.UserDao;
import com.wst.order.dao.OrderDao;
import com.wst.order.dao.PayChnConfigDao;
import com.wst.order.dao.PaymentDao;
import com.wst.order.helper.OrderHelper;
import com.wst.service.mem.service.MssageService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.dto.PaymentDTO;
import com.wst.service.order.service.OrderService;
import com.wst.service.pay.service.PayResultService;

/**
 * 订单查询
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class OrderServiceImpl implements OrderService {

	@Resource
	private OrderDao orderDao;

	@Resource
	private PaymentDao paymentDao;

	@Resource
	private UserDao userDao;

	@Resource
	private MssageService mssageService;

	@Resource
	private CustomDao customDao;

	@Resource
	private PayChnConfigDao payChnConfigDao;

	@Resource
	private PayResultService payResultService;
	
	@Override
	public Page<OrderDTO> findOrderPaging(Page<OrderDTO> pageParam, OrderDTO order) throws Exception {
		if (StringUtils.isNotEmpty(order.getOrderName())) {
			order.setOrderName("%" + order.getOrderName() + "%");
		}
		return orderDao.findOrderPaging(pageParam, order);
	}

	@Override
	public OrderDTO getOrderByCode(String orderCode) throws Exception {
		return orderDao.getOrderByCode(orderCode);
	}

	@Override
	public ResultDTO<String> updatePayType(String orderCode, long payType) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		OrderDTO order = orderDao.getOrderByCode(orderCode);
		if (order == null) {
		    return result.set(ResultCodeEnum.ERROR_HANDLE, "订单不存在");
		}
		
        // 不是线上转线下不需要关闭支付记录
        if (payType != 1) {
        	// 修改订单支付方式
            orderDao.updatePayType(orderCode, payType == 1 ? 2 : 1);
            return result.set(ResultCodeEnum.SUCCESS, "切换成功");
        }
        
        // 关闭支付记录
        payResultService.closePayment(order.getOrderCode());
        
        // 修改订单支付方式
        orderDao.updatePayType(orderCode, payType == 1 ? 2 : 1);

		return result.set(ResultCodeEnum.SUCCESS, "切换成功");
	}

	@Override
	public ResultDTO<String> orderPay(OrderDTO order, String notes, long status) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		/** ==生成支付记录== **/
		PaymentDTO payment = new PaymentDTO();
		// 线下
		payment.setTradeSource(TradeSourceEnum.DOWN_PAY.type);
		payment.setPayNo(OrderHelper.getTradeNo(9));
		payment.setOrderCode(order.getOrderCode());
		payment.setPayAmount(order.getDiscountPrice());
		// 消费
		payment.setTradeType(TradeTypeEnum.CONSUME.type);
		// 支付成功
		payment.setTradeStatus(TradeStatusEnum.PAY_SUCCESS.status);
		payment.setTradeExplain(notes);
		if (status == 2) {
			payment.setTradeStatus(TradeStatusEnum.PAY_FAIL.status);
		}
		paymentDao.save(payment);

		/** ===支付成功=== */
		if (status == 1) {
			// 更新订单表，支付记录表
			orderDao.paySuccess(order.getOrderCode());
			customDao.buyCustom(order.getBusiId());
			// 更改用户等级
			userDao.updateUserLevel(order.getUserId(), UserLevelTypeEnmu.VIP.type);
			mssageService.baseSendMssage(order.getUserId(), 0, order.getBusiId() + "", 2, 1);
			String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + order.getUserId();
			if (!RedisClient.exists(userLimitKey)) {
				RedisClient.set(userLimitKey, order.getUserId());
			}
		} else {
			/** ===支付失败=== */
			// 订单状态 4.退款
			orderDao.payFail(order.getOrderCode());
		}

		return result.set(ResultCodeEnum.SUCCESS, "success");
	}

}
