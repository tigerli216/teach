/*
 * ProductDao.java Created on 2016年9月25日 下午2:25:04
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
package com.wst.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.sys.dto.BusiConfigDTO;

/**
 * 公用业务配置Dao
 * 
 * @author <a href="mailto:luozj@hiwitech.com">luozj</a>
 * @version 1.0
 */
@Repository
public class BusiConfigDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 新增配置信息
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public long save(BusiConfigDTO busiConfigDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into sys_busi_config (busi_name, busi_code, busi_type, status, params) ");
        sql.append("values (:busiName, :busiCode, :busiType, 2, :params) ");

        return baseDao.insertInto(sql.toString(), busiConfigDTO, "config_id");
    }

    /**
     * 更新数据
     * 
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public int update(BusiConfigDTO busiConfigDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_busi_config ");
        sql.append("   set busi_name = :busiName, params = :params, ");
        sql.append("       busi_code = :busiCode, busi_type = :busiType ");
        sql.append(" where config_id = :configId ");

        return baseDao.executeSQL(sql.toString(), busiConfigDTO);
    }

    /**
     * 更细状态
     * 
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public int updateStatus(long status, long configId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_busi_config ");
        sql.append("   set status = :status ");
        sql.append(" where config_id = :configId ");

        Map<String, Long> params = new HashMap<>(2);
        params.put("status", status);
        params.put("configId", configId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 查询共用业务配置信息
     * 
     * @param busiConfigDTO
     *            请求参数对象
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_sys_busi_config")
    public List<BusiConfigDTO> findBusiConfigList(BusiConfigDTO busiConfigDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select config_id, busi_name, busi_code, busi_type, status, params ");
        sql.append("  from sys_busi_config ");
        sql.append(" where status = 1 ");
        
        if (StringUtils.isNotEmpty(busiConfigDTO.getBusiCode())) {
            sql.append(" and busi_code = :busiCode ");
        }
        if (busiConfigDTO.getBusiType() > 0) {
            sql.append(" and busi_type = :busiType ");
        } else {
            sql.append(" and busi_type in(1, 2) ");
        }
        sql.append(" order by priority desc ");
 
        Map<String, Object> params = new HashMap<>(2);
        params.put("busiCode", busiConfigDTO.getBusiCode());
        params.put("busiType", busiConfigDTO.getBusiType());
        
        return baseDao.findList(sql.toString(), params, BusiConfigDTO.class);
    }

    /**
     * 分页查询共用业务配置列表
     * 
     * @param page
     * @param busiConfigReq
     * @return
     * @throws Exception
     */
    public Page<BusiConfigDTO> findPaging(Page<BusiConfigDTO> page, BusiConfigDTO busiConfigDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select config_id, busi_name, busi_code, ");
        sql.append("	   busi_type, status, params, priority ");
        sql.append("  from sys_busi_config ");
        sql.append(" where 1 = 1 ");

        if (busiConfigDTO != null) {
            if (StringUtils.isNotEmpty(busiConfigDTO.getBusiName())) {
                sql.append(" and busi_name like concat('%', :busiName, '%') ");
            }
            if (StringUtils.isNotEmpty(busiConfigDTO.getBusiCode())) {
                sql.append(" and busi_code like concat('%', :busiCode, '%') ");
            }
            if (busiConfigDTO.getBusiType() > 0) {
                sql.append(" and busi_type = :busiType ");
            } else {
                sql.append(" and busi_type in(1,2) ");
            }
            if (busiConfigDTO.getStatus() > 0) {
                sql.append(" and status = :status ");
            }
        }

        return baseDao.findPaging(sql.toString(), page, busiConfigDTO, BusiConfigDTO.class);
    }

    /**
     * 查询共用业务配置信息
     * 
     * @param busiConfigDTO
     *            请求参数对象
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_sys_busi_config")
    public List<BusiConfigDTO> findBusiConfigList(String busiCode) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select config_id, busi_name, busi_code, busi_type, status, params, priority ");
        sql.append("  from sys_busi_config ");
        sql.append(" where status = 1 ");
        sql.append("   and busi_code = :busiCode ");
        sql.append(" order by priority desc ");
        
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("busiCode", busiCode);
        
        return baseDao.findList(sql.toString(), params, BusiConfigDTO.class);
    }

    /**
     * 通过ID获取配置信息
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public BusiConfigDTO getBusiConfigById(long configId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select config_id, busi_name, busi_code, ");
        sql.append("	   busi_type, status, params, priority ");
        sql.append("  from sys_busi_config ");
        sql.append(" where config_id = :configId ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("configId", configId);

        return baseDao.get(sql.toString(), params, BusiConfigDTO.class);
    }

    /**
     * 通过业务类型获开启的取配置信息
     * 
     * @param busiType
     * @return
     * @throws Exception
     */
    public BusiConfigDTO getConfigByBusiType(long busiType) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select config_id, busi_name, busi_code, ");
        sql.append("	   busi_type, status, params, priority ");
        sql.append("  from sys_busi_config ");
        sql.append(" where busi_type = :busiType ");
        sql.append("   and status = 1 ");

        Map<String, Long> params = new HashMap<>(1);
        params.put("busiType", busiType);

        return baseDao.get(sql.toString(), params, BusiConfigDTO.class);
    }
}
