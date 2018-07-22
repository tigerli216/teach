/*
 * ActivityExtractService.java Created on 2017年9月28日 下午3:56:05
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
package com.wst.service.act.service;

import java.util.List;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.act.dto.ActivityExtractDTO;

/**
 * 活动提取码配置Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface ActivityExtractService {

	/**
	 * 活动提取码信息分页数据
	 * 
	 * @param pageParam
	 * @param activityExtractDTO
	 * @return
	 * @throws Exception
	 */
	public Page<ActivityExtractDTO> findPaging(Page<ActivityExtractDTO> pageParam,
			ActivityExtractDTO activityExtractDTO) throws Exception;

	/**
	 * 生成活动提取码
	 * 
	 * @param activityExtractDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> createActivityExtract(ActivityExtractDTO activityExtractDTO) throws Exception;

	/**
	 * 通过活动提取码获取提取码信息
	 * 
	 * @param extractCode
	 * @return
	 * @throws Exception
	 */
	public ActivityExtractDTO getByExtractCode(String extractCode) throws Exception;

	/**
	 * 使用提取码
	 * 
	 * @param userId
	 * @param userIp
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	public int useExtractCode(long userId, String userIp, long configId) throws Exception;
	
	/**
	 * 通过课程ID获取提取码列表
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<ActivityExtractDTO> findByLessonId(long lessonId) throws Exception;
	
	/**
     * 通过用户ID和课程ID获取在有效期内的提取码
     * 
     * @param lessonId
     * @param userId
     * @return
     * @throws Exception
     */
	public List<ActivityExtractDTO> findBylessonIduserId(long lessonId, long userId) throws Exception;
}
