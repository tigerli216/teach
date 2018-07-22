/*
 * TutorController.java Created on 2017年10月11日 下午7:11:22
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.util.file.FileUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.base.helper.UserHelper;
import com.wst.constant.MssageConstant.MssageStatusEnmu;
import com.wst.constant.RedisConstant;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.TutorMssageDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.TutorMssageService;
import com.wst.service.mem.service.TutorService;
import com.wst.service.mem.service.UserService;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.RegionSchoolService;
import com.wst.service.sys.service.RegionService;

/**
 * 导师管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/tutor")
public class TutorController {

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	@Reference(version = "0.0.1")
	private CustomClassService customClassService;

	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private RegionSchoolService regionSchoolService;

	@Reference(version = "0.0.1")
	private RegionService regionService;

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;

	@Reference(version = "0.0.1")
	private TutorMssageService tutorMssageService;

	/**
	 * 获取导师个人信息
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTutorInfo")
	public ResultDTO<Map<String, Object>> getTutorInfo(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		Map<String, Object> tutorMap = new HashMap<>(4);

		TutorDTO tutor = UserHelper.getTutorDTO(dto);

		// 头像地址，姓名，性别，地区，手机号码，邮箱地址，LinkedIn连接，毕业院校，最近一份工作，所在行业，职位，收款方式，收款账号
		String[] keys = new String[] { "portraitImage", "realName", "sex", "country", "bindMobile", "email",
				"linkedinUri", "graduateSchool", "newlyJob", "industry", "position", "receiveType", "receiveAccount" };
		String[] attrNames = new String[] { "portraitImgUrl", "realName", "sex", "country", "bindMobile", "email",
				"linkedinUri", "graduateSchool", "newlyJob", "industryName", "position", "receiveType",
				"receiveAccount" };

		tutorMap = ConvertToMapHelper.convertToMap(tutor, keys, attrNames);

		return result.set(ResultCodeEnum.SUCCESS, "获取成功", tutorMap);
	}

	/**
	 * 修改导师个人信息
	 * 
	 * @param dto
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/upTutorInfo")
	public ResultDTO<String> updateTutor(CommonParamsDTO dto, TutorDTO tutorDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (tutorDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}
		if (StringUtils.isEmpty(tutorDTO.getRealName())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The name cannot be empty");
		}
		if (tutorDTO.getSex() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Sex can't be empty");
		}

		// 手机号码校验
		if (StringUtils.isEmpty(tutorDTO.getBindMobile())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The cell phone number can't be empty");
		}
		if (tutorDTO.getBindMobile().length() > 13) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Cell phone number is illegal");
		}

		if (StringUtils.isEmpty(tutorDTO.getLinkedinUri())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Linkedin links can't be empty");
		}
		if (StringUtils.isEmpty(tutorDTO.getNewlyJob())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "A recent job can't be empty");
		}
		if (StringUtils.isEmpty(tutorDTO.getPosition())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The position school can't be empty");
		}
		if (tutorDTO.getReceiveType() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The collection method can't be empty");
		}
		if (StringUtils.isEmpty(tutorDTO.getReceiveAccount())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The collection account can't be empty");
		}
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The mentor doesn't exist");
		}
		// 收款账号格式判断

		if (tutorDTO.getRegionId() > 0) { // 地区
			RegionDTO regionDTO = regionService.getRegionById(tutorDTO.getRegionId());
			if (regionDTO == null) { // 地区ID不合法
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Area ID is not valid");
			}
			tutorDTO.setCountry(regionDTO.getRegionName()); // 地区

			// 如果没有地区ID，学校ID写了也不会放到数据库里
			if (tutorDTO.getSchoolId() > 0) { // 毕业学校
				RegionSchoolDTO regionSchoolDTO = regionSchoolService.getRegionSchoolById(tutorDTO.getSchoolId());
				if (regionSchoolDTO == null) { // 学校ID不合法
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "School ID not legal");
				}
				if (!regionSchoolDTO.getRegionName().equals(regionDTO.getRegionName())) { // 学校不在该地区中
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The school is not in the area");
				}
				tutorDTO.setGraduateSchool(regionSchoolDTO.getSchoolName()); // 学校
			} else {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The school ID cannot be empty"); // 地区不能为空
			}
		} else {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The locale ID cannot be empty"); // 地区不能为空
		}

		tutorDTO.setTutorId(tutor.getTutorId());
		tutorService.updateTutor(tutorDTO);

		return result.set(ResultCodeEnum.SUCCESS, "modify successfully");
	}

	/**
	 * 获取我的学生列表
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyStudents")
	public ResultDTO<List<Map<String, Object>>> findMyStudents(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();

		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Teacher's not leagal");
		}
		Page<CustomClassDTO> CustomClassParame = new Page<>();
		CustomClassParame.setPage(page);
		CustomClassParame.setPageSize(pageSize);

		// 获取导师ID课时列表（课程ID相同的合并）
		List<Map<String, Object>> resultList = customClassService.findLessons(CustomClassParame, tutor.getTutorId());

		return result.set(ResultCodeEnum.SUCCESS, "success", resultList);
	}

	/**
	 * 获取我的学生详情
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getStudentInfo")
	public ResultDTO<Map<String, Object>> getStudentInfo(CommonParamsDTO dto, long userId) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		if (userId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Lack of necessary parameters");
		}
		UserDTO user = userService.getUserById(userId);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Student's not leagal");
		}
		// 头像地址，姓名，性别，国家，手机号码，邮箱地址，学校，年级，毕业时间，我的简历
		String[] keys = new String[] { "portraitImage", "userName", "sex", "country", "bindMobile", "email", "school",
				"grade", "resume" };
		String[] attrNames = new String[] { "portraitImgUrl", "realName", "sex", "country", "bindMobile", "email",
				"school", "grade", "resume" };
		Map<String, Object> studentMap = ConvertToMapHelper.convertToMap(user, keys, attrNames);

		String finishTimeStr = "";
		if (user.getFinishTime() != null) {
			SimpleDateFormat sqf = new SimpleDateFormat("yyyy-MM");
			finishTimeStr = sqf.format(user.getFinishTime());
		}
		// 毕业时间
		studentMap.put("finishTime", finishTimeStr);

		String resumeName = "";
		if (StringUtils.isNotEmpty(user.getResume())) {
			resumeName = user.getResume().substring(user.getResume().lastIndexOf("/") + 1);
		}
		// 我的简历名称
		studentMap.put("resumeName", resumeName);

		return result.set(ResultCodeEnum.SUCCESS, "获取成功", studentMap);
	}

	/**
	 * 导师我的网课列表
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param lessonName
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyPublic")
	public ResultDTO<List<Map<String, Object>>> findMyPublic(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		Page<PublicClassDTO> param = new Page<>();
		param.setPage(page);
		param.setPageSize(pageSize);
		return lessonPublicService.findMyPublic(param, tutor.getTutorId());
	}

	/**
	 * 获取导师消息
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "message")
	public ResultDTO<List<Map<String, Object>>> getTutorMessage(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		Page<TutorMssageDTO> pageParam = new Page<TutorMssageDTO>();
		pageParam.setPage(page);
		pageParam.setPageSize(pageSize);
		TutorMssageDTO tutorMssageQuery = new TutorMssageDTO();
		tutorMssageQuery.setTutorId(tutor.getTutorId());
		// 有效消息
		tutorMssageQuery.setMsgStatus(MssageStatusEnmu.VALID.status);
		List<TutorMssageDTO> tutorMssageList = tutorMssageService.findPaging(pageParam, tutorMssageQuery).getList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		if (tutorMssageList != null && tutorMssageList.size() > 0) {
			for (TutorMssageDTO tutorMssage : tutorMssageList) {
				String[] keys = new String[] { "msgType", "msgTitle", "msgContent", "msgSource", "busiType" };
				String[] attrNames = new String[] { "msgType", "msgTitle", "msgContent", "msgSource", "busiType" };
				Map<String, Object> map = ConvertToMapHelper.convertToMap(tutorMssage, keys, attrNames);
				map.put("sendTime", sdf.format(tutorMssage.getSendTime()));
				resultList.add(map);
			}
		}
		return result.set(ResultCodeEnum.SUCCESS, "", resultList);
	}

	/**
	 * 上传导师图片
	 * 
	 * @param request
	 * @param dto
	 * @param imgFile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("uplodePortrait")
	public ResultDTO<String> uplodeTutorImg(HttpServletRequest request, CommonParamsDTO dto) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
		if (tutorDTO == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
		MultipartFile imgFile = standardRequest.getFile("imgFile");
		if (imgFile == null) {
			// 请选择图片
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Please select pictures");
		}
		ImageInfoDTO imageInfo = FileUtils.uploadImage(imgFile);
		if (imageInfo == null) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Server exception");
		}
		tutorService.upTutorPortrait(tutorDTO.getTutorId(), imageInfo.getPicUrl());

		// 修改缓存信息
		TutorDTO tutor = UserHelper.getTutorDTO(dto);
		tutor.setPortraitImgUrl(imageInfo.getPicUrl());
		UserHelper.setTutor(dto, tutor);

		return result.set(ResultCodeEnum.SUCCESS, "", imageInfo.getPicUrl());
	}
	
	
	/**
	 * 群组禁言
	 * @param dto
	 * @param silenceStatus  禁言状态（1-正常；2-取消禁言）
	 * @param groupId  群组ID
	 * @return
	 * @throws Exception
	 */
    @ResponseBody
    @RequestMapping("silenceAll")
    public ResultDTO<String> silenceAll(CommonParamsDTO dto, int silenceStatus, String groupId)
            throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        
        String redisKey = RedisConstant.LIVE_GROUP_SILENCE_ + groupId;
        RedisClient.set(redisKey, silenceStatus);
        RedisClient.expire(redisKey, RedisConstant.LIVE_GROUP_SILENCE_EXPIRE);
        
        return result.set(ResultCodeEnum.SUCCESS, "");
    }
}
