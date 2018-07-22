/*
 * AdService.java Created on 2017年9月26日 下午8:24:18
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
package com.wst.service.operate.service;

import java.util.List;
import java.util.Map;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.operate.dto.AdDTO;

/**
 * 广告管理Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface AdService {

    /**
     * 获取广告分页数据
     * 
     * @param pageParam
     * @param adDTO
     * @return
     * @throws Exception
     */
    public Page<AdDTO> findPaging(Page<AdDTO> pageParam, AdDTO adDTO) throws Exception;

    /**
     * 新增广告
     * 
     * @param adDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> save(AdDTO adDTO) throws Exception;

    /**
     * 修改广告
     * 
     * @param adDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> update(AdDTO adDTO) throws Exception;

    /**
     * 根据广告ID删除广告
     * 
     * @param adId
     * @return
     * @throws Exception
     */
    public ResultDTO<String> delete(long adId) throws Exception;

    /**
     * 通过广告ID获取广告信息
     * 
     * @param adId
     * @return
     * @throws Exception
     */
    public AdDTO getAdById(long adId) throws Exception;

    /**
     * 获取首页广告列表
     * 
     * @param param
     * @param isHomePage
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findAds(Page<AdDTO> param, boolean isHomePage) throws Exception;
}
