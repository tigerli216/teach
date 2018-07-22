/*
 * PaymentService.java Created on 2017年10月12日 下午3:33:45
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

import java.util.List;

import com.hiwi.common.dao.Page;
import com.wst.service.order.dto.PaymentDTO;

/**
 * 支付记录service
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public interface PaymentService {

    /**
     * 根据条件分页查询支付记录
     * 
     * @param pageParam
     * @param paymentDTO
     * @return
     * @throws Exception
     */
    Page<PaymentDTO> pageFindPayment(Page<PaymentDTO> pageParam, PaymentDTO paymentDTO) throws Exception;

    /**
     * 根据id查询支付记录
     * 
     * @param payId
     * @return
     * @throws Exception
     */
    PaymentDTO getById(long payId) throws Exception;

    /**
     * 根据交易流水号查询支付记录
     * 
     * @param payNo
     * @return
     * @throws Exception
     */
    PaymentDTO getByPayNo(String payNo) throws Exception;

    /**
     * 根据订单查询所有类型支付记录
     * 
     * @param orderCode
     *            订单号
     * @param tradeType
     *            交易类型：1-消费；2-退款；3-提现
     * @return
     * @throws Exception
     */
    List<PaymentDTO> getByorderCode(String orderCode, long tradeType) throws Exception;
}
