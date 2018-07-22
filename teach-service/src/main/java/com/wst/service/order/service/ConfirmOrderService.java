/*
 * ConfigOrderService.java Created on 2017年10月13日 下午2:59:05
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

import com.hiwi.common.dto.ResultDTO;
import com.wst.service.order.dto.OrderDTO;

/**
 * 订单相关操作
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public interface ConfirmOrderService {

	/**
	 * 创建网课订单
	 * 
	 * @param publicLessonId
	 *            网课课程id
	 * @param userId
	 *            用户
	 * @param amount
	 *            订单金额(分)
	 * @return
	 * @throws Exception
	 */
	ResultDTO<OrderDTO> createPublicLessonOrder(long publicLessonId, long userId, long amount) throws Exception;

	/**
	 * 创建定制课订单
	 * 
	 * @param lessonId
	 *            定制课程id
	 * @param payType
	 *            付款方式（1-线上；2-线下）
	 * @param notes
	 *            订单备注
	 * @return
	 * @throws Exception
	 */
	ResultDTO<OrderDTO> createCustomLessonOrder(long lessonId, long payType, String notes) throws Exception;

	/**
	 * 取消订单
	 * 
	 * @param userId
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	ResultDTO<String> cancel(long userId, String orderCode) throws Exception;
}
