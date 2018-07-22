/**
 * @{#} CustomSettle.java Created on 2017-09-25 21:54:15
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * 定制课程结算记录表
 * 
 * @author masb 对应表(LESSON_CUSTOM_SETTLE)
 */
public class CustomSettle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    protected long recordId;

    /**
     * 导师ID
     */
    protected long tutorId;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 课程名称
     */
    protected String lessonName;

    /**
     * 课时ID
     */
    protected long classId;

    /**
     * 课时名称
     */
    protected String className;

    /**
     * 结算时长（单位：分钟）
     */
    protected long settleDuration;

    /**
     * 结算单价（单位：美分）
     */
    protected long settlePrice;

    /**
     * 创建时间
     */
    protected Date createTime;
    
    /**
     * 结算状态（1-未结算；2-已结算）
     */
    protected long settleStatus;
    /**
     * 付款状态（1-未付款；2-已付款）
     */
    protected long payStatus;
    /**
     * 付款备注
     */
    protected String payMemo;
    /** 无参构造方法 */
    public CustomSettle() {
    }

    /**
     * 属性get||set方法
     */
    public long getRecordId() {
        return this.recordId;
    }

    public long getPayStatus() {
		return this.payStatus;
	}

	public void setPayStatus(long payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayMemo() {
		return this.payMemo;
	}

	public void setPayMemo(String payMemo) {
		this.payMemo = payMemo;
	}

	public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getTutorId() {
        return this.tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getLessonId() {
        return this.lessonId;
    }

    public void setLessonId(long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonName() {
        return this.lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public long getClassId() {
        return this.classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getSettleDuration() {
        return this.settleDuration;
    }

    public void setSettleDuration(long settleDuration) {
        this.settleDuration = settleDuration;
    }

    public long getSettlePrice() {
        return this.settlePrice;
    }

    public void setSettlePrice(long settlePrice) {
        this.settlePrice = settlePrice;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getSettleStatus() {
        return this.settleStatus;
    }

    public void setSettleStatus(long settleStatus) {
        this.settleStatus = settleStatus;
    }

}