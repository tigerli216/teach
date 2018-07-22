/*
 * PracticeLessonDTO.java Created on 2017年9月26日 上午10:44:57
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
package com.wst.service.operate.dto;

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.PracticeLessonConstant.PracticeLessonCompanyCountryEnmu;
import com.wst.constant.PracticeLessonConstant.PracticeLessonIsTopEnum;
import com.wst.constant.PracticeLessonConstant.PracticeLessonStatusEnmu;
import com.wst.entity.operate.PracticeLesson;

/**
 * 对应实体类(PracticeLesson)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class PracticeLessonDTO extends PracticeLesson {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否是首页, true 是; false 否
	 */
	private boolean isHomePage;

	/**
	 * 公司介绍Str
	 */
	private String companyRcmdStr;

	/**
	 * 职位介绍Str
	 */
	private String practiceRcmdStr;

	public boolean isHomePage() {
		return isHomePage;
	}

	public void setHomePage(boolean isHomePage) {
		this.isHomePage = isHomePage;
	}

	public String getStatusStr() {
		PracticeLessonStatusEnmu practiceLessonStatusEnmu = EnumUtils.getEnum(PracticeLessonStatusEnmu.values(),
				"status", status);
		return practiceLessonStatusEnmu != null ? practiceLessonStatusEnmu.name : "";
	}

	public String getCompanyCountryStr() {
		PracticeLessonCompanyCountryEnmu practiceLessonCompanyCountryEnmu = EnumUtils
				.getEnum(PracticeLessonCompanyCountryEnmu.values(), "type", companyCountry);
		return practiceLessonCompanyCountryEnmu != null ? practiceLessonCompanyCountryEnmu.name : "";
	}

	public String getIsTopStr() {
		PracticeLessonIsTopEnum practiceLessonIsTopEnum = EnumUtils.getEnum(PracticeLessonIsTopEnum.values(), "status",
				isTop);
		return practiceLessonIsTopEnum != null ? practiceLessonIsTopEnum.name : "";
	}

	public String getCompanyRcmdStr() {
		return companyRcmdStr;
	}

	public void setCompanyRcmdStr(String companyRcmdStr) {
		this.companyRcmdStr = companyRcmdStr;
	}

	public String getPracticeRcmdStr() {
		return practiceRcmdStr;
	}

	public void setPracticeRcmdStr(String practiceRcmdStr) {
		this.practiceRcmdStr = practiceRcmdStr;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
