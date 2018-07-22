/*
 * LessonFileOnloadSericeImpl.java Created on 2017年11月23日 下午4:54:13
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

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.lesson.dao.LessonFileOnloadDao;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.LessoonFileOnloadService;

/**
 * 
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class LessonFileOnloadSericeImpl implements LessoonFileOnloadService {

    @Resource
    private LessonFileOnloadDao lessonFileOnloadDao;

    @Override
    public ResultDTO<String> lessonFileOnload(long lessonId, long classId, String courSewarePic) throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        lessonFileOnloadDao.findLessonClass(lessonId, classId, courSewarePic);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
    }

    @Override
    public List<PublicDTO> findPublicLessonList() throws Exception {
        return lessonFileOnloadDao.findPublicLessonList();
    }

    @Override
    public List<PublicClassDTO> findPublicClassList(long lessonId) throws Exception {
        return lessonFileOnloadDao.findPublicClassList(lessonId);
    }
}
