/*
 * TutorServiceImpl.java Created on 2017年9月27日 下午7:52:04
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
package com.wst.mem.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.security.Base64;
import com.hiwi.common.util.security.MD5Util;
import com.wst.mem.dao.TutorDao;
import com.wst.mem.dao.TutorSalaryRecordDao;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.TutorSalaryRecordDTO;
import com.wst.service.mem.service.TutorService;
import com.wst.service.notify.dto.MailNotifyDTO;
import com.wst.service.notify.service.MailNotifyService;

/**
 * 导师Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class TutorServiceImpl implements TutorService {

	@Resource
	private TutorDao tutorDao;

	@Resource
	private MailNotifyService mailNotifyService;

	@Resource
	private TutorSalaryRecordDao tutorSalaryRecordDao;

	@Override
	public Page<TutorDTO> findPaging(Page<TutorDTO> pageParam, TutorDTO tutorDTO) throws Exception {
		if (tutorDTO.getEndTime() != null) {
			tutorDTO.setEndTime(DateUtils.addEndTime(tutorDTO.getEndTime()));
		}
		return tutorDao.findPaging(pageParam, tutorDTO);
	}

	@Override
	public TutorDTO getTutorById(long tutorId) throws Exception {
		return tutorDao.getTutorById(tutorId);
	}

	@Override
	public int updateExamineStatus(long tutorId, long examineStatus) throws Exception {
		return tutorDao.updateExamineStatus(tutorId, examineStatus);
	}

	@Override
	public int updateTutorSalary(long tutorId, long tutorSalary) throws Exception {
		return tutorDao.updateTutorSalary(tutorId, tutorSalary);
	}

	@Override
	public TutorDTO getTutorByAccount(String loginAccount) throws Exception {
		return tutorDao.getTutorByAccount(loginAccount);
	}

	@Override
	public TutorDTO getTutorByAccountOfStatus(String loginAccount, long validStatus) throws Exception {
		return tutorDao.getTutorByAccountOfStatus(loginAccount, validStatus);
	}

	@Override
	public ResultDTO<Map<String, Object>> register(TutorDTO tutor) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

		tutor.setCreateTime(new Date()); // 注册时间
		tutor.setTutorType(1);
		tutor.setPassword(MD5Util.MD5(tutor.getPassword()));
		tutor.setExamineStatus(1);
		tutor.setValidStatus(2);
		tutor.setReceiveType(1);

		// 新增用户
		long totorId = tutorDao.addTutor(tutor);

		// 添加邮件参数
		MailNotifyDTO mailNotifyDTO = new MailNotifyDTO();
		mailNotifyDTO.setToAccs(tutor.getEmail());

		String recommendCode = Base64.encode(String.valueOf(totorId).getBytes("utf-8"));// Base64加密

		Map<String, Object> bodyParms = new HashMap<String, Object>();
		bodyParms.put("emailAddress", tutor.getEmail());
		bodyParms.put("recommendCode", recommendCode);
		bodyParms.put("roleType", tutor.getRoleType());
		mailNotifyDTO.setBodyParms(bodyParms);

		// 推送注册激活邮件
		this.mailNotifyService.sendRegisterActivationMail(mailNotifyDTO);

		// 用户注册成功,请登录邮箱完成激活
		return result.set(ResultCodeEnum.SUCCESS, "User registration is successful, please login email activation！");
	}

	@Override
	public ResultDTO<String> activate(String totorId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		totorId = new String(Base64.decode(totorId), "utf-8"); // Base64解密

		TutorDTO tutor = this.tutorDao.getTutorById(Long.parseLong(totorId));
		if (tutor == null) { // 帐号未注册
			return result.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered!");
		}

		// 用户已激活
		if (tutor.getValidStatus() == 1L) {// 该邮箱已激活
			return result.set(ResultCodeEnum.ERROR_HANDLE, "This mailbox has been activated!");
		}

		tutor.setValidStatus(1L); // 修改用户激活状态
		this.tutorDao.updateTutorStatus(tutor.getTutorId(), tutor.getValidStatus());

		return result.set(ResultCodeEnum.SUCCESS, "User activation success, please login!"); // 用户激活成功，请登陆使用！
	}

	@Override
	public int updatePassword(long tutorId, String password) throws Exception {
		// 密码加密
		password = MD5Util.MD5(password);
		return tutorDao.updatePassword(tutorId, password);
	}

	@Override
	public ResultDTO<TutorDTO> tutorLogin(String loginAccount, String password) throws Exception {
		ResultDTO<TutorDTO> result = new ResultDTO<TutorDTO>();

		TutorDTO tutor = this.tutorDao.getTutorByAccount(loginAccount);

		return result.set(ResultCodeEnum.SUCCESS, "succeed", tutor);
	}

	@Override
	public int updateTutor(TutorDTO tutorDTO) throws Exception {
		return tutorDao.updateTutor(tutorDTO);
	}

	@Override
	public int upTutor(TutorDTO tutorDTO) throws Exception {
		return tutorDao.upTutor(tutorDTO);
	}

	@Override
	public long upTutorPortrait(long tutorId, String imgUrl) throws Exception {
		return tutorDao.upTutorPortrait(tutorId, imgUrl);
	}

	@Override
	public Page<TutorSalaryRecordDTO> findTutorSalaryPaging(Page<TutorSalaryRecordDTO> pageParam,
			TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception {
		if (tutorSalaryRecordDTO.getEndTime() != null) {
			tutorSalaryRecordDTO.setEndTime(DateUtils.addEndTime(tutorSalaryRecordDTO.getEndTime()));
		}
		return tutorSalaryRecordDao.findTutorSalaryPaging(pageParam, tutorSalaryRecordDTO);
	}

}
