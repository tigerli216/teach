/*
 * CustomDTO.java Created on 2017年9月26日 上午10:47:52
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
package com.wst.service.lesson.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.EnumUtils;
import com.wst.constant.LessonCustomConstant.LessonCustomTypeEnmu;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.entity.lesson.Custom;
import com.wst.service.mem.dto.TutorDTO;

/**
 * 对应实体类(Custom)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class CustomDTO extends Custom {

	private static final long serialVersionUID = 1L;

	/**
	 * 课程分类（1-定制课；2-实习课）
	 */
	private String lessonTypeStr;

	/**
	 * 学生帐号
	 * 
	 */
	private String loginAccount;

	/**
	 * 导师ID
	 */
	private long tutorId;

	/**
	 * 课程原价:金额（美分转元）
	 */
	private String lessonPriceStr;

	/**
	 * 定制课课时list
	 */
	private List<CustomClassDTO> attrList;

	/**
	 * 行业下导师集合
	 * 
	 */
	private List<TutorDTO> tutorList;

	/**
	 * 课程介绍
	 */
	private String lessonRcmdStr;

	/**
	 * 课程是否已购买
	 */
	private boolean isBuy = false;

	/**
	 * 订单编码
	 */
	private String orderCode;

	/**
	 * 支付状态
	 */
	private long payStatus;

	private long classNum;

	/**
	 * 支付状态Str
	 */
	private String payStatusStr;

	public String getLessonTypeStr() {
		LessonCustomTypeEnmu lessonCustomTypeEnmu = EnumUtils.getEnum(LessonCustomTypeEnmu.values(), "type",
				lessonType);
		if (lessonCustomTypeEnmu != null) {
			lessonTypeStr = lessonCustomTypeEnmu.name;
		} else {
			lessonTypeStr = "";
		}
		return lessonTypeStr;
	}

	public void setLessonTypeStr(String lessonTypeStr) {
		this.lessonTypeStr = lessonTypeStr;
	}

	public String getLessonPriceStr() throws Exception {
		if (StringUtils.isEmpty(lessonPriceStr)) {
			lessonPriceStr = AmountUtils.changeF2YString(lessonPrice);
		}
		return lessonPriceStr;
	}

	public void setLessonPriceStr(String lessonPriceStr) {
		this.lessonPriceStr = lessonPriceStr;
	}

	public List<CustomClassDTO> getAttrList() {
		return attrList;
	}

	public List<TutorDTO> getTutorList() {
		return tutorList;
	}

	public void setTutorList(List<TutorDTO> tutorList) {
		this.tutorList = tutorList;
	}

	public void setAttrList(List<CustomClassDTO> attrList) {
		this.attrList = attrList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getTutorId() {
		return tutorId;
	}

	public void setTutorId(long tutorId) {
		this.tutorId = tutorId;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLessonRcmdStr() {
		return lessonRcmdStr;
	}

	public void setLessonRcmdStr(String lessonRcmdStr) {
		this.lessonRcmdStr = lessonRcmdStr;
	}

	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public int getBuy() {
		if (isBuy) {
			return 1;
		} else {
			return 2;
		}
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public long getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(long payStatus) {
		this.payStatus = payStatus;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public long getClassNum() {
		return classNum;
	}

	public void setClassNum(long classNum) {
		this.classNum = classNum;
	}

	public String getPayStatusStr() {
		PayStatusEnum payStatusEnum = EnumUtils.getEnum(PayStatusEnum.values(), "status", payStatus);
		if (payStatusEnum != null) {
			payStatusStr = payStatusEnum.name;
		} else {
			payStatusStr = "";
		}
		return payStatusStr;
	}

}
