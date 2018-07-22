/**
 * @{#} AppVersions.java Created on 2016-10-21 15:42:14
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.app;

import java.io.Serializable;
import java.util.Date;

/**
 * app版本配置表
 * 
 * @author masb 对应表(APP_VERSIONS)
 */
public class AppVersions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    protected long versionsId;

    /**
     * 用户类型，1：学生端，2：导师端
     */
    protected long userType;

    /**
     * app操作系统，1：安卓
     */
    protected long osname;

    /**
     * 版本名称
     */
    protected String versionsName;

    /**
     * app版本号（根据创建时间判断最新版本）
     */
    protected String versions;

    /**
     * 版本大小
     */
    protected String versionsSize;

    /**
     * 更新内容
     */
    protected String updateContent;

    /**
     * 下载地址
     */
    protected String downloadUrl;

    /**
     * 是否强制更新，1：强制，2：不强制
     */
    protected long isForcedUpdate;

    /**
     * 创建时间
     */
    protected Date createTime;

    /** 无参构造方法 */
    public AppVersions() {
    }

    /**
     * 属性get||set方法
     */
    public long getVersionsId() {
        return this.versionsId;
    }

    public void setVersionsId(long versionsId) {
        this.versionsId = versionsId;
    }

    public long getUserType() {
        return userType;
    }

    public void setUserType(long userType) {
        this.userType = userType;
    }

    public long getOsname() {
        return this.osname;
    }

    public void setOsname(long osname) {
        this.osname = osname;
    }

    public String getVersionsName() {
        return this.versionsName;
    }

    public void setVersionsName(String versionsName) {
        this.versionsName = versionsName;
    }

    public String getVersions() {
        return this.versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getVersionsSize() {
        return this.versionsSize;
    }

    public void setVersionsSize(String versionsSize) {
        this.versionsSize = versionsSize;
    }

    public String getUpdateContent() {
        return this.updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getIsForcedUpdate() {
        return this.isForcedUpdate;
    }

    public void setIsForcedUpdate(long isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}