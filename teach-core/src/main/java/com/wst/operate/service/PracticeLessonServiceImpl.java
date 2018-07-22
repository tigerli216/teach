/*
 * PracticeLessonServiceImpl.java Created on 2017年9月27日 下午2:53:14
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

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.operate.dao.PracticeLessonDao;
import com.wst.service.operate.dto.PracticeLessonDTO;
import com.wst.service.operate.service.PracticeLessonService;

/**
 * 运营实习职位Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class PracticeLessonServiceImpl implements PracticeLessonService {

	@Resource
	private PracticeLessonDao practiceLessonDao;

	@Override
	public Page<PracticeLessonDTO> findPaging(Page<PracticeLessonDTO> pageParam, PracticeLessonDTO practiceLessonDTO)
			throws Exception {
		Page<PracticeLessonDTO> page = practiceLessonDao.findPaging(pageParam, practiceLessonDTO);
		if (page != null) {
			List<PracticeLessonDTO> list = page.getList();
			if(list == null || list.size() == 0){
				return page;
			}
			for (PracticeLessonDTO dto : list) {
				// 公司介绍
				if (dto.getCompanyRcmd() != null) {
					dto.setCompanyRcmdStr(
							new String(dto.getCompanyRcmd().getBytes((long) 1, (int) dto.getCompanyRcmd().length())));
					dto.setCompanyRcmd(null);
				}
				// 职位介绍
				if (dto.getPracticeRcmd() != null) {
					dto.setPracticeRcmdStr(
							new String(dto.getPracticeRcmd().getBytes((long) 1, (int) dto.getPracticeRcmd().length())));
					dto.setPracticeRcmd(null);
				}
			}
		}
		return page;
	}

	@Override
	public ResultDTO<String> save(PracticeLessonDTO practiceLessonDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (StringUtils.isEmpty(practiceLessonDTO.getPracticeName()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "职位名称不能为空");

		if (practiceLessonDTO.getCompanyCountry() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "公司国家不能为空");

		if (StringUtils.isEmpty(practiceLessonDTO.getPracticeDuration()))
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "实习周期不能为空");

		if (practiceLessonDTO.getIsTop() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "是否置顶不能为空");

		if (practiceLessonDTO.getWeight() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "权重能为空");

		if (practiceLessonDTO.getStatus() == 0)
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "状态能为空");

		long lessonId = practiceLessonDao.save(practiceLessonDTO);
		return result.set(ResultCodeEnum.SUCCESS, "新增成功", String.valueOf(lessonId));
	}

	@Override
	public ResultDTO<String> update(PracticeLessonDTO practiceLessonDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		PracticeLessonDTO plDTO = practiceLessonDao.getPracticeById(practiceLessonDTO.getLessonId());
		if (plDTO == null)
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");

		practiceLessonDao.update(practiceLessonDTO);
		return result.set(ResultCodeEnum.SUCCESS, "修改成功");
	}

	@Override
	public ResultDTO<String> delete(long lessonId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		PracticeLessonDTO practiceLessonDTO = practiceLessonDao.getPracticeById(lessonId);
		if (practiceLessonDTO == null)
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");

		practiceLessonDao.delete(lessonId);
		return result.set(ResultCodeEnum.SUCCESS, "删除成功");
	}

	@Override
	public List<Map<String, Object>> findPracticeLessons(Page<PracticeLessonDTO> param,
			PracticeLessonDTO practiceLessonDTO) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<PracticeLessonDTO> practiceLessonList = practiceLessonDao.findPaging(param, practiceLessonDTO).getList();
		if (practiceLessonList != null && practiceLessonList.size() > 0) {
			for (PracticeLessonDTO practiceLesson : practiceLessonList) {
				// 课程ID,职位名称,课程简介,公司介绍,职位介绍,行业介绍,公司国家,公司位置,职位图片,实习周期
				Map<String, Object> practiceLessonMap = new HashMap<>();
				practiceLessonMap.put("lessonId", practiceLesson.getLessonId()); // 课程ID
				practiceLessonMap.put("practiceName", practiceLesson.getPracticeName()); // 职位名称
				practiceLessonMap.put("lessonAbstract", practiceLesson.getLessonAbstract()); // 课程简介
				practiceLessonMap.put("jobPic", practiceLesson.getJobPic()); // 职位图片
				practiceLessonMap.put("practiceDuration", practiceLesson.getPracticeDuration()); // 实习周期
				resultList.add(practiceLessonMap);
			}
		}
		return resultList;
	}

	@Override
	public PracticeLessonDTO getPracticeLessonById(long lessonId) throws Exception {
		PracticeLessonDTO dto = practiceLessonDao.getPracticeById(lessonId);
		if (dto.getCompanyRcmd() != null) {
			dto.setCompanyRcmdStr(
					new String(dto.getCompanyRcmd().getBytes((long) 1, (int) dto.getCompanyRcmd().length())));
			dto.setCompanyRcmd(null);
		}
		// 职位介绍
		if (dto.getPracticeRcmd() != null) {
			dto.setPracticeRcmdStr(
					new String(dto.getPracticeRcmd().getBytes((long) 1, (int) dto.getPracticeRcmd().length())));
			dto.setPracticeRcmd(null);
		}
		return dto;
	}

	@Override
	public int updateRcmdStr(long lessonId, String companyRcmdStr, String practiceRcmdStr)
			throws Exception {
		return practiceLessonDao.updateRcmdStr(lessonId, companyRcmdStr, practiceRcmdStr);
	}

}
