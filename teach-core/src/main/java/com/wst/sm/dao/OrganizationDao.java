/*
 * OrganizationDao.java Created on 2016年10月25日 下午8:17:40
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
import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.OrganizationDTO;

/**
 * 组织Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class OrganizationDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param organizationDTO
     * @return
     * @throws Exception
     */
    public long save(OrganizationDTO organizationDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_organization(org_code, org_type, org_name, org_desc, org_path, ");
        sql.append("       status, parent_id, create_op_id, create_time, modify_op_id, is_data_control, ");
        sql.append("       modify_time, org_level, supplier_id) ");
        sql.append(" value (:orgCode, :orgType, :orgName, :orgDesc, :orgPath, ");
        sql.append("       :status, :parentId, :createOpId, :createTime, :modifyOpId, :isDataControl, ");
        sql.append("       :modifyTime, :orgLevel, :supplierId)");
        
        return baseDao.insertInto(sql.toString(), organizationDTO, "org_id");
    }

    /**
     * 查询所有有效组织
     * 
     * @return
     * @throws Exception
     */
    public List<OrganizationDTO> findOrganizationList() throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select org_id, org_code, org_type, org_name, org_desc, org_path, ");
        sql.append("       status, parent_id, create_op_id, create_time, modify_op_id, ");
        sql.append("       is_data_control, modify_time, org_level, supplier_id ");
        sql.append("  from sso_organization");
        sql.append(" where status = 0");
        
        return baseDao.findList(sql.toString(), null, OrganizationDTO.class);
    }

    /**
     * 根据父ID查询所有有效组织
     * 
     * @return
     * @throws Exception
     */
    public List<OrganizationDTO> findOrganizationListByParentId(long parentId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select org_id, org_code, org_type, org_name, org_desc, org_path, status, ");
        sql.append("       parent_id, create_op_id, create_time, modify_op_id, is_data_control, ");
        sql.append("       modify_time, org_level, supplier_id ");
        sql.append("  from sso_organization");
        sql.append(" where status = 0 ");
        sql.append("   and parent_id = :parentId ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("parentId", parentId);
        
        return baseDao.findList(sql.toString(), null, OrganizationDTO.class);
    }

    /**
     * 根据主键查询组织信息
     * 
     * @return
     * @throws Exception
     */
    public OrganizationDTO getOrganizationByOrgId(long orgId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select org_id, org_code, org_type, org_name, org_desc, org_path, ");
        sql.append("       status, parent_id, create_op_id, create_time, modify_op_id, ");
        sql.append("       is_data_control, modify_time, org_level, supplier_id ");
        sql.append("  from sso_organization ");
        sql.append(" where status = 0 ");
        sql.append("   and org_id = :orgId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("orgId", orgId);
        
        return baseDao.get(sql.toString(), params, OrganizationDTO.class);
    }

    /**
     * 分页查询信息
     * 
     * @param pageParam
     *            分页参数
     * @param resourceDTO
     *            相关查询参数
     * @return
     * @throws Exception
     */
    public Page<OrganizationDTO> findOrganizationPaging(Page<OrganizationDTO> pageParam, OrganizationDTO organizationDTO)
            throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select org_id, org_code, org_type, org_name, org_desc, org_path, status, ");
        sql.append("       parent_id, create_op_id, create_time, modify_op_id, is_data_control, ");
        sql.append("       modify_time, org_level, supplier_id ");
        sql.append("  from sso_organization");
        sql.append(" where status = 0 ");
        
        if (organizationDTO.getParentId() > 0) {
            sql.append(" and parent_id = :parentId");
        }
        
        return baseDao.findPaging(sql.toString(), pageParam, organizationDTO, OrganizationDTO.class);
    }

    /**
     * 更新信息
     * 
     * @param organizationDTO
     * @return
     */
    public int updateOrganization(OrganizationDTO organizationDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update sso_organization ");
        sql.append("   set org_name = :orgName, is_data_control = :isDataControl, org_desc = :orgDesc ");
        sql.append(" where org_id = :orgId");
        
        return baseDao.executeSQL(sql.toString(), organizationDTO);
    }
}
