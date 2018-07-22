/*
 * LessonCustomClassController.java Created on 2017年10月11日 下午7:06:37
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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.FileInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.file.FileUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.RedisConstant;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.UserService;

/**
 * 课程课时控制层
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/tutor")
public class LessonCustomClassController {

	private Lock classLock = new ReentrantLock(); // 课时锁

	@Reference(version = "0.0.1")
	private CustomClassService customClassService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private LessonCustomService LessonCustomService;

	/**
	 * 修改课时
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @param classRcmd
	 *            课时介绍
	 * @param planDuration
	 *            计划时长
	 * @param beginTime
	 *            上课时间
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateClass")
	public ResultDTO<String> updateClass(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId,
			String className, String beginTime) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();
		String classLimitKey = RedisConstant.CLASS_LIMIT_ + classId;

		// 校验请求参数
		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}
		if (StringUtils.isEmpty(className)) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please fill in the class contents", "");
		}
		if (StringUtils.isEmpty(beginTime) || !com.hiwi.common.util.StringUtils.isDate(beginTime)) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please fill in the class time", "");
		}
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", "");
		}

		// 课时一次只能一个用户操作(课时已被锁定，正在操作)
		try {
			classLock.lock();
			if (RedisClient.exists(classLimitKey)) {
				return resultDTO.set(ResultCodeEnum.ERROR_REQ_OFTEN, "The class has been locked and is operating", "");
			}
			RedisClient.set(classLimitKey, classId);
			RedisClient.expire(classLimitKey, RedisConstant.CLASS_LIMIT_EXPIRE);
		} finally {
			classLock.unlock();
		}

		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);

		// 修改课时
		resultDTO = customClassService.updateClass(classId, className,
				DateUtils.getDateParse(beginTime, DateUtils.YYYY_MM_DD), tutorDTO.getTutorId());
		// 删掉redis中记录
		RedisClient.del(classLimitKey);
		return resultDTO;
	}

	/**
	 * 结束课时
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @param classRealDuration
	 *            实际时长（分钟）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "classFinish")
	public ResultDTO<String> classFinish(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId,
			@RequestParam(defaultValue = "0") float classRealDuration) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();
		String classLimitKey = RedisConstant.CLASS_LIMIT_ + classId;

		// 校验请求参数
		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}
		if (classRealDuration <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please fill in the schedule", "");
		}
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", "");
		}

		// 课时一次只能一个用户操作(课时已被锁定，正在操作)
		try {
			classLock.lock();
			if (RedisClient.exists(classLimitKey)) {
				return resultDTO.set(ResultCodeEnum.ERROR_REQ_OFTEN, "The class has been locked and is operating", "");
			}
			RedisClient.set(classLimitKey, classId);
			RedisClient.expire(classLimitKey, RedisConstant.CLASS_LIMIT_EXPIRE);
		} finally {
			classLock.unlock();
		}

		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);

		// 结束课时
		try {
		    resultDTO = customClassService.classConfirmFinish(classId, (long) (classRealDuration), tutorDTO.getTutorId());
		} finally {
		    RedisClient.del(classLimitKey);
		}
		
		return resultDTO;
	}

	/**
	 * 课时小结
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @param classBriefSummary
	 *            课时小结
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "classBriefSummary")
	public ResultDTO<String> classBriefSummary(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId,
			String classBriefSummary) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 校验请求参数
		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}
		if (StringUtils.isEmpty(classBriefSummary)) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please fill in the class summary", "");
		}
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", "");
		}

		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);

		// 课时小结
		resultDTO = customClassService.classBriefSummary(classId, classBriefSummary, tutorDTO.getTutorId());
		return resultDTO;
	}

	/**
	 * 上传备课文件
	 * 
	 * @param dto
	 * @param classId
	 * @param classFile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "uploadClassFile")
	public ResultDTO<String> uploadClassFile(HttpServletRequest request, CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long classId, @RequestParam("classFile") MultipartFile classFile)
			throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		FileInfoDTO fileInfoDTO = FileUtils.uploadFile(classFile);

		if (fileInfoDTO == null || StringUtils.isEmpty(fileInfoDTO.getUrl())) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select a file", "");
		}
		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", "");
		}

		// 导师信息
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);

		// 上传文件
		resultDTO = customClassService.uploadReadyFile(classId,
				"https://" + request.getServerName() + "/" + fileInfoDTO.getUrl(), tutorDTO.getTutorId());
		return resultDTO;
	}

	/**
	 * 视频地址上传
	 * 
	 * @param dto
	 * @param classId
	 * @param movieAddr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "movieUpload")
	public ResultDTO<String> movieUpload(CommonParamsDTO dto, long classId, String movieAddr) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 缺少必要参数
		if (classId == 0 || StringUtils.isEmpty(movieAddr)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}

		CustomClassDTO customClass = customClassService.getCustomClassById(classId);
		// 课时不合法
		if (customClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Class ID is not legal");
		}

		// 记录待拉取的视频信息
		customClassService.saveCustomClassVideo(classId, movieAddr);

		return result.set(ResultCodeEnum.SUCCESS, "Success");
	}

	/**
	 * 上课
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "attendClass")
	public ResultDTO<String> attendClass(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
			throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();
		String classLimitKey = RedisConstant.CLASS_LIMIT_ + classId;

		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}

		// 课时一次只能一个用户操作(课时已被锁定，正在操作)
		try {
			classLock.lock();
			if (RedisClient.exists(classLimitKey)) {
				return resultDTO.set(ResultCodeEnum.ERROR_REQ_OFTEN, "The class has been locked and is operating", "");
			}
			RedisClient.set(classLimitKey, classId);
			RedisClient.expire(classLimitKey, RedisConstant.CLASS_LIMIT_EXPIRE);
		} finally {
			classLock.unlock();
		}
		long userType = 0;
		long userId = 0;
		if (StringUtils.isNotEmpty(dto.getAppSessionId()) && dto.getAppSessionId().endsWith("T")) {
			userType = 2;
			TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
			userId = tutorDTO.getTutorId();

		} else {
			userType = 1;
			UserDTO userDTO = UserHelper.getUserDTO(dto);
			userId = userDTO.getUserId();
		}

		// 开始上课
		resultDTO = customClassService.attendClass(userType, classId, userId);
		// 删掉redis中记录
		RedisClient.del(classLimitKey);
		return resultDTO;
	}

	/**
	 * 获取导师剩余时长
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getSurplusTime")
	public ResultDTO<String> getSurplusTime(CommonParamsDTO dto, long classId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (classId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Required parameter missing");
		}

		return customClassService.getSurplusTime(classId);
	}
}
