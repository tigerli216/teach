/*
 * OperateController.java Created on 2017年10月9日 下午3:18:04
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
package com.wst.operate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.base.helper.UserHelper;
import com.wst.constant.LessonPublicConstant.LessonPublicTypeEnmu;
import com.wst.constant.RedisConstant;
import com.wst.service.act.dto.ActivityDTO;
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.act.service.ActivityExtractService;
import com.wst.service.act.service.ActivityService;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.UserService;
import com.wst.service.operate.dto.AdDTO;
import com.wst.service.operate.dto.CustomLessonDTO;
import com.wst.service.operate.dto.PracticeLessonDTO;
import com.wst.service.operate.service.AdService;
import com.wst.service.operate.service.CustomLessonService;
import com.wst.service.operate.service.PracticeLessonService;

/**
 * 首页
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/student")
public class OperateController {

	@Reference(version = "0.0.1")
	private AdService adService;

	@Reference(version = "0.0.1")
	private CustomLessonService customLessonService;

	@Reference(version = "0.0.1")
	private PracticeLessonService practiceLessonService;

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;

	@Reference(version = "0.0.1")
	private ActivityExtractService activityExtractService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private ActivityService activityService;

	/**
	 * 获取首页信息
	 * 
	 * @param commonParamsDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getIndexInfo")
	public ResultDTO<Map<String, Object>> getIndexInfo(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<Map<String, Object>>();
		Map<String, Object> indexInfoMap = new HashMap<>(5);
		// 访问权限，默认为1.游客
		long userLevel = 1;
		UserDTO user = UserHelper.getUserDTO(dto);
		long userId = 0;

		// 存在，说明不是游客
		if (user != null) {
			String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + user.getUserId();
			// 更新缓存信息
			if (RedisClient.exists(userLimitKey)) {
				user = userService.getUserById(user.getUserId());
				UserHelper.setUser(dto, user);
				RedisClient.del(userLimitKey);
			}
			// userLevel 1.是普通用户 2.是VIP
			userLevel = user.getUserLevel() + 1;
			userId = user.getUserId();
			indexInfoMap.put("userEmail", user.getEmail());
		}

		// 是首页
		boolean isHomePage = true;

		// 广告3条
		Page<AdDTO> adParam = new Page<>();
		adParam.setPage(1);
		adParam.setPageSize(3);
		List<Map<String, Object>> adList = adService.findAds(adParam, isHomePage);
		indexInfoMap.put("ad", adList);

		// 网上微课4条
		Page<PublicDTO> publicParam = new Page<>();
		publicParam.setPage(1);
		publicParam.setPageSize(4);
		PublicDTO publicdto = new PublicDTO();
		// 是否首页 true 是, false 否
		publicdto.setHomePage(isHomePage);
		// 上架状态 1.已上架
		publicdto.setShelfStatus(1);
		List<Map<String, Object>> publictList = lessonPublicService.findPublicLessons(publicParam, publicdto, userId,
				userLevel);
		indexInfoMap.put("public", publictList);

		// 运营定制课程1条
		Page<CustomLessonDTO> customLessonParam = new Page<>();
		customLessonParam.setPage(1);
		customLessonParam.setPageSize(1);
		List<Map<String, Object>> customLessonList = customLessonService.findCustomLessons(customLessonParam,
				isHomePage);
		indexInfoMap.put("customLesson", customLessonList);

		// 运营实习职位1条
		Page<PracticeLessonDTO> practiceLessonParam = new Page<>();
		practiceLessonParam.setPage(1);
		practiceLessonParam.setPageSize(1);

		PracticeLessonDTO practiceLessonDTO = new PracticeLessonDTO();
		// 状态 1.有效
		practiceLessonDTO.setStatus(1);
		// 是否是首页, true 是; false 否
		practiceLessonDTO.setHomePage(isHomePage);
		List<Map<String, Object>> practiceLessonList = practiceLessonService.findPracticeLessons(practiceLessonParam,
				practiceLessonDTO);
		indexInfoMap.put("practiceLesson", practiceLessonList);

		return resultDTO.set(ResultCodeEnum.SUCCESS, "成功", indexInfoMap);
	}

	/**
	 * 获取线上微课列表
	 * 
	 * @param page
	 *            当前页
	 * @param rows
	 *            每页初始记录数
	 * @param isHomePage
	 *            是否是首页, true 是; false 否
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findLessonPublics")
	public ResultDTO<List<Map<String, Object>>> findLessonPublics(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize,
			String lessonName) throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();

		// 访问权限，默认为1.游客
		long userLevel = 1;
		long userId = 0;
		UserDTO user = UserHelper.getUserDTO(dto);

		if (user != null) {
			String userLimitKey = RedisConstant.USER_LEVEL_UPDATE_ + user.getUserId();
			// 更新缓存信息
			if (RedisClient.exists(userLimitKey)) {
				user = userService.getUserById(user.getUserId());
				UserHelper.setUser(dto, user);
				RedisClient.del(userLimitKey);
			}
			userLevel = user.getUserLevel() + 1;
			userId = user.getUserId();
		}

		// 分页参数
		Page<PublicDTO> param = new Page<>();
		// 当前页,初始值1
		param.setPage(page);
		// 每页初始记录数
		param.setPageSize(pageSize);
		boolean isHomePage = true;

		PublicDTO publicdto = new PublicDTO();
		// 是否首页 true 是, false 否
		publicdto.setHomePage(isHomePage);
		// 上架状态 1.已上架
		publicdto.setShelfStatus(1);
		// 模糊搜索课程名，系列名
		if (StringUtils.isNotEmpty(lessonName)) {
			// 模糊搜索课程名，系列名
			publicdto.setLessonName(lessonName.trim());
		}
		resultList = lessonPublicService.findPublicLessons(param, publicdto, userId, userLevel);

		return result.set(ResultCodeEnum.SUCCESS, "success", resultList);
	}

	/**
	 * 运营定制课程列表
	 * 
	 * @param dto
	 * @param page
	 * @param rows
	 * @param isHomePage
	 *            是否是首页, true 是; false 否
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findCustomLessons")
	public ResultDTO<List<Map<String, Object>>> findCustomLessons(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();

		Page<CustomLessonDTO> param = new Page<>();
		// 当前页,初始值1
		param.setPage(page);
		// 每页初始记录数
		param.setPageSize(pageSize);
		boolean isHomePage = true;

		resultList = customLessonService.findCustomLessons(param, isHomePage);

		return result.set(ResultCodeEnum.SUCCESS, "success", resultList);
	}

	/**
	 * 运营实习职位列表
	 * 
	 * @param dto
	 * @param page
	 * @param rows
	 * @param companyCountry
	 *            公司国家（1-国内；2-国外）
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findPracticeLessons")
	public ResultDTO<List<Map<String, Object>>> findPracticeLessons(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "0") long companyCountry) throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 公司国家不能为空
		if (companyCountry == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Companies can't be empty");
		}

		Page<PracticeLessonDTO> param = new Page<>();
		// 当前页,初始值1
		param.setPage(page);
		// 每页初始记录数
		param.setPageSize(pageSize);
		boolean isHomePage = true;

		PracticeLessonDTO practiceLessonDTO = new PracticeLessonDTO();
		// 状态 1.有效
		practiceLessonDTO.setStatus(1);
		// 是否是首页, true 是; false 否
		practiceLessonDTO.setHomePage(isHomePage);
		// 公司国家
		practiceLessonDTO.setCompanyCountry(companyCountry);

		resultList = practiceLessonService.findPracticeLessons(param, practiceLessonDTO);

		return result.set(ResultCodeEnum.SUCCESS, "success", resultList);
	}

	/**
	 * 获取线上微课详情
	 * 
	 * @param dto
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getlessonPublic")
	public ResultDTO<Map<String, Object>> getlessonPublic(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();
		// 缺少必要的参数
		if (lessonId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}

		// 访问权限，默认为1.游客
		long userLevel = 1;
		long userId = 0;
		UserDTO user = UserHelper.getUserDTO(dto);
		// 存在，说明不是游客
		if (user != null) {
			userLevel = user.getUserLevel() + 1;
			userId = user.getUserId();
		}

		PublicDTO publicDTO = lessonPublicService.getLessonById(lessonId);
		// 课程不合法
		if (publicDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Curriculum is not legal");
		}

		// 课程ID,课程分类,课程名称,课程图片,系列名称,所属行业,课程介绍,讲师ID,讲师介绍，有效期
		String[] keys = new String[] { "lessonId", "lessonType", "lessonName", "seriesName", "industry", "lessonRcmd",
				"tutorId", "tutorRcmd" };
		String[] attrNames = new String[] { "lessonId", "lessonType", "lessonName", "seriesName", "industry",
				"lessonRcmdStr", "tutorId", "tutorRcmd" };
		Map<String, Object> publicLessonMap = ConvertToMapHelper.convertToMap(publicDTO, keys, attrNames);
		// 课程原价(元)
		publicLessonMap.put("origPrice", AmountUtils.changeF2Y(publicDTO.getOrigPrice()));
		// 课程折扣价(元)
		publicLessonMap.put("discountPrice", AmountUtils.changeF2Y(publicDTO.getDiscountPrice()));
		// 课程图片
		publicLessonMap.put("lessonPic", publicDTO.getLessonPic());
		// 用户级别，1-游客，2-会员，3-VIP
		publicLessonMap.put("userLevel", userLevel);

		List<ActivityExtractDTO> activityExtractList = activityExtractService.findBylessonIduserId(lessonId, userId);

		// 获取课程tag
		Map<String, Object> lessonTagMap = lessonPublicService.getLessonTag(userId, userLevel, publicDTO.getVisitAuth(),
				lessonId, activityExtractList);
		// 1、已购买；2-VIP专享；3-会员专享；4-游客专享；5.限时免费；6、活动专享（剩余几小时）
		Object lessonTag = lessonTagMap.get("lessonTag");
		publicLessonMap.put("lessonTag", lessonTagMap.get("lessonTag"));
		// 活动时间
		publicLessonMap.put("timeStr", lessonTagMap.get("timeStr"));
		// 课程课时
		List<Map<String, Object>> classList = new ArrayList<>();
		List<PublicClassDTO> publicClassList = publicDTO.getAttrList();
		// 已上完
		long lessonStatus = 2;
		if (publicClassList != null && publicClassList.size() > 0) {
			for (PublicClassDTO publicClassDTO : publicClassList) {
				Map<String, Object> publicClassMap = new HashMap<>(8);
				// 课时ID
				publicClassMap.put("classId", publicClassDTO.getClassId());
				// 课时名称
				publicClassMap.put("className", publicClassDTO.getClassName());
				// 课时介绍
				publicClassMap.put("classRcmd", publicClassDTO.getClassRcmd());

				// 是直播课
				if (publicDTO.getLessonType() == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type) {
					publicClassMap.put("coursewarePic", publicClassDTO.getCoursewarePic());
					publicClassMap.put("classStatus", publicClassDTO.getClassStatus());
					// 未上完
					if (publicClassDTO.getClassStatus() < 3) {
						lessonStatus = 1;
					}
					// 直播课距离开始时间
					if (publicClassDTO.getClassStatus() == 1) {
						publicClassMap.put("distanceTime",
								lessonPublicService.getTimeStr(publicClassDTO.getBeginTime()));
					}
					publicClassMap.put("groupId", publicClassDTO.getGroupId());
					publicClassMap.put("roomId", publicClassDTO.getRoomId());
					publicClassMap.put("beginTime", publicClassDTO.getBeginTime());
					// 课时时长
					publicLessonMap.put("duration", publicClassDTO.getDuration());
				} else if (publicDTO.getLessonType() == LessonPublicTypeEnmu.VIDEO_PLAY_CLASS.type) {
					publicClassMap.put("movieCover", publicClassDTO.getMovieCover());
					publicClassMap.put("movieUrl", publicClassDTO.getMovieUrl());
				}

				// 课程tag
				Map<String, Object> classTagMap = lessonPublicService.getClassTag(userId, lessonId,
						publicClassDTO.getClassId(), publicClassDTO.getIsFree(), userLevel,
						Integer.valueOf(lessonTagMap.get("lessonTag").toString()), activityExtractList);
				// 1-不可看，2-免费，3-活动， 4-可看
				publicClassMap.put("classTag", classTagMap.get("classTag"));
				// 活动时间
				publicClassMap.put("timeStr", classTagMap.get("timeStr"));

				classList.add(publicClassMap);
			}
		}
		if (publicDTO.getLessonType() == LessonPublicTypeEnmu.LIVE_PLAY_CLASS.type) {
			// 课程状态
			publicLessonMap.put("lessonStatus", lessonStatus);
		}

		// 网课课时集合
		publicLessonMap.put("lessonClassList", classList);

		// 是否有输入提取码
		boolean isAct = false;
		boolean isUsed = false;
		if ((long) lessonTag != 2 && (long) lessonTag != 6) {
			publicLessonMap.put("isAct", isAct);
			return result.set(ResultCodeEnum.SUCCESS, "success", publicLessonMap);
		}

		List<ActivityExtractDTO> extractList = activityExtractService.findByLessonId(lessonId);
		for (ActivityExtractDTO activityExtractDTO : extractList) {
			if (activityExtractDTO.getUserId() == 0) {
				isAct = true;
				ActivityDTO act = activityService.getActById(activityExtractDTO.getActId());
				if (act.getBeginTime().after(new Date())) {
					isAct = false;
				}
			}
			if (activityExtractDTO.getUserId() == userId && userId > 0) {
				isUsed = true;
			}
		}
		if (isUsed) {
			isAct = false;
		}

		// 只有VIP专享课程能使用提取码
		if (publicDTO.getVisitAuth() != 3) {
			isAct = false;
		}

		// VIP或者游客都不显示
		if (userLevel == 3 || userLevel == 1) {
			isAct = false;
		}
		publicLessonMap.put("isAct", isAct);

		return result.set(ResultCodeEnum.SUCCESS, "success", publicLessonMap);
	}

	/**
	 * 获取运营定制课程详情
	 * 
	 * @param dto
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCustomLesson")
	public ResultDTO<Map<String, Object>> getCustomLesson(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();
		// 缺少必要的参数
		if (lessonId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}
		CustomLessonDTO customLessonDTO = customLessonService.getCustomLessonById(lessonId);
		// 课程不合法
		if (customLessonDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Curriculum is not legal");
		}
		// 课程ID,课程名称,课程周期,课时数,课程简介,课程说明,目标客户,课程图片
		String[] keys = new String[] { "lessonId", "lessonName", "lessonCycle", "classNum", "lessonAbstract",
				"lessonRcmd", "targetMem", "lessonPic" };
		String[] attrNames = new String[] { "lessonId", "lessonName", "lessonCycle", "classNum", "lessonAbstract",
				"lessonRcmdStr", "targetMem", "lessonPic" };

		Map<String, Object> customLessonMap = ConvertToMapHelper.convertToMap(customLessonDTO, keys, attrNames);

		return result.set(ResultCodeEnum.SUCCESS, "success", customLessonMap);
	}

	/**
	 * 获取运营实习职位详情
	 * 
	 * @param dto
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPracticeLesson")
	public ResultDTO<Map<String, Object>> getPracticeLesson(CommonParamsDTO dto,
			@RequestParam(defaultValue = "0") long lessonId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();
		// 缺少必要的参数
		if (lessonId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}

		PracticeLessonDTO practiceLessonDTO = practiceLessonService.getPracticeLessonById(lessonId);
		// 课程不合法
		if (practiceLessonDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Curriculum is not legal");
		}

		// 课程ID,职位名称,课程简介,公司介绍,职位介绍,行业介绍,公司国家,公司位置,职位图片,实习周期
		String[] keys = new String[] { "lessonId", "practiceName", "lessonAbstract", "companyRcmd", "practiceRcmd",
				"industryRcmd", "companyCountry", "companyLocation", "jobPic", "practiceDuration" };
		String[] attrNames = new String[] { "lessonId", "practiceName", "lessonAbstract", "companyRcmdStr",
				"practiceRcmdStr", "industryRcmd", "companyCountry", "companyLocation", "jobPic", "practiceDuration" };

		Map<String, Object> practiceLessonMap = ConvertToMapHelper.convertToMap(practiceLessonDTO, keys, attrNames);

		return result.set(ResultCodeEnum.SUCCESS, "success", practiceLessonMap);
	}

	/**
	 * 获取VIP定制推荐课程
	 * 
	 * @param commonParamsDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getRecommend")
	public ResultDTO<Map<String, Object>> getRecommend(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<Map<String, Object>>();

		Map<String, Object> indexInfoMap = new HashMap<>(2);
		// 是首页
		boolean isHomePage = true;

		// 运营定制课程1条
		Page<CustomLessonDTO> customLessonParam = new Page<>();
		customLessonParam.setPage(1);
		customLessonParam.setPageSize(1);
		List<Map<String, Object>> customLessonList = customLessonService.findCustomLessons(customLessonParam,
				isHomePage);
		indexInfoMap.put("customLesson", customLessonList);

		// 运营实习职位1条
		Page<PracticeLessonDTO> practiceLessonParam = new Page<>();
		practiceLessonParam.setPage(1);
		practiceLessonParam.setPageSize(1);

		PracticeLessonDTO practiceLessonDTO = new PracticeLessonDTO();
		// 状态 1.有效
		practiceLessonDTO.setStatus(1);
		// 是否是首页, true 是; false 否
		practiceLessonDTO.setHomePage(isHomePage);

		List<Map<String, Object>> practiceLessonList = practiceLessonService.findPracticeLessons(practiceLessonParam,
				practiceLessonDTO);
		indexInfoMap.put("practiceLesson", practiceLessonList);

		return resultDTO.set(ResultCodeEnum.SUCCESS, "成功", indexInfoMap);
	}

	/**
	 * 获取app端相关静态页面地址
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findStaticPage")
	public ResultDTO<Map<String, Object>> findStaticPage(HttpServletRequest request, CommonParamsDTO dto)
			throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>();
		Map<String, Object> resultMap = new HashMap<>();
		String hoSt = "https://" + request.getServerName();
		String contextPath = request.getContextPath();
		String baseUrl = hoSt + "/" + contextPath;
		resultMap.put("about", baseUrl + "/About.html");
		resultMap.put("qA", baseUrl + "/Q&A.html");
		resultMap.put("terms", baseUrl + "/Terms.html");
		resultMap.put("userTerms", baseUrl + "/userTerms.html");

		return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}
}
