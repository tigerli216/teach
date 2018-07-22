/*
 * PaymentServiceImpl.java Created on 2017年10月12日 下午3:36:27
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

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.wst.order.dao.PaymentDao;
import com.wst.service.order.dto.PaymentDTO;
import com.wst.service.order.service.PaymentService;

/**
 * 支付记录
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentDao paymentDao;

    @Override
    public Page<PaymentDTO> pageFindPayment(Page<PaymentDTO> pageParam, PaymentDTO paymentDTO) throws Exception {
        return paymentDao.pageFindPayment(pageParam, paymentDTO);
    }

    @Override
    public PaymentDTO getById(long payId) throws Exception {
        return paymentDao.getPaymentById(payId);
    }

    @Override
    public PaymentDTO getByPayNo(String payNo) throws Exception {
        return paymentDao.getPaymentByPayNo(payNo);
    }

    @Override
    public List<PaymentDTO> getByorderCode(String orderCode, long tradeType) throws Exception {
        return paymentDao.getPayments(orderCode, tradeType);
    }

}
