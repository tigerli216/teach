/*
 * TutorService.java Created on 2017年9月27日 下午7:48:52
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
package com.wst.service.mem.service;

import java.util.Map;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.TutorSalaryRecordDTO;

/**
 * 导师Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface TutorService {

	/**
	 * 获取导师分页数据
	 * 
	 * @param pageParam
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	public Page<TutorDTO> findPaging(Page<TutorDTO> pageParam, TutorDTO tutorDTO) throws Exception;

	/**
	 * 通过导师ID获取导师信息
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public TutorDTO getTutorById(long tutorId) throws Exception;

	/**
	 * 注册导师审核
	 * 
	 * @param tutorId
	 * @param examineStatus
	 * @return
	 * @throws Exception
	 */
	public int updateExamineStatus(long tutorId, long examineStatus) throws Exception;

	/**
	 * 导师薪资调整
	 * 
	 * @param tutorId
	 * @param tutorSalary
	 * @return
	 * @throws Exception
	 */
	public int updateTutorSalary(long tutorId, long tutorSalary) throws Exception;

	/**
	 * 通过用户登录帐号获取用户信息
	 * 
	 * @param loginAccount
	 * @return
	 * @throws Exception
	 */
	public TutorDTO getTutorByAccount(String loginAccount) throws Exception;

	/**
	 * 通过用户登录帐号和用户状态获取用户信息
	 * 
	 * @param loginAccount
	 * 
	 * @param validStatus
	 * @return
	 * @throws Exception
	 */
	public TutorDTO getTutorByAccountOfStatus(String loginAccount, long validStatus) throws Exception;

	/**
	 * 邮箱激活
	 * 
	 * @param loginAccount
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> activate(String loginAccount) throws Exception;

	/**
	 * 用户注册
	 * 
	 * @param tutor
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<Map<String, Object>> register(TutorDTO tutor) throws Exception;

	/**
	 * 找回密码
	 * 
	 * @param tutorId
	 *            用户ID
	 * @param password
	 *            登录密码
	 * @return
	 * @throws Exception
	 */
	public int updatePassword(long tutorId, String password) throws Exception;

	/**
	 * 用户登录和密码校验
	 * 
	 * @param loginAccount
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<TutorDTO> tutorLogin(String loginAccount, String password) throws Exception;

	/**
	 * 修改导师个人信息
	 * 
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	public int updateTutor(TutorDTO tutorDTO) throws Exception;

	/**
	 * 修改导师个人信息后台
	 * 
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	public int upTutor(TutorDTO tutorDTO) throws Exception;

	/**
	 * 修改导师头像地址
	 * 
	 * @param tutorId
	 * @param imgUrl
	 * @return
	 * @throws Exception
	 */
	public long upTutorPortrait(long tutorId, String imgUrl) throws Exception;

	/**
	 * 获取导师薪资调整列表
	 * 
	 * @param pageParam
	 * @param tutorSalaryRecordDTO
	 * @return
	 * @throws Exception
	 */
	public Page<TutorSalaryRecordDTO> findTutorSalaryPaging(Page<TutorSalaryRecordDTO> pageParam,
			TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception;
}
