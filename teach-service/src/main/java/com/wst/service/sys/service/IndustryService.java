/*
 * IndustryService.java Created on 2017年9月26日 上午11:31:37
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

import com.wst.service.sys.dto.IndustryDTO;

/**
 * 行业信息Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface IndustryService {

    /**
     * 通过ID获取行业信息
     * 
     * @param industryId
     * @return
     * @throws Exception
     */
    public IndustryDTO getIndustryById(long industryId) throws Exception;
    
    /**
     * 通过行业名称获取行业信息
     * 
     * @param industryId
     * @return
     * @throws Exception
     */
    public IndustryDTO getIndustryByName(String industryName) throws Exception;

    /**
     * 获取行业集合
     * 
     * @return
     * @throws Exception
     */
    public List<IndustryDTO> findIndustryList() throws Exception;
}
