/**
 * @{#} TutorMssage.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员消息表
 * 
 * @author masb 对应表(MEM_TUTOR_MSSAGE)
 */
public class TutorMssage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息表主键Id
     */
    protected long msgId;

    /**
     * 操作员ID
     */
    protected long opId;

    /**
     * 发送人
     */
    protected long sendUser;

    /**
     * 消息类型 1：系统消息；2-课程消息；3-订单消息
     */
    protected long msgType;

    /**
     * 如订单编码，如无关联业务，则为空
     */
    protected String msgSource;

    /**
     * 业务类型：
     */
    protected long busiType;

    /**
     * 导师ID
     */
    protected long tutorId;

    /**
     * 消息标题
     */
    protected String msgTitle;

    /**
     * 消息内容
     */
    protected String msgContent;

    /**
     * 消息状态,1有效、2无效
     */
    protected long msgStatus;

    /**
     * 发送时间
     */
    protected Date sendTime;

    /**
     * 开始日期
     */
    protected Date beginTime;

    /**
     * 失效日期（可为null）
     */
    protected Date endTime;

    /** 无参构造方法 */
    public TutorMssage() {
    }

    /**
     * 属性get||set方法
     */
    public long getMsgId() {
        return this.msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getOpId() {
        return this.opId;
    }

    public void setOpId(long opId) {
        this.opId = opId;
    }

    public long getSendUser() {
        return this.sendUser;
    }

    public void setSendUser(long sendUser) {
        this.sendUser = sendUser;
    }

    public long getMsgType() {
        return this.msgType;
    }

    public void setMsgType(long msgType) {
        this.msgType = msgType;
    }

    public String getMsgSource() {
        return this.msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public long getBusiType() {
        return this.busiType;
    }

    public void setBusiType(long busiType) {
        this.busiType = busiType;
    }

    public long getTutorId() {
        return tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
    }

    public String getMsgTitle() {
        return this.msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getMsgStatus() {
        return this.msgStatus;
    }

    public void setMsgStatus(long msgStatus) {
        this.msgStatus = msgStatus;
    }

    public Date getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

}