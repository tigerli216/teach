/**
 * @{#} IndustrySalary.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * 导师行业薪资配置表
 * 
 * @author masb 对应表(SYS_INDUSTRY_SALARY)
 */
public class IndustrySalary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    protected long configId;

    /**
     * 行业ID
     */
    protected long industryId;

    /**
     * 基础薪资
     */
    protected long baseSalary;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected long createOp;

    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 修改人
     */
    protected long modifyOp;

    /**
     * 状态（1-有效；2-无效）
     */
    protected long status;

    /** 无参构造方法 */
    public IndustrySalary() {
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

    public long getIndustryId() {
        return this.industryId;
    }

    public void setIndustryId(long industryId) {
        this.industryId = industryId;
    }

    public long getBaseSalary() {
        return this.baseSalary;
    }

    public void setBaseSalary(long baseSalary) {
        this.baseSalary = baseSalary;
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

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getModifyOp() {
        return this.modifyOp;
    }

    public void setModifyOp(long modifyOp) {
        this.modifyOp = modifyOp;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}