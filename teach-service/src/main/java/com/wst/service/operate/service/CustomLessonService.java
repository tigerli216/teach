/*
 * CustomLessonService.java Created on 2017年9月27日 上午10:39:43
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
package com.wst.service.operate.service;

import java.util.List;
import java.util.Map;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.operate.dto.CustomLessonDTO;

/**
 * 运营定制辅导课程Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface CustomLessonService {

	/**
	 * 获取运营定制辅导课分页数据
	 * 
	 * @param pageParam
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomLessonDTO> findPaging(Page<CustomLessonDTO> pageParam, CustomLessonDTO customLessonDTO)
			throws Exception;

	/**
	 * 新增运营定制辅导课程
	 * 
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> save(CustomLessonDTO customLessonDTO) throws Exception;

	/**
	 * 修改运营定制辅导课程
	 * 
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> update(CustomLessonDTO customLessonDTO) throws Exception;

	/**
	 * 根据运营定制辅导课程ID删除运营定制辅导课程
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> delete(long lessonId) throws Exception;

	/**
	 * 获取运营定制辅导课程集合
	 * 
	 * @param param
	 * @param isHomePage
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findCustomLessons(Page<CustomLessonDTO> param, boolean isHomePage)
			throws Exception;

	/**
	 * 通过ID获取运营定制辅导课程信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public CustomLessonDTO getCustomLessonById(long lessonId) throws Exception;

	/**
	 * 修改课程说明
	 * 
	 * @param lessonId
	 * @param lessonRcmdStr
	 * @return
	 * @throws Exception
	 */
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception;

	/**
	 * 修改课程简介
	 * 
	 * @param lessonId
	 * @param lessonRcmdStr
	 * @return
	 * @throws Exception
	 */
	public int updateLessonAbstract(long lessonId, String lessonAbstract) throws Exception;
}
