/**
 * @{#} Public.java Created on 2017-09-25 21:54:15
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 网课信息表
 * 
 * @author masb 对应表(LESSON_PUBLIC)
 */
public class Public implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 课程ID
	 */
	protected long lessonId;

	/**
	 * 课程分类（1-录播课；2-直播课）
	 */
	protected long lessonType;

	/**
	 * 课程名称
	 */
	protected String lessonName;

	/**
	 * 课程图片
	 */
	protected String lessonPic;

	/**
	 * 系列名称
	 */
	protected String seriesName;

	/**
	 * 行业ID
	 */
	protected long industryId;

	/**
	 * 所属行业
	 */
	protected String industry;

	/**
	 * 访问权限（1-游客；2-普通用户；3-VIP）
	 */
	protected long visitAuth;

	/**
	 * 课程原价
	 */
	protected long origPrice;

	/**
	 * 课程折扣价
	 */
	protected long discountPrice;

	/**
	 * 课程介绍（富文本）
	 */
	protected Blob lessonRcmd;

	/**
	 * 讲师ID
	 */
	protected long tutorId;

	/**
	 * 讲师介绍
	 */
	protected String tutorRcmd;

	/**
	 * 讲师薪资
	 */
	protected long tutorSalary;

	/**
	 * 上架状态（1-已上架；2-已下架）
	 */
	protected long shelfStatus;

	/**
	 * 有效期
	 */
	// protected Date validTime;

	/**
	 * 是否置顶（1-否；2-是）
	 */
	protected long isTop;

	/**
	 * 权重（由高到低排序）
	 */
	protected long weight;

	/**
	 * 创建时间
	 */
	protected Date createTime;

	/**
	 * 创建人
	 */
	protected long createOp;

	/**
	 * 修改时间
	 */
	protected Date modifyTime;

	/**
	 * 修改人
	 */
	protected long modifyOp;

	/**
	 * 有效时长
	 */
	protected int validDuration;

	/** 无参构造方法 */
	public Public() {
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

	public long getLessonType() {
		return this.lessonType;
	}

	public void setLessonType(long lessonType) {
		this.lessonType = lessonType;
	}

	public String getLessonName() {
		return this.lessonName;
	}

	public String getLessonPic() {
		return lessonPic;
	}

	public void setLessonPic(String lessonPic) {
		this.lessonPic = lessonPic;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getSeriesName() {
		return this.seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public long getIndustryId() {
		return this.industryId;
	}

	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public long getVisitAuth() {
		return this.visitAuth;
	}

	public void setVisitAuth(long visitAuth) {
		this.visitAuth = visitAuth;
	}

	public long getOrigPrice() {
		return this.origPrice;
	}

	public void setOrigPrice(long origPrice) {
		this.origPrice = origPrice;
	}

	public long getDiscountPrice() {
		return this.discountPrice;
	}

	public void setDiscountPrice(long discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Blob getLessonRcmd() {
		return this.lessonRcmd;
	}

	public void setLessonRcmd(Blob lessonRcmd) {
		this.lessonRcmd = lessonRcmd;
	}

	public long getTutorId() {
		return this.tutorId;
	}

	public void setTutorId(long tutorId) {
		this.tutorId = tutorId;
	}

	public String getTutorRcmd() {
		return this.tutorRcmd;
	}

	public void setTutorRcmd(String tutorRcmd) {
		this.tutorRcmd = tutorRcmd;
	}

	public long getTutorSalary() {
		return tutorSalary;
	}

	public void setTutorSalary(long tutorSalary) {
		this.tutorSalary = tutorSalary;
	}

	public long getShelfStatus() {
		return this.shelfStatus;
	}

	public void setShelfStatus(long shelfStatus) {
		this.shelfStatus = shelfStatus;
	}

	// public Date getValidTime() {
	// return this.validTime;
	// }
	//
	// public void setValidTime(Date validTime) {
	// this.validTime = validTime;
	// }

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

	public long getCreateOp() {
		return this.createOp;
	}

	public void setCreateOp(long createOp) {
		this.createOp = createOp;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public long getModifyOp() {
		return this.modifyOp;
	}

	public void setModifyOp(long modifyOp) {
		this.modifyOp = modifyOp;
	}

	public int getValidDuration() {
		return validDuration;
	}

	public void setValidDuration(int validDuration) {
		this.validDuration = validDuration;
	}

}