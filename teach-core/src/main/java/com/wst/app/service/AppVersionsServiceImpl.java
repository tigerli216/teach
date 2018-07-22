/*
 * AppVersionsServiceImpl.java Created on 2017年10月18日 下午3:40:40
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
package com.wst.app.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.app.dao.AppVersionsDao;
import com.wst.service.app.dto.AppVersionsDTO;
import com.wst.service.app.service.AppVersionsService;

/**
 * app版本信息服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class AppVersionsServiceImpl implements AppVersionsService {

    @Resource
    private AppVersionsDao appVersionsDao;

    @Override
    public List<AppVersionsDTO> findByUtypeAndOsname(long userType, long osname) throws Exception {
        return appVersionsDao.findByUtypeAndOsname(userType, osname);
    }
}
