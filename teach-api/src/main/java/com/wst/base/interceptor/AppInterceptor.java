/*
 * AppInterceptor.java Created on 2016年10月11日 下午9:28:14
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.base.interceptor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.Globals;
import com.hiwi.common.util.JsonUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;

/**
 * APP拦截器
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class AppInterceptor implements HandlerInterceptor {

	private static HiwiLog log = HiwiLogFactory.getLogger(AppInterceptor.class);

	private static String[] mapReqArr = new String[] {}; // result是map的请求
	private static String[] listReqArr = new String[] {}; // result是list的请求

	private static String[] permissionSkipPattern = { "/d-app/activate", "/d-app/restPwd", "/d-app/doRestPwd",
			"/d-app/API/wxOatuh2", "/d-app/API/deputyLogin", "/d-app/API/tutor/uplodePortrait" }; // 检查权限过滤条件

	private static String[] notLoginPattern = { "/d-app/API/student/getIndexInfo", "/d-app/API/userLogin",
			"/d-app/API/userRegister", "/d-app/API/restPwd", "/d-app/API/findAds",
			"/d-app/API/student/findCustomLessons", "/d-app/API/student/findLessonPublics",
			"/d-app/API/student/findPracticeLessons", "/d-app/API/student/getlessonPublic",
			"/d-app/API/student/getCustomLesson", "/d-app/API/student/getPracticeLesson",
			"/d-app/API/region/findRegionList", "/d-app/API/region/findSchoolList", "/d-app/API/region/findIndustrys",
			"/d-app/API/AppVersions", "/d-app/API/act/extractAct", "/d-app/API/student/findStaticPage",
			"/d-app/API/live/getGroupId", "/d-app/API/live/findPic", "/d-app/API/live/getMoiveUrl",
			"/d-app/API/live/getHeadImg", "/d-app/API/live/getClassInfo", "/d-app/API/student/getVipSeekCode",
			"/d-app/API/student/live/getLiveUrl" };

	/**
	 * 方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getServletPath(); // 请求地址
		String ip = Globals.getRealIP(request); // 请求IP
		JSONObject jsonObject = Globals.getRequestParamsJson(request);

		// 不做鉴权接口
		for (String pattern : permissionSkipPattern) {
			if (url.equals(pattern)) {
				log.debug(new StringBuilder("======请求拦截器=====（不鉴权），请求URL：").append(url).append("，请求IP：").append(ip)
						.append("，请求参数：").append(jsonObject.toJSONString()).toString());
				return true;
			}
		}
		String appVersion = jsonObject.getString("appVersion");// app版本
		String sign = jsonObject.getString("sign"); // 签名
		String submitTime = jsonObject.getString("submitTime"); // 提交时间

		if (StringUtils.isNotEmpty(appVersion) && (StringUtils.isBlank(sign) || StringUtils.isBlank(submitTime))) {
			log.info(new StringBuilder("======请求拦截器===（缺少必要的参数或未登陆）请求IP：").append(ip).append("，请求URL：").append(url)
					.append("，请求参数：").append(jsonObject.toJSONString()).toString());

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			ResultDTO<Object> result = new ResultDTO<>();
			result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要的参数或未登陆", getResult(url));

			out.write(JsonUtils.getJson(result));
			out.flush();
			out.close();

			return false;
		}

		// 校验是否需要登录
		boolean isCheck = true;
		for (String pattern : notLoginPattern) {
			if (url.equals(pattern)) {
				isCheck = false;
			}
		}
		String appSessionId = jsonObject.getString("appSessionId"); // appSessionId

		if (isCheck && (StringUtils.isNotEmpty(appVersion) && StringUtils.isEmpty(appSessionId))) {
			log.info(new StringBuilder("======请求拦截器===（缺少必要的参数或未登陆appSessionId）请求IP：").append(ip).append("，请求URL：")
					.append(url).append("，请求参数：").append(jsonObject.toJSONString()).toString());

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			ResultDTO<Object> result = new ResultDTO<>();
			result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "缺少必要的参数或未登陆", getResult(url));

			out.write(JsonUtils.getJson(result));
			out.flush();
			out.close();

			return false;
		}
		
		CommonParamsDTO commonParamsDTO = new CommonParamsDTO();
        commonParamsDTO.setRequest(request);
        commonParamsDTO.setAppSessionId(appSessionId);

		// app端校验签名
		if ((StringUtils.isNotEmpty(appVersion) || StringUtils.isNotEmpty(appSessionId))
				&& !UserHelper.checkSign(jsonObject)) {
		    // 清空缓存登陆信息
		    UserHelper.delUserSession(commonParamsDTO);
			// 签名校验失败
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			ResultDTO<Object> result = new ResultDTO<>();
			result.set(ResultCodeEnum.ERROR_AUTH, "您的账号存在异常，在其他地方使用，请确认账号安全。", getResult(url));
			log.info(new StringBuilder("======APP请求拦截器=====（签名不合法）").append("，请求URL：").append(url).append("，请求IP：")
					.append(ip).append("，请求参数：").append(jsonObject.toJSONString()).toString());
			out.write(JsonUtils.getJson(result));
			out.flush();
			out.close();

			return false;
		}

		// 获取用户信息
		UserDTO user = null;
		TutorDTO tutor = null;
		if (StringUtils.isNotEmpty(appSessionId) && appSessionId.endsWith("T")) {
			tutor = UserHelper.getTutorDTO(commonParamsDTO);
		} else {// 从session中获取
			user = UserHelper.getUserDTO(commonParamsDTO);
		}
		if (isCheck && user == null && tutor == null) {
			log.info(new StringBuilder("======请求拦截器===(用户不存在)请求IP：").append(ip).append("，请求URL：").append(url)
					.append("，请求参数：").append(jsonObject.toJSONString()).toString());

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			ResultDTO<Object> result = new ResultDTO<>();
			result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "缺少必要的参数或未登陆", getResult(url));

			out.write(JsonUtils.getJson(result));
			out.flush();
			out.close();

			return false;
		}

		if (tutor != null) {
			log.info(new StringBuilder("======APP请求拦截器=====导师：").append(tutor.getLoginAccount()).append("，tutorId:")
					.append(tutor.getTutorId()).append("，请求URL：").append(url).append("，请求IP：").append(ip)
					.append("，请求参数：").append(jsonObject.toJSONString()).toString());
		} else if (user != null) {
			log.info(new StringBuilder("======APP请求拦截器=====用户：").append(user.getLoginAccount()).append("，userId:")
					.append(user.getUserId()).append("，请求URL：").append(url).append("，请求IP：").append(ip).append("，请求参数：")
					.append(jsonObject.toJSONString()).toString());
		} else {
			log.info(new StringBuilder("======APP请求拦截器=====").append("，请求URL：").append(url).append("，请求IP：").append(ip)
					.append("，请求参数：").append(jsonObject.toJSONString()).toString());
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 处理完拦截，用于拦截异常
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		if (exception != null) {
			String url = request.getServletPath(); // 请求地址
			String ip = Globals.getRealIP(request); // 请求IP
			JSONObject jsonObject = Globals.getRequestParamsJson(request);

			StringBuilder logStr = new StringBuilder("======APP请求拦截器=====（捕获异常）");
			logStr.append("，请求URL：").append(url).append("，请求IP：").append(ip)
					.append("，请求参数：" + jsonObject.toJSONString());
			log.error(logStr.toString(), exception);

			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			ResultDTO<String> result = new ResultDTO<String>();
			result.set(ResultCodeEnum.ERROR_HANDLE, "请稍后重试");

			out.write(JsonUtils.getJson(result));
			out.flush();
			out.close();
		}
	}

	/**
	 * 根据url，匹配返回数据类型
	 * 
	 * @param url
	 * @return
	 */
	private Object getResult(String url) {
		for (String str : mapReqArr) {
			if (str.equals(url))
				return new HashMap<>();
		}
		for (String str : listReqArr) {
			if (str.equals(url))
				return new ArrayList<>();
		}
		return "";
	}

}
