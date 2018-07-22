/*
 * ResourceDao.java Created on 2016年10月26日 下午9:53:10
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
import com.wst.service.sm.dto.ResourceDTO;

/**
 * 功能菜单Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class ResourceDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long save(ResourceDTO resourceDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_resource(parent_id, resource_title, resource_type, resource_code, ");
        sql.append("       resource_path, menu_url, menu_image, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, sys_id, menu_sort) ");
        sql.append(" value (:parentId, :resourceTitle, :resourceType, :resourceCode, ");
        sql.append("        :resourcePath, :menuUrl, :menuImage, :status, :createOpId, :createTime, ");
        sql.append("        :modifyOpId, :modifyTime, :sysId, :menuSort) ");
        
        return baseDao.insertInto(sql.toString(), resourceDTO, "resource_id");
    }

    /**
     * 查询所有菜单
     * 
     * @return
     * @throws Exception
     */
    public List<ResourceDTO> findResourceList() throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select resource_id, parent_id, resource_title, resource_type, resource_code, ");
        sql.append("       resource_path, menu_url, menu_image, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, sys_id, menu_sort ");
        sql.append("  from sso_resource ");
        sql.append(" where status = 0 ");
        sql.append(" order by parent_id, menu_sort ");
        
        return baseDao.findList(sql.toString(), null, ResourceDTO.class);
    }

    /**
     * 根据操作员ID查询操作员权限
     * 
     * @param opId
     *            操作员ID
     * @return
     * @throws Exception
     */
    public List<ResourceDTO> getByOpId(long opId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select distinct res.resource_id, parent_id, resource_title, resource_type, ");
        sql.append("       resource_path, menu_url, menu_image, res.status, sys_id, menu_sort ");
        sql.append("  from sso_resource res ");
        sql.append("  left join sso_rel_res_role resrole on res.resource_id = resrole.resource_id");
        sql.append("  left join sso_rel_op_role oprole on resrole.role_id = oprole.role_id");
        sql.append(" where (resource_type != 3 ");
        sql.append("   and res.status = 0 ");
        sql.append("   and resrole.status = 0 ");
        sql.append("   and oprole.status = 0 ");
        sql.append("   and oprole.op_id = :opId) ");
        sql.append("    or (1 = :opId) ");
        sql.append(" order by parent_id, menu_sort asc");
        
        Map<String, Long> params = new HashMap<>(1);
        params.put("opId", opId);
        
        return baseDao.findList(sql.toString(), params, ResourceDTO.class);
    }

    /**
     * 根据主键查询信息
     * 
     * @param resourceId
     * @return
     * @throws Exception
     */
    public ResourceDTO getByResourceId(long resourceId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select resource_id, parent_id, resource_title, resource_type, resource_code, ");
        sql.append("       resource_path, menu_url, menu_image, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, sys_id, menu_sort ");
        sql.append("  from sso_resource ");
        sql.append(" where resource_id = :resourceId");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("resourceId", resourceId);
        
        return baseDao.get(sql.toString(), params, ResourceDTO.class);
    }

    /**
     * 根据父ID查询子信息
     * 
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<ResourceDTO> findResourceListByParentId(long parentId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select resource_id, parent_id, resource_title, resource_type, resource_code, ");
        sql.append("       resource_path, menu_url, menu_image, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, sys_id, menu_sort ");
        sql.append("  from sso_resource ");
        sql.append(" where status = 0 ");
        sql.append("   and parent_id = :parentId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("parentId", parentId);
        
        return baseDao.findList(sql.toString(), params, ResourceDTO.class);
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
    public Page<ResourceDTO> findOResourcePaging(Page<ResourceDTO> pageParam, ResourceDTO resourceDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select resource_id, parent_id, resource_title, resource_type, resource_code, ");
        sql.append("       resource_path, menu_url, menu_image, status, create_op_id, create_time, ");
        sql.append("       modify_op_id, modify_time, sys_id, menu_sort ");
        sql.append("  from sso_resource ");
        sql.append(" where status = 0 ");
        
        if (resourceDTO.getParentId() > 0) {
            sql.append(" and parent_id = :parentId");
        }
        
        sql.append(" order by menu_sort asc ");
        
        return baseDao.findPaging(sql.toString(), pageParam, resourceDTO, ResourceDTO.class);
    }

    /**
     * 校验功能名称是否存在
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long checkResourceTitle(ResourceDTO resourceDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select count(1) ");
        sql.append("  from sso_resource ");
        sql.append(" where status = 0 ");
        sql.append("   and resource_title = :resourceTitle ");
        
        if (resourceDTO.getResourceId() > 0) {
            sql.append(" and resource_id != :resourceId");
        }
        
        return Long.valueOf(baseDao.get(sql.toString(), resourceDTO, String.class));
    }

    /**
     * 校验功能路径是否存在
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long checkResourceUrl(ResourceDTO resourceDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select count(1) ");
        sql.append("  from sso_resource ");
        sql.append(" where status = 0 ");
        sql.append("   and menu_url = :menuUrl ");
        
        if (resourceDTO.getResourceId() > 0) {
            sql.append(" and resource_id != :resourceId");
        }
        
        return Long.valueOf(baseDao.get(sql.toString(), resourceDTO, String.class));
    }

    /**
     * 修改信息
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public int updateResource(ResourceDTO resourceDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update sso_resource ");
        sql.append("   set resource_title = :resourceTitle, menu_url = :menuUrl, ");
        sql.append("       menu_sort = :menuSort, modify_op_id = :modifyOpId, ");
        sql.append("       modify_time = :modifyTime, menu_image = :menuImage ");
        sql.append(" where resource_id = :resourceId");
        
        return baseDao.executeSQL(sql.toString(), resourceDTO);
    }

    /**
     * 修改权限功能顺序
     * 
     * @param resourceId
     * @param menuSort
     * @return
     * @throws Exception
     */
    public int updateMenuSort(long resourceId, long menuSort) throws Exception {
        String sql = "update sso_resource set menu_sort = :menuSort where resource_id = :resourceId";
        
        Map<String, Long> parem = new HashMap<String, Long>(2);
        parem.put("menuSort", menuSort);
        parem.put("resourceId", resourceId);
        
        return baseDao.executeSQL(sql.toString(), parem);
    }

    /**
     * 根据主键删除功能菜单
     * 
     * @param resourceId
     * @return
     * @throws Exception
     */
    public int deleteResource(long resourceId) throws Exception {
        String sql = "delete from sso_resource where resource_id = :resourceId";

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("resourceId", resourceId);
        
        return baseDao.executeSQL(sql, params);
    }

    /**
     * 查询排序最后一条数据
     * 
     * @return
     * @throws Exception
     */
    public long getLastResourceByMenuSort() throws Exception {
        String sql = "select menu_sort from sso_resource order by menu_sort desc limit 1";
        return baseDao.get(sql, null, Long.class);
    }
}
