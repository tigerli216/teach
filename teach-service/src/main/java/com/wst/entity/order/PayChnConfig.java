/**
 * @{#} PayChnConfig.java Created on 2016-09-27 14:49:57
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.order;

import java.io.Serializable;

/**
 * 第三方支付渠道配置表
 * 
 * @author masb 对应表(BM_PAY_CHN_CONFIG)
 */
public class PayChnConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    protected long payChnId;

    /**
     * 商户名称
     */
    protected String payChnName;

    /**
     * 商户编码
     */
    protected String payChnCode;

    /**
     * 支付渠道状态（1-启用；2-禁用）
     */
    protected long payChnStatus;

    /**
     * 支付渠道参数配置
     */
    protected String payChnParams;

    /**
     * 类型：1、微信，2、学生端app，3、导师端app'
     */
    protected long paySourceChn;

    /**
     * 支付通道，1：支付宝，2：微信，3：微信公众号
     */
    protected long payChn;

    /** 无参构造方法 */
    public PayChnConfig() {
    }

    /**
     * 属性get||set方法
     * 
     * @return
     */
    public long getPayChnId() {
        return this.payChnId;
    }

    public void setPayChnId(long payChnId) {
        this.payChnId = payChnId;
    }

    public String getPayChnName() {
        return this.payChnName;
    }

    public void setPayChnName(String payChnName) {
        this.payChnName = payChnName;
    }

    public String getPayChnCode() {
        return this.payChnCode;
    }

    public void setPayChnCode(String payChnCode) {
        this.payChnCode = payChnCode;
    }

    public long getPayChnStatus() {
        return this.payChnStatus;
    }

    public void setPayChnStatus(long payChnStatus) {
        this.payChnStatus = payChnStatus;
    }

    public String getPayChnParams() {
        return this.payChnParams;
    }

    public void setPayChnParams(String payChnParams) {
        this.payChnParams = payChnParams;
    }

    public long getPaySourceChn() {
        return paySourceChn;
    }

    public void setPaySourceChn(long paySourceChn) {
        this.paySourceChn = paySourceChn;
    }

    public long getPayChn() {
        return payChn;
    }

    public void setPayChn(long payChn) {
        this.payChn = payChn;
    }
}