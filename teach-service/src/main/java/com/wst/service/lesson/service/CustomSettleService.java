/*
 * CustomSettleService.java Created on 2017年10月11日 下午4:23:01
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
package com.wst.service.lesson.service;

import java.util.List;

import com.hiwi.common.dao.Page;
import com.wst.service.lesson.dto.CustomSettleDTO;

/**
 * 定制课程结算记录管理Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface CustomSettleService {

	/**
	 * 获取定制课程结算记录分页数据
	 * 
	 * @param pageParam
	 * @param customSettleDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomSettleDTO> findPaging(Page<CustomSettleDTO> pageParam, CustomSettleDTO customSettleDTO)
			throws Exception;

	/**
	 * 获取导师薪资信息
	 * 
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public CustomSettleDTO getSalaryInfo(long tutorId) throws Exception;

	/**
	 * 获取结算的课程
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public Page<CustomSettleDTO> findLesson(Page<CustomSettleDTO> pageParams, long tutorId) throws Exception;

	/**
	 * 通过课程ID获取结算列表
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public List<CustomSettleDTO> findByLesson(long lessonId, long tutorId) throws Exception;

	/**
	 * 强制结算
	 * 
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public int haveSettle(long recordId) throws Exception;

	/**
	 * 通过ID获取薪资结算信息
	 * 
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public CustomSettleDTO getCustomSettleById(long recordId) throws Exception;

	/**
	 * 人为结算
	 * 
	 * @param recordId
	 * @param payMemo
	 * @return
	 * @throws Exception
	 */
	public int memPaySettle(long recordId, String payMemo) throws Exception;
}
