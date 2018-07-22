/*
 * AppAccessService.java Created on 2017年3月25日 下午2:24:27
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

import com.hiwi.common.dao.Page;
import com.wst.service.sys.dto.AppAccessDTO;
import com.wst.service.sys.dto.AppIpDTO;

/**
 * 第三方应用接入Service
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public interface AppAccessService {

    /**
     * 根据appId获取接入信息
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    public AppAccessDTO getByAppId(String appId) throws Exception;

    /**
     * 获取白名单IP列表
     * 
     * @param accsId
     * @return
     * @throws Exception
     */
    public List<AppIpDTO> findWhiteIpList(long accsId) throws Exception;

    /*    *//**
             * 保存/更新第三方应用接入配置
             * 
             * @param appAccessDTO
             * @throws Exception
             */
    /*
     * public ResultDTO<String> saveorUpdate(AppAccessDTO appAccessDTO) throws
     * Exception;
     * 
     *//**
       * 更新应用秘钥
       * 
       * @param accsId
       * @param opId
       * @throws Exception
       */
    /*
     * public ResultDTO<String> upAppSecret(long accsId, long opId) throws
     * Exception
     */;

    /**
     * 第三方应用接入配置信息分页查询
     * 
     * @param pageParam
     * @param appAccess
     * @return
     * @throws Exception
     */
    public Page<AppAccessDTO> findPage(Page<AppAccessDTO> pageParam, AppAccessDTO appAccess) throws Exception;

    /**
     * 根据Id获取第三方应用接入配置信息（包括有效和禁用状态）
     * 
     * @param accsId
     * @return
     * @throws Exception
     */
    public AppAccessDTO getAllById(long accsId) throws Exception;

    /**
     * 根据appId获取第三方应用接入配置信息（包括有效和禁用状态）
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    public AppAccessDTO getAllByAppId(String appId) throws Exception;
}
