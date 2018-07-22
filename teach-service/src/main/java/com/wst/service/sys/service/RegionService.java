/*
 * RegionService.java Created on 2016年11月16日 下午2:54:44
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
package com.wst.service.sys.service;

import java.util.List;

import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;

/**
 * Region 区域Servcie接口
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
public interface RegionService {

    /**
     * 获取地区
     * 
     * @return
     * @throws Exception
     */
    public List<RegionDTO> findRegionList() throws Exception;

    /**
     * 根据地区查询学校
     * 
     * @param regionCode
     * @return
     * @throws Exception
     */
    public List<RegionSchoolDTO> findSchoolList(String regionCode) throws Exception;

    /**
     * 通过地区ID获取用户信息
     * 
     * @param regionId
     * @return
     * @throws Exception
     */
    public RegionDTO getRegionById(long regionId) throws Exception;

    /**
     * 通过地区名称获取地区信息
     * 
     * @param regionName
     * @return
     * @throws Exception
     */
    public RegionDTO getRegionByName(String regionName) throws Exception;
}
