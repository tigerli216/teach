/*
 * RelOpRole.java Created on 2016年10月26日 下午1:59:05
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
import com.wst.entity.sm.RelOpRole;
import com.wst.service.sm.dto.RelOpRoleDTO;

/**
 * 操作员与角色关系Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class RelOpRoleDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param relOpOrg
     * @return
     * @throws Exception
     */
    public long save(RelOpRole relOpRole) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_rel_op_role(op_id, role_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time, expire_time, org_id)");
        sql.append(" value(:opId, :roleId, :status, :createOpId, ");
        sql.append("       :createTime, :modifyOpId, :modifyTime, :expireTime, :orgId) ");
        
        return baseDao.insertInto(sql.toString(), relOpRole, "rel_id");
    }

    /**
     * 根据用户ID获取用户关联角色信息
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public List<RelOpRoleDTO> findRelOpRoleListByOpId(long opId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, op_id, role_id, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, expire_time, org_id ");
        sql.append("  from sso_rel_op_role ");
        sql.append(" where status = 0 ");
        sql.append("   and op_id = :opId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("opId", opId);
        
        return baseDao.findList(sql.toString(), params, RelOpRoleDTO.class);
    }

    /**
     * 根据用户ID获取用户关联角色ID
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public List<String> findRelOpRoleIdsByOpId(long opId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select role_id ");
        sql.append("  from sso_rel_op_role ");
        sql.append(" where status = 0 ");
        sql.append("   and op_id = :opId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("opId", opId);
        
        return baseDao.findList(sql.toString(), params, String.class);
    }

    /**
     * 删除操作员与角色的关系
     * 
     * @param opId
     * @param roleId
     * @return
     * @throws Exception
     */
    public int deleteRelOpOrg(long opId, long roleId) throws Exception {
        String sql = "delete from sso_rel_op_role where op_id = :opId and role_id = :roleId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("opId", opId);
        params.put("roleId", roleId);
        
        return baseDao.executeSQL(sql.toString(), params);
    }
}
