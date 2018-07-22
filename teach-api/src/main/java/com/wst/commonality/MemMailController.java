/*
 * MemMailController.java Created on 2017年8月31日 下午8:17:02
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
package com.wst.commonality;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.wst.service.mem.service.TutorService;
import com.wst.service.mem.service.UserService;

/**
 * 用户邮箱注册激活
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app")
public class MemMailController {

	private static HiwiLog logger = HiwiLogFactory.getLogger(MemMailController.class);

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	/**
	 * 邮箱注册激活
	 * 
	 * @param c
	 *            注册用户帐号
	 * @param p
	 *            用户类型 学生:"S" 导师: "T"
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/activate")
	public String userActivation(String c, String p, ModelMap model) throws Exception {
		if (StringUtils.isEmpty(c)) { // 请输入正确注册用户帐号
			model.put("activateResult", "Please enter the correct registered user account");
			return "email/userActivation";
		}

		try {
			ResultDTO<String> result = null;
			if (StringUtils.equals(p, "S")) {
				result = this.userService.activate(c);
			} else if (StringUtils.equals(p, "T")) {
				result = this.tutorService.activate(c);
			}
			model.put("activateResult", result.getErrDesc());
		} catch (Exception ex) { // 服务器异常，请稍后重试
			model.put("activateResult", "Server exception, please try again later");
		}

		return "email/userActivation";
	}

	/**
	 * 找回密码
	 * 
	 * @param t
	 *            TOKEN
	 * @param id
	 *            用户id  
	 * @param u
	 *            登陆账号
	 * @param p
	 *            用户类型 学生:"S" 导师: "T"
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/restPwd")
	public String userRestPwd(String t, String id, String u, String p, ModelMap model) throws Exception {
		if (StringUtils.isEmpty(t) || StringUtils.isEmpty(id) || StringUtils.isEmpty(u)) { // 找回密码错误
			model.put("restPwdResult", "Retrieve password error");
			return "email/userRestPwd";
		}

		// 通过令牌获取用户登录名修改密码类型,并校验,规则见LoginController.restPwd
		String cacheData = RedisClient.get(t);
		if (StringUtils.isEmpty(cacheData)) { // 操作时间过长，请重新通过邮箱找回密码
			model.put("restPwdResult",
					"The operation time is too long, please retrace the password through the mailbox");
			return "email/userRestPwd";
		}

		String account = cacheData.substring(0, cacheData.lastIndexOf("||"));
		if (!StringUtils.equals(id, account)) { // 找回密码错误
			model.put("restPwdResult", "Retrieve password error");
			return "email/userRestPwd";
		}

		// 设置令牌与参数
		model.put("t", t);
		model.put("u", u);
		model.put("p", p);
		model.put("id", id);

		return "email/userRestPwd";
	}

	/**
	 * 找回密码
	 * 
	 * @param t
	 *            TOKEN
	 * @param id
	 *            用户id  
	 * @param u
	 *            登陆账号
	 * @param p
	 *            用户类型 学生:"S" 导师: "T"
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/doRestPwd")
	public ResultDTO<String> doRestPwd(String t, String id, String u, String password, String p,
			String confirmPassword) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (StringUtils.isEmpty(t) || StringUtils.isEmpty(id) || StringUtils.isEmpty(u) || 
				StringUtils.isEmpty(password) || StringUtils.isEmpty(confirmPassword)) { // 找回密码错误
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Retrieve password error");
		}

		if (!StringUtils.equals(password, confirmPassword)) {//两次输入的密码不一致
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The two input passwords do not match");
		}
		if (!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) { // 登录密码格式不正确
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Incorrect login password format");
		}
		if (!confirmPassword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) { // 确认登录密码格式不正确
			return result.set(ResultCodeEnum.ERROR_HANDLE,
					"Verify that the login password is not in the correct format");
		}

		// 通过令牌获取用户登录名,并校验
		// 通过令牌获取用户登录名修改密码类型,并校验,规则见LoginController.restPwd
		String cacheData = RedisClient.get(t);
		if (StringUtils.isEmpty(cacheData)) { // 操作时间过长，请重新通过邮箱找回密码
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS,
					"The operation time is too long, please retrace the password through the mailbox");
		}

		String account = cacheData.substring(0, cacheData.lastIndexOf("||"));
		if (!StringUtils.equals(id, account)) { // 找回密码错误
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Retrieve password error");
		}

		try {
			if (StringUtils.equals(p, "S")) {
				userService.updatePassword(Long.valueOf(id), password);
			} else if (StringUtils.equals(p, "T")) {
				tutorService.updatePassword(Long.valueOf(id), password);
			}
			RedisClient.delete(t); // 找回成功删除缓存
		} catch (Exception e) { // 系统繁忙,请稍后再试
			logger.error("修改密码接口发生系统异常,用户号:" + account, e);
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The system is busy. Please try again later");
		}

		return result.set(ResultCodeEnum.SUCCESS, "Password recovery"); // 密码找回成功
	}
}
