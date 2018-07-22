/**
 * @{#} RegionSchool.java Created on 2017-09-25 21:54:18
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区学校表
 * 
 * @author masb 对应表(SYS_REGION_SCHOOL)
 */
public class RegionSchool implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校ID
     */
    protected long schoolId;

    /**
     * 地区编码
     */
    protected String regionCode;

    /**
     * 地区名称
     */
    protected String regionName;

    /**
     * 学校名称
     */
    protected String schoolName;

    /**
     * 学校简介
     */
    protected String schoolExplain;

    /**
     * 学校地址
     */
    protected String schoolAddr;

    /**
     * 学校排序（由大往下）
     */
    protected long schoolSort;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 无参构造方法
     */
    public RegionSchool() {
    }

    /**
     * 属性get||set方法
     */
    public long getSchoolId() {
        return this.schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public String getRegionCode() {
        return this.regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getSchoolName() {
        return this.schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolExplain() {
        return this.schoolExplain;
    }

    public void setSchoolExplain(String schoolExplain) {
        this.schoolExplain = schoolExplain;
    }

    public String getSchoolAddr() {
        return this.schoolAddr;
    }

    public void setSchoolAddr(String schoolAddr) {
        this.schoolAddr = schoolAddr;
    }

    public long getSchoolSort() {
        return this.schoolSort;
    }

    public void setSchoolSort(long schoolSort) {
        this.schoolSort = schoolSort;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}