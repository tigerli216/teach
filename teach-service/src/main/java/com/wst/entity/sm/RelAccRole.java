/**
 * @{#} RelAccRole.java Created on 2016-09-22 15:10:21
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户与角色关系
 * 
 * @author masb 对应表(SSO_REL_ACC_ROLE)
 */
public class RelAccRole implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long relId;

    protected long accId;

    protected long roleId;

    /**
     * 状态 0 : 有效; 1 : 无效
     */
    protected long status;

    protected long createAccId;

    protected Date createTime;

    protected long modifyAccId;

    protected Date modifyTime;

    protected Date expireTime;

    protected long orgId;

    /** 无参构造方法 */
    public RelAccRole() {
    }

    /**
     * 属性get||set方法
     */
    public long getRelId() {
        return this.relId;
    }

    public void setRelId(long relId) {
        this.relId = relId;
    }

    public long getAccId() {
        return this.accId;
    }

    public void setAccId(long accId) {
        this.accId = accId;
    }

    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getCreateAccId() {
        return this.createAccId;
    }

    public void setCreateAccId(long createAccId) {
        this.createAccId = createAccId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getModifyAccId() {
        return this.modifyAccId;
    }

    public void setModifyAccId(long modifyAccId) {
        this.modifyAccId = modifyAccId;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

}