/*
 * WxCallbackRespDTO.java Created on 2017年3月28日 下午12:01:09
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

import java.io.Serializable;
import java.util.Date;

import com.hiwi.common.util.DateUtils;

/**
 * 微信回调返回参数
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class WxOrderRespDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台订单号
     */
    protected String orderNo;

    /**
     * 微信订单号
     */
    protected String outOrderNo;

    /**
     * 时间
     */
    protected Date time;

    /**
     * 订单状态，1：成功，2：失败
     */
    protected long orderStatus;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(long orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("orderNo:").append(orderNo);
        str.append(",outOrderNo:").append(outOrderNo);
        str.append(",time:")
                .append(time != null ? DateUtils.convertDateToString(time, DateUtils.YYYY_MM_DD_HH_MM_SS) : time);
        str.append(",orderStatus:").append(orderStatus);
        return str.toString();
    }

}
