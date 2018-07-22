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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
import com.hiwi.common.util.security.MD5Util;
import com.wst.service.sys.dto.AppAccessDTO;
import com.wst.service.sys.dto.AppIpDTO;
import com.wst.service.sys.service.AppAccessService;
import com.wst.third.InterceptorNeedBean;

/**
 * 第三方应用接入拦截器
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class ThirdInterceptor implements HandlerInterceptor {

	private InterceptorNeedBean interceptorNeedBean;

	private static HiwiLog log = HiwiLogFactory.getLogger(ThirdInterceptor.class);

	public ThirdInterceptor(InterceptorNeedBean interceptorNeedBean) {
		this.setInterceptorNeedBean(interceptorNeedBean);
	}

	public ThirdInterceptor() {
	}

	/**
	 * 方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String url = request.getServletPath(); // 请求地址
		String ip = Globals.getRealIP(request); // 请求IP

		JSONObject jsonObject = Globals.getRequestParamsJson(request);

		String appId = jsonObject.getString("appId"); // 应用ID
		String sign = jsonObject.getString("sign"); // 签名
		String submitTime = jsonObject.getString("submitTime"); // 提交时间

		StringBuilder logInfo = new StringBuilder();
		logInfo.append("======第三方应用请求拦截器===appId:").append(appId).append("，请求IP：").append(ip).append("，请求URL：")
				.append(url).append("，请求参数：").append(jsonObject.toJSONString());

		if (StringUtils.isBlank(appId) || StringUtils.isBlank(sign) || StringUtils.isBlank(submitTime)) {
			log.error(logInfo.insert(0, "【缺少必要的参数】"));
			errorInfoResponse(response, ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要的参数");
			return false;
		}

		AppAccessService appAccessService = interceptorNeedBean.getAppAccessService();
		AppAccessDTO appAccessDTO = appAccessService.getByAppId(appId);
		if (appAccessDTO == null) {
			log.error(logInfo.insert(0, "【未找到appId对应的接入配置】"));
			errorInfoResponse(response, ResultCodeEnum.ERROR_AUTH, "无接入权限");
			return false;
		}

		// 白名单IP列表
		List<AppIpDTO> whiteIpList = appAccessService.findWhiteIpList(appAccessDTO.getAccsId());
		boolean isIpAllow = false; // IP地址是否允许
		for (AppIpDTO appIpDTO : whiteIpList) {
			if (ip.equalsIgnoreCase(appIpDTO.getIpAddr())) {
				isIpAllow = true;
			}
		}
		if (!isIpAllow) {
			log.error(logInfo.insert(0, "【IP不合法】"));
			errorInfoResponse(response, ResultCodeEnum.ERROR_AUTH, "IP不合法");
			return false;
		}

		if (!this.checkSign(jsonObject, appAccessDTO.getAppSecret())) {
			log.error(logInfo.insert(0, "【签名错误】"));
			errorInfoResponse(response, ResultCodeEnum.ERROR_AUTH, "签名错误");
			return false;
		}

		log.debug(logInfo);
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
			log.error("======APP请求拦截器=====（捕获异常）", exception);

			errorInfoResponse(response, ResultCodeEnum.ERROR_UNKNOW, "请稍后重试");
		}
	}

	/**
	 * 拦截错误信息response返回结果
	 * 
	 * @param response
	 * @param resultEnum
	 * @param msg
	 * @throws Exception
	 */
	private void errorInfoResponse(HttpServletResponse response, ResultCodeEnum resultEnum, String msg)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		ResultDTO<Object> result = new ResultDTO<>();
		result.set(resultEnum, msg, "");

		out.write(JsonUtils.getJson(result));
		out.flush();
		out.close();
	}

	/**
	 * 校验签名是否合法
	 * 
	 * @param requestParams
	 *            客户端请求参数
	 * @param secret
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	private boolean checkSign(JSONObject requestParams, String secret) throws Exception {
		String appId = requestParams.getString("appId");
		String sign = requestParams.getString("sign");
		if (StringUtils.isBlank(appId))
			return false;
		if (StringUtils.isBlank(sign))
			return false;
		if (StringUtils.isBlank(secret))
			return false;

		Map<String, Object> paramsMap = new TreeMap<>();
		Iterator<Entry<String, Object>> iter = requestParams.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String key = entry.getKey();
			if (key.equals("appId") || key.equals("sign")) {
				continue;
			}
			paramsMap.put(key, entry.getValue());
		}

		StringBuilder signBody = new StringBuilder();
		Iterator<Entry<String, Object>> mapIter = paramsMap.entrySet().iterator();
		while (mapIter.hasNext()) {
			Entry<String, Object> entry = mapIter.next();
			signBody.append(entry.getKey() + "=" + entry.getValue() + "&");
		}

		String signBodyStr = secret + signBody.substring(0, signBody.length() - 1) + secret;
		log.debug("===========加密参数：" + signBodyStr + ",加密后：" + MD5Util.MD5(signBodyStr));
		if (sign.equals(MD5Util.MD5(signBodyStr)))
			return true;

		return false;
	}

	public InterceptorNeedBean getInterceptorNeedBean() {
		return interceptorNeedBean;
	}

	public void setInterceptorNeedBean(InterceptorNeedBean interceptorNeedBean) {
		this.interceptorNeedBean = interceptorNeedBean;
	}
}
