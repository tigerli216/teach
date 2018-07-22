/*
 * CustomTutorClass.java Created on 2017年10月13日 下午1:46:49
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
package com.wst.entity.lesson;

import java.io.Serializable;

/**
 * 定制课程导师课时计划
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public class CustomTutorClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划ID
     */
    private long planId;

    /**
     * 课程ID
     */
    private long lessonId;

    /**
     * 导师ID
     */
    private long tutorId;

    /**
     * 总课时（单位：分钟）
     */
    private long totalClass;

    /**
     * 已完成课时（单位：分钟）
     */
    private long finishClass;

    /**
     * 属性get||set方法
     */
    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public long getLessonId() {
        return lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getTotalClass() {
        return totalClass;
    }

    public void setTotalClass(long totalClass) {
        this.totalClass = totalClass;
    }

    public long getFinishClass() {
        return finishClass;
    }

    public void setFinishClass(long finishClass) {
        this.finishClass = finishClass;
    }

}
