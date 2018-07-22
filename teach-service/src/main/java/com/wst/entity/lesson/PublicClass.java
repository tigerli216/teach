/**
 * @{#} PublicClass.java Created on 2017-09-25 21:54:15
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * 网课课时信息表
 * 
 * @author masb 对应表(LESSON_PUBLIC_CLASS)
 */
public class PublicClass implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 课时ID
	 */
	protected long classId;

	/**
	 * 课程ID
	 */
	protected long lessonId;

	/**
	 * 课时名称
	 */
	protected String className;

	/**
	 * 课时介绍
	 */
	protected String classRcmd;

	/**
	 * 时长（分钟）
	 */
	protected long duration;

	/**
	 * 是否免费（1-否；2-是）
	 */
	protected long isFree;

	/**
	 * 开始时间
	 */
	protected Date beginTime;

	/**
	 * 结束时间
	 */
	protected Date endTime;

	/**
	 * 房间ID
	 */
	protected String roomId;

	/**
	 * 群组ID
	 */
	protected String groupId;

	/**
	 * 视频地址
	 */
	protected String movieUrl;

	/**
	 * 课时状态（1-未开始；2-进行中；3-已完结）
	 */
	protected long classStatus;

	/**
	 * 视频封面（录播课）
	 */
	protected String movieCover;

	/**
	 * 课件图片（PPT转图片，多个以,隔开，直播课）
	 */
	protected String coursewarePic;

	/** 无参构造方法 */
	public PublicClass() {
	}

	/**
	 * 属性get||set方法
	 */
	public long getClassId() {
		return this.classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getLessonId() {
		return this.lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassRcmd() {
		return this.classRcmd;
	}

	public void setClassRcmd(String classRcmd) {
		this.classRcmd = classRcmd;
	}

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getIsFree() {
		return this.isFree;
	}

	public void setIsFree(long isFree) {
		this.isFree = isFree;
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

	public String getMovieUrl() {
		return this.movieUrl;
	}

	public void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}

	public long getClassStatus() {
		if (beginTime != null && classStatus != 3 && endTime != null) {
			long now = System.currentTimeMillis();
			
			if (beginTime.getTime() > now) {
				classStatus = 1;
			}
			if (beginTime.getTime() <= now) {
				classStatus = 2;
			}
			if (endTime.getTime() <= now) {
				classStatus = 3;
			}
		}
		return this.classStatus;
	}

	public void setClassStatus(long classStatus) {
		this.classStatus = classStatus;
	}

	public String getMovieCover() {
		return movieCover;
	}

	public void setMovieCover(String movieCover) {
		this.movieCover = movieCover;
	}

	public String getCoursewarePic() {
		return coursewarePic;
	}

	public void setCoursewarePic(String coursewarePic) {
		this.coursewarePic = coursewarePic;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}