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
package com.wst.service.pay.dto;

import java.io.Serializable;

/**
 * 微信预支付订单信息
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxPrePayRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微信appId
     */
    private String appId;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 预支付交易会话标识
     */
    private String prepayId;

    /**
     * 扩展字段package,由于package为java保留关键字，因此改为packageValue
     */
    private String packageValue;

    /**
     * 商户号
     * 
     */
    private String partnerId;

    /**
     * 签名方式
     */
    private String signType;

    /**
     * 支付url<br>
     * trade_type=NATIVE，返回此参数
     */
    private String codeUrl;

    /**
     * 签名
     */
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("appId:").append(appId);
        str.append(",timeStamp:").append(timeStamp);
        str.append(",nonceStr:").append(nonceStr);
        str.append(",prepayId:").append(prepayId);
        str.append(",packageValue:").append(packageValue);
        str.append(",partnerId:").append(partnerId);
        str.append(",signType:").append(signType);
        str.append(",codeUrl:").append(codeUrl);
        str.append(",sign:").append(sign);
        return str.toString();
    }

}
