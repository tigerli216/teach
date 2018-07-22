/**
 * @{#} CustomClassDate.java Created on 2017-09-25 21:54:15
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * 定制课程课时约课记录表
 * 
 * @author masb 对应表(LESSON_CUSTOM_CLASS_DATE)
 */
public class CustomClassDate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    protected long recordId;

    /**
     * 课时ID
     */
    protected long classId;

    /**
     * 约课时间
     */
    protected Date dateTime;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 约课用户类型（1-学生；2-导师）
     */
    protected long dateMemType;

    /**
     * 确认状态（1-未确认；2-已确认）
     */
    protected long confirmStatus;

    /**
     * 取消时间
     */
    protected Date cancelTime;

    /**
     * 取消会员ID
     */
    protected long cancelUserId;

    /** 无参构造方法 */
    public CustomClassDate() {
    }

    /**
     * 属性get||set方法
     */
    public long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getClassId() {
        return this.classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public Date getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getDateMemType() {
        return this.dateMemType;
    }

    public void setDateMemType(long dateMemType) {
        this.dateMemType = dateMemType;
    }

    public long getConfirmStatus() {
        return this.confirmStatus;
    }

    public void setConfirmStatus(long confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public Date getCancelTime() {
        return this.cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public long getCancelUserId() {
        return this.cancelUserId;
    }

    public void setCancelUserId(long cancelUserId) {
        this.cancelUserId = cancelUserId;
    }

}