/*
 * CustomClassServiceImpl.java Created on 2017年10月11日 下午7:45:22
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.RandomUtils;
import com.wst.constant.RedisConstant;
import com.wst.entity.lesson.CustomClassVideo;
import com.wst.lesson.dao.CustomClassDao;
import com.wst.lesson.dao.CustomClassDateDao;
import com.wst.lesson.dao.CustomClassVideoDao;
import com.wst.lesson.dao.CustomDao;
import com.wst.lesson.dao.CustomSettleDao;
import com.wst.lesson.dao.CustomTutorClassDao;
import com.wst.mem.dao.TutorDao;
import com.wst.mem.dao.TutorSalaryRecordDao;
import com.wst.mem.dao.UserDao;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomClassDateDTO;
import com.wst.service.lesson.dto.CustomClassVideoDtlDTO;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.dto.CustomSettleDTO;
import com.wst.service.lesson.dto.CustomTutorClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;

/**
 * 课时服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class CustomClassServiceImpl implements CustomClassService {

	private HiwiLog logger = HiwiLogFactory.getLogger(CustomClassServiceImpl.class);

	@Resource
	private CustomClassDao customClassDao;

	@Resource
	private CustomClassDateDao customClassDateDao;

	@Resource
	private CustomClassVideoDao customClassVideoDao;

	@Resource
	private MssageService mssageService;

	@Resource
	private CustomDao customDao;

	@Resource
	private UserDao userDao;

	@Resource
	private TutorDao tutorDao;

	@Resource
	private CustomTutorClassDao customTutorClassDao;

	@Resource
	private CustomSettleDao customSettleDao;

	@Resource
	private TutorSalaryRecordDao tutorSalaryRecordDao;

	@Override
	public CustomClassDTO getCustomClassById(long classId) throws Exception {
		return customClassDao.getCustomClass(classId);
	}

	@Override
	public List<Map<String, Object>> findLessons(Page<CustomClassDTO> param, long tutorId) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();

		CustomClassDTO customClassDTO = new CustomClassDTO();
		customClassDTO.setTutorId(tutorId);

		List<CustomClassDTO> customClassList = customClassDao.findLessons(param, customClassDTO).getList();
		if (customClassList == null || customClassList.size() == 0) {
			return resultList;
		}

		if (customClassList != null && customClassList.size() > 0) {
			for (CustomClassDTO customClass : customClassList) {
				CustomDTO custom = customDao.getLessonById(customClass.getLessonId());
				UserDTO user = userDao.getUserById(custom.getUserId());
				Map<String, Object> resultMap = new HashMap<>();
				// 学生ID
				resultMap.put("userId", user.getUserId());
				// 学生姓名
				resultMap.put("userName", user.getRealName());
				// 课程名称
				resultMap.put("lessonName", custom.getLessonName());
				// 学生头像地址
				resultMap.put("userPicUrl", user.getPortraitImgUrl());
				resultList.add(resultMap);
			}
		}
		return resultList;
	}

	@Override
	public ResultDTO<String> classAppointment(long dateMemType, long classId, Date appointmentTime, long userId)
			throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 校验约课时间，约课时间不能小于当前时间
		if (appointmentTime.before(new Date())) {
			logger.error("===发起约课，约课时间不能小于当前时间");
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class time is not less than current time", "");
		}

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);
		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}
		CustomDTO customDTO = customResult.getResult();

		// 校验时间
		/*
		 * if (appointmentTime.after(customDTO.getEndTime())) {
		 * logger.error("===发起约课，约课时间不能大于课程结束时间"); return
		 * resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
		 * "Class time is not more than the end of the course time", ""); }
		 */

		// 校验用户合法性
		if (dateMemType == 1 && customDTO.getUserId() != userId) {
			logger.error("===发起约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		} else if (dateMemType == 2 && customClassDTO.getTutorId() != userId) {
			logger.error("===发起约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 查询约课记录
		CustomClassDateDTO customClassDateDTO = customClassDateDao.getCustomClassDateByClassId(classId);
		if (customClassDateDTO != null) {
			// 校验状态（约课已确认，不能再次约课）
			if (customClassDateDTO.getCancelUserId() <= 0) {
				if (customClassDateDTO.getConfirmStatus() == 2) {// 已确认
					logger.error("===发起约课，约课记录已确认，课时ID=" + classId);
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
							"The appointment has been confirmed and no further appointment is allowed", "");
				} else {// 未确认
					if (customClassDateDTO.getDateMemType() != dateMemType) {
						if (customClassDateDTO.getDateMemType() == 1) {
							logger.error("===发起约课，学生已发起约课，导师不能再次发起，课时ID=" + classId + "，约课记录ID="
									+ customClassDateDTO.getRecordId());
							// (学生已发起约课，请去确认)
							return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
									"The students have already started an appointment. Please go to confirm");
						} else {
							logger.error("===发起约课，导师已发起约课，学生不能再次发起，课时ID=" + classId + "，约课记录ID="
									+ customClassDateDTO.getRecordId());
							// (导师已发起约课，请去确认)
							return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
									"The tutor has started an appointment. Please go to confirm");
						}
					}
				}
			}
		}

		// 保存约课记录（约课发起成功）
		customClassDateDTO = new CustomClassDateDTO();
		customClassDateDTO.setClassId(classId);
		customClassDateDTO.setDateTime(appointmentTime);
		customClassDateDTO.setDateMemType(dateMemType);
		customClassDateDTO.setConfirmStatus(1);
		customClassDateDao.save(customClassDateDTO);
		// 更新约课时间
		customClassDao.updateDateTime(classId, appointmentTime);

		// 保存消息（未确认约课信息）
		if (dateMemType == 1) {
			// 给导师发送消息
			// mssageService.tutorCustomMssage(customDTO.getUserId(),
			// customClassDTO.getTutorId(),
			// customDTO.getLessonId() + "", "Unconfirmed course information",
			// "");
		} else {
			// 给学生推送消息
			mssageService.baseSendMssage(customClassDTO.getTutorId(), customDTO.getUserId(),
					customDTO.getLessonId() + "", 1, 2);
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "About class initiated success", "");
	}

	@Override
	public ResultDTO<String> classAppointmentAffirm(long dateMemType, long classId, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}
		CustomDTO customDTO = customResult.getResult();

		// 校验用户合法性
		if (dateMemType == 1 && customDTO.getUserId() != userId) {
			logger.error("===确认约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		} else if (dateMemType == 2 && customClassDTO.getTutorId() != userId) {
			logger.error("===确认约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 查询约课记录
		CustomClassDateDTO customClassDateDTO = customClassDateDao.getCustomClassDateByClassId(classId);

		// 约课记录不存在
		if (customClassDateDTO == null) {
			logger.error("===确认约课，约课记录不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		// 校验状态（约课已确认，不能再次确认）
		if (customClassDateDTO.getConfirmStatus() == 2) {
			logger.error("===确认约课，约课已确认，不能再次确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
					"The appointment has been confirmed and cannot be reconfirmed", "");
		}

		// 约课已被取消
		if (customClassDateDTO.getCancelUserId() > 0) {
			logger.error("===确认约课，约课已取消，不能确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The appointment has been cancelled", "");
		}

		// 校验用户类型
		if (customClassDateDTO.getDateMemType() == dateMemType) {
			String desc = "";
			if (customClassDateDTO.getDateMemType() == 1) {
				logger.error("===确认约课，约课只能被学生确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId()
						+ "，用户ID=" + userId);
				desc = "Classes can only be confirmed by students";// (约课只能被学生确认)
			} else {
				logger.error("===确认约课，约课只能被导师确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId()
						+ "，用户ID=" + userId);
				desc = "The appointment class can only be confirmed by the supervisor"; // (约课只能被导师确认)
			}
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, desc, "");
		}

		// 确认约课
		customClassDateDao.confirmClassStatus(customClassDateDTO.getRecordId());
		if (dateMemType == 1) {
			mssageService.baseSendMssage(customDTO.getUserId(), customClassDTO.getTutorId(),
					customDTO.getLessonId() + "", 2, 16);
		}
		// 保存消息（约课已确认）
		// if (dateMemType == 1) {
		// // 给导师发送消息

		// } else {
		// // 给学生发送消息
		// mssageService.memCustomMssage(customClassDTO.getTutorId(),
		// customDTO.getUserId(),
		// customDTO.getLessonId() + "", "Appointment has been confirmed", "");
		// UserDTO user = userDao.getUserById(customDTO.getUserId());
		// try {
		// if (StringUtils.isNotEmpty(user.getDeputyLoginAccount())) {
		// WxMpKefuMessage.TEXT().toUser(user.getDeputyLoginAccount())
		// .content("Appointment has been confirmed").build();
		// }
		// } catch (Exception e) {
		// logger.error("约课已确认，微信通知学生异常：", e);
		// }
		// }
		return resultDTO.set(ResultCodeEnum.SUCCESS, "Appointment confirms success", "");
	}

	@Override
	public ResultDTO<String> classAppointmentCancel(long dateMemType, long classId, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}
		CustomDTO customDTO = customResult.getResult();
		// 校验用户合法性
		if (dateMemType == 1 && customDTO.getUserId() != userId) {
			logger.error("===取消约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		} else if (dateMemType == 2 && customClassDTO.getTutorId() != userId) {
			logger.error("===取消约课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 查询约课记录
		CustomClassDateDTO customClassDateDTO = customClassDateDao.getCustomClassDateByClassId(classId);

		// 约课记录不存在
		if (customClassDateDTO == null) {
			logger.error("===取消约课，约课记录不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		// 约课已取消，不能再次取消
		if (customClassDateDTO.getCancelUserId() > 0) {
			logger.error("===取消约课，约课已取消，不能再次确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
					"The appointment has been cancelled and cannot be cancelled again", "");
		}
		// 约课未确认，随时取消
		if (customClassDateDTO.getConfirmStatus() == 1) {
			customClassDateDao.cancel(customClassDateDTO.getRecordId(), userId);
			customClassDao.cancelClass(classId);
			// // 保存消息（约课未确认被取消）
			// if (dateMemType == 1) {
			// // 给导师发送消息
			// mssageService.tutorCustomMssage(customDTO.getUserId(),
			// customClassDTO.getTutorId(),
			// customDTO.getLessonId() + "", "The appointment was cancelled",
			// "");
			// } else {
			// // 给学生发送消息
			// mssageService.memCustomMssage(customClassDTO.getTutorId(),
			// customDTO.getUserId(),
			// customDTO.getLessonId() + "", "The appointment was cancelled",
			// "");
			// UserDTO user = userDao.getUserById(customDTO.getUserId());
			// try {
			// if (StringUtils.isNotEmpty(user.getDeputyLoginAccount())) {
			// WxMpKefuMessage.TEXT().toUser(user.getDeputyLoginAccount())
			// .content("The appointment was cancelled").build();
			// }
			// } catch (Exception e) {
			// logger.error("约课取消，微信通知学生异常：", e);
			// }
			// }
		} else { // 约课已被确认
			// 学生取消约课，需校验取消次数
			if (dateMemType == 1) {
				// 校验取消时间是否在约课时间两小时内
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.HOUR, 2);
				// 如果取消时间在约课时间两小时内，校验取消次数
				if (calendar.getTime().after(customClassDateDTO.getDateTime())) {
					// 获取学生取消次数
					String cancelNum = RedisClient.get(RedisConstant.CLASS_CANCEL_NUMBER_ + userId);
					// 取消次数大于3次，直接默认上课
					if (StringUtils.isNotEmpty(cancelNum) && Long.valueOf(cancelNum) >= 3) {
						// 塞一个开始时间
						customClassDao.upBeginTime(customClassDTO.getClassId());
						// 课时结束
						ResultDTO<String> result = classEnd(customClassDTO, customClassDTO.getPlanDuration());
						if (!result.isSuccess()) {
							return result;
						}
						// 学生在上课前2小时内取消上课,给导师推送消息
						mssageService.baseSendMssage(customDTO.getUserId(), customClassDTO.getTutorId(),
								customDTO.getLessonId() + "", 2, 9);
						logger.debug("===取消约课，取消次数大于3次，直接默认课时结束，课时ID=" + classId + "，用户ID=" + userId);
						return resultDTO.set(ResultCodeEnum.SUCCESS,
								"Cancel class, cancel more than 3 times, direct default class end", "");
					}
					// 两小时内取消次数+1
					RedisClient.incr(RedisConstant.CLASS_CANCEL_NUMBER_ + userId);
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.DAY_OF_MONTH, 1);// 设为当月的1号
					cal.set(Calendar.HOUR_OF_DAY, 0); // 小时
					cal.set(Calendar.MINUTE, 0); // 分钟
					cal.set(Calendar.SECOND, 0); // 秒
					cal.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
					// 设置失效时间
					RedisClient.expire(RedisConstant.CLASS_CANCEL_NUMBER_ + userId,
							(cal.getTimeInMillis() - System.currentTimeMillis()) / 1000);
				}
			}

			// 导师取消或学生两小时外取消
			customClassDateDao.cancel(customClassDateDTO.getRecordId(), userId);
			customClassDao.cancelClass(classId);
			if (dateMemType == 1) {
				mssageService.baseSendMssage(customDTO.getUserId(), customClassDTO.getTutorId(),
						customDTO.getLessonId() + "", 2, 9);
			} else {
				mssageService.baseSendMssage(customClassDTO.getTutorId(), customDTO.getUserId(),
						customDTO.getLessonId() + "", 1, 17);
			}

			// // 保存消息（已确认约课被取消）
			// if (dateMemType == 1) {
			// // 给导师发送消息
			// mssageService.tutorCustomMssage(customDTO.getUserId(),
			// customClassDTO.getTutorId(),
			// customDTO.getLessonId() + "", "It has been cancelled", "");
			// } else {
			// // 给学生发送消息
			// mssageService.memCustomMssage(customClassDTO.getTutorId(),
			// customDTO.getUserId(),
			// customDTO.getLessonId() + "", "It has been cancelled", "");
			// UserDTO user = userDao.getUserById(customDTO.getUserId());
			// try {
			// if (StringUtils.isNotEmpty(user.getDeputyLoginAccount())) {
			// WxMpKefuMessage.TEXT().toUser(user.getDeputyLoginAccount()).content("It
			// has been cancelled")
			// .build();
			// }
			// } catch (Exception e) {
			// logger.error("已确认约课被取消，微信通知学生异常：", e);
			// }
			// }
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "Appointment cancelled", "");
	}

	@Override
	public ResultDTO<String> attendClass(long dateMemType, long classId, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}
		CustomDTO customDTO = customResult.getResult();
		// 校验用户合法性
		if (dateMemType == 1 && customDTO.getUserId() != userId) {
			logger.error("===开始上课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		} else if (dateMemType == 2 && customClassDTO.getTutorId() != userId) {
			logger.error("===开始上课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 查询约课记录
		CustomClassDateDTO customClassDateDTO = customClassDateDao.getCustomClassDateByClassId(classId);

		// 约课记录不存在
		if (customClassDateDTO == null) {
			logger.error("===开始上课，约课记录不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		// 校验状态（约课未确认）
		if (customClassDateDTO.getConfirmStatus() != 2) {
			logger.error("===开始上课，约课未确认，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
					"The appointment has been confirmed and cannot be reconfirmed", "");
		}

		// 约课已被取消
		if (customClassDateDTO.getCancelUserId() > 0) {
			logger.error("===开始上课，约课已取，课时ID=" + classId + "，记录ID=" + customClassDateDTO.getRecordId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The appointment has been cancelled", "");
		}

		// 开始上课
		customClassDao.attendClass(classId);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
	}

	@Override
	public ResultDTO<String> updateClass(long classId, String className, Date beginTime, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, false);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}
		// 校验用户合法性
		if (customClassDTO.getTutorId() != userId) {
			logger.error("===修改课时信息，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		customClassDao.updateClass(classId, className, beginTime);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "Modify successfully", "");
	}

	@Override
	public ResultDTO<String> classConfirmFinish(long classId, long realDuration, long tutorId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), "");
		}

		// 校验用户合法性
		if (customClassDTO.getTutorId() != tutorId) {
			logger.error("===课时结束确认，用户不合法，课时ID=" + classId + "，导师ID=" + tutorId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 校验课时状态，状态为1，课时未上课，学生取消约课
		if (customClassDTO.getClassStatus() == 1) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Your students have canceled the appointment");
		}

		// 结束课时
		resultDTO = classEnd(customClassDTO, realDuration);
		if (resultDTO.isSuccess()) {
			// 给学生推送消息
			mssageService.baseSendMssage(customClassDTO.getTutorId(), customResult.getResult().getUserId(),
					customClassDTO.getLessonId() + "", 1, 5);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO<String> classBriefSummary(long classId, String classSummary, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		// 课时不存在
		if (customClassDTO == null) {
			logger.error("===课时小结，课时不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		// 校验用户合法性
		if (customClassDTO.getTutorId() != userId) {
			logger.error("===课时小结，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 校验课时状态，状态为1或2，课时未完结
		if (customClassDTO.getClassStatus() == 1 || customClassDTO.getClassStatus() == 2) {
			logger.error("===课时小结，课时未完结，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The class was not finished", "");
		}

		customClassDao.classBriefSummary(classId, classSummary);

		// 给学生推送消息
		mssageService.baseSendMssage(customClassDTO.getTutorId(), userId, customClassDTO.getLessonId() + "", 1, 4);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
	}

	@Override
	public ResultDTO<String> uploadReadyFile(long classId, String readyFile, long userId) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		// 课时不存在
		if (customClassDTO == null) {
			logger.error("===上传文件，课时不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		// 校验用户合法性
		if (customClassDTO.getTutorId() != userId) {
			logger.error("===上传文件，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		customClassDao.uploadReadyFile(classId, readyFile);
		// 给学生推送消息
		mssageService.baseSendMssage(customClassDTO.getTutorId(), userId, customClassDTO.getLessonId() + "", 1, 3);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
	}

	@Override
	public ResultDTO<String> confirmAttendClass(long classId, long userId, long method) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 查询课时信息
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);

		// 课时不存在
		if (customClassDTO == null) {
			logger.error("===确认上课，课时不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}
		// 课程信息
		CustomDTO customDTO = customDao.getLessonById(customClassDTO.getLessonId());
		// 课程不存在
		if (customDTO == null) {
			logger.error("===确认上课，课程不存在，课时ID=" + classId);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Lesson does not exist", "");
		}
		// 校验用户合法性
		if (customDTO.getUserId() != userId) {
			logger.error("===确认上课，用户不合法，课时ID=" + classId + "，用户ID=" + userId);
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "User illegal", "");
		}

		// 校验课时状态，状态为3
		if (customClassDTO.getClassStatus() != 3) {
			logger.error("===确认上课，课时状态不合法，课时ID=" + classId + "，状态=" + customClassDTO.getClassStatus());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class hour is not legal", "");
		}
		customClassDao.confirmAttendClass(classId);
		// 记录课程已完成课时时间，当前课时指向下一课时
		List<CustomClassDTO> customClassList = customClassDao.findNotFinishCustomClass(classId,
				customClassDTO.getLessonId(), 0);
		if (customClassList != null && customClassList.size() > 0) {
			customDao.updateCurrentClass(customClassDTO.getLessonId(), customClassList.get(0).getClassId(),
					customClassDTO.getRealDuration());
		} else {
			customDao.updateCurrentClass(customClassDTO.getLessonId(), classId, customClassDTO.getRealDuration());
		}
		confirmAttendClass(customClassDTO, customDTO);
		if (method == 1) {
			// 给导师推送消息
			mssageService.baseSendMssage(customDTO.getUserId(), customClassDTO.getTutorId(),
					customDTO.getLessonId() + "", 2, 6);
		} else {
			// 给导师推送消息
			mssageService.baseSendMssage(customDTO.getUserId(), customClassDTO.getTutorId(),
					customDTO.getLessonId() + "", 2, 7);
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public List<CustomClassDTO> findLessonClassList(long lessonId) throws Exception {
		return customClassDao.findLessonClassList(lessonId);
	}

	@Override
	public Page<CustomClassDTO> findGiveLessonsPaging(Page<CustomClassDTO> page, String lessonName, String userLoginAccount,
			String tutorLoginAccount, Date beginTime, Date endTime) throws Exception {
		return customClassDao.findGiveLessonsPaging(page, lessonName, userLoginAccount, tutorLoginAccount, beginTime, endTime);
	}

	/**
	 * 课时结束（导师）
	 * 
	 * @param customClass
	 *            课时
	 * @param realDuration
	 *            实际时长（分钟）
	 * @return
	 * @throws Exception
	 */
	private ResultDTO<String> classEnd(CustomClassDTO customClass, long realDuration) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		// 导师课时计划总时长
		CustomTutorClassDTO customTutorClassDTO = customTutorClassDao.getCustomTutorClass(customClass.getLessonId(),
				customClass.getTutorId());
		// 导师计划课时不存在
		if (customTutorClassDTO == null) {
			logger.debug("===课时结束，导师计划课时不存在，课时ID=" + customClass.getClassId());
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The tutor plan class does not exist", "");
		}

		// 剩余时长
		long residueClass = customTutorClassDTO.getTotalClass() - customTutorClassDTO.getFinishClass();
		// 校验总时长与已完成课时
		if (residueClass <= realDuration) {
			realDuration = residueClass;
			residueClass = 0;
		}

		// 更新导师已完成时长
		customTutorClassDao.updateFinishClass(customTutorClassDTO.getPlanId(), realDuration);
		// 确认结束当前选择课时
		customClassDao.classConfirmFinish(customClass.getClassId(), realDuration);

		// 剩余时长为0，如果同一课程同一导师还有未上的课时，全部置为结束
		long classId = customClass.getClassId();
		if (residueClass == 0) {
			List<CustomClassDTO> customClassList = customClassDao.findNotFinishCustomClass(classId,
					customClass.getLessonId(), customClass.getTutorId());
			if (customClassList != null && customClassList.size() > 0) {
				for (CustomClassDTO customClassDTO : customClassList) {
					classId = customClassDTO.getClassId();
					// 塞一个开始时间
					customClassDao.upBeginTime(customClassDTO.getClassId());
					customClassDao.classConfirmFinish(customClassDTO.getClassId(), residueClass);
				}
			}
		}

		// 校验是否需要取消VIP
		//checkVip(classId, customClass.getLessonId());

		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", realDuration + "");
	}

	/**
	 * 确认上课（学生,结算相关操作）
	 * 
	 * @param customClass
	 *            课时
	 * @param custom
	 *            课程
	 * @return
	 * @throws Exception
	 */
	private ResultDTO<String> confirmAttendClass(CustomClassDTO customClass, CustomDTO custom) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<>();

		List<CustomSettleDTO> customSettleList = customSettleDao.findNotSettleCustomSettle(customClass.getTutorId(),
				custom.getLessonId());

		// 若结算时间为0，不生成新的结算记录
		if (customClass.getRealDuration() == 0) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
		}

		// 未结算总时长
		long totalDuration = 0;
		if (customSettleList != null && customSettleList.size() > 0) {
			for (CustomSettleDTO customSettleDTO : customSettleList) {
				totalDuration += customSettleDTO.getSettleDuration();
			}
		}

		// 导师薪资
		CustomClassDTO customClassDTO = customClassDao.getCustomClass(customClass.getClassId());
		TutorDTO tutorDTO = tutorDao.getTutorById(customClassDTO.getTutorId());
		long baseSalary = 0;
		if (tutorDTO != null) {
			baseSalary = tutorDTO.getTutorSalary();
		}

		List<CustomClassDTO> customClassDTOList = customClassDao.findNotFinishCustom(customClass.getLessonId(),
				customClass.getClassId());
		List<CustomSettleDTO> customNotSettleList = customSettleDao.findNoSttleByLessonId(customClass.getLessonId());

		long finishClass = customClass.getRealDuration() + custom.getFinishClass();
		// 结束时间等于总时间，课程所有未记录结算 或者 课时全部完结
		if (custom.getTotalClass() == finishClass || customClassDTOList == null || customClassDTOList.size() == 0) {
			saveCustomSettle(customClassDTO, custom, baseSalary, 2, customClassDTO.getRealDuration());
			if (customNotSettleList != null && customNotSettleList.size() > 0) {
				for (CustomSettleDTO customSettleDTO : customNotSettleList) {
					customSettleDao.haveSettle(customSettleDTO.getRecordId());
				}
			}
			return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
		}

		// 实际时长
		long realDuration = customClass.getRealDuration();
		if (totalDuration + realDuration >= 300) {
			long settleDuration = 300 - totalDuration;
			if (customSettleList != null && customSettleList.size() > 0) {
				for (CustomSettleDTO customSettleDTO : customSettleList) {
					customSettleDao.haveSettle(customSettleDTO.getRecordId());
				}
			}
			// 补满上次结算5小时
			saveCustomSettle(customClassDTO, custom, baseSalary, 2, settleDuration);
			// 剩余实际时长
			realDuration = realDuration - settleDuration;
			while (realDuration > 300) {
				saveCustomSettle(customClassDTO, custom, baseSalary, 2, 300);
			}
		}

		// 剩余实际时长不足5小时
		if (realDuration > 0) {
			saveCustomSettle(customClassDTO, custom, baseSalary, 1, realDuration);
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "success", "");
	}

	/**
	 * 校验是否取消VIP
	 * 
	 * @param classId
	 *            课时id
	 * @param lessonId
	 *            课程id
	 * @throws Exception
	 */
	/*private void checkVip(long classId, long lessonId) throws Exception {

		// 校验用户是否有别的课程未结束
		CustomDTO customDTO = customDao.getLessonById(lessonId);
		List<CustomDTO> customList = customDao.findUnclosedCustom(lessonId, customDTO.getUserId());
		// 用户有效时间内没有课程，关闭用VIP
		if (customList == null || customList.size() <= 0) {
			userDao.updateUserLevel(customDTO.getUserId(), 1);
			String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + customDTO.getUserId();
			if (!RedisClient.exists(userLimitKey)) {
				RedisClient.set(userLimitKey, customDTO.getUserId());
			}
		}
	}*/

	@Override
	public ResultDTO<Map<String, String>> enterClass(long classId, long userId, long tutorId) throws Exception {
		ResultDTO<Map<String, String>> resultDTO = new ResultDTO<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>();

		CustomClassDTO customClassDTO = customClassDao.getCustomClass(classId);
		ResultDTO<CustomDTO> customResult = checkCustomClass(customClassDTO, true);
		if (!customResult.isSuccess()) {
			return resultDTO.set(customResult.getErrCode(), customResult.getErrDesc(), resultMap);
		}

		TutorDTO tutor = tutorDao.getTutorById(customClassDTO.getTutorId());
		long userID = userId;
		if (userID == 0) {
			userID = customDao.getLessonById(customClassDTO.getLessonId()).getUserId();
		}
		UserDTO user = userDao.getUserById(userID);

		// 学生邮箱
		resultMap.put("userEmail", user.getLoginAccount());
		// 导师邮箱
		resultMap.put("tutorEmail", tutor.getLoginAccount());

		if (customClassDTO.getRoomId() > 0) {
			resultMap.put("roomId", String.valueOf(customClassDTO.getRoomId()));
			return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultMap);
		}

		// 导师端无房间号则生成
		String classIdStr = String.valueOf(classId);
		int idLength = classIdStr.length();
		if (idLength < 6) {
			int[] randArry = RandomUtils.randomCommon(0, 9, 6 - idLength);
			for (int i : randArry) {
				classIdStr += i;
			}
		}
		classIdStr = "10" + classIdStr; // 定制课固定以10开头
		resultMap.put("roomId", classIdStr);

		customClassDao.addMovieId(classId, Long.parseLong(classIdStr));
		return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}

	@Override
	public int upMovieUrl(long classId, String movieUrl) throws Exception {
		return customClassDao.upMovieUrl(classId, movieUrl);
	}

	/**
	 * 校验课时信息
	 * 
	 * @param customClassDTO
	 *            课时信息
	 * @param isCurrentClass
	 *            是否校验当前课时
	 * @return 成功返回课程信息
	 * @throws Exception
	 */
	public ResultDTO<CustomDTO> checkCustomClass(CustomClassDTO customClassDTO, boolean isCurrentClass)
			throws Exception {
		ResultDTO<CustomDTO> resultDTO = new ResultDTO<>();

		if (customClassDTO == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist");
		}

		// 校验课时状态，状态为3或4，课时结束或已完结
		if (customClassDTO.getClassStatus() == 3 || customClassDTO.getClassStatus() == 4) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "The class is over");
		}

		CustomDTO customDTO = customDao.getLessonById(customClassDTO.getLessonId());
		if (customDTO == null) {
			// 课程不存在
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "Curriculum does not exist");
		}

		// 校验当前课时
		if (isCurrentClass && customDTO.getCurrentClass() != customClassDTO.getClassId()) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The current class hour operation is not legal");
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "", customDTO);
	}

	/**
	 * 新增结算记录工具
	 * 
	 * @param customClass
	 * @param custom
	 * @param baseSalary
	 * @throws Exception
	 */
	private long saveCustomSettle(CustomClassDTO customClass, CustomDTO custom, long baseSalary, long settleStatus,
			long realDuration) throws Exception {
		CustomSettleDTO customSettle = new CustomSettleDTO();
		customSettle.setTutorId(customClass.getTutorId());
		customSettle.setLessonId(custom.getLessonId());
		customSettle.setLessonName(custom.getLessonName());
		customSettle.setClassId(customClass.getClassId());
		customSettle.setClassName(customClass.getClassName());
		customSettle.setSettleDuration(realDuration);
		customSettle.setSettlePrice(baseSalary);
		customSettle.setCreateTime(new Date());
		customSettle.setSettleStatus(settleStatus);
		// 未付款
		customSettle.setPayStatus(1);
		return customSettleDao.saveCustomSettle(customSettle);
	}

	@Override
	public List<CustomClassDTO> findCustomClassByNHourNotConfirm(long hour) throws Exception {
		return customClassDao.findCustomClassByNHourNotConfirm(hour);
	}

	@Override
	public List<CustomClassDTO> findCustomClassListByPlanTimeNDay(long day) throws Exception {
		return customClassDao.findCustomClassListByPlanTimeNDay(day);
	}

	@Override
	public List<CustomClassDTO> findCustomClassListByNDayNotClass(long day) throws Exception {
		return customClassDao.findCustomClassListByNDayNotClass(day);
	}

	@Override
	public List<CustomClassDTO> findCustomClassListByPayNDayNotClass(long day) throws Exception {
		return customClassDao.findCustomClassListByPayNDayNotClass(day);
	}

	@Override
	public List<CustomClassDTO> findCustomClassListByDataTimeNHour(long hour) throws Exception {
		return customClassDao.findCustomClassListByDataTimeNHour(hour);
	}

	@Override
	public long saveCustomClassVideo(long classId, String movieUrl) throws Exception {
		CustomClassVideo customClassVideo = new CustomClassVideo();

		customClassVideo.setClassId(classId);
		customClassVideo.setVid(movieUrl);
		customClassVideo.setUploadTime(new Date());

		return customClassVideoDao.insertCustomClassVideo(customClassVideo);
	}

	@Override
	public List<CustomClassVideo> getPendingCustomClassVideo() throws Exception {
		return customClassVideoDao.getPendingCustomClassVideo();
	}

	@Override
	public int updateCustomClassVideoFail(CustomClassVideo customClassVideo) throws Exception {
		return customClassVideoDao.updateCustomClassVideo(customClassVideo);
	}

	@Override
	public void updateCustomClassVideoSuccess(CustomClassVideo customClassVideo) throws Exception {
		customClassVideoDao.deleteCustomClassVideo(customClassVideo);
	}

	@Override
	public List<CustomClassVideoDtlDTO> findVideoByClassId(long classId) throws Exception {
		return customClassVideoDao.findVideoByClassId(classId);
	}

	@Override
	public ResultDTO<String> getSurplusTime(long classId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		CustomClassDTO customClass = customClassDao.getCustomClass(classId);
		if (customClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Class does not exist", "");
		}

		CustomTutorClassDTO customTutorClassDTO = customTutorClassDao.getCustomTutorClass(customClass.getLessonId(),
				customClass.getTutorId());
		if (customTutorClassDTO == null) {
			logger.debug("===课时结束，导师计划课时不存在，课时ID=" + customClass.getClassId());
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The tutor plan class does not exist", "");
		}

		long residueClass = (customTutorClassDTO.getTotalClass() - customTutorClassDTO.getFinishClass());

		long hour = residueClass / 60; // 小时
		long min = residueClass - hour * 60; // 分钟

		String residueClassStr = "";
		if (hour > 1) {
			residueClassStr = hour + " Hours";
			if (min > 0) {
				residueClassStr = min + " minutes " + hour + " Hours";
			}
		} else if (hour == 1) {
			residueClassStr = hour + " Hour";
			if (min > 0) {
				residueClassStr = min + " minutes " + hour + " Hour";
			}
		} else {
			residueClassStr = min + " minutes";
		}

		return result.set(ResultCodeEnum.SUCCESS, "", residueClassStr);
	}

	@Override
	public CustomClassDTO getByRoomId(long roomId) throws Exception {
		return customClassDao.getByRoomId(roomId);
	}
}
