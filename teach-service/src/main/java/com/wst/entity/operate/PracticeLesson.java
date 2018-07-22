/**
 * @{#} PracticeLesson.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.operate;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * 运营实习职位表
 * 
 * @author masb 对应表(OPERATE_PRACTICE_LESSON)
 */
public class PracticeLesson implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 课程ID
	 */
	protected long lessonId;

	/**
	 * 职位名称
	 */
	protected String practiceName;

	/**
	 * 课程简介
	 */
	protected String lessonAbstract;

	/**
	 * 公司介绍（富文本）
	 */
	protected Blob companyRcmd;

	/**
	 * 职位介绍（富文本）
	 */
	protected Blob practiceRcmd;

	/**
	 * 行业介绍
	 */
	protected String industryRcmd;

	/**
	 * 公司国家（1-国内；2-国外）
	 */
	protected long companyCountry;

	/**
	 * 公司位置
	 */
	protected String companyLocation;

	/**
	 * 职位图片
	 */
	protected String jobPic;

	/**
	 * 实习周期
	 */
	protected String practiceDuration;

	/**
	 * 是否置顶（1-否；2-是）
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
	 * 修改时间
	 */
	protected Date modifyTime;

	/**
	 * 状态（1-有效；2-无效）
	 */
	protected long status;

	/** 无参构造方法 */
	public PracticeLesson() {
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

	public String getPracticeName() {
		return this.practiceName;
	}

	public void setPracticeName(String practiceName) {
		this.practiceName = practiceName;
	}

	public String getLessonAbstract() {
		return lessonAbstract;
	}

	public void setLessonAbstract(String lessonAbstract) {
		this.lessonAbstract = lessonAbstract;
	}

	public Blob getCompanyRcmd() {
		return this.companyRcmd;
	}

	public void setCompanyRcmd(Blob companyRcmd) {
		this.companyRcmd = companyRcmd;
	}

	public Blob getPracticeRcmd() {
		return this.practiceRcmd;
	}

	public void setPracticeRcmd(Blob practiceRcmd) {
		this.practiceRcmd = practiceRcmd;
	}

	public String getIndustryRcmd() {
		return this.industryRcmd;
	}

	public void setIndustryRcmd(String industryRcmd) {
		this.industryRcmd = industryRcmd;
	}

	public long getCompanyCountry() {
		return this.companyCountry;
	}

	public void setCompanyCountry(long companyCountry) {
		this.companyCountry = companyCountry;
	}

	public String getCompanyLocation() {
		return this.companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public String getJobPic() {
		return this.jobPic;
	}

	public void setJobPic(String jobPic) {
		this.jobPic = jobPic;
	}

	public String getPracticeDuration() {
		return this.practiceDuration;
	}

	public void setPracticeDuration(String practiceDuration) {
		this.practiceDuration = practiceDuration;
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