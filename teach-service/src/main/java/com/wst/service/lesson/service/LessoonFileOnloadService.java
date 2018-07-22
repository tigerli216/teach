/*
 * LessoonFileOnload.java Created on 2017年11月23日 下午2:44:24
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

import java.util.List;

import com.hiwi.common.dto.ResultDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;

/**
 * 课程文件上传接口
 * 
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
public interface LessoonFileOnloadService {

    
    /**
     * 
     * 课程文件上传
     */
    
    public ResultDTO<String> lessonFileOnload(long lessonId , long classId , String courSewarePic) throws Exception;
    
    /**
     * 未开始的直播课
     *
     */
    public List<PublicDTO> findPublicLessonList() throws Exception;
    
    /**
     * 依据课程ID查询课时信息
     */
    public List<PublicClassDTO> findPublicClassList(long lessonId) throws Exception;
}
