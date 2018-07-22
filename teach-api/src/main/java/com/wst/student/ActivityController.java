/*
 * ActivityController.java Created on 2017年11月2日 下午7:54:59
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.Globals;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.service.act.dto.ActivityDTO;
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.act.service.ActivityExtractService;
import com.wst.service.act.service.ActivityService;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;

/**
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/act/")
public class ActivityController {

	@Reference(version = "0.0.1")
	private ActivityService activityService;
	@Reference(version = "0.0.1")
	private BusiConfigService busiConfigService;
	@Reference(version = "0.0.1")
	private ActivityExtractService activityExtractService;

	/**
	 * 验证提取码活动是否有效
	 * 
	 * @param dto
	 * @param extractAct
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "extractAct")
	public ResultDTO<Map<String, String>> extractAct(CommonParamsDTO dto) throws Exception {
		ResultDTO<Map<String, String>> result = new ResultDTO<>();
		Map<String, String> resultMap = new HashMap<>();

		// 活动类型 1-提取码活动
		ActivityDTO act = activityService.getActByActType(1);

		// 活动不存在
		if (act == null) {
			return result.set(ResultCodeEnum.SUCCESS, "Activity does not exist");
		}

		// 业务类型 2.活动咨询配置
		BusiConfigDTO busiConfigDTO = busiConfigService.getConfigByBusiType(2);
		if (busiConfigDTO != null) {
			// 配置参数不存在
			if (StringUtils.isEmpty(busiConfigDTO.getParams())) {
				return result.set(ResultCodeEnum.SUCCESS, "Configuration parameters do not exist");
			}

			JSONObject jsStr = JSONObject.parseObject(busiConfigDTO.getParams());
			String nickName = jsStr.getString("nickName");
			String wechatNumber = jsStr.getString("wechatNumber");

			// 参数不全
			if (StringUtils.isEmpty(nickName) || StringUtils.isEmpty(wechatNumber)) {
				return result.set(ResultCodeEnum.SUCCESS, "Parameter is not complete");
			}

			resultMap.put("nickName", nickName);
			resultMap.put("wechatNumber", wechatNumber);
			return result.set(ResultCodeEnum.SUCCESS, "success", resultMap);
		}

		// 活动咨询配置不存在
		return result.set(ResultCodeEnum.SUCCESS, "Activity advisory configuration does not exist");
	}

	/**
	 * 使用活动提取码
	 * 
	 * @param dto
	 * @param extractCode
	 *            提取码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "useExtractCode")
	public ResultDTO<String> useExtractCode(HttpServletRequest request, CommonParamsDTO dto, long lessonId, String extractCode) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		// 校验请求参数
		if (StringUtils.isEmpty(extractCode)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Extract code cannot be empty");
		}
		if(extractCode.length() != 6){
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Extraction codes are not valid");
		}
		if(lessonId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The course ID cannot be empty");
		}

		// 校验提取码是否合法
		ActivityExtractDTO activityExtractDTO = activityExtractService.getByExtractCode(extractCode.toUpperCase());
		if(activityExtractDTO == null){
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Extraction codes are not valid");
		}
		if(lessonId != activityExtractDTO.getLessonId()){
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The code does not apply to this course");
		}
		if(activityExtractDTO.getUserId() > 0){
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The extract code has been used");
		}

		UserDTO user = UserHelper.getUserDTO(dto);
		String userIp = Globals.getRealIP(request);
		
		activityExtractService.useExtractCode(user.getUserId(), userIp, activityExtractDTO.getConfigId());

		return result.set(ResultCodeEnum.SUCCESS, "success");
	}
}
