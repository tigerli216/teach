/*
 * CustomSettleController.java Created on 2017年10月13日 上午11:21:15
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.dto.CustomSettleDTO;
import com.wst.service.lesson.dto.CustomTutorClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.CustomSettleService;
import com.wst.service.lesson.service.CustomTutorClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.UserService;

/**
 * 薪资控制层
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/tutor")
public class CustomSettleController {

	@Reference(version = "0.0.1")
	private CustomSettleService customSettleService;

	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private CustomTutorClassService customTutorClassService;

	@Reference(version = "0.0.1")
	private CustomClassService customClassService;

	/**
	 * 获取导师薪资信息
	 * 
	 * @param dto
	 *            公共参数
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getSalaryInfo")
	public ResultDTO<Map<String, Object>> getSalaryInfo(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>();
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", resultMap);
		}
		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
		CustomSettleDTO customSettleDTO = customSettleService.getSalaryInfo(tutorDTO.getTutorId());
		resultMap.put("baseSalary", AmountUtils.changeF2YString(customSettleDTO.getBaseSalary()));
		resultMap.put("haveSettleSalary", BigDecimal.valueOf(customSettleDTO.getHaveSettleSalary())
				.divide(new BigDecimal(6000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		resultMap.put("notSettleSalary", BigDecimal.valueOf(customSettleDTO.getNotSettleSalary())
				.divide(new BigDecimal(6000)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", resultMap);
	}

	/**
	 * 获取薪资列表
	 * 
	 * @param dto
	 *            公共参数
	 * @param pageParams
	 *            分页参数
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findSalaryList")
	public ResultDTO<List<Map<String, Object>>> findSalaryList(CommonParamsDTO dto, Page<CustomSettleDTO> pageParams)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> resultDTO = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", resultList);
		}
		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
		CustomSettleDTO queryCustomSettle = new CustomSettleDTO();
		queryCustomSettle.setTutorId(tutorDTO.getTutorId());

		Page<CustomSettleDTO> customSettlePage = customSettleService.findLesson(pageParams, tutorDTO.getTutorId());
		List<CustomSettleDTO> customSettleList = null;
		if (customSettlePage != null) {
			customSettleList = customSettlePage.getList();
		}
		if (customSettleList == null) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "success", resultList);
		}

		for (CustomSettleDTO customSettleDTO : customSettleList) {
			List<CustomSettleDTO> customSettList = customSettleService.findByLesson(customSettleDTO.getLessonId(),
					tutorDTO.getTutorId());
			Map<String, Object> resultMap = new HashMap<String, Object>(9);
			CustomDTO custom = lessonCustomService.getLessonById(customSettleDTO.getLessonId());
			// 课程ID
			resultMap.put("lessonId", custom.getLessonId());
			// 课程名称
			resultMap.put("lessonName", custom.getLessonName());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 开始时间
			resultMap.put("beginTime", sdf.format(custom.getBeginTime()));
			// 结束时间
			resultMap.put("endTime", sdf.format(custom.getEndTime()));

			CustomTutorClassDTO customTutorClassDTO = customTutorClassService.getByLessonIdTutorId(custom.getLessonId(), tutorDTO.getTutorId());
			if (customTutorClassDTO == null) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The customised course mentor class schedule is empty");
			}
			// 已完成课时
			resultMap.put("finishClass", customTutorClassDTO.getFinishClass());
			// 总课时
			resultMap.put("totalClass", customTutorClassDTO.getTotalClass());

			// 用户名称
			UserDTO user = userService.getUserById(custom.getUserId());
			if (StringUtils.isEmpty(user.getRealName())) {
				resultMap.put("userName", user.getLoginAccount());
			}
			resultMap.put("userName", user.getRealName());

			long settle = 0;
			long notSettle = 0;
			for (CustomSettleDTO customSettle : customSettList) {
				if (customSettle.getSettleStatus() == 2) {
					settle += customSettle.getSettleDuration() * customSettle.getSettlePrice() / 6000;
				} else { // 未结算
					notSettle += customSettle.getSettleDuration() * customSettle.getSettlePrice() / 6000;
				}
			}
			long totalSalary = settle + notSettle;
			// 已结算薪资
			resultMap.put("settleSalary", settle);
			// 总薪资
			resultMap.put("totalSalary", totalSalary);
			resultList.add(resultMap);
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", resultList);
	}

	/**
	 * 获取薪资详情
	 * 
	 * @param dto
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findSettDtl")
	public ResultDTO<List<Map<String, Object>>> findSettDtl(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId, Page<CustomSettleDTO> pageParams) throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<List<Map<String, Object>>>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
		CustomSettleDTO customSettleDTO = new CustomSettleDTO();
		customSettleDTO.setLessonId(lessonId);
		customSettleDTO.setTutorId(tutorDTO.getTutorId());

		Page<CustomSettleDTO> customSettPage = customSettleService.findPaging(pageParams, customSettleDTO);
		List<CustomSettleDTO> customSettleList = null;

		if (customSettPage != null) {
			customSettleList = customSettPage.getList();
		}

		if (customSettleList == null) {
			return result.set(ResultCodeEnum.SUCCESS, "", resultList);
		}

		for (CustomSettleDTO customSettle : customSettleList) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			// 课时名称
			resultMap.put("className", customSettle.getClassName());
			// 单价(美元)
			resultMap.put("settlePrice", customSettle.getSettlePriceStr());
			// 创建时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			resultMap.put("createTime", sdf.format(customSettle.getCreateTime()));
			// 结算状态
			resultMap.put("settleStatus", customSettle.getSettleStatus());
			// 结算金额(美元)
			long settleSalary = customSettle.getSettlePrice() * customSettle.getSettleDuration() / 60;
			resultMap.put("settleSalary", AmountUtils.changeF2YString(settleSalary));
			// 结算时长(分钟)
			resultMap.put("settleDuration", customSettle.getSettleDuration());
			resultList.add(resultMap);
		}

		return result.set(ResultCodeEnum.SUCCESS, "", resultList);
	}
}
