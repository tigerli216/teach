/*
 * AppIpService.java Created on 2017年3月29日 下午3:28:59
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
import com.wst.service.sys.dto.AppIpDTO;

/**
 * 第三方应用IP配置Service
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
public interface AppIpService {

    /**
     * 新增、更新第三方应用IP配置
     * 
     * @param appIpDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> saveOrUpdate(AppIpDTO appIpDTO) throws Exception;

    /**
     * 第三方应用IP配置信息分页查询
     * 
     * @param pageParam
     * @param appIpDTO
     * @return
     * @throws Exception
     */
    public Page<AppIpDTO> findPage(Page<AppIpDTO> pageParam, AppIpDTO appIpDTO) throws Exception;

    /**
     * 根据配置ID获取所有应用IP配置
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public AppIpDTO getAllIpByConfigId(long configId) throws Exception;
}
