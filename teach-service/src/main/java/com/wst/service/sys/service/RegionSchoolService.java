/*
 * RegionSchoolService.java Created on 2017年10月18日 上午10:29:44
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
package com.wst.service.sys.service;

import java.util.List;

import com.wst.service.sys.dto.RegionSchoolDTO;

/**
 * 地区学校service
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public interface RegionSchoolService {

    /**
     * 通过ID获取学校信息
     * 
     * @param schoolId
     * @return
     * @throws Exception
     */
    public RegionSchoolDTO getRegionSchoolById(long schoolId) throws Exception;

    /**
     * 通过学校名称获取学校信息
     * 
     * @param schoolName
     * @return
     * @throws Exception
     */
    public RegionSchoolDTO getRegionSchoolByName(String schoolName) throws Exception;
    
    /**
     * 通过地区名称获取学校集合
     * 
     * @param regionCode
     * @return
     * @throws Exception
     */
    public List<RegionSchoolDTO> findRegionSchoolList(String regionName) throws Exception;
}
