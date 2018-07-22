/**
 * @{#} Role.java Created on 2016-09-22 15:10:23
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息
 * 
 * @author masb 对应表(SSO_ROLE)
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long roleId;

    protected String roleName;

    protected String roleDesc;

    protected long roleType;

    /**
     * 状态 0 ：有效; 1 : 无效
     */
    protected long status;

    /**
     * 所属业务系统
     */
    protected long sysId;

    protected long createOpId;

    protected Date createTime;

    protected long modifyOpId;

    protected Date modifyTime;

    protected String residlist;

    protected long supplierId;

    protected long parentId;

    protected long roleLevel;

    protected String rolePath;

    /** 无参构造方法 */
    public Role() {
    }

    /**
     * 属性get||set方法
     */
    public long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return this.roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public long getRoleType() {
        return this.roleType;
    }

    public void setRoleType(long roleType) {
        this.roleType = roleType;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getSysId() {
        return this.sysId;
    }

    public void setSysId(long sysId) {
        this.sysId = sysId;
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

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getResidlist() {
        return this.residlist;
    }

    public void setResidlist(String residlist) {
        this.residlist = residlist;
    }

    public long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getRoleLevel() {
        return this.roleLevel;
    }

    public void setRoleLevel(long roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getRolePath() {
        return this.rolePath;
    }

    public void setRolePath(String rolePath) {
        this.rolePath = rolePath;
    }

}