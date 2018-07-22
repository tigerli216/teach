/*
 * RoleService.java Created on 2016年10月25日 下午8:36:14
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.service.sm.service;

import java.util.List;

import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.RoleDTO;

/**
 * 角色服务接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface RoleService {

    /**
     * 查询所有有效角色
     * 
     * @return
     * @throws Exception
     */
    public List<RoleDTO> findRoleList() throws Exception;

    /**
     * 角色分页查询
     * 
     * @param pageParam
     * @param roleQuery
     * @return
     * @throws Exception
     */
    public Page<RoleDTO> findRolePaging(Page<RoleDTO> pageParam, RoleDTO roleQuery) throws Exception;

    /**
     * 查询角色
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public RoleDTO findRoleById(long roleId) throws Exception;

    /**
     * 查询父类角色
     * 
     * @param parentId
     * @return
     * @throws Exception
     */
    public RoleDTO findRoleByPrentId(long parentId) throws Exception;

    /**
     * 查看角色名是否重复
     * 
     * @param role
     * @return
     * @throws Exception
     */
    public boolean ifRepeatName(RoleDTO role) throws Exception;

    /**
     * 添加角色
     * 
     * @param role
     * @param resIdListStr
     * @param orgIdListStr
     * @throws Exception
     */
    public void addRole(RoleDTO role, String resIdListStr, String orgIdListStr) throws Exception;

    /**
     * 修改 角色
     * 
     * @param role
     * @param resIdListStr
     * @param orgIdListStr
     * @throws Exception
     */
    public void updateRole(RoleDTO role, String resIdListStr, String orgIdListStr) throws Exception;

    /**
     * 通过角色Id查询角色拥有的菜单权限
     * 
     * @param role
     * @return
     * @throws Exception
     */
    public List<Long> findRelResRoleByRoleId(RoleDTO role) throws Exception;

    /**
     * 通过角色Id查询角色与组织之间的关系
     * 
     * @param role
     * @return
     * @throws Exception
     */
    public List<Long> findRelOrgRoleByRoleId(RoleDTO role) throws Exception;

    /**
     * 启用或禁用
     * 
     * @param roleIds
     * @param status
     * @throws Exception
     */
    public void enableOrDisableRole(String roleIds, Long status) throws Exception;
}
