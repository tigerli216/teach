/**
 * @{#} Region.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区信息
 * 
 * @author masb 对应表(SYS_REGION)
 */
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地区ID
     */
    protected long regionId;

    /**
     * 地区名称
     */
    protected String regionName;

    /**
     * 地区编码
     */
    protected String regionCode;

    /**
     * 时区
     */
    protected String timeZone;

    /**
     * 上级地区ID
     */
    protected long parentId;

    /**
     * 地区路径
     */
    protected String regionPath;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 无参构造方法
     */
    public Region() {
    }

    /**
     * 属性get||set方法
     */
    public long getRegionId() {
        return this.regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return this.regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getRegionPath() {
        return this.regionPath;
    }

    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}