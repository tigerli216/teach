/*
 * LessonPublicService.java Created on 2017年9月27日 下午3:04:16
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
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;

/**
 * 网课接口 service
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public interface LessonPublicService {
	/**
	 * 
	 * 分页查询网课信息
	 * 
	 * @param page
	 * @param publicDTO
	 * @return
	 * @throws Exception
	 */
	public Page<PublicDTO> findPaging(Page<PublicDTO> page, PublicDTO publicDTO) throws Exception;

	/**
	 * 根据课程id获取课程信息
	 * 
	 * @param lessonId
	 *            课程id
	 * @return PublicDTO 默认返回产品信息的对象（包含产品各个参数）
	 * @throws Exception
	 */
	public PublicDTO getLessonById(long lessonId) throws Exception;

	/**
	 * 添加课程
	 * 
	 * @param publicDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> addLesson(PublicDTO publicDTO) throws Exception;

	/**
	 * 修改课程信息
	 * 
	 * @param publicDTO
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> updateLesson(PublicDTO publicDTO) throws Exception;

	/**
	 * 批量更新课程状态
	 * 
	 * @param lessonIds
	 * @param shelfStatus
	 * @throws Exception
	 */
	public void upOrDownLesson(String lessonIds, long shelfStatus) throws Exception;

	/**
	 * 获取网课列表
	 * 
	 * @param param
	 * @param publicdto
	 * @param userId
	 * @param userLevel
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findPublicLessons(Page<PublicDTO> param, PublicDTO publicdto, long userId,
			long userLevel) throws Exception;

	/**
	 * 获取课程tag
	 * 
	 * @param userId
	 *            用户ID
	 * @param userLevel
	 *            用户级别，1-游客，2-会员，3-VIP
	 * @param visitAuth
	 *            访问权限，1-游客，2-会员，3-VIP
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getLessonTag(long userId, long userLevel, long visitAuth, long lessonId,
			List<ActivityExtractDTO> extractList) throws Exception;

	/**
	 * 获取课时tag
	 * 
	 * @param userId
	 *            用户ID
	 * @param lessonId
	 *            课程ID
	 * @param classId
	 *            课时ID
	 * @param isFree
	 *            是否免费，1-否，2-是
	 * @param userLevel
	 *            用户级别，1-游客，2-会员，3-VIP
	 * @param lessonTag
	 *            课程标签 1、已购买；2-VIP专享；3-会员专享；4-游客专享；5.限时免费；6、活动专享（剩余几小时）
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getClassTag(long userId, long lessonId, long classId, long isFree, long userLevel,
			int lessonTag, List<ActivityExtractDTO> extractList) throws Exception;

	/**
	 * 根据课时id获取课时信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO findPublicClassByClassId(long classId) throws Exception;

	/**
	 * 更新课程说明(富文本)
	 * 
	 * @param lessonId
	 * @param lessonRcmd
	 * @return
	 * @throws Exception
	 */
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception;

	/**
	 * 修改录播课的视频地址和封面地址
	 * 
	 * @param moveUrl
	 *            视频地址
	 * @param coverUrl
	 *            封面地址
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> upMoveUrlById(String moveUrl, String coverUrl, long classId) throws Exception;

	/**
	 * 我的网课列表
	 * 
	 * @param param
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<List<Map<String, Object>>> findMyPublic(Page<PublicClassDTO> param, long tutorId) throws Exception;

	/**
	 * 通过课时ID获取课时信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO getById(long classId) throws Exception;

	/**
	 * 获取学生已购买网课列表
	 * 
	 * @param page
	 * @param publicDTO
	 * @return
	 * @throws Exception
	 */
	public Page<PublicDTO> findUserPublicPaging(Page<PublicDTO> page, long userId, String lessonName, long lessonType)
			throws Exception;

	/**
	 * 获取距离时间字符串
	 * 
	 * @param beginTime
	 * @return
	 * @throws Exception
	 */
	public String getTimeStr(Date beginTime) throws Exception;

	/**
	 * 更新网课课程图片
	 * 
	 * @param lessonId
	 * @param lessonPic
	 * @return
	 * @throws Exception
	 */
	public int updateTutorRcmd(long lessonId, String tutorRcmd) throws Exception;
}
