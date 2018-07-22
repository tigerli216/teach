/**
 * @{#} Payment.java Created on 2017-09-25 21:54:14
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.order;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付记录
 * 
 * @author masb 对应表(BM_PAYMENT)
 */
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    protected long payId;

    /**
     * 交易流水号
     */
    protected String payNo;

    /**
     * 订单编码
     */
    protected String orderCode;

    /**
     * 交易来源（1-线上；2-线下）
     */
    protected long tradeSource;

    /**
     * 交易说明
     */
    protected String tradeExplain;

    /**
     * 支付金额
     */
    protected long payAmount;

    /**
     * 支付方式；1-微信；2-支付宝
     */
    protected long payType;

    /**
     * 交易类型：1-消费；2-退款；3-提现
     */
    protected long tradeType;

    /**
     * 支付时间
     */
    protected Date payTime;

    /**
     * 第三方支付交易号
     */
    protected String thridTradeNo;

    /**
     * 交易备注
     */
    protected String tradeMemo;

    /**
     * 交易状态；1-未支付；2-支付中；3-成功；4-失败；5-关闭
     */
    protected long tradeStatus;

    /**
     * 支付通道id
     */
    protected long payChnId;

    /** 无参构造方法 */
    public Payment() {
    }

    /**
     * 属性get||set方法
     */
    public long getPayId() {
        return this.payId;
    }

    public void setPayId(long payId) {
        this.payId = payId;
    }

    public String getPayNo() {
        return this.payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getOrderCode() {
        return this.orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public long getTradeSource() {
        return this.tradeSource;
    }

    public void setTradeSource(long tradeSource) {
        this.tradeSource = tradeSource;
    }

    public String getTradeExplain() {
        return this.tradeExplain;
    }

    public void setTradeExplain(String tradeExplain) {
        this.tradeExplain = tradeExplain;
    }

    public long getPayAmount() {
        return this.payAmount;
    }

    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }

    public long getPayType() {
        return this.payType;
    }

    public void setPayType(long payType) {
        this.payType = payType;
    }

    public long getTradeType() {
        return this.tradeType;
    }

    public void setTradeType(long tradeType) {
        this.tradeType = tradeType;
    }

    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getThridTradeNo() {
        return this.thridTradeNo;
    }

    public void setThridTradeNo(String thridTradeNo) {
        this.thridTradeNo = thridTradeNo;
    }

    public String getTradeMemo() {
        return this.tradeMemo;
    }

    public void setTradeMemo(String tradeMemo) {
        this.tradeMemo = tradeMemo;
    }

    public long getTradeStatus() {
        return this.tradeStatus;
    }

    public void setTradeStatus(long tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public long getPayChnId() {
        return payChnId;
    }

    public void setPayChnId(long payChnId) {
        this.payChnId = payChnId;
    }

}