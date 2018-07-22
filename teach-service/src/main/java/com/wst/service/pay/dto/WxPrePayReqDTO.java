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
public class WxPrePayReqDTO extends WxOrderReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品描述，必传
     */
    private String body;

    /**
     * 支付总金额（分），必传
     */
    private long totalFee;

    /**
     * 请求IP，必传
     */
    private String reqIp;

    /**
     * 用户标识，非必传<br>
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识
     */
    private String openid;

    /**
     * 商品Id，非必传<br>
     * trade_type=NATIVE，此参数必传。此id为二维码中包含的商品Id，商户自行定义
     */
    private String productId;

    /**
     * 支付类型，JSAPI|APP|NATIVE：
     */
    private String payType;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(long totalFee) {
        this.totalFee = totalFee;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("body:").append(body);
        str.append(",totalFee:").append(totalFee);
        str.append(",reqIp:").append(reqIp);
        str.append(",openid:").append(openid);
        str.append(",productId:").append(productId);
        str.append(",payType:").append(payType);
        str.append(",").append(super.toString());
        return str.toString();
    }

}
