/**
 * @{#} BusiConfig.java Created on 2016-10-09 10:37:57
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;

/**
 * 公用业务配置表,参数都以JSON格式配置，只有开发人员，系统维护人员可以改动
 * 
 * @author masb 对应表(SYS_BUSI_CONFIG)
 */
public class BusiConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    protected long configId;

    /**
     * 业务名称
     */
    protected String busiName;

    /**
     * 业务编码
     */
    protected String busiCode;

    /**
     * 业务类型（1-定制课咨询配置；2-活动咨询配置）
     */
    protected long busiType;

    /**
     * 状态（1-启用；2-禁用）
     */
    protected long status;

    /**
     * 参数配置，以JSON的形式
     */
    protected String params;

    /**
     * 优先级（值越大，越优先）
     */
    protected long priority;

    /** 无参构造方法 */
    public BusiConfig() {
    }

    /**
     * 属性get||set方法
     */
    public long getConfigId() {
        return this.configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getBusiName() {
        return this.busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public String getBusiCode() {
        return this.busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public long getBusiType() {
        return this.busiType;
    }

    public void setBusiType(long busiType) {
        this.busiType = busiType;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public long getPriority() {
        return this.priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

}