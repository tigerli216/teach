/*
 * LessonCustomOfTutorController.java Created on 2017年10月12日 上午10:25:50
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
package com.wst.tutor;

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
import com.wst.service.mem.service.UserService;

/**
 * 导师 获取定制课 相关
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/tutor/")
public class CustomOfTutorController {

	// private static HiwiLog logger =
	// HiwiLogFactory.getLogger(LessonCustomOfTutorController.class);

	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private TutorService TutorService;

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

		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}

		CustomDTO customDTO = new CustomDTO();
		customDTO.setTutorId(tutor.getTutorId());
		customDTO.setBuy(true);
		Page<CustomDTO> customPage = lessonCustomService.fingPagingByTutorId(null, customDTO);

		Map<String, Object> resultMap = new HashMap<>(5);
		if (customPage == null || customPage.getList() == null || customPage.getList().size() == 0) {
			return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
		}

		for (CustomDTO custom : customPage.getList()) {
			if (custom.getCurrentClass() == 0L) {
				continue;
			}

			CustomClassDTO customClassDTO = customClassService.getCustomClassById(custom.getCurrentClass());
			// 查询约课记录
			if (customClassDTO.getTutorId() != tutor.getTutorId()) {
				continue;
			}

			CustomClassDateDTO customClassDateDTO = customClassDateService
					.getCustomClassDateByClassId(custom.getCurrentClass());
			long classStatus = customClassDTO.getClassStatus();
			RealCustomStatusEnum realCustomStatusEnum = null;
			if (classStatus != 1L) {
				continue;
			}
			if (customClassDateDTO == null || customClassDateDTO.getCancelUserId() > 0) {
				// 待约课
				realCustomStatusEnum = realCustomStatusEnum.UN_HAS_CONFIRM;
				resultMap.put("classStatusStr", realCustomStatusEnum.name);
				resultMap.put("classStatus", realCustomStatusEnum.status);
				resultMap.put("lessonId", custom.getLessonId());
				resultMap.put("content", "你有课程可约课！");
				resultMap.put("tutorEmail", tutor.getEmail());
				return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
			}
		}
		resultMap.put("tutorEmail", tutor.getEmail());
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

		// 获得用户信息
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		// 非法请求
		if (tutor == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}
		CustomDTO customDTO = new CustomDTO();
		customDTO.setTutorId(tutor.getTutorId());
		customDTO.setBuy(true);
		Page<CustomDTO> customPage = lessonCustomService.fingPagingByTutorId(pageParam, customDTO);
		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> resultMap = null;
		if (customPage != null && customPage.getList() != null && customPage.getList().size() > 0) {
			for (CustomDTO custom : customPage.getList()) {
				resultMap = new HashMap<>();
				UserDTO user = userService.getUserById(custom.getUserId());
				// 学生真实姓名
				resultMap.put("studentName", user.getRealName() == null || user.getRealName().equals("")
						? user.getLoginAccount() : user.getRealName());
				// 开始时间
				resultMap.put("beginTime", custom.getBeginTime());
				// 课程id
				resultMap.put("lessonId", custom.getLessonId());
				// 课程名称
				resultMap.put("lessonName", custom.getLessonName());
				// 课程分类描述
				resultMap.put("lessonTypeStr", custom.getLessonTypeStr());
				// 课程分类
				resultMap.put("lessonType", custom.getLessonType());
				// 总课时
				resultMap.put("totalClassTime", custom.getTotalClass());
				List<CustomClassDTO> customClassList = customClassService.findLessonClassList(custom.getLessonId());
				// 总课节
				resultMap.put("totalClass", customClassList.size());
				// 已完成课节
				long finishClass = 0;
				for (CustomClassDTO customClassDTO : customClassList) {
					if (customClassDTO.getClassStatus() > 2) {
						finishClass++;
					}
				}
				resultMap.put("finishClass", finishClass);
				int totalMonth = DateUtils.getMonthSpace(custom.getBeginTime(), custom.getEndTime());
				// 课程周期月数
				resultMap.put("totalMonth", totalMonth + 1);

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
	@SuppressWarnings({ "static-access" })
	@ResponseBody
	@RequestMapping("findLessonCustomDtl")
	public ResultDTO<Map<String, Object>> findLessonCustomDtl(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();
		// 课程ID不能为空
		if (lessonId <= 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The lesson ID cannot be empty");
		}

		// 获得用户信息
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		// 非法请求
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "illegal request");
		}

		CustomDTO customDTO = lessonCustomService.getLessonById(lessonId);
		Map<String, Object> resultMap = new HashMap<>();
		// 当前课时id
		resultMap.put("currentClass", customDTO.getCurrentClass());
		// 课程名称
		resultMap.put("lessonName", customDTO.getLessonName());
		// 课程分类描述
		resultMap.put("lessonTypeStr", customDTO.getLessonTypeStr());
		// 课程分类
		resultMap.put("lessonType", customDTO.getLessonType());
		// 课程简介
		resultMap.put("lessonRcmd", customDTO.getLessonRcmdStr());
		// 总课时
		resultMap.put("totalClassTime", customDTO.getTotalClass());
		List<CustomClassDTO> customClassList = customClassService.findLessonClassList(customDTO.getLessonId());
		// 总课节
		resultMap.put("totalClass", customClassList.size());
		// 已完成课时
		resultMap.put("finishClassTime", customDTO.getFinishClass());
		// 学生ID
		resultMap.put("studentId", customDTO.getUserId());
		// 获取学生信息
		UserDTO user = userService.getUserById(customDTO.getUserId());
		// 当前课时学生姓名
		resultMap.put("studentName", user.getRealName() == null || user.getRealName().equals("")
				? user.getLoginAccount() : user.getRealName());
		// 导师ID
		resultMap.put("tutorId", tutor.getTutorId());
		List<CustomClassDTO> attrList = customDTO.getAttrList();
		List<Map<String, Object>> lessonClassList = new ArrayList<>();
		Map<String, Object> lessonClassMap = null;
		if (attrList != null && attrList != null && attrList.size() > 0) {
			for (CustomClassDTO customClass : attrList) {
				lessonClassMap = new HashMap<>();
				// 课时id
				lessonClassMap.put("classId", customClass.getClassId());
				// 课时名称
				lessonClassMap.put("className", customClass.getClassName());
				// 课时状态描述
				lessonClassMap.put("classStatusStr", customClass.getClassStatusStr());
				// 课时状态
				lessonClassMap.put("classStatus", customClass.getClassStatus());
				// 计划时长
				lessonClassMap.put("planDuration", customClass.getPlanDuration());
				// 实际时长
				lessonClassMap.put("realDuration", customClass.getRealDuration());
				// 备课文件
				lessonClassMap.put("readyFile", customClass.getReadyFile());
				// 课程小结
				lessonClassMap.put("classSummary", customClass.getClassSummary());
				// 导师ID
				lessonClassMap.put("tutorId", customClass.getTutorId());
				// 导师姓名
				lessonClassMap.put("tutorName",
						customClass.getTutorName() == null || customClass.getTutorName().equals("")
								? customClass.getLoginAccount() : customClass.getTutorName());

				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				String planTime = sd.format(customClass.getPlanTime());
				lessonClassMap.put("classTime", planTime); // 课时时间
				// 查询约课记录
				CustomClassDateDTO customClassDateDTO = customClassDateService
						.getCustomClassDateByClassId(customClass.getClassId());
				// 课时状态
				long classStatus = customClass.getClassStatus();
				// 约课状态
				long classDateStatus = 0;
				if (customClassDateDTO != null) {
					// 约课状态
					classDateStatus = customClassDateDTO.getConfirmStatus();
				}
				// 课程状态码Enum
				// 待约课;
				RealCustomStatusEnum realCustomStatusEnum = RealCustomStatusEnum.UN_HAS_CONFIRM;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String dateTime = "";
				if (customClass.getDateTime() != null) {
					dateTime = sdf.format(customClass.getDateTime());
				}
				if (classStatus == 1) {
					if (customClassDateDTO == null) {
						// 待约课
						realCustomStatusEnum = realCustomStatusEnum.UN_HAS_CONFIRM;
					} else {
						lessonClassMap.put("classTime", dateTime);
						if (classDateStatus == 1) {
							// 待确认约课
							realCustomStatusEnum = realCustomStatusEnum.HAS_CANCEL;
						} else if (classDateStatus == 2) {
							// 已约课
							realCustomStatusEnum = realCustomStatusEnum.HAS_CLASS;
							// 若已取消约课， 变为待约课
							if (customClassDateDTO.getCancelUserId() > 0) {
								realCustomStatusEnum = realCustomStatusEnum.UN_HAS_CONFIRM;
								lessonClassMap.put("classTime", planTime);
							}
						}
					}
				} else if (classStatus == 2) {
					// 开始上课
					realCustomStatusEnum = realCustomStatusEnum.UN_CLASS;
				} else if (classStatus == 3) {
					// 课程结束
					realCustomStatusEnum = realCustomStatusEnum.UN_END;
				} else if (classStatus == 4) {
					// 课程完结
					realCustomStatusEnum = realCustomStatusEnum.HAS_END;
				}
				if (classStatus > 1) {
				    if (customClass.getBeginTime() != null) {
	                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    String beginTime = s.format(customClass.getBeginTime());
	                    lessonClassMap.put("classTime", beginTime);// 约课时间
				    }
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
		resultMap.put("lessonClassList", lessonClassList);

		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);

	}

}
