/**
 * @{#} TutorSalarySendRecord.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 导师薪资发放记录表
 * 
 * @author masb 对应表(MEM_TUTOR_SALARY_SEND_RECORD)
 */
public class TutorSalarySendRecord implements Serializable {

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
     * 发放金额
     */
    protected long sendAmount;

    /**
     * 发放时间
     */
    protected Date sendTime;

    /**
     * 发放人
     */
    protected long sendOp;

    /**
     * 发放备注
     */
    protected String sendRemark;

    /** 无参构造方法 */
    public TutorSalarySendRecord() {
    }

    /**
     * 属性get||set方法
     * 
     * @return
     */
    public long getRecordId() {
        return this.recordId;
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

    public long getSendAmount() {
        return this.sendAmount;
    }

    public void setSendAmount(long sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public long getSendOp() {
        return this.sendOp;
    }

    public void setSendOp(long sendOp) {
        this.sendOp = sendOp;
    }

    public String getSendRemark() {
        return this.sendRemark;
    }

    public void setSendRemark(String sendRemark) {
        this.sendRemark = sendRemark;
    }

}