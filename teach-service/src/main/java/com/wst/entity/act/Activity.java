/**
 * @{#} Activity.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.act;

import java.io.Serializable;
import java.util.Date;

/**
 * 运营活动表
 * 
 * @author masb 对应表(OPERATE_ACTIVITY)
 */
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    protected long actId;

    /**
     * 活动名称
     */
    protected String actName;

    /**
     * 活动编码（唯一）
     */
    protected String actCode;

    /**
     * 活动说明
     */
    protected String actExplain;

    /**
     * 活动类型（1-提取码活动；）
     */
    protected long actType;

    /**
     * 扩展配置
     */
    protected String expandConfig;

    /**
     * 开始时间
     */
    protected Date beginTime;

    /**
     * 结束时间
     */
    protected Date endTime;

    /**
     * 备注
     */
    protected String memo;

    /**
     * 状态 1-开启 2-关闭
     */
    protected long status;

    /** 无参构造方法 */
    public Activity() {
    }

    /** 属性get||set方法 */
    public long getActId() {
        return this.actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public String getActName() {
        return this.actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActCode() {
        return this.actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getActExplain() {
        return this.actExplain;
    }

    public void setActExplain(String actExplain) {
        this.actExplain = actExplain;
    }

    public long getActType() {
        return this.actType;
    }

    public void setActType(long actType) {
        this.actType = actType;
    }

    public String getExpandConfig() {
        return this.expandConfig;
    }

    public void setExpandConfig(String expandConfig) {
        this.expandConfig = expandConfig;
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

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}