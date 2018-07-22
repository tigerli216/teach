/*
 * LessonFeedbackDTO.java Created on 2017年10月10日 下午5:27:10
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

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.LessonFeedbackConstant.LessonTypeEnmu;
import com.wst.entity.mem.LessonFeedback;

/**
 * 对应实体类(LessonFeedback)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class LessonFeedbackDTO extends LessonFeedback {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户账号
	 */
	private String loginAccount;
	private Date beginTime;
	private Date endTime;
	
	public String getLessonTypeStr() {
		LessonTypeEnmu lessonTypeEnmu = EnumUtils.getEnum(LessonTypeEnmu.values(), "type", lessonType);
		return lessonTypeEnmu == null ? "" : lessonTypeEnmu.name;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
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

	@Override
	public String toString() {
		return super.toString();
	}
}
