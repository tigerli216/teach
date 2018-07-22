/**
 * @{#} OperatorDTO.java Created on 2016-09-28 13:42:39
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.service.sm.dto;

import java.util.Date;
import java.util.List;

import com.wst.entity.sm.Operator;

/**
 * @author masb
 * 
 *         对应实体类(Operator)
 */
public class OperatorDTO extends Operator {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createOpName;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 归属组织
     */
    private String orgName;
    
    /**
     * 修改人
     */
    private String modifyOpName;
    
    /**
     * 状态枚举
     */
    private String statusStr;

    /**
     * 用户所属组织
     */
    private List<RelOpOrgDTO> relOpOrgList;
    
    /**
     * 用户所属角色
     */
    private List<RelOpRoleDTO> relOpRoleList;

    /**
     * 用户所属组织IDS
     */
    private String orgIds;
    
    /**
     * 用户拥有角色IDS
     */
    private String roleIds;

    public String getCreateOpName() {
        return createOpName;
    }

    public void setCreateOpName(String createOpName) {
        this.createOpName = createOpName;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getModifyOpName() {
        return modifyOpName;
    }

    public void setModifyOpName(String modifyOpName) {
        this.modifyOpName = modifyOpName;
    }

    public String getStatusStr() {
        if (status == 0) {
            statusStr = "启用";
        } else if (status == 1) {
            statusStr = "禁用";
        }
        return statusStr;
    }

    public List<RelOpOrgDTO> getRelOpOrgList() {
        return relOpOrgList;
    }

    public void setRelOpOrgList(List<RelOpOrgDTO> relOpOrgList) {
        this.relOpOrgList = relOpOrgList;
    }

    public List<RelOpRoleDTO> getRelOpRoleList() {
        return relOpRoleList;
    }

    public void setRelOpRoleList(List<RelOpRoleDTO> relOpRoleList) {
        this.relOpRoleList = relOpRoleList;
    }

    public String getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(String orgIds) {
        this.orgIds = orgIds;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}