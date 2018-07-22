/*
 * IndustryServiceImpl.java Created on 2017年9月26日 上午11:34:11
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
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.service.IndustryService;
import com.wst.sys.dao.IndustryDao;

/**
 * 行业信息Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class IndustryServiceImpl implements IndustryService{

	@Resource
	private IndustryDao industryDao;
	
	@Override
	public IndustryDTO getIndustryById(long industryId) throws Exception {
		return industryDao.getIndustryById(industryId);
	}
	
	@Override
    public IndustryDTO getIndustryByName(String industryName) throws Exception {
        return industryDao.getIndustryByName(industryName);
    }

	@Override
	public List<IndustryDTO> findIndustryList() throws Exception {
		return industryDao.findIndustryList();
	}
}
