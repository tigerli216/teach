/*
 * LessonFeedback.java Created on 2017年10月10日 下午5:21:02
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
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户课程反馈表 对应表(mem_lesson_feedback)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class LessonFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 反馈ID
     */
    protected long fbId;

    /**
     * 会员ID
     */
    protected long userId;

    /**
     * 课程类别（1-定制课；2-网课）
     */
    protected long lessonType;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 课程名称
     */
    protected String lessonName;

    /**
     * 反馈内容
     */
    protected String content;

    /**
     * 创建时间
     */
    protected Date createTime;
    
    public long getFbId() {
        return fbId;
    }

    public void setFbId(long fbId) {
        this.fbId = fbId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLessonType() {
        return lessonType;
    }

    public void setLessonType(long lessonType) {
        this.lessonType = lessonType;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}
