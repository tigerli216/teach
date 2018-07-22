/*
 * PublicClassDTO.java Created on 2017年9月26日 上午10:51:25
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
import com.wst.constant.LessonPublicConstant.IsFreeStatusEnum;
import com.wst.constant.LessonPublicConstant.PublicClassStatusEnum;
import com.wst.entity.lesson.PublicClass;

/**
 * 对应实体类(PublicClass)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class PublicClassDTO extends PublicClass {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    private long identifying;

    /**
     * 课时状态（1-未开始；2-进行中；3-已完结）
     */
    @SuppressWarnings("unused")
    private String classStatusStr;

    /**
     * 腾讯云的文件ID
     */
    private String fileId;

    /**
     * 学生ID
     */
    private long userId;

    /**
     * 导师ID
     */
    private long tutorId;
    
    /**
     * 课程名称
     */
    private String lessonName;

    /**
     * 枚举是否免费
     * 
     * @return
     */
    public String getIsFreeStr() {
        IsFreeStatusEnum isFreeStatusEnum = EnumUtils.getEnum(IsFreeStatusEnum.values(), "status", isFree);
        return isFreeStatusEnum == null ? "" : isFreeStatusEnum.name;
    }

    public String getClassStatusStr() {
        PublicClassStatusEnum publicClassStatusEnum = EnumUtils.getEnum(PublicClassStatusEnum.values(), "status",
                classStatus);
        return publicClassStatusEnum == null ? "" : publicClassStatusEnum.name;
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

    @Override
    public String toString() {
        return super.toString();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

}
