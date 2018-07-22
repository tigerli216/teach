/**
 * @{#} CustomLesson.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.operate;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 运营定制辅导课程表
 * 
 * @author masb 对应表(OPERATE_CUSTOM_LESSON)
 */
public class CustomLesson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    protected long lessonId;

    /**
     * 课程名称
     */
    protected String lessonName;

    /**
     * 课程周期
     */
    protected String lessonCycle;

    /**
     * 课时数
     */
    protected String classNum;

    /**
     * 课程简介
     */
    protected String lessonAbstract;

    /**
     * 课程说明（富文本）
     */
    protected Blob lessonRcmd;

    /**
     * 目标客户
     */
    protected String targetMem;

    /**
     * 课程图片
     */
    protected String lessonPic;

    /**
     * 是否置顶
     */
    protected long isTop;

    /**
     * 权重值
     */
    protected long weight;

    /**
     * 创建时间
     */
    protected Date createTime;
    
    /**
     * 结束时间
     */
    protected Date endTime;
    
    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 状态（1-有效；2-无效）
     */
    protected long status;

    /** 无参构造方法 */
    public CustomLesson() {
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

    public String getLessonName() {
        return this.lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonCycle() {
        return this.lessonCycle;
    }

    public void setLessonCycle(String lessonCycle) {
        this.lessonCycle = lessonCycle;
    }

    public String getClassNum() {
        return this.classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getLessonAbstract() {
        return this.lessonAbstract;
    }

    public void setLessonAbstract(String lessonAbstract) {
        this.lessonAbstract = lessonAbstract;
    }

    public Blob getLessonRcmd() {
        return this.lessonRcmd;
    }

    public void setLessonRcmd(Blob lessonRcmd) {
        this.lessonRcmd = lessonRcmd;
    }

    public String getTargetMem() {
        return this.targetMem;
    }

    public void setTargetMem(String targetMem) {
        this.targetMem = targetMem;
    }

    public String getLessonPic() {
        return this.lessonPic;
    }

    public void setLessonPic(String lessonPic) {
        this.lessonPic = lessonPic;
    }

    public long getIsTop() {
        return this.isTop;
    }

    public void setIsTop(long isTop) {
        this.isTop = isTop;
    }

    public long getWeight() {
        return this.weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}