/**
 * @{#} Resource.java Created on 2016-09-22 15:10:23
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限实体管理
 * 
 * @author masb 对应表(SSO_RESOURCE)
 */
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long resourceId;

    protected long parentId;

    protected String resourceTitle;

    /**
     * 0 ：接入系统权限（可有下级）; 1 ：目录菜单; 2 ：功能菜单; 3 ：功能实体
     */
    protected long resourceType;

    protected String resourceCode;

    protected String resourcePath;

    protected String menuUrl;

    protected String menuImage;

    protected long status;

    protected long createOpId;

    protected Date createTime;

    protected long modifyOpId;

    protected Date modifyTime;

    protected long sysId;

    protected long menuSort;

    protected String parentname;

    protected String sysname;

    /** 无参构造方法 */
    public Resource() {
    }

    /**
     * 属性get||set方法
     */
    public long getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public long getParentId() {
        return this.parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getResourceTitle() {
        return this.resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public long getResourceType() {
        return this.resourceType;
    }

    public void setResourceType(long resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceCode() {
        return this.resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourcePath() {
        return this.resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getMenuUrl() {
        return this.menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuImage() {
        return this.menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
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

    public long getSysId() {
        return this.sysId;
    }

    public void setSysId(long sysId) {
        this.sysId = sysId;
    }

    public long getMenuSort() {
        return this.menuSort;
    }

    public void setMenuSort(long menuSort) {
        this.menuSort = menuSort;
    }

    public String getParentname() {
        return this.parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getSysname() {
        return this.sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

}