/*
 * PublicDTO.java Created on 2017年9月26日 上午10:50:08
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.service.lesson.dto;

import java.util.Date;
import java.util.List;

import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.EnumUtils;
import com.hiwi.common.util.StringUtils;
import com.wst.constant.LessonPublicConstant.IsTopStatusEnum;
import com.wst.constant.LessonPublicConstant.LessonPublicTypeEnmu;
import com.wst.constant.LessonPublicConstant.ShelfStatusEnum;
import com.wst.constant.LessonPublicConstant.VisitAuthStatusEnmu;
import com.wst.entity.lesson.Public;

/**
 * 对应实体类(Public)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class PublicDTO extends Public {

	private static final long serialVersionUID = 1L;

	/**
	 * 开始时间
	 */
	private Date beginTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 课程原价:金额（美分转元）
	 */
	private String origPriceStr;

	/**
	 * 课程折扣价
	 */
	private String discountPriceStr;

	/**
	 * 网课课时list
	 */
	private List<PublicClassDTO> attrList;

	/**
	 * 是否首页 true 是, false 否
	 */
	private boolean isHomePage;

	/**
	 * 导师名称
	 */
	private String tutorName;

	/**
	 * 课程介绍Str
	 */
	private String lessonRcmdStr;
	
	/**
	 * 课时状态
	 */
	private long classStatus;
	
	public boolean isHomePage() {
		return isHomePage;
	}

	public void setHomePage(boolean isHomePage) {
		this.isHomePage = isHomePage;
	}

	/**
	 * 枚举课程类型
	 * 
	 * @return
	 */
	public String getLessonTypeStr() {
		LessonPublicTypeEnmu lessonPublicTypeEnmu = EnumUtils.getEnum(LessonPublicTypeEnmu.values(), "type",
				lessonType);
		return lessonPublicTypeEnmu == null ? "" : lessonPublicTypeEnmu.name;
	}

	/**
	 * 枚举访问权限
	 * 
	 * @return
	 */
	public String getVisitAuthStr() {
		VisitAuthStatusEnmu visitAuthStatusEnmu = EnumUtils.getEnum(VisitAuthStatusEnmu.values(), "status", visitAuth);
		return visitAuthStatusEnmu == null ? "" : visitAuthStatusEnmu.name;
	}

	/**
	 * 枚举上架状态
	 * 
	 * @return
	 */
	public String getShelfStatusStr() {
		ShelfStatusEnum shelfStatusEnum = EnumUtils.getEnum(ShelfStatusEnum.values(), "status", shelfStatus);
		return shelfStatusEnum == null ? "" : shelfStatusEnum.name;
	}

	/**
	 * 枚举是否置顶
	 * 
	 * @return
	 */
	public String getIsTopStr() {
		IsTopStatusEnum isTopStatusEnum = EnumUtils.getEnum(IsTopStatusEnum.values(), "status", isTop);
		return isTopStatusEnum == null ? "" : isTopStatusEnum.name;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOrigPriceStr() throws Exception {
		if (StringUtils.isEmpty(origPriceStr)) {
			origPriceStr = AmountUtils.changeF2YString(origPrice);
		}
		return origPriceStr;
	}

	public void setOrigPriceStr(String origPriceStr) {
		this.origPriceStr = origPriceStr;
	}

	public String getDiscountPriceStr() throws Exception {
		if (StringUtils.isEmpty(discountPriceStr)) {
			discountPriceStr = AmountUtils.changeF2YString(discountPrice);
		}
		return discountPriceStr;
	}

	public void setDiscountPriceStr(String discountPriceStr) {
		this.discountPriceStr = discountPriceStr;
	}

	public List<PublicClassDTO> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<PublicClassDTO> attrList) {
		this.attrList = attrList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}

	public String getLessonRcmdStr() {
		return lessonRcmdStr;
	}

	public void setLessonRcmdStr(String lessonRcmdStr) {
		this.lessonRcmdStr = lessonRcmdStr;
	}

	public long getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(long classStatus) {
		this.classStatus = classStatus;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
