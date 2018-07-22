/**
 * @{#} ResourceDTO.java Created on 2016-09-22 16:04:07
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.service.sm.dto;

import java.util.ArrayList;
import java.util.List;

import com.wst.entity.sm.Resource;

/**
 * @author masb
 * 
 *         对应实体类(Resource)
 */
public class ResourceDTO extends Resource {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单类型 '0：接入系统权限（可有下级），1：目录菜单，2：功能菜单；3：功能实体'
     */
    private String resourceTypeStr;

    private List<ResourceDTO> childrens = new ArrayList<ResourceDTO>();

    public String getResourceTypeStr() {
        if (resourceType == 0) {
            resourceTypeStr = "系统权限";
        } else if (resourceType == 1) {
            resourceTypeStr = "功能目录";
        } else if (resourceType == 2) {
            resourceTypeStr = "功能菜单";
        } else if (resourceType == 3) {
            resourceTypeStr = "功能实体";
        }
        return resourceTypeStr;
    }

    public List<ResourceDTO> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<ResourceDTO> childrens) {
        this.childrens = childrens;
    }

    public void addChildren(ResourceDTO res) {
        this.childrens.add(res);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}