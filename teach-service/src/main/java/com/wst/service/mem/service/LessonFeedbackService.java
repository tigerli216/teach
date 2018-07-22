/*
 * LessonFeedbackService.java Created on 2017年10月10日 下午5:37:06
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
package com.wst.service.mem.service;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.mem.dto.LessonFeedbackDTO;

/**
 * 用户课程反馈Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface LessonFeedbackService {

    /**
     * 获取用户课程反馈管理分页数据
     * 
     * @param pageParam
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public Page<LessonFeedbackDTO> findPaging(Page<LessonFeedbackDTO> pageParam, LessonFeedbackDTO lessonFeedbackDTO)
            throws Exception;

    /**
     * 添加课程反馈
     * 
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public ResultDTO<String> addLessonFeedback(LessonFeedbackDTO lessonFeedbackDTO) throws Exception;
    
    /**
     * 获取用户可反馈的课程列表
     * 
     * @param pageParam
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public Page<LessonFeedbackDTO> findNotFeedbackLesson(Page<LessonFeedbackDTO> pageParam,
            LessonFeedbackDTO lessonFeedbackDTO) throws Exception;
}
