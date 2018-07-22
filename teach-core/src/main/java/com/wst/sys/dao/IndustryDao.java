/*
 * IndustryDao.java Created on 2017年9月26日 上午11:36:34
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
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

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.service.sys.dto.IndustryDTO;

/**
 * 行业信息DAO
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class IndustryDao {

	@Resource
	private BaseDao baseDao;
	
	/**
	 * 通过ID获取行业信息
	 * 
	 * @param IndustryId
	 * @return
	 * @throws Exception
	 */
	public IndustryDTO getIndustryById(long industryId) throws Exception{
		StringBuilder sql = new StringBuilder();
		
		sql.append("select industry_id, industry_type, industry_name, ");
		sql.append("	   industry_code, create_time ");
		sql.append("  from sys_industry ");
		sql.append(" where industry_id = :industryId ");
		
		Map<String, Long> params = new HashMap<>(1);
		params.put("industryId", industryId);
		
		return baseDao.get(sql.toString(), params, IndustryDTO.class);
	}
	
	/**
     * 通过行业名称获取行业信息
     * 
     * @param IndustryId
     * @return
     * @throws Exception
     */
    public IndustryDTO getIndustryByName(String industryName) throws Exception{
        StringBuilder sql = new StringBuilder();
        
        sql.append("select industry_id, industry_type, industry_name, ");
        sql.append("       industry_code, create_time ");
        sql.append("  from sys_industry ");
        sql.append(" where industry_name = :industryName ");
        
        Map<String, String> params = new HashMap<>(1);
        params.put("industryName", industryName);
        
        return baseDao.get(sql.toString(), params, IndustryDTO.class);
    }
	
	/**
	 * 获取行业集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<IndustryDTO> findIndustryList() throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select industry_id, industry_type, industry_name, ");
		sql.append("	   industry_code, create_time ");
		sql.append("  from sys_industry ");
		
		return baseDao.findList(sql.toString(), null, IndustryDTO.class);
	}
}
