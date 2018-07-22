/**
 * @{#} RelResRole.java Created on 2016-09-22 15:10:23
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限实体与角色关系
 * 
 * @author masb 对应表(SSO_REL_RES_ROLE)
 */
public class RelResRole implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long relId;

    protected long resourceId;

    protected long roleId;
    /**
     * 状态 0 : 有效; 1 : 无效
     */
    protected long status;

    protected long createOpId;

    protected Date createTime;

    protected long modifyOpId;

    protected Date modifyTime;

    /** 无参构造方法 */
    public RelResRole() {
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

    public long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
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

}