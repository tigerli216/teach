/*
 * LessonCustomService.java Created on 2017年9月29日 上午10:38:58
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

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomDTO;

/**
 * 定制课接口 service
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public interface LessonCustomService {

	/**
	 * 分页查询定制课信息
	 * 
	 * @param page
	 * @param customDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomDTO> fingPaging(Page<CustomDTO> page, CustomDTO customDTO) throws Exception;

	/**
	 * 根据课程id获取课程信息
	 * 
	 * @param lessonId
	 *            课程id
	 * @return CustomDTO 默认返回产品信息的对象（包含产品各个参数）
	 * @throws Exception
	 */
	public CustomDTO getLessonById(long lessonId) throws Exception;

	/**
	 * 添加课程
	 * 
	 * @param customDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> addLesson(CustomDTO customDTO) throws Exception;

	/**
	 * 修改课程信息
	 * 
	 * @param customDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> updateLesson(CustomDTO customDTO) throws Exception;

	/**
	 * 根据导师id分页查询定制课课程信息
	 * 
	 * @param page
	 * @param customDTO
	 *            tutorId 导师ID
	 * @return
	 * @throws Exception
	 */
	public Page<CustomDTO> fingPagingByTutorId(Page<CustomDTO> page, CustomDTO customDTO) throws Exception;

	/**
	 * 购买课程
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public int buyCustom(long lessonId) throws Exception;

	/**
	 * 更新课程说明(富文本)
	 * 
	 * @param lessonId
	 * @param lessonRcmd
	 * @return
	 * @throws Exception
	 */
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception;

	/**
	 * 通过课程名称获取课程信息
	 * 
	 * @param lessonName
	 * @return
	 * @throws Exception
	 */
	public CustomDTO getLessonByLessonName(String lessonName) throws Exception;

	/**
	 * 通过课程ID获取课时集合
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findLessonClassList(long lessonId) throws Exception;
}
