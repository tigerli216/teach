/*
 * LoginController.java Created on 2017年10月9日 下午6:18:21
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.session.SessionIdGenerator;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.security.MD5Util;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.constant.RedisConstant;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.TutorService;
import com.wst.service.mem.service.UserService;
import com.wst.service.notify.dto.MailNotifyDTO;
import com.wst.service.notify.service.MailNotifyService;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.dto.IndustrySalaryDTO;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.IndustrySalaryService;
import com.wst.service.sys.service.IndustryService;
import com.wst.service.sys.service.RegionSchoolService;
import com.wst.service.sys.service.RegionService;
import com.wst.service.wx.WeixinService;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 登陆 注册 密码 相关控制
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API")
public class LoginController {

	private static HiwiLog logger = HiwiLogFactory.getLogger(LoginController.class);

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	@Reference(version = "0.0.1")
	private MailNotifyService mailNotifyService;

	@Reference(version = "0.0.1")
	private RegionService regionService;

	@Reference(version = "0.0.1")
	private RegionSchoolService regionSchoolService;

	@Reference(version = "0.0.1")
	private IndustryService industryService;

	@Reference(version = "0.0.1")
	private IndustrySalaryService industrySalaryService;

	@Reference(version = "0.0.1")
	private WeixinService weixinService;

	/**
	 * 用户注册接口
	 * 
	 * @param dto
	 * 
	 * @param regType
	 *            注册类型 : 1-邮箱 ;2-手机号;3-第三方账号
	 * @param loginAccount
	 *            regType : 1-邮箱 ;2-手机号;3-第三方账号
	 * @param password
	 *            登录密码
	 * @param confirmPassword
	 *            确认登录密码
	 * @param realName
	 *            真实姓名
	 * @param sex
	 *            性别（1-男；2-女）
	 * @param regionId
	 *            地区ID
	 * @param bindMobile
	 *            手机号
	 * @param schoolId
	 *            学校ID
	 * @param industryId
	 *            行业ID
	 * @param newlyJob
	 *            最近一份工作
	 * @param position
	 *            职位
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userRegister")
	public ResultDTO<Map<String, Object>> userRegister(CommonParamsDTO dto, String loginAccount,
			@RequestParam(defaultValue = "1") long regType, String password, String confirmPassword, String realName,
			@RequestParam(defaultValue = "1") long sex, @RequestParam(defaultValue = "0") long regionId,
			@RequestParam(defaultValue = "0") long schoolId, @RequestParam(defaultValue = "0") long industryId,
			String bindMobile, String newlyJob, String position) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<Map<String, Object>>();

		if (regType != 1L) {// 注册类型不是邮箱注册
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Registration type error");
		}
		if (!StringUtils.equals(dto.getUserType(), "T") && !StringUtils.equals(dto.getUserType(), "S")) {// 用户类型错误
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User type error");
		}
		if (StringUtils.isBlank(loginAccount)) {// 注册账号不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The registered account cannot be empty");
		}
		// 邮箱注册
		if (regType == 1L && !com.hiwi.common.util.StringUtils.isEmail(loginAccount)) {// 邮箱格式不正确
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Incorrect email format");
		}
		if (StringUtils.isBlank(password)) {// 登录密码不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The login password cannot be empty");
		}
		if (StringUtils.isBlank(confirmPassword)) {// 确认登录密码不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Verify that the login password cannot be empty");
		}
		if (!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) {// 登录密码格式不正确
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Incorrect login password format");
		}
		if (!confirmPassword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) {// 确认登录密码格式不正确
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
					"Verify that the login password is not in the correct format");
		}
		if (!password.equals(confirmPassword)) {// 两次密码输入不一致
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The two input passwords do not match");
		}
		if (StringUtils.equals(dto.getUserType(), "T")) {// 导师注册信息完善验证
			if (StringUtils.isBlank(realName)) {// 真实姓名不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The realName cannot be empty");
			}
			if (sex != 1L && sex != 2L) {// 性别类型错误
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Sex type error");
			}
			if (regionId <= 0L) {// 地区不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The country cannot be empty");
			}
			if (StringUtils.isBlank(bindMobile)) {// 手机号不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The bindMobile cannot be empty");
			}
			if (schoolId <= 0L) {// 毕业院校不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The graduateSchool cannot be empty");
			}
			if (industryId <= 0L) {// 所在行业不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The industry cannot be empty");
			}
			if (StringUtils.isBlank(newlyJob)) {// 最近一份工作不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The newlyJob cannot be empty");
			}
			if (StringUtils.isBlank(position)) {// 职位不能为空
				return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The position cannot be empty");
			}
		}
		loginAccount = loginAccount.toLowerCase();
		try {
			if (StringUtils.equals(dto.getUserType(), "S")) { // 学生注册
				// 登录名是否占用
				UserDTO loginAccountDTO = userService.getUserByAccountOfStatus(loginAccount, 1);
				if (loginAccountDTO != null) {// 账号已存在
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Account already exists");
				}

				UserDTO user = new UserDTO();
				user.setLoginAccount(loginAccount);
				user.setEmail(loginAccount);
				user.setPassword(password);
				user.setRegType(regType);
				user.setRoleType(dto.getUserType());// 区分用户类型

				resultDTO = this.userService.register(user);// 注册用户
			} else if (StringUtils.equals(dto.getUserType(), "T")) { // 导师注册
				// 登录名是否占用
				TutorDTO loginAccountDTO = tutorService.getTutorByAccountOfStatus(loginAccount, 1);
				if (loginAccountDTO != null) {// 账号已存在
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Account already exists");
				}

				TutorDTO tutor = new TutorDTO();
				tutor.setLoginAccount(loginAccount);
				tutor.setEmail(loginAccount);
				tutor.setPassword(password);
				tutor.setRegType(regType);
				tutor.setRoleType(dto.getUserType());// 区分用户类型
				tutor.setRealName(realName);
				tutor.setSex(sex);
				tutor.setBindMobile(bindMobile);
				tutor.setNewlyJob(newlyJob);
				tutor.setPosition(position);

				RegionDTO regionDTO = regionService.getRegionById(regionId);
				if (regionDTO != null) {
					tutor.setCountry(regionDTO.getRegionName());
				}
				RegionSchoolDTO regionSchoolDTO = regionSchoolService.getRegionSchoolById(schoolId);
				if (regionSchoolDTO != null) {
					tutor.setGraduateSchool(regionSchoolDTO.getSchoolName());
				}
				IndustryDTO industryDTO = industryService.getIndustryById(industryId);
				if (industryDTO != null) {
					tutor.setIndustryId(industryId);
					tutor.setIndustryName(industryDTO.getIndustryName());
				}
				IndustrySalaryDTO industrySalaryDTO = industrySalaryService.getIndustrySalaryByIndustryId(industryId);
				if (industrySalaryDTO != null) {
					tutor.setTutorSalary(industrySalaryDTO.getBaseSalary());// 导师行业薪资
				}

				resultDTO = this.tutorService.register(tutor);// 注册用户
			}
		} catch (Exception e) {
			logger.error("用户提交注册发生系统异常,注册用户号:" + loginAccount, e);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "系统繁忙,请稍后再试");
		}

		return resultDTO;

	}

	/**
	 * 找回密码 (登录密码)
	 * 
	 * @param dto
	 *            公用参数DTO
	 * @param loginAccount
	 *            登录帐号
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/restPwd")
	public ResultDTO<String> restPwd(CommonParamsDTO dto, String loginAccount) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		if (!StringUtils.equals(dto.getUserType(), "T") && !StringUtils.equals(dto.getUserType(), "S")) {// 用户类型错误
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User type error");
		}
		if (StringUtils.isBlank(loginAccount)) {// 注册账号不能为空
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The registered account cannot be empty");
		}
		// 验证是否是邮箱
		if (!com.hiwi.common.util.StringUtils.isEmail(loginAccount)) {// 邮箱格式不正确
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Incorrect email format");
		}

		loginAccount = loginAccount.trim().toLowerCase();
		// 验证填写注册账号是否存在
		long id = 0;
		if (StringUtils.equals(dto.getUserType(), "S")) {
			UserDTO user = this.userService.getUserByAccountOfStatus(loginAccount, 1);
			if (user == null) {// 账号未注册
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered");
			}
			id = user.getUserId();
		} else if (StringUtils.equals(dto.getUserType(), "T")) {
			TutorDTO tutor = this.tutorService.getTutorByAccountOfStatus(loginAccount, 1);
			if (tutor == null) {// 账号未注册
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered");
			}
			if (tutor.getExamineStatus() != 2L)// 用户审核未通过,请联系管理人员审核通过
				return result.set(ResultCodeEnum.ERROR_HANDLE,
						"The account is not approved, please contact the administrator for approval！");
			id = tutor.getTutorId();
		}

		// 邮箱找回密码
		try {
			// 添加邮件参数
			MailNotifyDTO mailNotifyDTO = new MailNotifyDTO();
			mailNotifyDTO.setToAccs(loginAccount);

			Map<String, Object> bodyParms = new HashMap<String, Object>(2);
			bodyParms.put("emailAddress", loginAccount);

			// 生成32 TOKEN,并附带时间戳
			String token = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis();
			bodyParms.put("token", token);
			bodyParms.put("validPeriod", 60); // 分钟
			bodyParms.put("roleType", dto.getUserType()); // 找回密码用户类型
			bodyParms.put("id", id); // 找回密码用户类型
			mailNotifyDTO.setBodyParms(bodyParms);

			// 密码重置邮件
			this.mailNotifyService.sendRestPwdMail(mailNotifyDTO);

			// 缓存推送TOKEN,并在跳转页面验证
			RedisClient.set(token, String.valueOf(id) + "||" + dto.getUserType());
			RedisClient.expire(token, 3600); // 60分钟失效
		} catch (Exception e) {
			logger.error("修改密码接口发生系统异常,用户ID:" + String.valueOf(id), e);
			return result.set(ResultCodeEnum.ERROR_HANDLE, "系统繁忙,请稍后再试");
		}
		// 密码重置邮件已发送至您的邮箱，请登录邮箱进行下一步操作吧
		return result.set(ResultCodeEnum.SUCCESS,
				"Password reset email has been sent, " + "please login email operation！");

	}

	/**
	 * 修改密码 (登录密码)
	 * 
	 * @param dto
	 *            公用参数DTO
	 * @param loginAccount
	 *            登录帐号
	 * @param oldPassword
	 *            旧密码
	 * @param password
	 *            新密码
	 * @param confirmPassword
	 *            确认密码 验证码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/updateUserPwd")
	public ResultDTO<String> updateUserPwd(CommonParamsDTO dto, String loginAccount, String oldPassword,
			String password, String confirmPassword) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		if (StringUtils.isNotEmpty(loginAccount)) {
			loginAccount = loginAccount.trim();
		}
		if (!StringUtils.equals(dto.getUserType(), "T") && !StringUtils.equals(dto.getUserType(), "S")) {// 用户类型错误
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User type error");
		}
		if (StringUtils.isBlank(oldPassword)) {// 请填写原密码
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Please fill in the original password");
		}

		if (StringUtils.isBlank(password)) {// 请填写新密码
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Please fill in the new password");
		}
		if (StringUtils.isBlank(confirmPassword)) {// 请填写确认密码
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Please fill in the confirmation password");
		}
		if (!StringUtils.equals(password, confirmPassword)) {// 两次输入的密码不一致
			return result.set(ResultCodeEnum.ERROR_HANDLE, "The two input passwords do not match");
		}

		// 登陆密码校验
		if (!password.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) {// 登录密码格式不正确
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Incorrect login password format");
		}
		if (!confirmPassword.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,18}$")) {// 确认登录密码格式不正确
			return result.set(ResultCodeEnum.ERROR_HANDLE,
					"Verify that the login password is not in the correct format");
		}
		try {
			if (StringUtils.equals(dto.getUserType(), "S")) {
				UserDTO user = UserHelper.getUserDTO(dto);
				if (user == null) {// 请登录后再修改密码
					return result.set(ResultCodeEnum.ERROR_HANDLE, "Please log in and change your password");
				}
				if (!StringUtils.equals(MD5Util.MD5(oldPassword), user.getPassword())) {// 用户原密码输入错误
					return result.set(ResultCodeEnum.ERROR_HANDLE, "User password entered incorrectly");
				}
				this.userService.updatePassword(user.getUserId(), password);

				// 密码重置成功,更新缓存用户密码
				user.setPassword(MD5Util.MD5(password));

				UserHelper.setUser(dto, user);
			} else if (StringUtils.equals(dto.getUserType(), "T")) {
				TutorDTO tutor = UserHelper.getTutorDTO(dto);
				if (tutor == null) {// 请登录后再修改密码
					return result.set(ResultCodeEnum.ERROR_HANDLE, "Please log in and change your password");
				}
				if (!StringUtils.equals(MD5Util.MD5(oldPassword), tutor.getPassword())) {// 用户原密码输入错误
					return result.set(ResultCodeEnum.ERROR_HANDLE, "User password entered incorrectly");
				}
				this.tutorService.updatePassword(tutor.getTutorId(), password);

				// 密码重置成功,更新缓存用户密码
				tutor.setPassword(MD5Util.MD5(password));

				UserHelper.setTutor(dto, tutor);
			}

		} catch (Exception e) {
			logger.error("修改密码接口发生系统异常,用户号:" + loginAccount.toLowerCase(), e);
			return result.set(ResultCodeEnum.ERROR_HANDLE, "系统繁忙,请稍后再试");
		}
		// 更改密码成功
		return result.set(ResultCodeEnum.SUCCESS, "Change password success");
	}

	/**
	 * 用户登录接口
	 * 
	 * @param dto
	 * 
	 * @param loginAccount
	 *            登录帐号
	 * @param passWord
	 *            登录密码
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/userLogin")
	public ResultDTO<Map<String, Object>> userLogin(HttpServletRequest request, CommonParamsDTO dto,
			String loginAccount, String password) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<Map<String, Object>>();
		if (!StringUtils.equals(dto.getUserType(), "T") && !StringUtils.equals(dto.getUserType(), "S")) {// 用户类型错误
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User type error");
		}
		if (StringUtils.isBlank(loginAccount)) {// 登录账号不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The login account cannot be empty");
		}

		if (StringUtils.isBlank(password)) {// 登录密码不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The login password cannot be empty");
		}

		loginAccount = loginAccount.trim().toLowerCase();
		// 校验登陆信息
		if (StringUtils.equals(dto.getUserType(), "S")) { // 学生登录
			UserDTO userLogin = userService.getUserByAccountOfStatus(loginAccount, 1);
			if (userLogin == null) { // 帐号未注册
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered!");
			}
			ResultDTO<UserDTO> result = userService.userLogin(loginAccount, password);
			if (result.isSuccess()) {
				// 登陆成功后将用户信息存入redis
				UserDTO user = result.getResult();

				// 校验登陆密码
				if (!StringUtils.equals(user.getPassword(), MD5Util.MD5(password))) {
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Password input error");
				}
				if (user.getStatus() != 1L)// 帐号未激活,请登录邮箱激活
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
							"No account activation, please login email activation！");

				Map<String, Object> resultData = new HashMap<String, Object>();
				String appVersion = dto.getAppVersion();
				if (StringUtils.isNotEmpty(appVersion)) { // app端学生
					String appSessionId = SessionIdGenerator.generateSessionId(String.valueOf(user.getUserId())) + "_S";
					dto.setAppSessionId(appSessionId);
					resultData.put("sessionId", appSessionId);
					resultData.put("secret", UserHelper.generateSecret(appSessionId));
				} else { // 微信端学生
					String exMpUserStr = RedisClient.get(request.getParameter("openId"));
					if (StringUtils.isBlank(exMpUserStr)) {
						String baseUrl = "https://" + request.getServerName();
						String oatuh2Url = weixinService
								.oauth2buildAuthorizationUrl(baseUrl + "/d-app/API/deputyLogin");

						return resultDTO.set(ResultCodeEnum.ERROR_AUTH_TIME_OUT, oatuh2Url);
					}
					WxMpUser wxMpUser = JsonUtils.parseJson(exMpUserStr, WxMpUser.class);
					// 校验openId是否已存在
					UserDTO bakUser = userService.getUserByDeputyLoginAccount(wxMpUser.getOpenId());
					if (bakUser != null) {
						if (bakUser.getUserId() != user.getUserId()) {
							userService.updateDeputyLoginAccount(bakUser.getUserId(), "", "");
						}
					}

					String headImg = wxMpUser.getHeadImgUrl();
					if (StringUtils.isNotEmpty(headImg)) {
						if (!headImg.contains("https")) {
							headImg = headImg.replaceAll("http", "https");
						}
					}

					userService.updateDeputyLoginAccount(user.getUserId(), wxMpUser.getOpenId(), headImg);
					user.setDeputyLoginAccount(wxMpUser.getOpenId());
					user.setPortraitImgUrl(headImg);
				}
				UserHelper.setUser(dto, user);
				resultData.put("email", user.getEmail());
				return resultDTO.set(ResultCodeEnum.SUCCESS, "succeed", resultData);
			}
		} else if (StringUtils.equals(dto.getUserType(), "T")) { // 导师登录
			TutorDTO tutorLogin = tutorService.getTutorByAccountOfStatus(loginAccount, 1);
			if (tutorLogin == null) { // 帐号未注册
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered!");
			}
			ResultDTO<TutorDTO> result = tutorService.tutorLogin(loginAccount, password);
			if (result.isSuccess()) {
				TutorDTO tutor = result.getResult();

				// 校验登陆密码
				if (!StringUtils.equals(tutor.getPassword(), MD5Util.MD5(password))) {
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "Password input error");
				}
				if (tutor.getValidStatus() != 1L)// 帐号未激活,请登录邮箱激活
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
							"No account activation, please login email activation！");
				if (tutor.getExamineStatus() != 2L)// 用户审核未通过,请联系管理人员审核通过
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE,
							"The account is not approved, please contact the administrator for approval！");

				// 登陆成功后将用户信息存入redis
				String appSessionId = SessionIdGenerator.generateSessionId(String.valueOf(tutor.getTutorId())) + "_T";
				dto.setAppSessionId(appSessionId);
				UserHelper.setTutor(dto, tutor);

				Map<String, Object> resultData = new HashMap<String, Object>();
				resultData.put("sessionId", appSessionId);
				resultData.put("secret", UserHelper.generateSecret(appSessionId));
				resultData.put("email", tutor.getEmail());

				return resultDTO.set(ResultCodeEnum.SUCCESS, "succeed", resultData);
			}
		}
		return resultDTO.set(resultDTO.getErrCode(), resultDTO.getErrDesc());

	}

	/**
	 * 退出登录
	 * 
	 * @param request
	 * @param userDto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/logout")
	public ResultDTO<String> logout(CommonParamsDTO dto) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		if (!StringUtils.equals(dto.getUserType(), "T") && !StringUtils.equals(dto.getUserType(), "S")) {// 用户类型错误
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User type error");
		}
		if (StringUtils.equals(dto.getUserType(), "S")) {
			UserDTO user = UserHelper.getUserDTO(dto);
			if (user == null)// 请先登录,appSessionId错误
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Please login first", "appSessionId error");

			userService.updateDeputyLoginAccount(user.getUserId(), "", "");
		} else if (StringUtils.equals(dto.getUserType(), "T")) {
			TutorDTO tutor = UserHelper.getTutorDTO(dto);
			if (tutor == null)// 请先登录,appSessionId错误
				return result.set(ResultCodeEnum.ERROR_HANDLE, "Please login first", "appSessionId error");
		}

		UserHelper.delUserOrTutor(dto); // 移除登录
		// 退出成功
		return result.set(ResultCodeEnum.SUCCESS, "", "Exit the success");
	}

	/**
	 * 微信授权
	 * 
	 * @param dto
	 *            公共参数
	 * @param indexUrl
	 *            首页地址
	 * @param loginUrl
	 *            登录页地址
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/wxOatuh2")
	public ResultDTO<String> wxOatuh2(CommonParamsDTO dto, String indexUrl, String loginUrl) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (StringUtils.isEmpty(indexUrl)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "index address is empty");
		}
		if (StringUtils.isEmpty(loginUrl)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "login address is empty");
		}
		RedisClient.set(RedisConstant.WX_INDEX_URL, indexUrl);
		RedisClient.set(RedisConstant.WX_LOGIN_URL, loginUrl);
		HttpServletRequest request = dto.getRequest();
		String baseUrl = "https://" + request.getServerName();
		String oatuh2Url = weixinService.oauth2buildAuthorizationUrl(baseUrl + "/d-app/API/deputyLogin");
		return result.set(ResultCodeEnum.SUCCESS, "success", oatuh2Url);
	}

	/**
	 * 微信网页授权回调
	 * 
	 * @param request
	 * @param code
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/deputyLogin")
	public void deputyLogin(HttpServletRequest request, String code, HttpServletResponse response) throws Exception {
		logger.debug("微信公众号网页授权回调,code=：" + code);
		if (StringUtils.isBlank(code)) {
			logger.error("微信自动登陆授权失败,获取code失败!");
			return;
		}
		WxMpUser wxMpUser = weixinService.getWxMpUser(code);
		if (wxMpUser == null) {
			logger.error("微信自动登陆授权失败!");
			return;
		}
		// 存放微信认证信息
		RedisClient.set(wxMpUser.getOpenId(), JsonUtils.getJson(wxMpUser));

		// 根据openId判断用户是否存在
		UserDTO user = userService.getUserByDeputyLoginAccount(wxMpUser.getOpenId());
		if (user != null) {
			// 使用微信头像
			String headImg = wxMpUser.getHeadImgUrl();
			if (StringUtils.isNotEmpty(headImg)) {
				if (!headImg.contains("https")) {
					headImg = headImg.replaceAll("http", "https");
				}
			}

			userService.updateDeputyLoginAccount(user.getUserId(), wxMpUser.getOpenId(), headImg);

			user.setPortraitImgUrl(headImg);

			// 用户已存在，自动登录，放入缓存
			UserHelper.setUserSession(request, user);
			String indexUrl = RedisClient.get(RedisConstant.WX_INDEX_URL);
			if (StringUtils.isEmpty(indexUrl)) {
				logger.error("微信授权登录成功，重定向首页地址为空");
				return;
			}
			response.sendRedirect(indexUrl + "?openId=" + wxMpUser.getOpenId());
		} else {
			String loginUrl = RedisClient.get(RedisConstant.WX_LOGIN_URL);
			if (StringUtils.isEmpty(loginUrl)) {
				logger.error("微信授权成功，重定向登录页地址为空");
				return;
			}
			response.sendRedirect(loginUrl + "?openId=" + wxMpUser.getOpenId());
		}
	}
}
