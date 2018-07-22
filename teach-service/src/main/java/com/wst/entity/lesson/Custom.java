/**
 * @{#} Custom.java Created on 2017-09-25 21:54:14
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 定制课程表
 * 
 * @author masb 对应表(LESSON_CUSTOM)
 */
public class Custom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 会员ID
     */
    protected long userId;

    /**
     * 课程分类（1-定制课；2-实习课）
     */
    protected long lessonType;

    /**
     * 课程名称
     */
    protected String lessonName;

    /**
     * 行业ID
     */
    protected long industryId;

    /**
     * 所属行业
     */
    protected String industry;

    /**
     * 课程价格
     */
    protected long lessonPrice;

    /**
     * 专属顾问二维码
     */
    protected String adviserCode;

    /**
     * 总课时
     */
    protected long totalClass;

    /**
     * 已完成课时
     */
    protected long finishClass;

    /**
     * 当前课时ID
     */
    protected long currentClass;

    /**
     * 课程介绍（富文本）
     */
    protected Blob lessonRcmd;

    /**
     * 课程开始时间
     */
    protected Date beginTime;

    /**
     * 课程结束时间 课程结束时间
     */
    protected Date endTime;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 创建人
     */
    protected long createOp;

    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 修改人
     */
    protected long modifyOp;

    /**
     * 购买时间
     */
    protected Date buyTime;

    /** 无参构造方法 */
    public Custom() {
    }

    /**
     * 属性get||set方法
     */
    public long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getLessonType() {
        return this.lessonType;
    }

    public void setLessonType(long lessonType) {
        this.lessonType = lessonType;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public long getIndustryId() {
        return this.industryId;
    }

    public void setIndustryId(long industryId) {
        this.industryId = industryId;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public long getLessonPrice() {
        return this.lessonPrice;
    }

    public void setLessonPrice(long lessonPrice) {
        this.lessonPrice = lessonPrice;
    }

    public String getAdviserCode() {
        return adviserCode;
    }

    public void setAdviserCode(String adviserCode) {
        this.adviserCode = adviserCode;
    }

    public long getTotalClass() {
        return this.totalClass;
    }

    public void setTotalClass(long totalClass) {
        this.totalClass = totalClass;
    }

    public long getFinishClass() {
        return this.finishClass;
    }

    public void setFinishClass(long finishClass) {
        this.finishClass = finishClass;
    }

    public long getCurrentClass() {
        return this.currentClass;
    }

    public void setCurrentClass(long currentClass) {
        this.currentClass = currentClass;
    }

    public Blob getLessonRcmd() {
        return this.lessonRcmd;
    }

    public void setLessonRcmd(Blob lessonRcmd) {
        this.lessonRcmd = lessonRcmd;
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

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getCreateOp() {
        return this.createOp;
    }

    public void setCreateOp(long createOp) {
        this.createOp = createOp;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getModifyOp() {
        return this.modifyOp;
    }

    public void setModifyOp(long modifyOp) {
        this.modifyOp = modifyOp;
    }

    public Date getBuyTime() {
        return this.buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

}