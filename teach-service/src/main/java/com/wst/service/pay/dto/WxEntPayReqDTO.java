/*
 * WxEntPayReqDTO.java Created on 2017年3月28日 下午3:30:14
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
 * 微信打款请求参数
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxEntPayReqDTO extends WxOrderReqDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识，必传<br>
     * 用户在商户appid下的唯一标识
     */
    private String openid;

    /**
     * 打款金额，必传
     */
    private long transferFee;

    /**
     * 打款描述，必传
     */
    private String transferDesc;

    /**
     * 请求IP，必传
     */
    private String reqIp;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(long transferFee) {
        this.transferFee = transferFee;
    }

    public String getTransferDesc() {
        return transferDesc;
    }

    public void setTransferDesc(String transferDesc) {
        this.transferDesc = transferDesc;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("openid:").append(openid);
        str.append(",transferFee:").append(transferFee);
        str.append(",transferDesc:").append(transferDesc);
        str.append(",reqIp:").append(reqIp);
        str.append(",").append(super.toString());
        return str.toString();
    }

}
