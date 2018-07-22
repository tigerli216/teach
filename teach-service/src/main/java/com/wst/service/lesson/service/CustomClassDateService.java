/*
 * CustomClassDateService.java Created on 2017年10月11日 下午7:39:39
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
package com.wst.service.lesson.service;

import com.wst.entity.lesson.CustomClassDate;
import com.wst.service.lesson.dto.CustomClassDateDTO;

/**
 * 课时约课记录接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface CustomClassDateService {

    /**
     * 保存约课信息
     * 
     * @param customClassDate
     *            约课信息
     * @return
     * @throws Exception
     */
    public long save(CustomClassDate customClassDate) throws Exception;
    
    /**
     * 根据课时ID获取最新的约课信息
     * 
     * @param classId
     *            课时ID
     * @return
     * @throws Exception
     */
    public CustomClassDateDTO getCustomClassDateByClassId(long classId) throws Exception;
}
