/*
 * BusiConfigService.java Created on 2016年10月16日 下午5:36:19
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

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sys.dto.BusiConfigDTO;

/**
 * 公用业务配置Service
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public interface BusiConfigService {

    /**
     * 获取配置集合
     * 
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public List<BusiConfigDTO> getConfigList(BusiConfigDTO busiConfigDTO) throws Exception;

    /**
     * 根据业务编码获取配置集合
     * 
     * @param busiCode
     *            业务编码
     * @return
     * @throws Exception
     */
    public List<BusiConfigDTO> findConfigList(String busiCode) throws Exception;

    /**
     * 业务系统配置分页列表
     * 
     * @param page
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public Page<BusiConfigDTO> findPaging(Page<BusiConfigDTO> page, BusiConfigDTO busiConfigDTO) throws Exception;

    /**
     * 保存
     * 
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> save(BusiConfigDTO busiConfigDTO) throws Exception;

    /**
     * 修改
     * 
     * @param busiConfigDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> update(BusiConfigDTO busiConfigDTO) throws Exception;

    /**
     * 修改状态
     * 
     * @param status
     * @param configId
     * @return
     * @throws Exception
     */
    public int upConfigStatus(long status, long configId) throws Exception;

    /**
     * 通过配置ID获取系统业务配置信息
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public BusiConfigDTO getBusiConfigBuId(long configId) throws Exception;

    /**
     * 通过业务类型获取配置信息
     * 
     * @param busiType
     * @return
     * @throws Exception
     */
    public BusiConfigDTO getConfigByBusiType(long busiType) throws Exception;
}
