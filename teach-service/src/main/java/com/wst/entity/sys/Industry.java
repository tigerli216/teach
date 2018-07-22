/**
 * @{#} Industry.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 行业信息表
 * 
 * @author masb 对应表(SYS_INDUSTRY)
 */
public class Industry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 行业ID
     */
    protected long industryId;

    /**
     * 行业分类
     */
    protected long industryType;

    /**
     * 行业名称
     */
    protected String industryName;

    /**
     * 行业编码
     */
    protected String industryCode;

    /**
     * 创建时间
     */
    protected Date createTime;

    /** 无参构造方法 */
    public Industry() {
    }

    /**
     * 属性get||set方法
     */
    public long getIndustryId() {
        return this.industryId;
    }

    public void setIndustryId(long industryId) {
        this.industryId = industryId;
    }

    public long getIndustryType() {
        return this.industryType;
    }

    public void setIndustryType(long industryType) {
        this.industryType = industryType;
    }

    public String getIndustryName() {
        return this.industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryCode() {
        return this.industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}