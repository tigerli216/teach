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
import com.wst.entity.sm.RelOpOrg;
import com.wst.service.sm.dto.RelOpOrgDTO;

/**
 * 操作员与组织关系Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class RelOpOrgDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param relOpOrg
     * @return
     * @throws Exception
     */
    public long save(RelOpOrg relOpOrg) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_rel_op_org(op_id, org_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time) ");
        sql.append(" value(:opId, :orgId, :status, :createOpId, ");
        sql.append("       :createTime, :modifyOpId, :modifyTime)");
        
        return baseDao.insertInto(sql.toString(), relOpOrg, "rel_id");
    }

    /**
     * 根据用户ID获取用户关联组织信息
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public List<RelOpOrgDTO> findRelOpOrgListByOpId(long opId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select rel_id, op_id, org_id, status, create_op_id, ");
        sql.append("       create_time, modify_op_id, modify_time ");
        sql.append("  from sso_rel_op_org ");
        sql.append(" where status = 0 ");
        sql.append("   and op_id = :opId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("opId", opId);
        
        return baseDao.findList(sql.toString(), params, RelOpOrgDTO.class);
    }

    /**
     * 根据用户ID获取用户关联组织ID
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public List<String> findRelOpOrgIdsByOpId(long opId) throws Exception {
        String sql = "select org_id  from sso_rel_op_org where status = 0 and op_id = :opId";
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("opId", opId);
        
        return baseDao.findList(sql.toString(), params, String.class);
    }

    /**
     * 删除操作员与组织的关系
     * 
     * @param opId
     * @param orgId
     * @return
     * @throws Exception
     */
    public int deleteRelOpOrg(long opId, long orgId) throws Exception {
        String sql = "delete from sso_rel_op_org where op_id = :opId and org_id = :orgId";
        
        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("opId", opId);
        params.put("orgId", orgId);
        
        return baseDao.executeSQL(sql.toString(), params);
    }
}
