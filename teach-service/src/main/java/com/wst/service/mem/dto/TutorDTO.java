/*
 * TutorDTO.java Created on 2017年9月26日 上午10:23:01
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
package com.wst.service.mem.dto;

import java.util.Date;

import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.EnumUtils;
import com.wst.constant.UserConstant.ExamineStatusEnmu;
import com.wst.constant.UserConstant.ReceiveTypeEnmu;
import com.wst.constant.UserConstant.RegTypeEnmu;
import com.wst.constant.UserConstant.SexTypeEnmu;
import com.wst.constant.UserConstant.TutorTypeEnmu;
import com.wst.constant.UserConstant.ValidStatusEnmu;
import com.wst.entity.mem.Tutor;

/**
 * 对应实体类(Tutor)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class TutorDTO extends Tutor {

	private static final long serialVersionUID = 1L;

	/**
	 * 导师薪资
	 */
	private String tutorSalaryStr;

	/**
	 * 用户类型，导师端（tutor）T，学生端（student）S
	 */
	private String roleType;

	/**
	 * 调整说明
	 */
	private String changeExplain;

	/**
	 * 调整前金额
	 */
	private long beforSalary;

	/**
	 * 调整后金额
	 */
	private long endSalary;

	/**
	 * 学校ID
	 */
	private long schoolId;

	/**
	 * 地区ID
	 */
	private long regionId;
	
	/**
	 * 开始时间
	 */
	private Date beginTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 枚举导师类型
	 * 
	 * @return
	 */
	public String getTutorTypeStr() {
		TutorTypeEnmu tutorTypeEnmu = EnumUtils.getEnum(TutorTypeEnmu.values(), "type", tutorType);
		return tutorTypeEnmu == null ? "" : tutorTypeEnmu.name;
	}

	/**
	 * 枚举导师状态
	 * 
	 * @return
	 */
	public String getValidStatusStr() {
		ValidStatusEnmu validStatusEnmu = EnumUtils.getEnum(ValidStatusEnmu.values(), "status", validStatus);
		return validStatusEnmu == null ? "" : validStatusEnmu.name;
	}

	/**
	 * 枚举注册类型
	 * 
	 * @return
	 */
	public String getRegTypeStr() {
		RegTypeEnmu regTypeEnmu = EnumUtils.getEnum(RegTypeEnmu.values(), "type", regType);
		return regTypeEnmu == null ? "" : regTypeEnmu.name;
	}

	/**
	 * 枚举收款方式
	 * 
	 * @return
	 */
	public String getReceiveTypeStr() {
		ReceiveTypeEnmu receiveTypeEnmu = EnumUtils.getEnum(ReceiveTypeEnmu.values(), "type", receiveType);
		return receiveTypeEnmu == null ? "" : receiveTypeEnmu.name;
	}

	/**
	 * 枚举性别
	 * 
	 * @return
	 */
	public String getSexStr() {
		SexTypeEnmu sexTypeEnmu = EnumUtils.getEnum(SexTypeEnmu.values(), "type", sex);
		return sexTypeEnmu == null ? "" : sexTypeEnmu.name;
	}

	/**
	 * 枚举审核状态
	 * 
	 * @return
	 */
	public String getExamineStatusStr() {
		ExamineStatusEnmu examineStatusEnmu = EnumUtils.getEnum(ExamineStatusEnmu.values(), "status", examineStatus);
		return examineStatusEnmu == null ? "" : examineStatusEnmu.name;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getChangeExplain() {
		return changeExplain;
	}

	public void setChangeExplain(String changeExplain) {
		this.changeExplain = changeExplain;
	}

	public long getBeforSalary() {
		return beforSalary;
	}

	public void setBeforSalary(long beforSalary) {
		this.beforSalary = beforSalary;
	}

	public long getEndSalary() {
		return endSalary;
	}

	public void setEndSalary(long endSalary) {
		this.endSalary = endSalary;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public long getRegionId() {
		return regionId;
	}

	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	public String getTutorSalaryStr() throws Exception {
		tutorSalaryStr = AmountUtils.changeF2YString(tutorSalary);
		return tutorSalaryStr;
	}

	public void setTutorSalaryStr(String tutorSalaryStr) {
		this.tutorSalaryStr = tutorSalaryStr;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
