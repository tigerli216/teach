/*
 * CustomClassDateDTO.java Created on 2017年9月26日 上午10:44:28
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
import com.wst.constant.CustomLessonConstant.CustomLessonStatusEnmu;
import com.wst.constant.CustomLessonConstant.IsTopEnmu;
import com.wst.entity.operate.CustomLesson;

/**
 * 对应实体类(CustomLesson)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class CustomLessonDTO extends CustomLesson {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否是首页, true 是; false 否
	 */
	private boolean isHomePage;
	
	/**
	 * 课程说明Str
	 */
	private String lessonRcmdStr;

	public boolean isHomePage() {
		return isHomePage;
	}

	public void setHomePage(boolean isHomePage) {
		this.isHomePage = isHomePage;
	}

	public String getStatusStr() {
		CustomLessonStatusEnmu customLessonStatusEnmu = EnumUtils.getEnum(CustomLessonStatusEnmu.values(), "status",
				status);
		return customLessonStatusEnmu != null ? customLessonStatusEnmu.name : "";
	}

	public String getIsTopStr() {
		IsTopEnmu isTopEnmu = EnumUtils.getEnum(IsTopEnmu.values(), "status", isTop);
		return isTopEnmu != null ? isTopEnmu.name : "";
	}
	
	public String getLessonRcmdStr() {
		return lessonRcmdStr;
	}

	public void setLessonRcmdStr(String lessonRcmdStr) {
		this.lessonRcmdStr = lessonRcmdStr;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
