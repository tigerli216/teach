/*
 * PayResultService.java Created on 2017年10月13日 上午11:26:41
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
package com.wst.service.pay.service;

import com.hiwi.common.dto.ResultDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.pay.dto.AppPaymentResp;

/**
 * 支付处理service
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public interface PayResultService {

    /**
     * 发起支付
     * 
     * @param orderCode
     *            订单编码
     * @param user
     *            用户
     * @param payAmount
     *            支付金额
     * @param payType
     *            支付方式（1-微信；2-支付宝）
     * @param reqIp
     *            客户端ip（微信支付需要）
     * @return
     * @throws Exception
     */
    ResultDTO<AppPaymentResp> prePayment(String orderCode, UserDTO user, long payAmount, long payType, String reqIp)
            throws Exception;

    /**
     * 微信支付回调处理
     * 
     * @param wxRespInfo
     * @return
     * @throws Exception
     */
    ResultDTO<String> payCallbackResult(String wxRespInfo) throws Exception;

    /**
     * 关闭订单
     * 
     * @param orderCode
     *            订单编码
     * @return
     * @throws Exception
     */
    ResultDTO<String> closeOrder(String orderCode) throws Exception;

    /**
     * 关闭支付记录
     * 
     * @param orderCode
     *            订单编码
     * @return
     * @throws Exception
     */
    public ResultDTO<String> closePayment(String orderCode) throws Exception;
    
    /**
     * 线下支付处理
     * 
     * @param orderCode
     *            订单号
     * @param success
     *            是否成功
     * @return
     * @throws Exception
     */
    ResultDTO<String> offlinePayHandle(String orderCode, boolean success) throws Exception;

}
