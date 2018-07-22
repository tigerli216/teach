/*
 * RegionServiceImpl.java Created on 2016年11月16日 下午2:56:53
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
package com.wst.sys.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.RegionService;
import com.wst.sys.dao.RegionDao;

/**
 * 区域Servcie实现接口
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class RegionServiceImpl implements RegionService {

	@Resource
	private RegionDao regionDao;

	@Override
	public List<RegionDTO> findRegionList() throws Exception {
		return regionDao.findRegionList();
	}

	@Override
	public List<RegionSchoolDTO> findSchoolList(String regionCode) throws Exception {
		return regionDao.findSchoolList(regionCode);
	}

	@Override
	public RegionDTO getRegionById(long regionId) throws Exception {
		return regionDao.getRegionById(regionId);
	}

	@Override
	public RegionDTO getRegionByName(String regionName) throws Exception {
		return regionDao.getRegionByName(regionName);
	}

}
