/*
 * appPaymentResp.java Created on 2016年9月27日 上午9:55:20
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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

import java.io.Serializable;

/**
 * app支付调用第三方所需要的数据（包括支付宝，微信）
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class AppPaymentResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微信应用标识
     */
    private String appId;

    /**
     * 微信商户ID
     */
    private String partnerId;

    /**
     * 时间戳（秒）
     */
    private String timeStamp;

    /**
     * 随机数
     */
    private String nonceStr;

    /**
     * 固定值Sign=WXPay，参与加密时参数名改为package
     */
    private String packageValue;

    /**
     * 微信预支付订单标识
     */
    private String prepayId;

    /**
     * 微信签名
     */
    private String sign;

    /**
     * 支付宝数据
     */
    private String alipay_code;

    /**
     * 第三方编码
     */
    private String outOrderCode;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAlipay_code() {
        return alipay_code;
    }

    public void setAlipay_code(String alipay_code) {
        this.alipay_code = alipay_code;
    }

    public String getOutOrderCode() {
        return outOrderCode;
    }

    public void setOutOrderCode(String outOrderCode) {
        this.outOrderCode = outOrderCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
