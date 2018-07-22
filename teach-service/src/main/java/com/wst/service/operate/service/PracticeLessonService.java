/*
 * PracticeLessonService.java Created on 2017年9月27日 下午2:46:49
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
import com.wst.service.operate.dto.PracticeLessonDTO;

/**
 * 运营实习职位Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface PracticeLessonService {

	/**
	 * 获取运营实习职位分页数据
	 * 
	 * @param pageParam
	 * @param practiceLessonDTO
	 * @return
	 * @throws Exception
	 */
	public Page<PracticeLessonDTO> findPaging(Page<PracticeLessonDTO> pageParam, PracticeLessonDTO practiceLessonDTO)
			throws Exception;

	/**
	 * 新增运营实习职位
	 * 
	 * @param practiceLessonDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> save(PracticeLessonDTO practiceLessonDTO) throws Exception;

	/**
	 * 修改运营实习职位
	 * 
	 * @param practiceLessonDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> update(PracticeLessonDTO practiceLessonDTO) throws Exception;

	/**
	 * 根据运营实习职位ID删除运营实习职位
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> delete(long lessonId) throws Exception;

	/**
	 * 获取运营实习职位集合
	 * 
	 * @param param
	 * @param practiceLessonDTO
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findPracticeLessons(Page<PracticeLessonDTO> param,
			PracticeLessonDTO practiceLessonDTO) throws Exception;

	/**
	 * 根据ID获取运营实习职位信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public PracticeLessonDTO getPracticeLessonById(long lessonId) throws Exception;

	/**
	 * 更新富文本
	 * 
	 * @param lessonId
	 * @param companyRcmdStr
	 * @param practiceRcmdStr
	 * @return
	 * @throws Exception
	 */
	public int updateRcmdStr(long lessonId, String companyRcmdStr, String practiceRcmdStr) throws Exception;
}
