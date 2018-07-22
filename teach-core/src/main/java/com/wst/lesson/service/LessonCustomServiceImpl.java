/*
 * LessonCustomServiceImpl.java Created on 2017年9月29日 上午10:52:21
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
package com.wst.lesson.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;
import com.wst.constant.LessonCustomConstant.CustomClassStatusEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.constant.UserConstant.ExamineStatusEnmu;
import com.wst.constant.UserConstant.ValidStatusEnmu;
import com.wst.lesson.dao.CustomClassDao;
import com.wst.lesson.dao.CustomDao;
import com.wst.lesson.dao.CustomTutorClassDao;
import com.wst.mem.dao.UserDao;
import com.wst.order.dao.OrderDao;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.dto.CustomTutorClassDTO;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.service.TutorService;
import com.wst.service.order.dto.OrderDTO;

/**
 * 定制课接口 service 实现类
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class LessonCustomServiceImpl implements LessonCustomService {

	@Resource
	private CustomDao customDao;

	@Resource
	private CustomClassDao customClassDao;

	@Resource
	private CustomTutorClassDao customTutorClassDao;

	@Resource
	private TutorService tutorService;

	@Resource
	private UserDao userDao;

	@Resource
	private OrderDao orderDao;

	@Override
	public Page<CustomDTO> fingPaging(Page<CustomDTO> page, CustomDTO customDTO) throws Exception {
		if (customDTO != null && customDTO.getEndTime() != null) {
			customDTO.setEndTime(DateUtils.addEndTime(customDTO.getEndTime()));
		}

		Page<CustomDTO> customDTOPage = customDao.fingPaging(page, customDTO);
		if (customDTOPage == null)
			return customDTOPage;

		List<CustomDTO> customList = customDTOPage.getList();
		if (customList == null || customList.size() == 0) {
			return customDTOPage;
		}

		for (CustomDTO dto : customList) {
			if (dto.getLessonRcmd() != null) {
				dto.setLessonRcmdStr(
						new String(dto.getLessonRcmd().getBytes((long) 1, (int) dto.getLessonRcmd().length())));
				dto.setLessonRcmd(null);
			}
		}

		return customDTOPage;
	}

	@Override
	public CustomDTO getLessonById(long lessonId) throws Exception {
		// 获取课程基础信息
		CustomDTO customDTO = customDao.getLessonById(lessonId);
		if (customDTO == null) {
			return customDTO;
		}

		customDTO.setLoginAccount(userDao.getUserById(customDTO.getUserId()).getLoginAccount());
		customDTO.setAttrList(customClassDao.findLessonClassList(lessonId));
		TutorDTO tutorQuery = new TutorDTO();
		tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
		tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
		tutorQuery.setIndustryId(customDTO.getIndustryId());
		List<TutorDTO> tutorList = tutorService.findPaging(null, tutorQuery).getList();
		customDTO.setTutorList(tutorList);
		if (customDTO.getLessonRcmd() != null) {
			customDTO.setLessonRcmdStr(
					new String(customDTO.getLessonRcmd().getBytes((long) 1, (int) customDTO.getLessonRcmd().length())));
			customDTO.setLessonRcmd(null);
		}

		OrderDTO order = orderDao.getByLessonIdUSerId(customDTO.getUserId(), customDTO.getLessonId(), 2);
		if (order != null) {
			customDTO.setOrderCode(order.getOrderCode());
			if (order.getOrderStatus() == OrderStatusEnum.HAS_CONFIRM.status
					&& order.getPayStatus() == PayStatusEnum.HAS_PAY.status) {
				// 是否已购买
				customDTO.setBuy(true);
			}
		}

		return customDTO;
	}

	@Override
	public ResultDTO<String> addLesson(CustomDTO customDTO) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();

		/** 校验课程名称是否被占用 */
		CustomDTO custom = customDao.getLessonByLessonName(customDTO.getLessonName());
		if (custom != null) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课程名称已被占用");
		}

		/** 保存课程信息 */
		Long lessonId = customDao.insertLesson(customDTO);
		if (StringUtils.isNotEmpty(customDTO.getLessonRcmdStr()) && customDTO.getLessonId() <= 0) {
			updateLessonRcmd(lessonId, customDTO.getLessonRcmdStr().replace("<p></p>", ""));
		}

		/** 保存课时信息 */
		String ids = "";
		List<CustomClassDTO> customClassAttrList = customDTO.getAttrList();
		Map<String, Long> customTutorClassMap = new HashMap<>();
		if (null != customClassAttrList && customClassAttrList.size() > 0) {
			for (CustomClassDTO customClassAttr : customClassAttrList) {
				customClassAttr.setLessonId(lessonId);
				customClassAttr.setClassStatus(CustomClassStatusEnum.NOT_START.status);
				long classId = customClassDao.insertLessonClass(customClassAttr);
				long tutorId = customClassAttr.getTutorId();
				if (customTutorClassMap.containsKey(String.valueOf(tutorId))) {
					customTutorClassMap.put(String.valueOf(tutorId),
							customTutorClassMap.get(String.valueOf(tutorId)) + customClassAttr.getPlanDuration());
				} else {
					customTutorClassMap.put(String.valueOf(tutorId), customClassAttr.getPlanDuration());
				}
				ids += classId + ",";
			}
		}
		// 保存定制课程导师课时计划
		if (customTutorClassMap != null) {
			for (Map.Entry<String, Long> entry : customTutorClassMap.entrySet()) {
				CustomTutorClassDTO customTutorClass = new CustomTutorClassDTO();
				// 导师ID
				customTutorClass.setTutorId(Long.valueOf(entry.getKey()));
				// 总课时
				customTutorClass.setTotalClass(entry.getValue());
				// 课程ID
				customTutorClass.setLessonId(lessonId);
				customTutorClassDao.save(customTutorClass);
			}
		}

		// 当前课时id
		String currentClass = ids.substring(0, ids.indexOf(","));
		customDao.updateCurrentClass(lessonId, Long.parseLong(currentClass), 0);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "新增课程成功", String.valueOf(lessonId));
	}

	@Override
	public ResultDTO<String> updateLesson(CustomDTO customDTO) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();
		/** 校验课程名称是否重复 */
		CustomDTO custom = customDao.getLessonByLessonName(customDTO.getLessonName());
		if (custom != null && custom.getLessonId() != customDTO.getLessonId()) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课程名称已被占用");
		}

		/** 修改课程信息 */
		customDao.updateLesson(customDTO);

		/** 删除以前的定制课程导师课时计划 */
		customTutorClassDao.deleteByLessonId(customDTO.getLessonId());

		/** 保存课时信息 */
		List<CustomClassDTO> customClassAttrList = customDTO.getAttrList();
		String ids = "";
		Map<String, Long> customTutorClassMap = new HashMap<>();
		if (null != customClassAttrList && customClassAttrList.size() > 0) {
			for (CustomClassDTO customClassAttr : customClassAttrList) {
				customClassAttr.setLessonId(customDTO.getLessonId());
				if (customClassAttr.getClassId() > 0) {
					customClassDao.updateLessonClassById(customClassAttr);
					ids += customClassAttr.getClassId() + ",";
				} else if (customClassAttr.getClassId() <= 0) {
					customClassAttr.setClassStatus(CustomClassStatusEnum.NOT_START.status);
					long classId = customClassDao.insertLessonClass(customClassAttr);
					ids += classId + ",";
				}
				long tutorId = customClassAttr.getTutorId();
				if (customTutorClassMap.containsKey(String.valueOf(tutorId))) {
					customTutorClassMap.put(String.valueOf(tutorId),
							customTutorClassMap.get(String.valueOf(tutorId)) + customClassAttr.getPlanDuration());
				} else {
					customTutorClassMap.put(String.valueOf(tutorId), customClassAttr.getPlanDuration());
				}
			}
			/** 删除课时信息 */
			if (ids.length() > 0) {
				String classIds = ids.substring(0, ids.length() - 1);
				customClassDao.deleteLessonClassOfId(customDTO.getLessonId(), classIds);
			}
		}

		// 保存定制课程导师课时计划
		if (customTutorClassMap != null) {
			for (Map.Entry<String, Long> entry : customTutorClassMap.entrySet()) {
				CustomTutorClassDTO customTutorClass = new CustomTutorClassDTO();
				// 导师ID
				customTutorClass.setTutorId(Long.valueOf(entry.getKey()));
				// 总课时
				customTutorClass.setTotalClass(entry.getValue());
				// 课程ID
				customTutorClass.setLessonId(customDTO.getLessonId());
				customTutorClassDao.save(customTutorClass);
			}
		}

		// 更新当前课时
		List<CustomClassDTO> customClassList = customClassDao.findNotFinishCustomClass(0, customDTO.getLessonId(), 0);
		if (customClassList != null && customClassList.size() > 0) {
			customDao.updateCurrentClass(customDTO.getLessonId(), customClassList.get(0).getClassId(), 0);
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "更新课程成功");
	}

	@Override
	public Page<CustomDTO> fingPagingByTutorId(Page<CustomDTO> page, CustomDTO customDTO) throws Exception {
		Page<CustomDTO> customDTOPage = customDao.fingPagingByTutorId(page, customDTO);
		if (customDTOPage != null) {
			List<CustomDTO> customList = customDTOPage.getList();
			if (customList == null || customList.size() == 0) {
				return customDTOPage;
			}
			for (CustomDTO dto : customList) {
				if (dto.getLessonRcmd() != null) {
					dto.setLessonRcmdStr(
							new String(dto.getLessonRcmd().getBytes((long) 1, (int) dto.getLessonRcmd().length())));
					dto.setLessonRcmd(null);
				}
			}
		}
		return customDTOPage;
	}

	@Override
	public int buyCustom(long lessonId) throws Exception {
		return customDao.buyCustom(lessonId);
	}

	/**
	 * 更新课程介绍(富文本)
	 * 
	 * @param lessonId
	 * @param lessonRcmd
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		return customDao.updateLessonRcmd(lessonId, lessonRcmdStr);
	}

	@Override
	public CustomDTO getLessonByLessonName(String lessonName) throws Exception {
		CustomDTO customDTO = customDao.getLessonByLessonName(lessonName);
		if (customDTO != null) {
			customDTO.setLessonRcmd(null);
		}
		return customDTO;
	}

	@Override
	public List<CustomClassDTO> findLessonClassList(long lessonId) throws Exception {
		return customClassDao.findLessonClassList(lessonId);
	}
}
