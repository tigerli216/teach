/**
 * @{#} User.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员主表
 * 
 * @author masb 对应表(MEM_USER)
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主表主键
     */
    protected long userId;

    /**
     * 登陆账号
     */
    protected String loginAccount;

    /**
     * 登录密码
     */
    protected String password;

    /**
     * 绑定手机
     */
    protected String bindMobile;

    /**
     * 邮箱
     */
    protected String email;

    /**
     * 注册方式,1:邮箱注册,2:手机注册 3：第三方账号
     */
    protected long regType;

    /**
     * 用户类型(1:个人账户,2:企业账户)
     */
    protected long userType;

    /**
     * 用户类型(1:个人账户,2:企业账户)
     */
    protected long subUserType;

    /**
     * 用户级别 1注册；2-VIP
     */
    protected long userLevel;

    /**
     * 密码安全级别,注：存整数
     */
    protected long pwdSecurityLevel;

    /**
     * 第三方登陆来源（1-微信）
     */
    protected long deputyLoginSource;

    /**
     * 第三方登陆账号
     */
    protected String deputyLoginAccount;

    /**
     * 头像图片存储路径URL
     */
    protected String portraitImgUrl;

    /**
     * 昵称
     */
    protected String nickName;

    /**
     * 真实姓名
     */
    protected String realName;

    /**
     * 性别（1-男；2-女）
     */
    protected long sex;

    /**
     * 国家
     */
    protected String country;

    /**
     * 学校
     */
    protected String school;

    /**
     * 年级
     */
    protected long grade;

    /**
     * 毕业时间
     */
    protected Date finishTime;

    /**
     * 简历
     */
    protected String resume;

    /**
     * offer
     */
    protected String offer;

    /**
     * 注册时间
     */
    protected Date regTime;

    /**
     * 推荐码:十位,userId，长度不够补零
     */
    protected String recommendCode;

    /**
     * 支付密码
     */
    protected String payPassword;

    /**
     * 状态（1-有效；2-失效）
     */
    protected long status;

    /** 无参构造方法 */
    public User() {
    }

    /**
     * 属性get||set方法
     */
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLoginAccount() {
        if (loginAccount != null) {
            return this.loginAccount.toLowerCase();
        }
        return this.loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBindMobile() {
        return this.bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getRegType() {
        return this.regType;
    }

    public void setRegType(long regType) {
        this.regType = regType;
    }

    public long getUserType() {
        return this.userType;
    }

    public void setUserType(long userType) {
        this.userType = userType;
    }

    public long getSubUserType() {
        return this.subUserType;
    }

    public void setSubUserType(long subUserType) {
        this.subUserType = subUserType;
    }

    public long getUserLevel() {
        return this.userLevel;
    }

    public void setUserLevel(long userLevel) {
        this.userLevel = userLevel;
    }

    public long getPwdSecurityLevel() {
        return this.pwdSecurityLevel;
    }

    public void setPwdSecurityLevel(long pwdSecurityLevel) {
        this.pwdSecurityLevel = pwdSecurityLevel;
    }

    public long getDeputyLoginSource() {
        return this.deputyLoginSource;
    }

    public void setDeputyLoginSource(long deputyLoginSource) {
        this.deputyLoginSource = deputyLoginSource;
    }

    public String getDeputyLoginAccount() {
        return this.deputyLoginAccount;
    }

    public void setDeputyLoginAccount(String deputyLoginAccount) {
        this.deputyLoginAccount = deputyLoginAccount;
    }

    public String getPortraitImgUrl() {
        return this.portraitImgUrl;
    }

    public void setPortraitImgUrl(String portraitImgUrl) {
        this.portraitImgUrl = portraitImgUrl;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getSex() {
        return this.sex;
    }

    public void setSex(long sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSchool() {
        return this.school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public long getGrade() {
        return this.grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }

    public Date getFinishTime() {
        return this.finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getResume() {
        return this.resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getOffer() {
        return this.offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public Date getRegTime() {
        return this.regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public String getRecommendCode() {
        return this.recommendCode;
    }

    public void setRecommendCode(String recommendCode) {
        this.recommendCode = recommendCode;
    }

    public String getPayPassword() {
        return this.payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}