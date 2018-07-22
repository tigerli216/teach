/**
 * @{#} RoleDTO.java Created on 2016-09-22 16:04:07
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.service.sm.dto;

import com.wst.entity.sm.Role;

/**
 * 角色实体
 * 
 * @author <a href="mailto:luozj@hiwitech.com">luozhongjie</a>
 * @version 1.0
 */
public class RoleDTO extends Role {

    private static final long serialVersionUID = 1L;

    /**
     * 操作员名称
     */
    private String opName;
    
    /**
     * 操作员别名
     */
    private String supplierShortName;
    
    /**
     * 状态枚举
     */
    private String statusStr;

    /**
     * 功能菜单集
     */
    private String resIdList;
    
    /**
     * 组织菜单集
     */
    private String orgIdList;

    public RoleDTO() {

    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getSupplierShortName() {
        return supplierShortName;
    }

    public void setSupplierShortName(String supplierShortName) {
        this.supplierShortName = supplierShortName;
    }

    public String getStatusStr() {
        if (status == 0) {
            statusStr = "有效";
        } else {
            statusStr = "无效";
        }
        return statusStr;
    }

    public String getResIdList() {
        return resIdList;
    }

    public void setResIdList(String resIdList) {
        this.resIdList = resIdList;
    }

    public String getOrgIdList() {
        return orgIdList;
    }

    public void setOrgIdList(String orgIdList) {
        this.orgIdList = orgIdList;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}