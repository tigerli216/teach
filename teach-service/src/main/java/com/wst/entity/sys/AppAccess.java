/**
 * @{#} AppAccess.java Created on 2017-03-23 21:05:01
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 第三方应用接入配置表
 * 
 * @author masb 对应表(SYS_APP_ACCESS)
 */
public class AppAccess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接入ID
     */
    protected long accsId;

    /**
     * 公司名称
     */
    protected String company;

    /**
     * 公司法人
     */
    protected String legalPerson;

    /**
     * 地址
     */
    protected String address;

    /**
     * 联系人
     */
    protected String contactName;

    /**
     * 联系人手机号
     */
    protected String contactMobile;

    /**
     * 应用名称
     */
    protected String appName;

    /**
     * 应用Id
     */
    protected String appId;

    /**
     * 应用密钥
     */
    protected String appSecret;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected long opId;

    /**
     * 状态（1-正常；2-禁用）
     */
    protected long status;

    /**
     * 无参构造方法
     */
    public AppAccess() {
    }

    /**
     * 属性get||set方法
     */
    public long getAccsId() {
        return this.accsId;
    }

    public void setAccsId(long accsId) {
        this.accsId = accsId;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLegalPerson() {
        return this.legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactName() {
        return this.contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return this.contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getOpId() {
        return this.opId;
    }

    public void setOpId(long opId) {
        this.opId = opId;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}