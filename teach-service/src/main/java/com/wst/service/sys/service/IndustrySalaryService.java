/*
 * IndustrySalaryService.java Created on 2017年9月26日 上午11:16:26
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

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sys.dto.IndustrySalaryDTO;

/**
 * 导师行业薪资管理Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface IndustrySalaryService {

    /**
     * 导师行业薪资-新增
     * 
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> addIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception;

    /**
     * 导师行业薪资配置-修改
     * 
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> updateIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception;

    /**
     * 导师行业薪资配置-分页
     * 
     * @param pageParam
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public Page<IndustrySalaryDTO> findPaging(Page<IndustrySalaryDTO> pageParam, IndustrySalaryDTO industrySalaryDTO)
            throws Exception;

    /**
     * 通过配置ID获取导师行业基础薪资信息
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalary(long configId) throws Exception;

    /**
     * 导师行业薪资配置-删除
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public int delete(long configId) throws Exception;

    /**
     * 通过code获取导师行业薪资信息
     * 
     * @param industryCode
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalaryByIndustryCode(String industryCode) throws Exception;

    /**
     * 通过Id获取导师行业薪资信息
     * 
     * @param industryId
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalaryByIndustryId(long industryId) throws Exception;

}
