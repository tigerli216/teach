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
import com.wst.entity.sm.RelOrgRole;
import com.wst.service.sm.dto.RelOrgRoleDTO;

/**
 * 组织和角色关系Dao
 * 
 * @author <a href="mailto:luozj@hiwitech.com">luozhongjie</a>
 * @version 1.0
 */
@Repository
public class RelOrgRoleDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param relOrgRole
     * @return
     * @throws Exception
     */
    public long save(RelOrgRole relOrgRole) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_rel_org_role(org_id, role_id, status, create_op_id, create_time)");
        sql.append(" value (:orgId, :roleId, :status, :createOpId, :createTime)");
        
        return baseDao.insertInto(sql.toString(), relOrgRole, "rel_id");
    }

    /**
     * 通过角色Id查询组织
     * 
     * @param roleId
     * @return
     * @throws Exception
     */
    public List<RelOrgRole> findOrgIdsByRoleId(long roleId) throws Exception {
        String sql = "select org_id from sso_rel_org_role where status = 0 and role_id = " + roleId;
        return baseDao.findList(sql, null, RelOrgRole.class);
    }

    /**
     * 通过角色ID与组织ID查询关系
     * 
     * @param roleId
     * @param orgId
     * @return
     * @throws Exception
     */
    public RelOrgRoleDTO findRelOrgRoleByRoleIdAndOrgId(long roleId, long orgId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, org_id, role_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time ");
        sql.append("  from sso_rel_org_role ");
        sql.append(" where status = 0 ");
        sql.append("   and role_id = :roleId ");
        sql.append("   and org_id = :orgId ");
        
        Map<String, Long> paremMap = new HashMap<String, Long>(2);
        paremMap.put("roleId", roleId);
        paremMap.put("orgId", orgId);
        
        return baseDao.get(sql.toString(), paremMap, RelOrgRoleDTO.class);
    }

    /**
     * 删除
     * 
     * @param relResRole
     * @throws Exception
     */
    public void delete(RelOrgRoleDTO relOrgRole) throws Exception {
        String sql = "delete from sso_rel_org_role where rel_id = :relId";
        
        Map<String, Long> paremMap = new HashMap<String, Long>(1);
        paremMap.put("relId", relOrgRole.getRelId());
        
        baseDao.executeSQL(sql.toString(), paremMap);
    }
}
