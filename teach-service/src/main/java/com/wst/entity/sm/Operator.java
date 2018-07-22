/**
 * @{#} Operator.java Created on 2016-09-28 13:42:34
 *
 * Copyright (c) 2016 by HIWI software.
 */
package com.wst.entity.sm;

import java.io.Serializable;
import java.util.Date;

/**
 * SSO系统账号信息
 * 
 * @author masb 对应表(SSO_OPERATOR)
 */
public class Operator implements Serializable {

    private static final long serialVersionUID = 1L;

    protected long opId;

    /**
     * 账号登陆名
     */
    protected String opLoginName;

    /** 是否主账户 0:主账户;1:从账户 */
    protected long isOwner;

    /** 从账号关联主账号ID */
    protected long ownOpId;

    /** 账号登陆密码 */
    protected String password;

    /** 账号归属组织 */
    protected long orgId;

    /** 账号状态 0:有效;1:无效 */
    protected long status;

    /** 账号锁定状态 0:解锁;1:锁定 */
    protected long lockStatus;

    protected String opName;

    protected String email;

    protected String qq;

    protected String address;

    protected String mobilePhone;

    protected String telePhone;

    protected String postcode;

    protected String jobNumber;

    protected long identityType;

    protected String identityNo;

    protected long age;

    protected String photo;

    protected String memo;

    protected long createOpId;

    protected Date createTime;

    protected long modifyOpId;

    protected Date modifyTime;

    /** 账号失效时间 */
    protected Date overdueTime;

    /** 上次文件批次 */
    protected long batchId;

    /** 是否为系统管理人员0:不是;1:是 */
    protected long isAdmin;

    /** 归属供货商 */
    protected long supplierId;

    /** 是否为供货商管理员0:不是,1:是 */
    protected long isSupplierAdmin;

    /** 下次重置密码时间 */
    protected Date nextResetPwdTime;

    /** 密码重置原因 */
    protected String resetPwdNotes;

    /** 无参构造方法 */
    public Operator() {
    }

    /**
     * 属性get||set方法
     */
    public long getOpId() {
        return this.opId;
    }

    public void setOpId(long opId) {
        this.opId = opId;
    }

    public String getOpLoginName() {
        return this.opLoginName;
    }

    public void setOpLoginName(String opLoginName) {
        this.opLoginName = opLoginName;
    }

    public long getIsOwner() {
        return this.isOwner;
    }

    public void setIsOwner(long isOwner) {
        this.isOwner = isOwner;
    }

    public long getOwnOpId() {
        return this.ownOpId;
    }

    public void setOwnOpId(long ownOpId) {
        this.ownOpId = ownOpId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getOrgId() {
        return this.orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public long getLockStatus() {
        return this.lockStatus;
    }

    public void setLockStatus(long lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getOpName() {
        return this.opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getTelePhone() {
        return this.telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getJobNumber() {
        return this.jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public long getIdentityType() {
        return this.identityType;
    }

    public void setIdentityType(long identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNo() {
        return this.identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public long getAge() {
        return this.age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getCreateOpId() {
        return this.createOpId;
    }

    public void setCreateOpId(long createOpId) {
        this.createOpId = createOpId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public long getModifyOpId() {
        return this.modifyOpId;
    }

    public void setModifyOpId(long modifyOpId) {
        this.modifyOpId = modifyOpId;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getOverdueTime() {
        return this.overdueTime;
    }

    public void setOverdueTime(Date overdueTime) {
        this.overdueTime = overdueTime;
    }

    public long getBatchId() {
        return this.batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public long getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(long isAdmin) {
        this.isAdmin = isAdmin;
    }

    public long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getIsSupplierAdmin() {
        return this.isSupplierAdmin;
    }

    public void setIsSupplierAdmin(long isSupplierAdmin) {
        this.isSupplierAdmin = isSupplierAdmin;
    }

    public Date getNextResetPwdTime() {
        return this.nextResetPwdTime;
    }

    public void setNextResetPwdTime(Date nextResetPwdTime) {
        this.nextResetPwdTime = nextResetPwdTime;
    }

    public String getResetPwdNotes() {
        return this.resetPwdNotes;
    }

    public void setResetPwdNotes(String resetPwdNotes) {
        this.resetPwdNotes = resetPwdNotes;
    }

}