/**
 * @{#} Order.java Created on 2017-09-25 21:54:14
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单主表
 * 
 * @author masb 对应表(BM_ORDER)
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	protected long orderId;

	/**
	 * 订单编码
	 */
	protected String orderCode;

	/**
	 * 订单名称
	 */
	protected String orderName;

	/**
	 * 订单类型（1-网课订单；2-定制课订单；3-会员购买订单）
	 */
	protected long orderType;

	/**
	 * 订单子类型（11-录播课；12-直播课；21-定制课；22-实习课）
	 */
	protected long orderSubType;

	/**
	 * 会员ID
	 */
	protected long userId;

	/**
	 * 业务ID
	 */
	protected long busiId;

	/**
	 * 订单原价
	 */
	protected long origPrice;

	/**
	 * 订单折扣价
	 */
	protected long discountPrice;

	/**
	 * 创建时间
	 */
	protected Date createTime;

	/**
	 * 付款时间
	 */
	protected Date payTime;

	/**
	 * 成交时间
	 */
	protected Date dealTime;

	/**
	 * 付款方式（1-线上；2-线下）
	 */
	protected long payType;

	/**
	 * 订单状态（1-已确认；2-已取消；3-无效；4-退款；5-退货）
	 */
	protected long orderStatus;

	/**
	 * 支付状态；1-未付款；2-已付款；4-已退款
	 */
	protected long payStatus;

	/**
	 * 订单附言
	 */
	protected String orderNotes;

	/**
	 * 付款备注
	 */
	protected String payNotes;

	/**
	 * 有效时间
	 */
	protected Date validTime;

	/** 无参构造方法 */
	public Order() {
	}

	/**
	 * 属性get||set方法
	 */
	public long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderName() {
		return this.orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public long getOrderType() {
		return this.orderType;
	}

	public void setOrderType(long orderType) {
		this.orderType = orderType;
	}

	public long getOrderSubType() {
		return this.orderSubType;
	}

	public void setOrderSubType(long orderSubType) {
		this.orderSubType = orderSubType;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBusiId() {
		return this.busiId;
	}

	public void setBusiId(long busiId) {
		this.busiId = busiId;
	}

	public long getOrigPrice() {
		return this.origPrice;
	}

	public void setOrigPrice(long origPrice) {
		this.origPrice = origPrice;
	}

	public long getDiscountPrice() {
		return this.discountPrice;
	}

	public void setDiscountPrice(long discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPayTime() {
		return this.payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getDealTime() {
		return this.dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public long getPayType() {
		return this.payType;
	}

	public void setPayType(long payType) {
		this.payType = payType;
	}

	public long getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(long orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(long payStatus) {
		this.payStatus = payStatus;
	}

	public String getOrderNotes() {
		return this.orderNotes;
	}

	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}

	public String getPayNotes() {
		return this.payNotes;
	}

	public void setPayNotes(String payNotes) {
		this.payNotes = payNotes;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

}