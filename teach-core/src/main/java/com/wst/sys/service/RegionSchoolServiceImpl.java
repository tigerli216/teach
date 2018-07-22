/*
 * RegionSchoolServiceImpl.java Created on 2017年10月18日 上午10:31:50
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
package com.wst.sys.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.RegionSchoolService;
import com.wst.sys.dao.RegionSchoolDao;

/**
 * 地区学校 service 实现类
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class RegionSchoolServiceImpl implements RegionSchoolService {

	@Resource
	private RegionSchoolDao regionSchoolDao;

	@Override
	public RegionSchoolDTO getRegionSchoolById(long schoolId) throws Exception {
		return regionSchoolDao.getRegionById(schoolId);
	}

	@Override
	public RegionSchoolDTO getRegionSchoolByName(String schoolName) throws Exception {
		return regionSchoolDao.getRegionSchoolByName(schoolName);
	}

	@Override
	public List<RegionSchoolDTO> findRegionSchoolList(String regionName) throws Exception {
		return regionSchoolDao.findRegionSchoolList(regionName);
	}

}
