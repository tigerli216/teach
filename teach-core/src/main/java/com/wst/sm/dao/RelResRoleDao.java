/*
 * RelOpOrgDao.java Created on 2016年10月26日 下午1:49:02
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

import com.hiwi.common.dao.BaseDao;
import com.wst.entity.sm.RelResRole;
import com.wst.service.sm.dto.RelOrgRoleDTO;
import com.wst.service.sm.dto.RelResRoleDTO;

/**
 * 角色与功能菜单关系Dao
 * 
 * @author <a href="mailto:luozj@hiwitech.com">luozhongjie</a>
 * @version 1.0
 */
@Repository
public class RelResRoleDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param relResRole
     * @return
     * @throws Exception
     */
    public long save(RelResRole relResRole) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into sso_rel_res_role(resource_id, role_id, status, create_op_id, create_time) ");
        sql.append(" value (:resourceId, :roleId, :status, :createOpId, :createTime)");

        return baseDao.insertInto(sql.toString(), relResRole, "rel_id");
    }

    /**
     * 根据角色ID查询关联权限Id
     * 
     * @param OpId
     * @return
     * @throws Exception
     */
    public List<String> findResIdsByRoleId(long roleId) throws Exception {
        String sql = "select resource_id from sso_rel_res_role where status = 0 and role_id = " + roleId;
        return baseDao.findList(sql, null, String.class);
    }

    /**
     * 通过角色ID与权限ID查询关系
     * 
     * @param resId
     * @param roleId
     * @return
     * @throws Exception
     */
    public RelResRoleDTO findRelResRoleByRoleIdAndResId(long roleId, long resId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, resource_id, role_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time ");
        sql.append("  from sso_rel_res_role ");
        sql.append(" where status = 0 ");
        sql.append("   and role_id = :roleId ");
        sql.append("   and resource_id = :resourceId ");
        
        Map<String, Long> param = new HashMap<String, Long>(2);
        param.put("roleId", roleId);
        param.put("resourceId", resId);
        
        List<RelResRoleDTO> relResRoleList = baseDao.findList(sql.toString(), param, RelResRoleDTO.class);
        if (null != relResRoleList && relResRoleList.size() > 0) {
            return relResRoleList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 删除
     * 
     * @param relResRole
     * @throws Exception
     */
    public void delete(RelResRole relResRole) throws Exception {
        String sql = "delete from sso_rel_res_role where rel_id = :relId";
        baseDao.executeSQL(sql.toString(), relResRole);
    }

    /**
     * 查看权限是否与角色之间关系
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<RelResRoleDTO> findRelResRoleByResIdAndRoleId(long roleId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, resource_id, role_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time ");
        sql.append("  from sso_rel_res_role ");
        sql.append(" where rel.status = 0 ");
        sql.append("   and rel.role_id = " + roleId);
        
        return baseDao.findList(sql.toString(), null, RelResRoleDTO.class);
    }

    /**
     * 查看组织与角色之间的关系
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<RelOrgRoleDTO> findRelOrgRoleByRoleId(long roleId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, org_id, role_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time ");
        sql.append("  from sso_rel_org_role ");
        sql.append(" where status = 0 ");
        sql.append("   and role_id = " + roleId);
        
        return baseDao.findList(sql.toString(), null, RelOrgRoleDTO.class);
    }
}
