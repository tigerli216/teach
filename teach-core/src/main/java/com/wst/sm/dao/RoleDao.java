/*
 * RoleDao.java Created on 2016年10月25日 下午8:37:08
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
package com.wst.sm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.entity.sm.Role;
import com.wst.service.sm.dto.RoleDTO;

/**
 * 角色Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class RoleDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 分页查询角色
     * 
     * @param pageParam
     * @param roleQuery
     * @return
     * @throws Exception
     */
    public Page<RoleDTO> findRolePaging(Page<RoleDTO> pageParam, RoleDTO roleQuery) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select r.create_op_id, r.create_time, r.modify_op_id, r.modify_time, ");
        sql.append("       r.role_desc, r.role_id, r.parent_id, r.status, r.role_name, ");
        sql.append("       r.role_type, r.supplier_id, o.op_name ");
        sql.append("  from sso_role r ");
        sql.append(" inner join sso_operator o on o.op_id = r.create_op_id ");
        sql.append(" where 1 = 1 ");
        
        if (roleQuery.getParentId() > 0) {
            sql.append(" and r.parent_id = :parentId");
        } else {
            sql.append(" and r.parent_id is not null and r.role_path is not null");
        }
        if (roleQuery.getStatus() > 0) {
            sql.append(" and r.status = :status");
            roleQuery.setStatus(roleQuery.getStatus() - 1);
        }
        
        return baseDao.findPaging(sql.toString(), pageParam, roleQuery, RoleDTO.class);
    }

    /**
     * 查询所有有效角色
     * 
     * @return
     * @throws Exception
     */
    public List<RoleDTO> findRoleList() throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select role_id, role_name, role_desc, role_type, status, sys_id, ");
        sql.append("       create_op_id, create_time, modify_op_id, modify_time, resIdList, ");
        sql.append("       supplier_id, parent_id, role_level, role_path ");
        sql.append("  from sso_role ");
        sql.append(" where status = 0");
        
        return baseDao.findList(sql.toString(), null, RoleDTO.class);
    }

    /**
     * 保存数据
     * 
     * @param roleDTO
     * @return
     * @throws Exception
     */
    public long save(Role role) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_role (role_name, role_desc, role_type, ");
        sql.append("       status, sys_id, create_op_id, create_time, modify_op_id, ");
        sql.append("       modify_time, resIdList, supplier_id, parent_id, role_level, role_path)");
        sql.append("values (:roleName, :roleDesc, :roleType, ");
        sql.append("       :status, :sysId, :createOpId, :createTime, :modifyOpId, ");
        sql.append("       :modifyTime, :resIdList, :supplierId, :parentId, :roleLevel, :rolePath)");
        
        return baseDao.insertInto(sql.toString(), role, "role_id");
    }

    /**
     * 保存数据
     * 
     * @param roleDTO
     * @return
     * @throws Exception
     */
    public long update(RoleDTO roleDTO) throws Exception {
        if (roleDTO.getRoleId() == 0) {
            return 0;
        }
        StringBuilder sql = new StringBuilder();
        
        sql.append("update sso_role ");
        sql.append("   set role_name = :roleName, role_desc = :roleDesc, ");
        sql.append("       status = :status, modify_op_id = :modifyOpId, ");
        sql.append("       modify_time = :modifyTime, resIdList = :resIdList, ");
        sql.append("       parent_id = :parentId, role_level = :roleLevel, ");
        sql.append("       role_path = :rolePath ");
        sql.append(" where role_id = :roleId ");

        Map<String, Object> params = new HashMap<>();
        params.put("roleName", roleDTO.getRoleName());
        params.put("roleDesc", roleDTO.getRoleDesc());
        params.put("status", roleDTO.getStatus());
        params.put("modifyOpId", roleDTO.getModifyOpId());
        params.put("modifyTime", roleDTO.getModifyTime());
        params.put("parentId", roleDTO.getParentId());
        params.put("roleLevel", roleDTO.getRoleLevel());
        
        if (StringUtils.isNotEmpty(roleDTO.getResIdList())) {
            params.put("resIdList", null);
        } else {
            params.put("resIdList", roleDTO.getResIdList());
        }
        if (StringUtils.isNotEmpty(roleDTO.getRolePath())) {
            params.put("rolePath", null);
        } else {
            params.put("rolePath", roleDTO.getRolePath());
        }
        params.put("roleId", roleDTO.getRoleId());
        
        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 角色启用或禁用
     * 
     * @param roleId
     * @param status
     * @return
     * @throws Exception
     */
    public long updateRoleByStatus(Long roleId, Long status) throws Exception {
        String sql = "update sso_role set status = :status where role_id = :roleId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("status", status);
        params.put("roleId", roleId);
 
        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 根据roleId查询角色
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public RoleDTO getByRoleId(long roleId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select r.create_op_id, r.create_time, r.modify_op_id, r.modify_time, ");
        sql.append("       r.role_desc, r.role_id, r.parent_id, r.status, r.role_name, ");
        sql.append("       r.role_type, r.supplier_id, o.op_name ");
        sql.append("  from sso_role r ");
        sql.append(" inner join sso_operator o on o.op_id = r.create_op_id ");
        sql.append(" where r.role_id = :roleId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("roleId", roleId);
        
        return baseDao.get(sql.toString(), params, RoleDTO.class);
    }

    /**
     * 查询父类角色
     * 
     * @param parentId
     * @return
     * @throws Exception
     */
    public RoleDTO getRoleByPrentId(long parentId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select r.create_op_id, r.create_time, r.modify_op_id, r.modify_time, ");
        sql.append("       r.role_desc, r.role_id, r.parent_id, r.status, r.role_name, ");
        sql.append("       r.role_type, r.supplier_id, o.op_name ");
        sql.append("  from sso_role r ");
        sql.append(" inner join sso_operator o on o.op_id = r.create_op_id ");
        sql.append(" where r.role_id = :roleId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        if (parentId == 0) {
            return new RoleDTO();
        }
        params.put("roleId", parentId);
        
        return baseDao.get(sql.toString(), params, RoleDTO.class);
    }

    /**
     * 新增时查询角色名是否重复
     * 
     * @param role
     * @return
     * @throws Exception
     */
    public int ifRepeatNameAdd(RoleDTO role) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select count(1) ");
        sql.append("  from sso_role ");
        sql.append(" where role_name = :roleName");
        sql.append("   and parent_id = :parentId");
        
        return baseDao.findCount(sql.toString(), role);
    }

    /**
     * 修改时查询角色名是否重复
     * 
     * @param role
     * @return
     */
    public int ifRepeatNameUpdate(RoleDTO role) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select count(1) ");
        sql.append("  from sso_role ");
        sql.append(" where role_name = :roleName");
        sql.append("   and role_id != :roleId");
        if (role.getRoleId() == role.getParentId()) {
            sql.append("  and parent_id = role_id ");
        } else {
            sql.append("  and parent_id = :parentId");
        }
        
        return baseDao.findCount(sql.toString(), role);
    }
}
