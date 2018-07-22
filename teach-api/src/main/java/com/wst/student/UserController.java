/*
 * UserController.java Created on 2017年10月11日 下午7:11:10
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
package com.wst.student;

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
import com.hiwi.common.util.StringUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.base.helper.UserHelper;
import com.wst.constant.MssageConstant.MssageStatusEnmu;
import com.wst.service.mem.dto.MssageDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;
import com.wst.service.mem.service.UserService;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.RegionSchoolService;
import com.wst.service.sys.service.RegionService;

/**
 * 学生管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/student")
public class UserController {

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private MssageService mssageService;

	@Reference(version = "0.0.1")
	private RegionSchoolService regionSchoolService;

	@Reference(version = "0.0.1")
	private RegionService regionService;

	/**
	 * 获取学生个人信息
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserInfo")
	public ResultDTO<Map<String, Object>> getUserInfo(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		UserDTO userDTO = UserHelper.getUserDTO(dto);
		if (userDTO == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "Student's not leagal");
		}
		UserDTO user = userService.getUserById(userDTO.getUserId());

		// 头像地址，姓名，性别手机号码，邮箱地址，年级，我的简历
		String[] keys = new String[] { "portraitImgUrl", "realName", "sex", "bindMobile", "email", "grade", "school",
				"country" };
		String[] attrNames = new String[] { "portraitImgUrl", "realName", "sex", "bindMobile", "email", "grade",
				"school", "country" };
		Map<String, Object> userMap = ConvertToMapHelper.convertToMap(user, keys, attrNames);

		String finishTimeStr = "";
		if (user.getFinishTime() != null) {
			SimpleDateFormat sqf = new SimpleDateFormat("yyyy-MM");
			finishTimeStr = sqf.format(user.getFinishTime());
		}
		// 毕业时间
		userMap.put("finishTimeStr", finishTimeStr);

		return result.set(ResultCodeEnum.SUCCESS, "获取成功", userMap);
	}

	/**
	 * 修改学生个人信息
	 * 
	 * @param dto
	 * @param bindMobile
	 *            手机号
	 * @param finishTimeStr
	 *            毕业时间
	 * @param grade
	 *            年级
	 * @param realName
	 *            姓名
	 * @param regionId
	 *            地区ID
	 * @param schoolId
	 *            学校ID
	 * @param sex
	 *            性别
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/upUserInfo")
	public ResultDTO<String> updateUser(CommonParamsDTO dto, String bindMobile, String finishTimeStr,
			@RequestParam(defaultValue = "0") long grade, String realName,
			@RequestParam(defaultValue = "0") long regionId, @RequestParam(defaultValue = "0") long schoolId,
			@RequestParam(defaultValue = "1") long sex) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		// 参数校验
		if (bindMobile.length() > 13) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Cell phone number is illegal");
		}

		if (realName.length() > 50) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The name is long");
		}

		UserDTO user = UserHelper.getUserDTO(dto);

		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "The student does not exist");
		}
		UserDTO userDTO = new UserDTO();
		// 手机号
		userDTO.setBindMobile(bindMobile);
		// 年级
		userDTO.setGrade(grade);
		// 姓名
		userDTO.setRealName(realName);
		// 性别
		userDTO.setSex(sex);

		// 将时间字符串转换为Date
		if (StringUtils.isNotEmpty(finishTimeStr)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date finishTime = sdf.parse(finishTimeStr);
			// 毕业时间
			userDTO.setFinishTime(finishTime);
		}

		if (regionId > 0) {
			RegionDTO regionDTO = regionService.getRegionById(regionId);
			if (regionDTO == null) {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Area ID is not valid");
			}
			// 地区
			userDTO.setCountry(regionDTO.getRegionName());

			if (schoolId > 0) {
				RegionSchoolDTO regionSchoolDTO = regionSchoolService.getRegionSchoolById(schoolId);
				if (regionSchoolDTO == null) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "School ID not legal");
				}
				// 学校不在该地区中
				if (!regionSchoolDTO.getRegionName().equals(regionDTO.getRegionName())) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The school is not in the area");
				}
				// 学校
				userDTO.setSchool(regionSchoolDTO.getSchoolName());
			}
		}

		userDTO.setUserId(user.getUserId());
		userService.updateUser(userDTO);

		return result.set(ResultCodeEnum.SUCCESS, "modify successfully");
	}

	/**
	 * 获取学生消息
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("message")
	public ResultDTO<List<Map<String, Object>>> getUserMessage(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		Page<MssageDTO> pageParam = new Page<MssageDTO>();
		pageParam.setPage(page);
		pageParam.setPageSize(pageSize);
		MssageDTO mssageQuery = new MssageDTO();
		mssageQuery.setUserId(user.getUserId());
		// 有效消息
		mssageQuery.setMsgStatus(MssageStatusEnmu.VALID.status);
		List<MssageDTO> mssageList = mssageService.pageFindMssage(pageParam, mssageQuery).getList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		if (mssageList != null && mssageList.size() > 0) {
			for (MssageDTO mssage : mssageList) {
				String[] keys = new String[] { "msgType", "msgTitle", "msgContent", "msgSource", "busiType" };
				String[] attrNames = new String[] { "msgType", "msgTitle", "msgContent", "msgSource", "busiType" };
				Map<String, Object> map = ConvertToMapHelper.convertToMap(mssage, keys, attrNames);
				map.put("sendTime", sdf.format(mssage.getSendTime()));
				resultList.add(map);
			}
		}

		return result.set(ResultCodeEnum.SUCCESS, "", resultList);
	}

	/**
	 * 获取学生简历信息
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getUserResume")
	public ResultDTO<Map<String, String>> getUserResume(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, String>> result = new ResultDTO<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>(2);

		UserDTO userDTO = UserHelper.getUserDTO(dto);
		UserDTO user = userService.getUserById(userDTO.getUserId());
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Student ID is not legal");
		}
		// 简历路径
		resultMap.put("resume", user.getResume());

		String resumeName = "";
		if (StringUtils.isNotEmpty(user.getResume())) {
			resumeName = user.getResume().substring(user.getResume().lastIndexOf("/") + 1);
		}
		// 我的简历名称
		resultMap.put("resumeName", resumeName);

		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}
}
