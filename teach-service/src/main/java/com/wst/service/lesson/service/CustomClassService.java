/*
 * CustomClassService.java Created on 2017年10月11日 下午7:44:25
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
package com.wst.service.lesson.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.entity.lesson.CustomClassVideo;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomClassVideoDtlDTO;

/**
 * 课时接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface CustomClassService {

	/**
	 * 获取导师ID课时列表（课程ID相同的合并）
	 * 
	 * @param param
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findLessons(Page<CustomClassDTO> param, long tutorId) throws Exception;

	/**
	 * 约课发起
	 * 
	 * @param dateMemType
	 *            用户类型
	 * @param classId
	 *            课时ID
	 * @param appointmentTime
	 *            约课时间
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classAppointment(long dateMemType, long classId, Date appointmentTime, long userId)
			throws Exception;

	/**
	 * 约课确认
	 * 
	 * @param dateMemType
	 *            用户类型
	 * @param classId
	 *            课时ID
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classAppointmentAffirm(long dateMemType, long classId, long userId) throws Exception;

	/**
	 * 约课取消
	 * 
	 * @param dateMemType
	 *            用户类型
	 * @param classId
	 *            课时ID
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classAppointmentCancel(long dateMemType, long classId, long userId) throws Exception;

	/**
	 * 开始上课
	 * 
	 * @param dateMemType
	 *            用户类型
	 * @param classId
	 *            课时ID
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> attendClass(long dateMemType, long classId, long userId) throws Exception;

	/**
	 * 根据课时id获取课时信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public CustomClassDTO getCustomClassById(long classId) throws Exception;

	/**
	 * 修改课时信息
	 * 
	 * @param classId
	 *            课时ID
	 * @param classRcmd
	 *            课时介绍
	 * @param planDuration
	 *            计划时长
	 * @param beginTime
	 *            开始时间
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> updateClass(long classId, String classRcmd, Date beginTime, long userId) throws Exception;

	/**
	 * 课时结束确认
	 * 
	 * @param classId
	 *            课时ID
	 * @param realDuration
	 *            实际时长
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classConfirmFinish(long classId, long realDuration, long tutorId) throws Exception;

	/**
	 * 课时小结
	 * 
	 * @param classId
	 *            课时ID
	 * @param classSummary
	 *            课时小结
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> classBriefSummary(long classId, String classSummary, long userId) throws Exception;

	/**
	 * 上传文件
	 * 
	 * @param classId
	 *            课时ID
	 * @param readyFile
	 *            上传文件
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> uploadReadyFile(long classId, String readyFile, long userId) throws Exception;

	/**
	 * 确认上课
	 * 
	 * @param classId
	 *            课时id
	 * @param userId
	 *            用户id
	 * @param method
	 *            1-用户自己确认，2-系统任务确认
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> confirmAttendClass(long classId, long userId, long method) throws Exception;

	/**
	 * 根据定制课课程id获取定制课课时信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findLessonClassList(long lessonId) throws Exception;

	/**
	 * 分页查询授课记录
	 * 
	 * @param page
	 *            分页信息
	 * @param lessonId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @param tutorId
	 *            导师ID
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public Page<CustomClassDTO> findGiveLessonsPaging(Page<CustomClassDTO> page, String lessonName, String userLoginAccount,
			String tutorLoginAccount, Date beginTime, Date endTime) throws Exception;

	/**
	 * 进入上课房间
	 * 
	 * @param classId
	 *            课时id
	 * @param userId
	 *            用户id
	 * @param tutorId
	 *            导师id(无房间号则生成)
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<Map<String, String>> enterClass(long classId, long userId, long tutorId) throws Exception;

	/**
	 * 视频地址上传
	 * 
	 * @param classId
	 * @param movieUrl
	 * @return
	 * @throws Exception
	 */
	public int upMovieUrl(long classId, String movieUrl) throws Exception;
	
	/**
	 * 根据房间ID获取课时信息
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public CustomClassDTO getByRoomId(long roomId) throws Exception;

	/**
	 * 查询课时未确认且超过N小时的课时
	 * 
	 * @param hour
	 *            小时
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassByNHourNotConfirm(long hour) throws Exception;

	/**
	 * 查询离计划上课时长还有N天的课时
	 * 
	 * @param day
	 *            天
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByPlanTimeNDay(long day) throws Exception;

	/**
	 * 查询上节课结束后，N天未再次上课
	 * 
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByNDayNotClass(long day) throws Exception;

	/**
	 * 课程购买后，开始时间已过N天未上课
	 * 
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByPayNDayNotClass(long day) throws Exception;

	/**
	 * 已约课节，离上课还有N小时
	 * 
	 * @param hour
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByDataTimeNHour(long hour) throws Exception;

	/**
	 * 记录待拉取视频信息
	 * 
	 * @param customClassVideo
	 * @return
	 * @throws Exception
	 */
	public long saveCustomClassVideo(long classId, String movieUrl) throws Exception;

	/**
	 * 获取待处理数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassVideo> getPendingCustomClassVideo() throws Exception;

	/**
	 * 更新待拉取视频源信息
	 * 
	 * @param customClassVideo
	 * @return
	 * @throws Exception
	 */
	public int updateCustomClassVideoFail(CustomClassVideo customClassVideo) throws Exception;

	/**
	 * 更新待拉取视频源信息
	 * 
	 * @param customClassVideo
	 * @throws Exception
	 */
	public void updateCustomClassVideoSuccess(CustomClassVideo customClassVideo) throws Exception;

	/**
	 * 通过课时ID获取视频列表
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassVideoDtlDTO> findVideoByClassId(long classId) throws Exception;

	/**
	 * 获取导师剩余时长
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> getSurplusTime(long classId) throws Exception;
}
