/**
 * @{#} OnlineUsersHis.java Created on 2016-09-22 15:10:20
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线用户记录历史
 * 
 * @author masb 对应表(SSO_ONLINE_USERS_HIS)
 */
public class OnlineUsersHis implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史ID
     */
    protected long hisId;

    /**
     * 在线标识
     */
    protected long onlineId;

    /**
     * 在线用户
     */
    protected long opId;

    /**
     * 分配Session
     */
    protected String sessionId;

    /**
     * 登陆时间
     */
    protected Date loginTime;

    /**
     * 退出时间
     */
    protected Date quitTime;

    /**
     * 0 : 在线; 1 : 正常退出 ; 2 : sesstion失效退出 3 : 强制退出
     */
    protected long status;

    /**
     * 登陆IP地址
     */
    protected String loginIp;

    /**
     * 执行操作员
     */
    protected long executionOpId;

    /** 无参构造方法 */
    public OnlineUsersHis() {
    }

    /**
     * 属性get||set方法
     */
    public long getHisId() {
        return this.hisId;
    }

    public void setHisId(long hisId) {
        this.hisId = hisId;
    }

    public long getOnlineId() {
        return this.onlineId;
    }

    public void setOnlineId(long onlineId) {
        this.onlineId = onlineId;
    }

    public long getOpId() {
        return this.opId;
    }

    public void setOpId(long opId) {
        this.opId = opId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLoginTime() {
        return this.loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getQuitTime() {
        return this.quitTime;
    }

    public void setQuitTime(Date quitTime) {
        this.quitTime = quitTime;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public long getExecutionOpId() {
        return this.executionOpId;
    }

    public void setExecutionOpId(long executionOpId) {
        this.executionOpId = executionOpId;
    }

}