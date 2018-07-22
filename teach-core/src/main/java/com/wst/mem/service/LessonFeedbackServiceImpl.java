/*
 * LessonFeedbackServiceImpl.java Created on 2017年10月10日 下午5:39:07
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
package com.wst.mem.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.mem.dao.LessonFeedbackDao;
import com.wst.service.mem.dto.LessonFeedbackDTO;
import com.wst.service.mem.service.LessonFeedbackService;

/**
 * 用户课程反馈Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class LessonFeedbackServiceImpl implements LessonFeedbackService {

    @Resource
    private LessonFeedbackDao lessonFeedbackDao;

    @Override
    public Page<LessonFeedbackDTO> findPaging(Page<LessonFeedbackDTO> pageParam, LessonFeedbackDTO lessonFeedbackDTO)
            throws Exception {
        return lessonFeedbackDao.findPaging(pageParam, lessonFeedbackDTO);
    }

    @Override
    public ResultDTO<String> addLessonFeedback(LessonFeedbackDTO lessonFeedbackDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        // // 校验同一用户是否反馈过
        // LessonFeedbackDTO feedQuery = new LessonFeedbackDTO();
        // feedQuery.setUserId(lessonFeedbackDTO.getUserId());
        // feedQuery.setLessonType(lessonFeedbackDTO.getLessonType());
        // feedQuery.setLessonId(lessonFeedbackDTO.getLessonId());
        // List<LessonFeedbackDTO> list = lessonFeedbackDao.findPaging(null,
        // feedQuery).getList();
        // if (list != null && list.size() > 0) {
        // return result.set(ResultCodeEnum.ERROR_HANDLE, "Please do not repeat
        // the feedback");
        // }
        // 保存反馈信息
        lessonFeedbackDao.save(lessonFeedbackDTO);
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    @Override
    public Page<LessonFeedbackDTO> findNotFeedbackLesson(Page<LessonFeedbackDTO> pageParam,
            LessonFeedbackDTO lessonFeedbackDTO) throws Exception {
        return lessonFeedbackDao.findNotFeedbackLesson(pageParam, lessonFeedbackDTO);
    }

}
