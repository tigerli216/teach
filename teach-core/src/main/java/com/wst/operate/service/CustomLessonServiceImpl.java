/*
 * CustomLessonServiceImpl.java Created on 2017年9月27日 上午10:45:00
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
package com.wst.operate.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.StringUtils;
import com.wst.operate.dao.CustomLessonDao;
import com.wst.service.operate.dto.CustomLessonDTO;
import com.wst.service.operate.service.CustomLessonService;

/**
 * 运营定制辅导课程Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class CustomLessonServiceImpl implements CustomLessonService {

	@Resource
	private CustomLessonDao customLessonDao;

	@Override
	public Page<CustomLessonDTO> findPaging(Page<CustomLessonDTO> pageParam, CustomLessonDTO customLessonDTO)
			throws Exception {
		Page<CustomLessonDTO> page = customLessonDao.findPaging(pageParam, customLessonDTO);
		if (page != null) {
			List<CustomLessonDTO> list = page.getList();
			if (list == null || list.size() == 0) {
				return page;
			}
			for (CustomLessonDTO dto : list) {
				if (dto.getLessonRcmd() != null) {
					dto.setLessonRcmdStr(
							new String(dto.getLessonRcmd().getBytes((long) 1, (int) dto.getLessonRcmd().length())));
					dto.setLessonRcmd(null);
				}
			}
		}
		return page;
	}

	@Override
	public ResultDTO<String> save(CustomLessonDTO customLessonDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (StringUtils.isEmpty(customLessonDTO.getLessonName()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课程名称不能为空");

		if (StringUtils.isEmpty(customLessonDTO.getLessonCycle()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课程周期不能为空");

		if (StringUtils.isEmpty(customLessonDTO.getClassNum()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课时数不能为空");

		if (StringUtils.isEmpty(customLessonDTO.getTargetMem()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "目标客户不能为空");

		if (customLessonDTO.getIsTop() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "是否置顶不能为空");

		if (customLessonDTO.getWeight() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "权重值不能为空");

		if (customLessonDTO.getStatus() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "状态不能为空");

		long lessonId = customLessonDao.save(customLessonDTO);

		return result.set(ResultCodeEnum.SUCCESS, "新增成功", String.valueOf(lessonId));
	}

	@Override
	public ResultDTO<String> update(CustomLessonDTO customLessonDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		CustomLessonDTO clDTO = customLessonDao.getCustomLessonById(customLessonDTO.getLessonId());
		if (clDTO == null)
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");

		customLessonDao.update(customLessonDTO);
		return result.set(ResultCodeEnum.SUCCESS, "修改成功");
	}

	@Override
	public ResultDTO<String> delete(long lessonId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		CustomLessonDTO clDTO = customLessonDao.getCustomLessonById(lessonId);
		if (clDTO == null)
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");

		customLessonDao.delete(lessonId);
		return result.set(ResultCodeEnum.SUCCESS, "删除成功");
	}

	@Override
	public List<Map<String, Object>> findCustomLessons(Page<CustomLessonDTO> param, boolean isHomePage)
			throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		CustomLessonDTO customLessonDTO = new CustomLessonDTO();
		customLessonDTO.setStatus(1); // 状态 1.有效
		customLessonDTO.setHomePage(isHomePage); // 是否是首页, true 是; false 否

		List<CustomLessonDTO> customLessonList = customLessonDao.findPaging(param, customLessonDTO).getList();

		if (customLessonList != null && customLessonList.size() > 0) {
			for (CustomLessonDTO customLesson : customLessonList) {
				Map<String, Object> customLessonMap = new HashMap<>();
				// 课程ID
				customLessonMap.put("lessonId", customLesson.getLessonId());
				// 课程名称
				customLessonMap.put("lessonName", customLesson.getLessonName());
				// 课程简介
				customLessonMap.put("lessonAbstract", customLesson.getLessonAbstract());
				// 课程图片
				customLessonMap.put("lessonPic", customLesson.getLessonPic());
				// 课程周期
				customLessonMap.put("lessonCycle", customLesson.getLessonCycle());
				// 课时数
				customLessonMap.put("classNum", customLesson.getClassNum());
				resultList.add(customLessonMap);
			}
		}
		return resultList;
	}

	@Override
	public CustomLessonDTO getCustomLessonById(long lessonId) throws Exception {
		CustomLessonDTO customLessonDTO = customLessonDao.getCustomLessonById(lessonId);
		if (customLessonDTO == null) {
			return customLessonDTO;
		}
		if (customLessonDTO.getLessonRcmd() != null) {
			customLessonDTO.setLessonRcmdStr(new String(customLessonDTO.getLessonRcmd().getBytes((long) 1,
					(int) customLessonDTO.getLessonRcmd().length())));
			customLessonDTO.setLessonRcmd(null);
		}
		return customLessonDTO;
	}

	@Override
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		return customLessonDao.updateLessonRcmd(lessonId, lessonRcmdStr);
	}

	@Override
	public int updateLessonAbstract(long lessonId, String lessonAbstract) throws Exception {
		return customLessonDao.updateLessonAbstract(lessonId, lessonAbstract);
	}

}