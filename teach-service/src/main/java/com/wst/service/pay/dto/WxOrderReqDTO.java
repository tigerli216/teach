/*
 * WxOrderReqDTO.java Created on 2017年3月28日 下午3:11:42
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
 * 微信订单基本参数
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxOrderReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台订单号，与微信订单号二选一
     */
    protected String orderNo;

    /**
     * 微信订单号，与平台订单号二选一
     */
    protected String outOrderNo;

    /**
     * 公众号appid
     */
    protected String appId;

    /**
     * 服务商模式下的子商户公众账号ID
     */
    protected String subAppId;

    /**
     * 商户号
     */
    protected String mchId;

    /**
     * 商户密钥
     */
    protected String mchKey;

    /**
     * 服务商模式下的子商户号
     */
    protected String subMchId;

    /**
     * 微信支付异步回掉地址
     */
    protected String notifyUrl;

    /**
     * 证书
     */
    protected String keyPath;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSubAppId() {
        return subAppId;
    }

    public void setSubAppId(String subAppId) {
        this.subAppId = subAppId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("orderNo:").append(orderNo);
        str.append(",outOrderNo:").append(outOrderNo);
        str.append(",appId:").append(appId);
        str.append(",subAppId:").append(subAppId);
        str.append(",mchId:").append(mchId);
        str.append(",mchKey:").append(mchKey);
        str.append(",subMchId:").append(subMchId);
        str.append(",notifyUrl:").append(notifyUrl);
        str.append(",keyPath:").append(keyPath);
        return str.toString();
    }

}
