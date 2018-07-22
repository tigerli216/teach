/*
 * CustomSettleDTO.java Created on 2017年9月26日 上午10:49:27
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

import java.util.Date;

import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.EnumUtils;
import com.wst.constant.LessonCustomConstant.SettlePayStatusEnum;
import com.wst.constant.LessonCustomConstant.SettleStatusEnum;
import com.wst.entity.lesson.CustomSettle;

/**
 * 对应实体类(CustomSettle)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class CustomSettleDTO extends CustomSettle {

	private static final long serialVersionUID = 1L;

	/**
	 * 头像图片路径
	 */
	private String portraitImgUrl;

	/**
	 * 基础薪资
	 */
	private Long baseSalary;

	/**
	 * 已结算薪资
	 */
	private Long haveSettleSalary;

	/**
	 * 未结算薪资
	 */
	private Long notSettleSalary;

	/**
	 * 开始时间
	 */
	private Date beginTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 导师名称
	 */
	private String realName;

	/**
	 * 结算单价(美元)
	 */
	private String settlePriceStr;

	/**
	 * 登录账号
	 */
	private String loginAccount;

	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 结算状态
	 */
	private String settleStatusStr;

	/**
	 * 支付状态；1-未付款；
	 */
	private String payStatusStr;

	public String getPortraitImgUrl() {
		return portraitImgUrl;
	}

	public void setPortraitImgUrl(String portraitImgUrl) {
		this.portraitImgUrl = portraitImgUrl;
	}

	public Long getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(Long baseSalary) {
		this.baseSalary = baseSalary;
	}

	public Long getHaveSettleSalary() {
		return haveSettleSalary;
	}

	public void setHaveSettleSalary(Long haveSettleSalary) {
		this.haveSettleSalary = haveSettleSalary;
	}

	public Long getNotSettleSalary() {
		return notSettleSalary;
	}

	public void setNotSettleSalary(Long notSettleSalary) {
		this.notSettleSalary = notSettleSalary;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSettlePriceStr() throws Exception {
		if (settlePrice > 0) {
			settlePriceStr = AmountUtils.changeF2YString(settlePrice);
		}
		return settlePriceStr;
	}

	public void setSettlePriceStr(String settlePriceStr) {
		this.settlePriceStr = settlePriceStr;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPayStatusStr() {
		SettlePayStatusEnum settlePayStatusEnum = EnumUtils.getEnum(SettlePayStatusEnum.values(), "status", payStatus);
		if(settlePayStatusEnum != null){
			payStatusStr = settlePayStatusEnum.name;
		}
		return payStatusStr;
	}

	public void setPayStatusStr(String payStatusStr) {
		this.payStatusStr = payStatusStr;
	}

	public String getSettleStatusStr() {
		SettleStatusEnum settleStatusEnum = EnumUtils.getEnum(SettleStatusEnum.values(), "status", settleStatus);
		if(settleStatusEnum != null){
			settleStatusStr = settleStatusEnum.name;
		}
		return settleStatusStr;
	}

	public void setSettleStatusStr(String settleStatusStr) {
		this.settleStatusStr = settleStatusStr;
	}

}
