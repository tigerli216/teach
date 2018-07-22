/**
 * @{#} ActivityExtract.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.act;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动提取码配置表
 * 
 * @author masb 对应表(OPERATE_ACTIVITY_EXTRACT)
 */
public class ActivityExtract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    protected long configId;

    /**
     * 活动ID
     */
    protected long actId;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 课时IDS
     */
    protected String classIds;

    /**
     * 提取码
     */
    protected String extractCode;

    /**
     * 会员ID
     */
    protected long userId;

    /**
     * 用户IP地址
     */
    protected String userIp;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 使用时间
     */
    protected Date useTime;

    /**
     * 有效期
     */
    protected Date validTime;

    /** 无参构造方法 */
    public ActivityExtract() {
    }

    /**
     * 属性get||set方法
     */
    public long getConfigId() {
        return this.configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public long getActId() {
        return this.actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getClassIds() {
        return this.classIds;
    }

    public void setClassIds(String classIds) {
        this.classIds = classIds;
    }

    public String getExtractCode() {
        return this.extractCode;
    }

    public void setExtractCode(String extractCode) {
        this.extractCode = extractCode;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUseTime() {
        return this.useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public Date getValidTime() {
        return this.validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

}