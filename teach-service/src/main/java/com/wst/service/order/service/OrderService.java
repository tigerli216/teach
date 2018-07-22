/*
 * OrderService.java Created on 2017年9月26日 下午4:41:49
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
package com.wst.service.order.service;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.order.dto.OrderDTO;

/**
 * 订单查询相关service 用于订单信息查询 相关数据返回
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public interface OrderService {
	/**
	 * 修改支付方式
	 * 
	 * @param orderCode
	 * @param payType
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> updatePayType(String orderCode, long payType) throws Exception;

	/**
	 * 查询订单分页数据
	 * 
	 * @param pageParam
	 *            分页参数
	 * @param order
	 *            订单相关查询参数
	 * @return 只返回订单主体数据
	 * @throws Exception
	 */
	public Page<OrderDTO> findOrderPaging(Page<OrderDTO> pageParam, OrderDTO order) throws Exception;

	/**
	 * 根据订单号查询订单信息
	 * 
	 * @param orderCode
	 * @return 订单单表信息
	 * @throws Exception
	 */
	public OrderDTO getOrderByCode(String orderCode) throws Exception;

	/**
	 * 支付成功（后台）
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> orderPay(OrderDTO order, String notes, long status) throws Exception;
}
