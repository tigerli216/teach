/*
 * LessonCustomController.java Created on 2017年10月11日 下午7:33:06
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.LessonCustomConstant.RealCustomStatusEnum;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomClassDateDTO;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.service.CustomClassDateService;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.TutorService;

/**
 * 学生 获取定制课 相关
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/student/")
public class CustomOfUserController {

	// private static HiwiLog logger =
	// HiwiLogFactory.getLogger(LessonCustomOfUserController.class);

	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;
	@Reference(version = "0.0.1")
	private TutorService tutorService;
	@Reference(version = "0.0.1")
	private CustomClassService customClassService;
	@Reference(version = "0.0.1")
	private CustomClassDateService customClassDateService;

	/**
	 * 获取课程公告
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@ResponseBody
	@RequestMapping("findAdvertisingDtl")
	public ResultDTO<Map<String, Object>> findAdvertisingDtl(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}

		CustomDTO customDTO = new CustomDTO();
		customDTO.setUserId(user.getUserId());
		customDTO.setBuy(true);
		Page<CustomDTO> customPage = lessonCustomService.fingPaging(null, customDTO);

		Map<String, Object> resultMap = null;
		if (customPage == null || customPage.getList() == null || customPage.getList().size() <= 0) {
			return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
		}

		for (CustomDTO custom : customPage.getList()) {
			resultMap = new HashMap<>();
			if (custom.getCurrentClass() > 0L) {
				CustomClassDTO customClassDTO = customClassService.getCustomClassById(custom.getCurrentClass());
				// 查询约课记录
				CustomClassDateDTO customClassDateDTO = customClassDateService
						.getCustomClassDateByClassId(custom.getCurrentClass());

				long classStatus = customClassDTO.getClassStatus();
				// 约课状态
				long classDateStatus = 0;
				if (customClassDateDTO != null && customClassDateDTO.getCancelUserId() == 0) {
					classDateStatus = customClassDateDTO.getConfirmStatus();
				}
				RealCustomStatusEnum realCustomStatusEnum = null;
				if (classStatus == 1 && classDateStatus == 1) {
					// 待确认约课
					realCustomStatusEnum = realCustomStatusEnum.HAS_CANCEL;
					resultMap.put("classStatusStr", realCustomStatusEnum.name);
					resultMap.put("classStatus", realCustomStatusEnum.status);
					resultMap.put("lessonId", custom.getLessonId());
					resultMap.put("content", "你有待确认约课的课时需要确认, 请尽快确认！");
					return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
				} else if (classStatus == 3) {
					// 课程结束
					realCustomStatusEnum = realCustomStatusEnum.UN_END;
					resultMap.put("classStatusStr", realCustomStatusEnum.name);
					resultMap.put("classStatus", realCustomStatusEnum.status);
					resultMap.put("lessonId", custom.getLessonId());
					resultMap.put("content", "你有完成的课时需要确认, 请尽快确认！");
					return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
				}
			}
		}
		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageParam
	 * @param CustomDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findLessonCustom")
	public ResultDTO<List<Map<String, Object>>> findLessonCustom(CommonParamsDTO dto, Page<CustomDTO> pageParam)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> resultDTO = new ResultDTO<>();

		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}
		CustomDTO customDTO = new CustomDTO();
		customDTO.setUserId(user.getUserId());
		customDTO.setBuy(true);
		Page<CustomDTO> customPage = lessonCustomService.fingPaging(pageParam, customDTO);

		List<Map<String, Object>> resultList = new ArrayList<>();
		if (customPage != null && customPage.getList() != null && customPage.getList().size() > 0) {
			for (CustomDTO custom : customPage.getList()) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("beginTime", custom.getBeginTime());
				resultMap.put("finishClassTime", custom.getFinishClass());
				resultMap.put("lessonId", custom.getLessonId());
				resultMap.put("lessonName", custom.getLessonName());
				resultMap.put("lessonTypeStr", custom.getLessonTypeStr());
				resultMap.put("lessonType", custom.getLessonType());
				resultMap.put("totalClassTime", custom.getTotalClass());
				// 已完成课节
				long finishClass = 0;
				List<CustomClassDTO> customClassList = customClassService.findLessonClassList(custom.getLessonId());
				for (CustomClassDTO customClassDTO : customClassList) {
					if (customClassDTO.getClassStatus() > 2) {
						finishClass++;
					}
				}
				// 总课节
				resultMap.put("totalClass", customClassList.size());
				// 已完成课节
				resultMap.put("finishClass", finishClass);

				int totalMonth = DateUtils.getMonthSpace(custom.getBeginTime(), custom.getEndTime());
				// 课程周期月数
				resultMap.put("totalMonth", totalMonth + 1);
				if (custom.getCurrentClass() > 0L) {
					CustomClassDTO customClassDTO = customClassService.getCustomClassById(custom.getCurrentClass());
					if (customClassDTO.getTutorId() > 0) {
						TutorDTO tutorDTO = tutorService.getTutorById(customClassDTO.getTutorId());
						// 当前课时导师名称
						resultMap.put("classTutorName",
								tutorDTO.getRealName() == null || tutorDTO.getRealName().equals("")
										? tutorDTO.getLoginAccount() : tutorDTO.getRealName());
					}
				}

				resultList.add(resultMap);
			}
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultList);
	}

	/**
	 * 根据lessonId获取课程详情
	 * 
	 * @param dto
	 * 
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findLessonCustomDtl")
	public ResultDTO<Map<String, Object>> findLessonCustomDtl(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

		if (lessonId <= 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The lesson ID cannot be empty");
		}

		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}

		CustomDTO customDTO = lessonCustomService.getLessonById(lessonId);
		if (customDTO == null) {
			// 课程不存在
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Curriculum does not exist");
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("currentClass", customDTO.getCurrentClass());
		resultMap.put("lessonName", customDTO.getLessonName());
		resultMap.put("lessonTypeStr", customDTO.getLessonTypeStr());
		resultMap.put("lessonType", customDTO.getLessonType());
		resultMap.put("lessonRcmd", customDTO.getLessonRcmdStr());
		resultMap.put("totalClassTime", customDTO.getTotalClass());
		resultMap.put("finishClassTime", customDTO.getFinishClass());
		// 获取当前课时导师
		CustomClassDTO customClassDTO = customClassService.getCustomClassById(customDTO.getCurrentClass());
		if (customClassDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The current class does not exist");
		}
		TutorDTO tutor = tutorService.getTutorById(customClassDTO.getTutorId());
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The tutor does not exist at present");
		}
		resultMap.put("classTutorName", tutor.getRealName() == null || tutor.getRealName().equals("")
				? tutor.getLoginAccount() : tutor.getRealName());

		List<CustomClassDTO> attrList = customDTO.getAttrList();
		// 总课节
		resultMap.put("totalClass", attrList.size());

		List<Map<String, Object>> lessonClassList = new ArrayList<>();
		long finishClass = 0; // 已完成课节
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (attrList != null && attrList.size() > 0) {
			for (CustomClassDTO customClass : attrList) {
				Map<String, Object> lessonClassMap = new HashMap<>();
				TutorDTO tutorDTO = tutorService.getTutorById(customClass.getTutorId());
				lessonClassMap.put("classTutorName", tutorDTO.getRealName() == null || tutorDTO.getRealName().equals("")
						? tutorDTO.getLoginAccount() : tutorDTO.getRealName());
				lessonClassMap.put("portraitImgUrl", tutorDTO.getPortraitImgUrl());
				lessonClassMap.put("graduateSchool", tutorDTO.getGraduateSchool());
				lessonClassMap.put("currentPosition", tutorDTO.getNewlyJob() + " " + tutorDTO.getPosition());

				lessonClassMap.put("classId", customClass.getClassId());
				lessonClassMap.put("className", customClass.getClassName());
				lessonClassMap.put("planDuration", customClass.getPlanDuration());
				lessonClassMap.put("realDuration", customClass.getRealDuration());
				lessonClassMap.put("readyFile", customClass.getReadyFile());
				lessonClassMap.put("classSummary", customClass.getClassSummary());

				String planTime = sd.format(customClass.getPlanTime());
				lessonClassMap.put("classTime", planTime);
				// 查询约课记录
				CustomClassDateDTO customClassDateDTO = customClassDateService
						.getCustomClassDateByClassId(customClass.getClassId());
				// 课时状态
				long classStatus = customClass.getClassStatus();
				// 约课状态
				long classDateStatus = 0;
				if (customClassDateDTO != null && customClassDateDTO.getCancelUserId() <= 0) {
					classDateStatus = customClassDateDTO.getConfirmStatus();
				}
				// 待约课;
				RealCustomStatusEnum realCustomStatusEnum = RealCustomStatusEnum.UN_HAS_CONFIRM;
				String dateTime = "";
				if (customClass.getDateTime() != null) {
					dateTime = sdf.format(customClass.getDateTime());
				}
				if (classStatus == 1) {
					if (classDateStatus == 1) {
						// 待确认约课
						realCustomStatusEnum = RealCustomStatusEnum.HAS_CANCEL;
						lessonClassMap.put("classTime", dateTime);
					} else if (classDateStatus == 2) {
						// 已约课
						realCustomStatusEnum = RealCustomStatusEnum.HAS_CLASS;
						// 约课时间
						lessonClassMap.put("classTime", dateTime);
					}
				} else if (classStatus == 2) {
					// 开始上课
					realCustomStatusEnum = RealCustomStatusEnum.UN_CLASS;
				} else if (classStatus == 3) {
					// 课程结束
					realCustomStatusEnum = RealCustomStatusEnum.UN_END;
				} else if (classStatus == 4) {
					// 课程完结
					realCustomStatusEnum = RealCustomStatusEnum.HAS_END;
				}
				if (classStatus > 1) {
					String beginTime = s.format(customClass.getBeginTime());
					lessonClassMap.put("classTime", beginTime);// 约课时间
				} else if (classStatus > 2) {
					finishClass++;
				}

				// 校验距离上课时间是否是两小时以内 true是；false否
				boolean isInTwoHour = false;
				if (realCustomStatusEnum.status == 3) {
					Date now = new Date();
					long time = customClass.getDateTime().getTime() - now.getTime();
					long twoHourTime = 1000 * 60 * 60 * 2;
					if (time < twoHourTime) {
						isInTwoHour = true;
					}
					lessonClassMap.put("isInTwoHour", isInTwoHour);
				}
				// 课时状态描述
				lessonClassMap.put("classStatusStr", realCustomStatusEnum.name);
				// 课时状态
				lessonClassMap.put("classStatus", realCustomStatusEnum.status);
				lessonClassList.add(lessonClassMap);
			}
		}
		resultMap.put("finishClass", finishClass);
		resultMap.put("lessonClassList", lessonClassList);

		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);

	}

}
