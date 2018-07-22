/*
 * ActivityExtractDTO.java Created on 2017年9月26日 上午10:41:28
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
package com.wst.service.act.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.wst.entity.act.ActivityExtract;

/**
 * 对应实体类(ActivityExtract)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class ActivityExtractDTO extends ActivityExtract {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动提取码数量
	 */
	private int activityExtractNum;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 用户账号
	 */
	private String loginAccount;

	/**
	 * 课程名称
	 */
	private String lessonName;

	/**
	 * 有效时间
	 */
	private String validTimeStr;

	/**
	 * 活动名称
	 */
	private String actName;

	/**
	 * 是否使用： 1.未使用; 2.已使用
	 */
	private long useStatus;

	/**
	 * 开始时间Str
	 */
	private String createTimeStr;

	/**
	 * 使用时间Str
	 */
	private String useTimeStr;

	public String getValidTimeStr() {
		if (validTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			validTimeStr = sdf.format(validTime);
		}
		return validTimeStr;
	}

	public void setValidTimeStr(String validTimeStr) {
		this.validTimeStr = validTimeStr;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getActivityExtractNum() {
		return activityExtractNum;
	}

	public void setActivityExtractNum(int activityExtractNum) {
		this.activityExtractNum = activityExtractNum;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public long getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(long useStatus) {
		this.useStatus = useStatus;
	}

	public String getCreateTimeStr() {
		if (createTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createTimeStr = sdf.format(createTime);
		}
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getUseTimeStr() {
		if (useTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			useTimeStr = sdf.format(useTime);
		}
		return useTimeStr;
	}

	public void setUseTimeStr(String useTimeStr) {
		this.useTimeStr = useTimeStr;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
