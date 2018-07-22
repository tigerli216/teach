/*
 * AppIpServiceImpl.java Created on 2017年3月29日 下午3:31:23
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

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sys.dto.AppIpDTO;
import com.wst.service.sys.service.AppIpService;
import com.wst.sys.dao.AppIpDao;

/**
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class AppIpServiceImpl implements AppIpService {

    @Resource
    private AppIpDao appIpDao;

    @Override
    public ResultDTO<String> saveOrUpdate(AppIpDTO appIpDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<String>();
        if (appIpDTO.getConfigId() == 0) {
            appIpDao.save(appIpDTO);
        } else {
            appIpDao.update(appIpDTO);
        }
        return result.set(ResultCodeEnum.SUCCESS, "操作成功");
    }

    @Override
    public Page<AppIpDTO> findPage(Page<AppIpDTO> pageParam, AppIpDTO appIpDTO) throws Exception {
        return appIpDao.findPage(pageParam, appIpDTO);
    }

    @Override
    public AppIpDTO getAllIpByConfigId(long configId) throws Exception {
        return appIpDao.getAllIpByConfigId(configId);
    }

}
