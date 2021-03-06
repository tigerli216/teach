/*
 * CustomClassDateServiceImpl.java Created on 2017年10月11日 下午7:40:30
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
package com.wst.lesson.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.entity.lesson.CustomClassDate;
import com.wst.lesson.dao.CustomClassDateDao;
import com.wst.service.lesson.dto.CustomClassDateDTO;
import com.wst.service.lesson.service.CustomClassDateService;

/**
 * 课时约课记录服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class CustomClassDateServiceImpl implements CustomClassDateService {

    @Resource
    private CustomClassDateDao customClassDateDao;

    @Override
    public long save(CustomClassDate customClassDate) throws Exception {
        return customClassDateDao.save(customClassDate);
    }

    @Override
    public CustomClassDateDTO getCustomClassDateByClassId(long classId) throws Exception {
        return customClassDateDao.getCustomClassDateByClassId(classId);
    }

}
