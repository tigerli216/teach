/*
 * CustomClassController.java Created on 2017年10月13日 下午6:11:25
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
package com.wst.commonality;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.DateUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.RedisConstant;
import com.wst.constant.SysConfigConstant;
import com.wst.constant.SysConfigConstant.BusiTypeEnum;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 约课控制层
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API")
public class CustomClassController {

	private Lock classLock = new ReentrantLock(); // 课时锁

	@Reference(version = "0.0.1")
	private CustomClassService customClassService;
	@Reference(version = "0.0.1")
	private TxVideoService txVideoService;
	@Reference(version = "0.0.1")
	private BusiConfigService busiConfigService;

	/**
	 * 约课发起
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @param appointmentTime
	 *            约课时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "classAppointment")
	public ResultDTO<String> classAppointment(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId,
			String appointmentTime) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();
		if (classId <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select class hour", "");
		}

		if (StringUtils.isEmpty(appointmentTime) || !com.hiwi.common.util.StringUtils.isDate(appointmentTime)) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please enter an appointment time", "");
		}
		// 学生不能发起约课
		if (StringUtils.isEmpty(dto.getAppSessionId()) || !dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only mentors can operate", "");
		}

		String classLimitKey = RedisConstant.CLASS_LIMIT_ + classId;
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
		
		try {
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
	        // 约课发起
	        resultDTO = customClassService.classAppointment(userType, classId,
	                DateUtils.getDateParse(appointmentTime, DateUtils.YYYY_MM_DD_HH_MM_SS), userId);
		} finally {
	        RedisClient.del(classLimitKey);
		}

		return resultDTO;
	}

	/**
	 * 约课确认
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "classAppointmentAffirm")
	public ResultDTO<String> classAppointmentAffirm(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
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
		
		try {
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

	        // 约课确认
	        resultDTO = customClassService.classAppointmentAffirm(userType, classId, userId);
		} finally {
	        RedisClient.del(classLimitKey);
        }

		return resultDTO;
	}

	/**
	 * 约课取消
	 * 
	 * @param dto
	 *            公共参数
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "classAppointmentCancel")
	public ResultDTO<String> classAppointmentCancel(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
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
		
		try {
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

	        // 取消约课
	        resultDTO = customClassService.classAppointmentCancel(userType, classId, userId);
		} finally {
	        RedisClient.del(classLimitKey);
        }

		return resultDTO;
	}

	/**
	 * 用户取消约课次数是否超过3次（1-是，2-否）
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "checkClassCancelNum")
	public ResultDTO<String> checkClassCancelNum(CommonParamsDTO dto) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();
		if (StringUtils.isNotEmpty(dto.getAppSessionId()) && dto.getAppSessionId().endsWith("T")) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Only students can operate", "");
		}

		UserDTO userDTO = UserHelper.getUserDTO(dto);
		if (userDTO == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "", "");
		}

		long cancelNum = 0;
		// 获取学生取消次数
		String cancelNumStr = RedisClient.get(RedisConstant.CLASS_CANCEL_NUMBER_ + userDTO.getUserId());
		if (StringUtils.isNotEmpty(cancelNumStr)) {
			cancelNum = Long.parseLong(cancelNumStr);
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", String.valueOf(cancelNum));
	}

	/**
	 * 进入上课间（获取房间号）
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "enterClass")
	public ResultDTO<Map<String, String>> enterClass(HttpServletRequest request, CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long classId) throws Exception {
		ResultDTO<Map<String, String>> resultDTO = new ResultDTO<Map<String, String>>();
		if (classId <= 0) {
			// 课时不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "Class hours should not be empty");
		}
		String appSessonId = dto.getAppSessionId();
		if (StringUtils.isNotEmpty(appSessonId) && appSessonId.endsWith("T")) {
			TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
			if (tutorDTO == null) {
				return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
			}
			resultDTO = customClassService.enterClass(classId, 0, tutorDTO.getTutorId());
		} else {
			UserDTO userDTO = UserHelper.getUserDTO(dto);
			if (userDTO == null) {
				return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
			}
			resultDTO = customClassService.enterClass(classId, userDTO.getUserId(), 0);
		}
		
		return resultDTO;
	}
	
	/**
	 * 获取腾讯云账号信息
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value = "tencent/getUser")
	public ResultDTO<Map<String, String>> getUser(HttpServletRequest request, CommonParamsDTO dto) throws Exception {
	    ResultDTO<Map<String, String>> resultDTO = new ResultDTO<Map<String, String>>();
	    
	    Map<String, String> resultMap = new HashMap<>();
        String account = UserHelper.getUserAccount(dto);
        resultMap.put("userAccount", account);
        resultMap.put("userSig", txVideoService.createUserSig(account));
        
        BusiConfigDTO busiQuery = new BusiConfigDTO();
        busiQuery.setBusiCode(SysConfigConstant.TENCENT_VIDEO_ACC);
        busiQuery.setBusiType(BusiTypeEnum.SYS_CONFIG.type);
        
        List<BusiConfigDTO> busiConfigList = busiConfigService.getConfigList(busiQuery);
        if (busiConfigList == null || busiConfigList.size() == 0) {
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "busi handle error");
        }
        String adminAccount = JSONObject.parseObject(busiConfigList.get(0).getParams()).getString("identifier");
        resultMap.put("adminAccount", adminAccount);
        resultMap.put("adminSig", txVideoService.createUserSig(adminAccount));
        
        return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}
}
