/*
 * AppVersionsService.java Created on 2017年10月18日 下午3:39:12
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
package com.wst.service.app.service;

import java.util.List;

import com.wst.service.app.dto.AppVersionsDTO;

/**
 * app版本接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface AppVersionsService {

    /**
     * 根据类型，操作系统获取版本信息
     * 
     * @param userType
     *            用户类型
     * @param osname
     *            操作系统
     * @return
     * @throws Exception
     */
    public List<AppVersionsDTO> findByUtypeAndOsname(long userType, long osname) throws Exception;

}
