/**
 * @{#} TutorSalaryRecord.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 导师薪资调整记录表
 * 
 * @author masb 对应表(MEM_TUTOR_SALARY_RECORD)
 */
public class TutorSalaryRecord implements Serializable {

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
     * 调整前薪资
     */
    protected long beforSalary;

    /**
     * 调整后薪资
     */
    protected long endSalary;

    /**
     * 调整说明
     */
    protected String changeExplain;

    /**
     * 调整时间
     */
    protected Date changeTime;

    /**
     * 调整人
     */
    protected long changeOp;

    /** 无参构造方法 */
    public TutorSalaryRecord() {
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

    public long getTutorId() {
        return this.tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public long getBeforSalary() {
        return this.beforSalary;
    }

    public void setBeforSalary(long beforSalary) {
        this.beforSalary = beforSalary;
    }

    public long getEndSalary() {
        return this.endSalary;
    }

    public void setEndSalary(long endSalary) {
        this.endSalary = endSalary;
    }

    public String getChangeExplain() {
        return this.changeExplain;
    }

    public void setChangeExplain(String changeExplain) {
        this.changeExplain = changeExplain;
    }

    public Date getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public long getChangeOp() {
        return this.changeOp;
    }

    public void setChangeOp(long changeOp) {
        this.changeOp = changeOp;
    }

}