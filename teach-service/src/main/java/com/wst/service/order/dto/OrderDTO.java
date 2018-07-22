/*
 * OrderDTO.java Created on 2017年9月26日 上午10:35:06
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
package com.wst.service.order.dto;

import java.util.Date;

import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.EnumUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderSubTypeEnmu;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.entity.order.Order;

/**
 * 对应实体类(Order)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@SuppressWarnings("unused")
public class OrderDTO extends Order {

    private static final long serialVersionUID = 1L;

    /** 显示相关属性 **/
    /**
     * 会员登陆账号
     */
    private String loginAccount;

    /**
     * 会员真实姓名
     */
    private String realName;

    /**
     * 订单类型,1-网课订单；2-定制课订单；3-会员购买订单；
     */
    private String orderTypeStr;

    /**
     * 订单子类型（11-录播课；12-直播课；21-定制课；22-实习课）
     */
    private String orderSubTypeStr;

    /**
     * 订单状态（1-已确认；2-已取消；3-无效；4-退款；5-退货）
     */
    private String orderStatusStr;

    /**
     * 支付状态；1-未付款；2-已付款；4-已退款
     */
    private String payStatusStr;

    /**
     * 付款方式（1-线上；2-线下）
     */
    private String payTypeStr;

    /** 查询相关属性 **/
    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 支付开始时间
     */
    private Date payBeginTime;

    /**
     * 支付结束时间
     */
    private Date payEndTime;

    /**
     * 订单原价（美分转元）
     */
    private String origPriceStr;

    /**
     * 订单折扣价
     */
    private String discountPriceStr;
    
    /**
     * 用户级别（1注册；2-VIP）
     */
    private long userLevel;

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrderTypeStr() {
        OrderTypeEnmu orderTypeEnmu = EnumUtils.getEnum(OrderTypeEnmu.values(), "type", orderType);
        return orderTypeEnmu != null ? orderTypeEnmu.name : "";
    }

    public void setOrderTypeStr(String orderTypeStr) {
        this.orderTypeStr = orderTypeStr;
    }

    public String getOrderSubTypeStr() {
        OrderSubTypeEnmu orderSubTypeEnmu = EnumUtils.getEnum(OrderSubTypeEnmu.values(), "type", orderSubType);
        return orderSubTypeEnmu != null ? orderSubTypeEnmu.name : "";
    }

    public void setOrderSubTypeStr(String orderSubTypeStr) {
        this.orderSubTypeStr = orderSubTypeStr;
    }

    public String getOrderStatusStr() {
        OrderStatusEnum orderStatusEnum = EnumUtils.getEnum(OrderStatusEnum.values(), "status", orderStatus);
        return orderStatusEnum != null ? orderStatusEnum.name : "";
    }

    public void setOrderStatusStr(String orderStatusStr) {
        this.orderStatusStr = orderStatusStr;
    }

    public String getPayStatusStr() {
        PayStatusEnum payStatusEnum = EnumUtils.getEnum(PayStatusEnum.values(), "status", payStatus);
        return payStatusEnum != null ? payStatusEnum.name : "";
    }

    public void setPayStatusStr(String payStatusStr) {
        this.payStatusStr = payStatusStr;
    }

    public String getPayTypeStr() {
        OrderPayTypeEnum orderPayTypeEnum = EnumUtils.getEnum(OrderPayTypeEnum.values(), "type", payType);
        return orderPayTypeEnum != null ? orderPayTypeEnum.name : "";
    }

    public void setPayTypeStr(String payTypeStr) {
        this.payTypeStr = payTypeStr;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getPayBeginTime() {
        return payBeginTime;
    }

    public void setPayBeginTime(Date payBeginTime) {
        this.payBeginTime = payBeginTime;
    }

    public Date getPayEndTime() {
        return payEndTime;
    }

    public void setPayEndTime(Date payEndTime) {
        this.payEndTime = payEndTime;
    }

    public String getOrigPriceStr() throws Exception {
        if (StringUtils.isEmpty(origPriceStr)) {
            origPriceStr = AmountUtils.changeF2YString(origPrice);
        }
        return origPriceStr;
    }

    public void setOrigPriceStr(String origPriceStr) {
        this.origPriceStr = origPriceStr;
    }

    public String getDiscountPriceStr() throws Exception {
        if (StringUtils.isEmpty(discountPriceStr)) {
            discountPriceStr = AmountUtils.changeF2YString(discountPrice);
        }
        return discountPriceStr;
    }

    public void setDiscountPriceStr(String discountPriceStr) {
        this.discountPriceStr = discountPriceStr;
    }

    public long getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(long userLevel) {
		this.userLevel = userLevel;
	}

	public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
