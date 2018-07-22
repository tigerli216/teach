/**
 * @{#} BusiLimit.java Created on 2016-10-19 09:41:09
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;

/**
 * 业务限制表
 * 
 * @author masb 对应表(SYS_BUSI_LIMIT)
 */
public class BusiLimit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 限制ID
     */
    protected long limitId;

    /**
     * 限制编码（与用户限制组编码一致）
     */
    protected String limitCode;

    /**
     * 限制名称
     */
    protected String limitName;

    /**
     * 限制描述
     */
    protected String limitDesc;

    /**
     * 用户类型（0-用户组；1-普通用户；2-渠道用户；3-代理商；4-商城用户；99-全局）
     */
    protected long userType;

    /**
     * 优先级，值越大优先级越大，请根据限制处理性能设置优先级
     */
    protected long limitPriority;

    /**
     * 状态（1-有效；2-无效）
     */
    protected long status;

    /** 无参构造方法 */
    public BusiLimit() {
    }

    /**
     * 属性get||set方法
     */
    public long getLimitId() {
        return this.limitId;
    }

    public void setLimitId(long limitId) {
        this.limitId = limitId;
    }

    public String getLimitCode() {
        return this.limitCode;
    }

    public void setLimitCode(String limitCode) {
        this.limitCode = limitCode;
    }

    public String getLimitName() {
        return this.limitName;
    }

    public void setLimitName(String limitName) {
        this.limitName = limitName;
    }

    public String getLimitDesc() {
        return this.limitDesc;
    }

    public void setLimitDesc(String limitDesc) {
        this.limitDesc = limitDesc;
    }

    public long getUserType() {
        return this.userType;
    }

    public void setUserType(long userType) {
        this.userType = userType;
    }

    public long getLimitPriority() {
        return this.limitPriority;
    }

    public void setLimitPriority(long limitPriority) {
        this.limitPriority = limitPriority;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}