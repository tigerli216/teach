/*
 * RegionSchoolDao.java Created on 2017年10月18日 上午10:32:14
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
import com.wst.service.sys.dto.RegionSchoolDTO;

/**
 * 地区学校Dao
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class RegionSchoolDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 通过学校ID获取地区信息
	 * 
	 * @param schoolId
	 * @return
	 * @throws Exception
	 */
	public RegionSchoolDTO getRegionById(long schoolId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select school_id, region_code, region_name, school_name, school_explain, ");
		sql.append("       school_addr, school_sort, create_time ");
		sql.append("  from sys_region_school ");
		sql.append(" where school_id = :schoolId ");

		Map<String, Long> params = new HashMap<>(1);
		params.put("schoolId", schoolId);

		return baseDao.get(sql.toString(), params, RegionSchoolDTO.class);
	}

	/**
	 * 通过学校名称获取学校信息
	 * 
	 * @param schoolName
	 * @return
	 * @throws Exception
	 */
	public RegionSchoolDTO getRegionSchoolByName(String schoolName) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select school_id, region_code, region_name, school_name, school_explain, ");
		sql.append("       school_addr, school_sort, create_time ");
		sql.append("  from sys_region_school ");
		sql.append(" where school_name = :schoolName ");

		Map<String, String> params = new HashMap<>(1);
		params.put("schoolName", schoolName);

		return baseDao.get(sql.toString(), params, RegionSchoolDTO.class);
	}

	/**
	 * 按照地区编码获取学校集合
	 * 
	 * @param regionSchoolDTO
	 * @return
	 * @throws Exception
	 */
	public List<RegionSchoolDTO> findRegionSchoolList(String regionName) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select school_id, region_code, school_name ");
		sql.append("  from sys_region_school ");
		sql.append(" where region_name = :regionName ");
		sql.append(" order by school_sort desc ");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("regionName", regionName);

		return baseDao.findList(sql.toString(), params, RegionSchoolDTO.class);
	}
}
