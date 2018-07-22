/*
 * ActivityExtractServiceImpl.java Created on 2017年9月28日 下午3:58:58
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
package com.wst.act.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.RandomUtils;
import com.wst.act.dao.ActivityExtractDao;
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.act.service.ActivityExtractService;

/**
 * 活动提取码配置Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class ActivityExtractServiceImpl implements ActivityExtractService {

	@Resource
	private ActivityExtractDao activityExtractDao;

	@Override
	public Page<ActivityExtractDTO> findPaging(Page<ActivityExtractDTO> pageParam,
			ActivityExtractDTO activityExtractDTO) throws Exception {
		return activityExtractDao.findPaging(pageParam, activityExtractDTO);
	}

	@Override
	public ResultDTO<String> createActivityExtract(ActivityExtractDTO activityExtractDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		// 要生成的提取码数量
		int size = activityExtractDTO.getActivityExtractNum();
		Map<String, String> map = new HashMap<>(size);
		createCode(size, map);

		size = activityExtractDTO.getActivityExtractNum() - map.size();
		while (size > 0) {
			createCode(size, map);
			size = activityExtractDTO.getActivityExtractNum() - map.size();
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			activityExtractDTO.setExtractCode(entry.getKey());
			activityExtractDao.saveActivityExtract(activityExtractDTO);
		}

		return result.set(ResultCodeEnum.SUCCESS, "生成活动提取码成功");
	}

	/**
	 * 生成提取码
	 * 
	 * @param size
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createCode(int size, Map<String, String> map) throws Exception {
		for (int i = 0; i < size; i++) {
			// 生成提取码
			String extractCode = RandomUtils.generateString(6).toUpperCase();
			ActivityExtractDTO activityExtract = activityExtractDao.getByExtractCode(extractCode);
			if (activityExtract == null) {
				map.put(extractCode, "");
			}
		}
		return map;
	}

	@Override
	public ActivityExtractDTO getByExtractCode(String extractCode) throws Exception {
		return activityExtractDao.getByExtractCode(extractCode);
	}

	@Override
	public int useExtractCode(long userId, String userIp, long configId) throws Exception {
		return activityExtractDao.useExtractCode(userId, userIp, configId);
	}

	@Override
	public List<ActivityExtractDTO> findByLessonId(long lessonId) throws Exception {
		return activityExtractDao.findByLessonId(lessonId);
	}

	@Override
	public List<ActivityExtractDTO> findBylessonIduserId(long lessonId, long userId) throws Exception {
		return activityExtractDao.findBylessonIduserId(lessonId, userId);
	}
}
