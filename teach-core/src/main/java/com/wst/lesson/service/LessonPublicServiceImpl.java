/*
 * LessonPublicServiceImpl.java Created on 2017年9月27日 下午3:14:35
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

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.act.dao.ActivityExtractDao;
import com.wst.constant.LessonPublicConstant.LessonPublicTypeEnmu;
import com.wst.constant.LessonPublicConstant.PublicClassStatusEnum;
import com.wst.lesson.dao.PublicClassDao;
import com.wst.lesson.dao.PublicDao;
import com.wst.mem.dao.TutorDao;
import com.wst.order.dao.OrderDao;
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 网课接口 service 实现类
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class LessonPublicServiceImpl implements LessonPublicService {

	@Resource
	private PublicDao publicDao;

	@Resource
	private PublicClassDao publicClassDao;

	@Resource
	private ActivityExtractDao activityExtractDao;

	@Resource
	private OrderDao orderDao;

	@Resource
	private TutorDao tutorDao;

	@Resource
	private TxVideoService txVideoService;

	@Override
	public Page<PublicDTO> findPaging(Page<PublicDTO> page, PublicDTO publicDTO) throws Exception {
		Page<PublicDTO> publicDTOPage = publicDao.findPaging(page, publicDTO);
		List<PublicDTO> publicDTOlist = publicDTOPage.getList();
		if (publicDTOlist == null || publicDTOlist.size() == 0) {
			return publicDTOPage;
		}
		for (PublicDTO publicRcmd : publicDTOlist) {
			if (publicRcmd.getLessonRcmd() != null) {
				publicRcmd.setLessonRcmdStr(new String(
						publicRcmd.getLessonRcmd().getBytes((long) 1, (int) publicRcmd.getLessonRcmd().length())));
				publicRcmd.setLessonRcmd(null);
			}
		}

		return publicDTOPage;
	}

	@Override
	public ResultDTO<String> addLesson(PublicDTO publicDTO) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();

		/**
		 * 新增网课基本信息
		 */
		Long lessonId = publicDao.insertLesson(publicDTO);

		/**
		 * 新增课时信息,为空表示只新增网课基本信息
		 */
		List<PublicClassDTO> publicClassList = publicDTO.getAttrList();
		if (publicClassList == null || publicClassList.size() == 0) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
		}

		Calendar cal = Calendar.getInstance();
		for (PublicClassDTO publicClass : publicClassList) {
			if (publicClass.getIdentifying() == 1L) {
				publicClass.setLessonId(lessonId);
				publicClass.setClassStatus(PublicClassStatusEnum.NOT_START.status);

				// 设置课程结束时间
				if (publicDTO.getLessonType() == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type
						&& publicClass.getClassStatus() <= 1 && publicClass.getBeginTime() != null) {
					cal.setTime(publicClass.getBeginTime());
					cal.add(Calendar.MINUTE, (int) publicClass.getDuration());
					publicClass.setEndTime(cal.getTime());
				}

				publicClassDao.insertLessonClass(publicClass);
			}
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功", String.valueOf(lessonId));
	}

	@Override
	public ResultDTO<String> updateLesson(PublicDTO publicDTO) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();

		/**
		 * 修改网课基本信息
		 */
		publicDao.updateLesson(publicDTO);

		/**
		 * 修改课时信息,为空表示只修改网课基本信息
		 */
		List<PublicClassDTO> publicClassList = publicDTO.getAttrList();
		if (publicClassList == null || publicClassList.size() == 0) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
		}

		StringBuilder opClassIds = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		for (PublicClassDTO publicClass : publicClassList) {
			if (publicClass.getIdentifying() == 1L) {
				publicClass.setLessonId(publicDTO.getLessonId());

				// 设置课程结束时间
				if (publicDTO.getLessonType() == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type
						&& publicClass.getClassStatus() <= 1 && publicClass.getBeginTime() != null) {
					cal.setTime(publicClass.getBeginTime());
					cal.add(Calendar.MINUTE, (int) publicClass.getDuration());
					publicClass.setEndTime(cal.getTime());
				}

				if (publicClass.getClassId() > 0) {
					publicClassDao.updateLessonClassById(publicClass);

					opClassIds.append(publicClass.getClassId()).append(",");
				} else if (publicClass.getClassId() <= 0) {
					publicClass.setClassStatus(PublicClassStatusEnum.NOT_START.status);
					long classId = publicClassDao.insertLessonClass(publicClass);
					opClassIds.append(classId).append(",");
				}
			}
		}

		/**
		 * 删除已取消课时
		 */
		if (opClassIds.length() > 0) {
			String classIds = opClassIds.substring(0, opClassIds.length() - 1);
			publicClassDao.deleteLessonClassOfId(publicDTO.getLessonId(), classIds);
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
	}

	@Override
	public PublicDTO getLessonById(long lessonId) throws Exception {
		PublicDTO publicDTO = publicDao.getLessonById(lessonId);
		if (publicDTO == null) {
			return publicDTO;
		}

		if (publicDTO.getLessonType() == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type) {
			publicDTO.setTutorName(tutorDao.getTutorById(publicDTO.getTutorId()).getRealName());
		}

		publicDTO.setAttrList(publicClassDao.findLessonClassList(lessonId));

		if (publicDTO.getLessonRcmd() != null) {
			publicDTO.setLessonRcmdStr(
					new String(publicDTO.getLessonRcmd().getBytes((long) 1, (int) publicDTO.getLessonRcmd().length())));
			publicDTO.setLessonRcmd(null);
		}

		return publicDTO;
	}

	@Override
	public void upOrDownLesson(String lessonIds, long ShelfStatus) throws Exception {
		String[] leIds = lessonIds.split(",");
		for (String leId : leIds) {
			publicDao.upOrDownoLesson(Long.parseLong(leId), ShelfStatus);
		}
	}

	@Override
	public List<Map<String, Object>> findPublicLessons(Page<PublicDTO> param, PublicDTO publicdto, long userId,
			long userLevel) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<PublicDTO> publicList = publicDao.findPaging(param, publicdto).getList();
		if (publicList != null && publicList.size() > 0) {
			for (PublicDTO publicDTO : publicList) {
				Map<String, Object> publicMap = new HashMap<>();
				publicMap.put("lessonId", publicDTO.getLessonId());
				publicMap.put("lessonPic", publicDTO.getLessonPic());
				publicMap.put("lessonName", publicDTO.getLessonName());
				publicMap.put("seriesName", publicDTO.getSeriesName());
				// publicMap.put("validTime", publicDTO.getValidTime());
				publicMap.put("industry", publicDTO.getIndustry());
				// 课程原价（元）
				publicMap.put("origPrice", publicDTO.getOrigPriceStr());
				// 课程折扣价（元）
				publicMap.put("discountPrice", publicDTO.getDiscountPriceStr());
				publicMap.put("lessonType", publicDTO.getLessonType());
				// 用户级别 1.游客 2.普通用户 3.VIP
				publicMap.put("userLevel", userLevel);
				// 课程访问权限
				publicMap.put("visitAuth", publicDTO.getVisitAuth());

				// 直播课才有开始时间
				if (publicDTO.getLessonType() == 2) {
					PublicClassDTO publicClass = publicClassDao.getLiveClassByLessonId(publicDTO.getLessonId());
					Date beginTime = publicClass.getBeginTime();
					publicMap.put("beginTime", beginTime);
					// 课时状态 1.未开始 2.正在进行中 3.已结束
					publicMap.put("classStatus", publicClass.getClassStatus());
					// 当前距离课程开始时间 未开始才有
					if (publicClass.getClassStatus() == 1) {
						publicMap.put("distanceTime", getTimeStr(publicClass.getBeginTime()));
					}
				}
				// 通过用户ID和课程ID获取在有效期内的提取码
				List<ActivityExtractDTO> activityExtractList = activityExtractDao
						.findBylessonIduserId(publicDTO.getLessonId(), userId);
				/** 课程tag 1-已购买，2-VIP，3-会员专享，4-游客专享，5-限时免费，6-活动专享 */
				Map<String, Object> lessonTagMap = getLessonTag(userId, userLevel, publicDTO.getVisitAuth(),
						publicDTO.getLessonId(), activityExtractList);

				// 1、已购买；2-VIP专享；3-会员专享；4-游客专享；5.限时免费；6、活动专享（剩余几小时）
				publicMap.put("lessonTag", lessonTagMap.get("lessonTag"));
				publicMap.put("timeStr", lessonTagMap.get("timeStr"));
				resultList.add(publicMap);
			}
		}
		return resultList;
	}

	@Override
	public Map<String, Object> getLessonTag(long userId, long userLevel, long visitAuth, long lessonId,
			List<ActivityExtractDTO> extractList) throws Exception {
		/** 课程tag 1-已购买，2-VIP，3-会员专享，4-游客专享，5-限时免费，6-活动专享 */
		long lessonTag = 0;
		// 用户未登录
		if (userId == 0) {
			if (visitAuth == 1) { // 游客专享
				lessonTag = 4;
			} else if (visitAuth == 2) { // 会员专享
				lessonTag = 3;
			} else if (visitAuth == 3) { // VIP专享
				lessonTag = 2;
			}
		} else {
			// 如果用户登录，校验课程是否购买
			OrderDTO order = orderDao.getByLessonIdUserId(userId, lessonId, 1);
			if (order != null && order.getPayStatus() == 2 && order.getValidTime().after(new Date())) { // 已购买
				lessonTag = 1;
			} else {
				// 根据用户级别，变更课程级别
				if (userLevel == 2) {
					if (visitAuth == 1 || visitAuth == 2) { // 会员专享
						lessonTag = 3;
					} else if (visitAuth == 3) { // VIP专享
						lessonTag = 2;
					}
				} else if (userLevel == 3) {
					lessonTag = 2;
				}
			}
		}

		// 限时时间
		String timeStr = "";
		// 是否免费
		boolean isFree = true;
		List<PublicClassDTO> publicClassList = publicClassDao.findLessonClassList(lessonId);
		if (publicClassList != null && publicClassList.size() > 0) {
			for (PublicClassDTO publicClassDTO : publicClassList) {
				if (publicClassDTO.getIsFree() == 1) {
					isFree = false;
				}
			}
		}

		if (isFree) {
			// 课程免费
			lessonTag = 5;
		} else if (userId > 0) {
			// 校验课程是否活动专享
			String classIds = "";
			if (extractList != null && extractList.size() > 0) {
				Date validTime = null;
				for (ActivityExtractDTO activityExtractDTO : extractList) {
					classIds += activityExtractDTO.getClassIds() + "|";
					if (validTime != null && validTime.before(activityExtractDTO.getValidTime())) {
						validTime = activityExtractDTO.getValidTime();
					} else {
						validTime = activityExtractDTO.getValidTime();
					}
				}
				classIds = "|" + classIds;
				boolean isActExt = true;
				for (PublicClassDTO publicClassDTO : publicClassList) {
					if (!classIds.contains("|" + publicClassDTO.getClassId() + "|")) {
						isActExt = false;
					}
				}
				if (isActExt) {
					lessonTag = 6;
					// 获取时间
					Date now = new Date();
					long time = validTime.getTime() - now.getTime();
					long day = time / (24 * 60 * 60 * 1000);
					long hour = (time / (60 * 60 * 1000) - day * 24);
					if (day == 1) {
						timeStr = String.valueOf(day) + "DAY";
					} else if (day > 1) {
						timeStr = String.valueOf(day) + "DAYS";
					} else {
						timeStr = String.valueOf(hour) + "HOURS";
						if (hour == 1) {
							timeStr = String.valueOf(hour) + "HOUR";
						} else if (hour < 1) {
							timeStr = "小于一小时";
						}
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("lessonTag", lessonTag);
		resultMap.put("timeStr", timeStr);
		return resultMap;
	}

	@Override
	public Map<String, Object> getClassTag(long userId, long lessonId, long classId, long isFree, long userLevel,
			int lessonTag, List<ActivityExtractDTO> extractList) throws Exception {
		/** 课时tag 1-不可看，2-限时免费，3-活动专享，4-可看 */
		long classTag = isFree;
		String timeStr = "";

		// 用户级别，1-游客，2-会员，3-VIP
		// * @param lessonTag
		// * 课程标签 1、已购买；2-VIP专享；3-会员专享；4-游客专享；5.限时免费；6、活动专享（剩余几小时）

		if (userLevel == 1) { // 游客
			if (lessonTag == 4) {
				classTag = 4;
			} else {
				classTag = 1;
			}
		} else if (userLevel == 2) { // 会员
			if (lessonTag != 2) {
				classTag = 4;
			} else {
				classTag = 1;
			}
		} else if (userLevel == 3) { // VIP
			classTag = 4;
		}

		if (isFree == 2) {
			classTag = 2;
		}

		// 课时非免费，则查询是否有对应的提取码活动
		if (userId > 0 && isFree == 1) {
			// 校验课程是否活动专享
			if (extractList != null && extractList.size() > 0) {
				Date validTime = null;
				for (ActivityExtractDTO activityExtractDTO : extractList) {
					String classIds = "|" + activityExtractDTO.getClassIds() + "|";
					if (classIds.contains("|" + classId + "|")) {
						if (validTime != null && validTime.before(activityExtractDTO.getValidTime())) {
							validTime = activityExtractDTO.getValidTime();
						} else {
							validTime = activityExtractDTO.getValidTime();
						}
					}
				}
				if (validTime != null) {
					classTag = 3;
					// 获取时间
					Date now = new Date();
					long time = validTime.getTime() - now.getTime();
					long day = time / (24 * 60 * 60 * 1000);
					long hour = (time / (60 * 60 * 1000) - day * 24);
					if (day == 1) {
						timeStr = String.valueOf(day) + "DAY";
					} else if (day > 1) {
						timeStr = String.valueOf(day) + "DAYS";
					} else {
						timeStr = String.valueOf(hour) + "HOURS";
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("classTag", classTag);
		resultMap.put("timeStr", timeStr);
		return resultMap;
	}

	@Override
	public PublicClassDTO findPublicClassByClassId(long classId) throws Exception {
		return publicClassDao.findByClassId(classId);
	}

	@Override
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		return publicDao.updateLessonRcmd(lessonId, lessonRcmdStr);
	}

	@Override
	public ResultDTO<String> upMoveUrlById(String moveUrl, String coverUrl, long classId) throws Exception {
		publicClassDao.upMoveUrlById(moveUrl, coverUrl, classId);

		return new ResultDTO<String>().set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public ResultDTO<List<Map<String, Object>>> findMyPublic(Page<PublicClassDTO> param, long tutorId)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<List<Map<String, Object>>>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		Page<PublicClassDTO> publicPage = publicDao.findMyPublic(param, tutorId);
		if (publicPage == null) {
			return result.set(ResultCodeEnum.SUCCESS, "", resultList);
		}

		List<PublicClassDTO> publicClassList = publicPage.getList();
		if (publicClassList == null || publicClassList.size() == 0) {
			return result.set(ResultCodeEnum.SUCCESS, "", resultList);
		}

		for (PublicClassDTO publicClassDTO : publicClassList) {
			Map<String, Object> resultMap = new HashMap<String, Object>(5);
			resultMap.put("lessonName", publicClassDTO.getLessonName());
			resultMap.put("lessonId", publicClassDTO.getLessonId());
			resultMap.put("classId", publicClassDTO.getClassId());
			resultMap.put("beginTime", publicClassDTO.getBeginTime());
			// 2.正在上课 3.已完结 其余是距离上课时间
			resultMap.put("distanceTime", publicClassDTO.getClassStatus());
			if (publicClassDTO.getClassStatus() == 1) {
				resultMap.put("distanceTime", getTimeStr(publicClassDTO.getBeginTime()));
			}
			resultList.add(resultMap);
		}
		return result.set(ResultCodeEnum.SUCCESS, "", resultList);
	}

	/**
	 * 获取距离开始时间
	 * 
	 * @param beginTime
	 * @return
	 * @throws Exception
	 */
	public String getTimeStr(Date beginTime) throws Exception {
		String distanceTimeStr = "0 Minute";
		Date now = new Date();
		long distanceTime = beginTime.getTime() - now.getTime();
		long day = distanceTime / 86400000;
		long hour = (distanceTime / 3600000 - day * 24);
		long min = distanceTime % 86400000 % 3600000 / 60000;

		if (distanceTime <= 0) {
			return distanceTimeStr;
		}

		if (day == 1) {
			distanceTimeStr = String.valueOf(day) + " Day";
		} else if (day > 1) {
			distanceTimeStr = String.valueOf(day) + " Days";
		} else {
			distanceTimeStr = String.valueOf(hour) + " Hours";
			if (hour == 1) {
				distanceTimeStr = String.valueOf(hour) + " Hour";
			} else if (hour < 1) {
				distanceTimeStr = min + " Minutes";
				if (min == 1) {
					distanceTimeStr = "1 Minute";
				}
			}
		}
		return distanceTimeStr;
	}

	@Override
	public PublicClassDTO getById(long classId) throws Exception {
		return publicClassDao.getById(classId);
	}
	/*
	 * 
	 * private Map<String, Object> getClassInfo(long lessonId) throws Exception
	 * { Map<String, Object> resultMap= new HashMap<String, Object>();
	 * List<PublicClassDTO> publicClassList =
	 * publicClassDao.findLessonClassListAsc(lessonId); if(publicClassList ==
	 * null || publicClassList.size() == 0){ return resultMap; } for
	 * (PublicClassDTO publicClassDTO : publicClassList) { if
	 * (publicClassDTO.getClassStatus() != 3) { resultMap.put("classId",
	 * publicClassDTO.getClassId()); resultMap.put("beginTime",
	 * publicClassDTO.getBeginTime()); resultMap.put("classStatus",
	 * publicClassDTO.getClassStatus()); if (publicClassDTO.getClassStatus() ==
	 * 1) { resultMap.put("distanceTime",
	 * getTimeStr(publicClassDTO.getBeginTime())); } return resultMap; } }
	 * PublicClassDTO publicClassDTO =
	 * publicClassList.get(publicClassList.size() - 1); resultMap.put("classId",
	 * publicClassDTO.getClassId()); resultMap.put("beginTime",
	 * publicClassDTO.getBeginTime()); resultMap.put("classStatus",
	 * publicClassDTO.getClassStatus()); return resultMap; }
	 */

	@Override
	public Page<PublicDTO> findUserPublicPaging(Page<PublicDTO> page, long userId, String lessonName, long lessonType)
			throws Exception {
		return publicDao.findUserPublicPaging(page, userId, lessonName, lessonType);
	}

	@Override
	public int updateTutorRcmd(long lessonId, String tutorRcmd) throws Exception {
		return publicDao.updateTutorRcmd(lessonId, tutorRcmd);
	}
}
