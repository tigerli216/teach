/**
 * @{#} AppIp.java Created on 2017-03-23 21:05:20
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方应用IP配置表
 * 
 * @author masb 对应表(SYS_APP_IP)
 */
public class AppIp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    protected long configId;

    /**
     * 应用接入ID
     */
    protected long accsId;

    /**
     * ip地址
     */
    protected String ipAddr;

    /**
     * IP类型（1-白名单；2-黑名单）
     */
    protected long ipType;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected long createOp;

    /**
     * 更改时间
     */
    protected Date updateTime;

    /**
     * 更改人
     */
    protected long updateOp;

    /**
     * 状态（1-有效；2-无效）
     */
    protected long status;

    /** 无参构造方法 */
    public AppIp() {
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

    public long getAccsId() {
        return this.accsId;
    }

    public void setAccsId(long accsId) {
        this.accsId = accsId;
    }

    public String getIpAddr() {
        return this.ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public long getIpType() {
        return this.ipType;
    }

    public void setIpType(long ipType) {
        this.ipType = ipType;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCreateOp() {
        return this.createOp;
    }

    public void setCreateOp(long createOp) {
        this.createOp = createOp;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateOp() {
        return this.updateOp;
    }

    public void setUpdateOp(long updateOp) {
        this.updateOp = updateOp;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}