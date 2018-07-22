/**
 * @{#} CustomClass.java Created on 2017-09-25 21:54:15
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * 定制课程课时表
 * 
 * @author masb 对应表(LESSON_CUSTOM_CLASS)
 */
public class CustomClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课时ID
     */
    protected long classId;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 课时名称
     */
    protected String className;

    /**
     * 课时介绍
     */
    protected String classRcmd;

    /**
     * 导师ID
     */
    protected long tutorId;

    /**
     * 备课文件
     */
    protected String readyFile;

    /**
     * 课程小结
     */
    protected String classSummary;

    /**
     * 计划时长
     */
    protected long planDuration;

    /**
     * 实际时长
     */
    protected long realDuration;

    /**
     * 约课时间
     */
    protected Date dateTime;

    /**
     * 开始时间
     */
    protected Date beginTime;

    /**
     * 结束时间
     */
    protected Date endTime;

    /**
     * 确认时间
     */
    protected Date confirmTime;

    /**
     * 房间ID
     */
    protected long roomId;

    /**
     * 视频地址
     */
    protected String movieUrl;

    /**
     * 课时状态（1-未上课；2-开始上课；3-课时结束；4-已完结）
     */
    protected long classStatus;

    /**
     * 计划上课时间
     */
    protected Date planTime;

    /** 无参构造方法 */
    public CustomClass() {
    }

    /**
     * 属性get||set方法
     */
    public long getClassId() {
        return this.classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRcmd() {
        return classRcmd;
    }

    public void setClassRcmd(String classRcmd) {
        this.classRcmd = classRcmd;
    }

    public long getTutorId() {
        return this.tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public String getReadyFile() {
        return this.readyFile;
    }

    public void setReadyFile(String readyFile) {
        this.readyFile = readyFile;
    }

    public String getClassSummary() {
        return classSummary;
    }

    public void setClassSummary(String classSummary) {
        this.classSummary = classSummary;
    }

    public long getPlanDuration() {
        return this.planDuration;
    }

    public void setPlanDuration(long planDuration) {
        this.planDuration = planDuration;
    }

    public long getRealDuration() {
        return this.realDuration;
    }

    public void setRealDuration(long realDuration) {
        this.realDuration = realDuration;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getConfirmTime() {
        return this.confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getMovieUrl() {
        return this.movieUrl;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public long getClassStatus() {
        return this.classStatus;
    }

    public void setClassStatus(long classStatus) {
        this.classStatus = classStatus;
    }

    public Date getPlanTime() {
        return planTime;
    }

    public void setPlanTime(Date planTime) {
        this.planTime = planTime;
    }

}