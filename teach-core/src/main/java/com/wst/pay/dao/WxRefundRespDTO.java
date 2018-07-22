/*
 * WxPrePayDTO.java Created on 2017年3月28日 上午10:44:02
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
package com.wst.pay.dao;


/**
 * 微信预支付订单信息
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxRefundRespDTO extends WxOrderRespDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 平台退款订单号
     */
    private String refundNo;

    /**
     * 微信退款订单号
     */
    private String outRefundNo;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("orderNo:").append(orderNo);
        str.append(",outOrderNo:").append(outOrderNo);
        str.append(",refundNo:").append(refundNo);
        str.append(",outRefundNo:").append(outRefundNo);
        str.append(",orderStatus:").append(orderStatus);
        return str.toString();
    }
}
