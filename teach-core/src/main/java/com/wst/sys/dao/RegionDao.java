/*
 * RegionDao.java Created on 2016年11月16日 下午2:38:12
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

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;

/**
 * 地区Region Dao
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Repository
public class RegionDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 获取地区
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RegionDTO> findRegionList() throws Exception {
	    String sql = "select region_id, region_name, region_code, time_zone from sys_region";

		return baseDao.findList(sql, null, RegionDTO.class);
	}

	/**
	 * 根据地区查询学校
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RegionSchoolDTO> findSchoolList(String regionCode) throws Exception {
	    StringBuilder sql = new StringBuilder();

		sql.append("select school_id, region_code, region_name, school_name, ");
		sql.append("       school_explain, school_addr, school_sort ");
		sql.append("  from sys_region_school ");
		sql.append(" where region_code = :regionCode ");
		sql.append(" order by school_sort desc ");

		Map<String, String> params = new HashMap<>(1);
		params.put("regionCode", regionCode);

		return baseDao.findList(sql.toString(), params, RegionSchoolDTO.class);
	}

	/**
	 * 通过地区ID获取地区信息
	 * 
	 * @param regionId
	 * @return
	 * @throws Exception
	 */
	public RegionDTO getRegionById(long regionId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select region_id, region_name, region_code, time_zone ");
		sql.append("  from sys_region ");
		sql.append(" where region_id = :regionId ");

		Map<String, Long> params = new HashMap<>(1);
		params.put("regionId", regionId);

		return baseDao.get(sql.toString(), params, RegionDTO.class);
	}
	
	/**
	 * 通过地区名称获取地区信息
	 * 
	 * @param regionName
	 * @return
	 * @throws Exception
	 */
	public RegionDTO getRegionByName(String regionName) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select region_id, region_name, region_code, time_zone ");
		sql.append("  from sys_region");
		sql.append(" where region_name = :regionName ");

		Map<String, String> params = new HashMap<>(1);
		params.put("regionName", regionName);

		return baseDao.get(sql.toString(), params, RegionDTO.class);
	}
}
