/*
 * WxPrePayRequestDTO.java Created on 2017年3月28日 上午10:47:15
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
package com.wst.service.pay.dto;

/**
 * 微信预支付订单请求参数
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxRefundReqDTO extends WxOrderReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 平台退款订单号，必传
     */
    private String refundNo;

    /**
     * 支付总金额（分），必传
     */
    private long totalFee;

    /**
     * 退款金额（分），必传
     */
    private long refundFee;

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public long getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(long refundFee) {
        this.refundFee = refundFee;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("refundNo:").append(refundNo);
        str.append(",totalFee:").append(totalFee);
        str.append(",refundFee:").append(refundFee);
        str.append(",").append(super.toString());
        return str.toString();
    }

}
