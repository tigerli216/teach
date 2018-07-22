/*
 * LessonFeedbackController.java Created on 2017年10月12日 下午8:33:30
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
package com.wst.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.mem.dto.LessonFeedbackDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.LessonFeedbackService;

/**
 * 课程反馈控制层
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/API/student/feedback")
public class LessonFeedbackController {

    @Reference(version = "0.0.1")
    private LessonFeedbackService lessonFeedbackService;
    @Reference(version = "0.0.1")
    private LessonCustomService lessonCustomService;
    @Reference(version = "0.0.1")
    private LessonPublicService lessonPublicService;

    public ResultDTO<List<Map<String, Object>>> getFeedbackLessons(CommonParamsDTO dto) throws Exception {
        ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
        List<Map<String, Object>> resultList = new ArrayList<>();

        return result.set(ResultCodeEnum.SUCCESS, "", resultList);
    }

    /**
     * 课程反馈
     * 
     * @param dto
     * @param lessonId
     *            课程id
     * @param content
     *            反馈内容
     * @return
     * @throws Exception
     */
    @RequestMapping("lessonFeedback")
    public ResultDTO<String> lessonFeedback(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long lessonId,
            String content) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (lessonId <= 0) {
            // 请选择课程
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please choose the course");
        }
        if (content.length() > 1000) {
            // 内容超长
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Content super long");
        }
        UserDTO user = UserHelper.getUserDTO(dto);
        if (user == null) {
            return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
        }
        CustomDTO customDTO = lessonCustomService.getLessonById(lessonId);
        if (customDTO == null) {
            // 课程不存在
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Curriculum does not exist");
        }
        if (customDTO.getUserId() != user.getUserId()) {
            // 课程不存在
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Curriculum does not exist");
        }
        LessonFeedbackDTO lessonFeedbackDTO = new LessonFeedbackDTO();
        lessonFeedbackDTO.setUserId(user.getUserId());
        lessonFeedbackDTO.setContent(content);
        lessonFeedbackDTO.setLessonType(1);
        lessonFeedbackDTO.setLessonId(lessonId);
        lessonFeedbackDTO.setLessonName(customDTO.getLessonName());

        return lessonFeedbackService.addLessonFeedback(lessonFeedbackDTO);

    }

    /**
     * 获取用户可反馈的课程列表
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping("feedbackList")
    public ResultDTO<List<Map<String, Object>>> findNotFeedbackLesson(CommonParamsDTO dto) throws Exception {
        ResultDTO<List<Map<String, Object>>> resultDTO = new ResultDTO<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        LessonFeedbackDTO lessonFeedbackQuery = new LessonFeedbackDTO();
        UserDTO userDTO = UserHelper.getUserDTO(dto);
        if (userDTO == null) {
            return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
        }
        lessonFeedbackQuery.setUserId(userDTO.getUserId());
        List<LessonFeedbackDTO> list = lessonFeedbackService.findNotFeedbackLesson(null, lessonFeedbackQuery).getList();
        if (list != null && list.size() > 0) {
            for (LessonFeedbackDTO LessonFeedbackDTO : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("lessonId", LessonFeedbackDTO.getLessonId());
                map.put("lessonName", LessonFeedbackDTO.getLessonName());
                resultList.add(map);
            }
        }
        return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultList);
    }

}
