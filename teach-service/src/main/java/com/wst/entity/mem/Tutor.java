/**
 * @{#} Tutor.java Created on 2017-09-25 21:54:16
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.mem;

import java.io.Serializable;
import java.util.Date;

/**
 * 导师表
 * 
 * @author masb 对应表(MEM_TUTOR)
 */
public class Tutor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 导师ID
     */
    protected long tutorId;

    /**
     * 登录账号
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
     * 昵称
     */
    protected String nickName;

    /**
     * 头像图片
     */
    protected String portraitImgUrl;

    /**
     * 导师类型（1-普通）
     */
    protected long tutorType;

    /**
     * 领英链接
     */
    protected String linkedinUri;

    /**
     * 毕业院校
     */
    protected String graduateSchool;

    /**
     * 最近一份工作
     */
    protected String newlyJob;

    /**
     * 所在行业ID
     */
    protected long industryId;

    /**
     * 行业名称
     */
    protected String industryName;

    /**
     * 导师备注
     */
    protected String tutorRemark;

    /**
     * 真实姓名
     */
    protected String realName;

    /**
     * 职位
     */
    protected String position;

    /**
     * 收款方式（1-银行卡）
     */
    protected long receiveType;

    /**
     * 收款账户（开户行|卡号）
     */
    protected String receiveAccount;

    /**
     * 性别（1-男；2-女）
     */
    protected long sex;

    /**
     * 国家
     */
    protected String country;

    /**
     * 导师薪资
     */
    protected long tutorSalary;

    /**
     * 合同路径
     */
    protected String contractUrl;

    /**
     * 导师战绩
     */
    protected String tutorRecord;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 审核状态（1-未审核；2-审核通过；3-审核拒绝）
     */
    protected long examineStatus;

    /**
     * 审核时间
     */
    protected Date examineTime;

    /**
     * 审核人
     */
    protected long examineOp;

    /**
     * 有效状态（1-有效；2-无效）
     */
    protected long validStatus;

    /** 无参构造方法 */
    public Tutor() {
    }

    /**
     * 属性get||set方法
     */
    public long getTutorId() {
        return this.tutorId;
    }

    public void setTutorId(long tutorId) {
        this.tutorId = tutorId;
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

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPortraitImgUrl() {
        return this.portraitImgUrl;
    }

    public void setPortraitImgUrl(String portraitImgUrl) {
        this.portraitImgUrl = portraitImgUrl;
    }

    public long getTutorType() {
        return this.tutorType;
    }

    public void setTutorType(long tutorType) {
        this.tutorType = tutorType;
    }

    public String getLinkedinUri() {
        return this.linkedinUri;
    }

    public void setLinkedinUri(String linkedinUri) {
        this.linkedinUri = linkedinUri;
    }

    public String getGraduateSchool() {
        return this.graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getNewlyJob() {
        return this.newlyJob;
    }

    public void setNewlyJob(String newlyJob) {
        this.newlyJob = newlyJob;
    }

    public long getIndustryId() {
        return industryId;
    }

    public void setIndustryId(long industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getTutorRemark() {
        return this.tutorRemark;
    }

    public void setTutorRemark(String tutorRemark) {
        this.tutorRemark = tutorRemark;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getReceiveType() {
        return this.receiveType;
    }

    public void setReceiveType(long receiveType) {
        this.receiveType = receiveType;
    }

    public String getReceiveAccount() {
        return this.receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
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

    public long getTutorSalary() {
        return this.tutorSalary;
    }

    public void setTutorSalary(long tutorSalary) {
        this.tutorSalary = tutorSalary;
    }

    public String getContractUrl() {
        return this.contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getTutorRecord() {
        return this.tutorRecord;
    }

    public void setTutorRecord(String tutorRecord) {
        this.tutorRecord = tutorRecord;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getExamineStatus() {
        return this.examineStatus;
    }

    public void setExamineStatus(long examineStatus) {
        this.examineStatus = examineStatus;
    }

    public Date getExamineTime() {
        return this.examineTime;
    }

    public void setExamineTime(Date examineTime) {
        this.examineTime = examineTime;
    }

    public long getExamineOp() {
        return this.examineOp;
    }

    public void setExamineOp(long examineOp) {
        this.examineOp = examineOp;
    }

    public long getValidStatus() {
        return this.validStatus;
    }

    public void setValidStatus(long validStatus) {
        this.validStatus = validStatus;
    }

}