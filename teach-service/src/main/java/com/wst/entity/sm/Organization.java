/**
 * @{#} Organization.java Created on 2016-09-22 15:10:20
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 组织信息
 * 
 * @author masb 对应表(SSO_ORGANIZATION)
 */
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long orgId;

    protected String orgCode;

    protected long orgType;

    protected String orgName;

    protected String orgDesc;

    protected String orgPath;

    protected long status;

    protected long parentId;

    protected long createOpId;

    protected Date createTime;

    protected long modifyOpId;

    protected long isDataControl;

    protected Date modifyTime;

    protected long orgLevel;

    protected long supplierId;

    /** 无参构造方法 */
    public Organization() {
    }

    /**
     * 属性get||set方法
     */
    public long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public long getOrgType() {
        return this.orgType;
    }

    public void setOrgType(long orgType) {
        this.orgType = orgType;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDesc() {
        return this.orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getOrgPath() {
        return this.orgPath;
    }

    public void setOrgPath(String orgPath) {
        this.orgPath = orgPath;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getCreateOpId() {
        return this.createOpId;
    }

    public void setCreateOpId(long createOpId) {
        this.createOpId = createOpId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getModifyOpId() {
        return this.modifyOpId;
    }

    public void setModifyOpId(long modifyOpId) {
        this.modifyOpId = modifyOpId;
    }

    public long getIsDataControl() {
        return this.isDataControl;
    }

    public void setIsDataControl(long isDataControl) {
        this.isDataControl = isDataControl;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getOrgLevel() {
        return this.orgLevel;
    }

    public void setOrgLevel(long orgLevel) {
        this.orgLevel = orgLevel;
    }

    public long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

}