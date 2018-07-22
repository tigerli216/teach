/*
 * CustomClassDTO.java Created on 2017年9月26日 上午10:48:23
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

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.LessonCustomConstant.CustomClassStatusEnum;
import com.wst.entity.lesson.CustomClass;

/**
 * 对应实体类(CustomClass)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class CustomClassDTO extends CustomClass {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private long identifying;

    /**
     * 学生姓名
     */
    private String userName;

    /**
     * 学生邮箱
     */
    private String userEmail;

    /**
     * 导师姓名
     */
    private String tutorName;

    /**
     * 课程名称
     */
    private String lessonName;
    
    /**
     * 登陆账号（导师）
     */
    private String loginAccount;

    /**
     * 课时状态（1-未上课；2-开始上课；3-课时结束；4-已完结）
     */
    @SuppressWarnings("unused")
    private String classStatusStr;

    /**
     * 真实姓名(导师)
     */
    private String realName;

    /**
     * 学生ID
     */
    private long userId;
    
    /**
     * 导师邮箱
     */
    private String tutorEmail;

    public String getClassStatusStr() {
        CustomClassStatusEnum customClassStatusEnum = EnumUtils.getEnum(CustomClassStatusEnum.values(), "status",
                classStatus);
        return customClassStatusEnum == null ? "" : customClassStatusEnum.name;
    }

    public void setClassStatusStr(String classStatusStr) {
        this.classStatusStr = classStatusStr;
    }

    public long getIdentifying() {
        return identifying;
    }

    public void setIdentifying(long identifying) {
        this.identifying = identifying;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getTutorEmail() {
		return tutorEmail;
	}

	public void setTutorEmail(String tutorEmail) {
		this.tutorEmail = tutorEmail;
	}

	@Override
    public String toString() {
        return super.toString();
    }
}
