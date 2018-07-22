/*
 * CustomSettleServiceImpl.java Created on 2017年10月11日 下午4:26:09
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
package com.wst.lesson.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.wst.lesson.dao.CustomSettleDao;
import com.wst.mem.dao.TutorDao;
import com.wst.mem.dao.TutorSalaryRecordDao;
import com.wst.service.lesson.dto.CustomSettleDTO;
import com.wst.service.lesson.service.CustomSettleService;
import com.wst.service.mem.dto.TutorDTO;

/**
 * 定制课程结算记录管理ServiceImpl
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class CustomSettleServiceImpl implements CustomSettleService {

	@Resource
	private CustomSettleDao customSettleDao;
	@Resource
	private TutorSalaryRecordDao tutorSalaryRecordDao;
	@Resource
	private TutorDao tutorDao;

	@Override
	public Page<CustomSettleDTO> findPaging(Page<CustomSettleDTO> pageParam, CustomSettleDTO customSettleDTO)
			throws Exception {
		return customSettleDao.findPaging(pageParam, customSettleDTO);
	}

	@Override
	public CustomSettleDTO getSalaryInfo(long tutorId) throws Exception {
		CustomSettleDTO customSettleDTO = new CustomSettleDTO();
		/*
		 * // TODO 导师薪资 TutorSalaryRecordDTO tutorSalaryRecordDTO =
		 * tutorSalaryRecordDao.getTutorSalaryRecord(tutorId);
		 * 
		 * if (tutorSalaryRecordDTO != null) {
		 * customSettleDTO.setBaseSalary(tutorSalaryRecordDTO.getEndSalary()); }
		 */
		// 导师薪资
		TutorDTO tutor = tutorDao.getTutorById(tutorId);
		if (tutor != null) {
			customSettleDTO.setBaseSalary(tutor.getTutorSalary());
		}
		// 已结算薪资
		long haveSettleSalary = customSettleDao.getSettle(tutorId, 2);
		customSettleDTO.setHaveSettleSalary(haveSettleSalary);
		// 未结算薪资
		long notSettleSalary = customSettleDao.getSettle(tutorId, 1);
		customSettleDTO.setNotSettleSalary(notSettleSalary);
		return customSettleDTO;
	}

	@Override
	public Page<CustomSettleDTO> findLesson(Page<CustomSettleDTO> pageParams, long tutorId) throws Exception {
		return customSettleDao.findLesson(pageParams, tutorId);
	}

	@Override
	public List<CustomSettleDTO> findByLesson(long lessonId, long tutorId) throws Exception {
		return customSettleDao.findByLesson(lessonId, tutorId);
	}

	@Override
	public int haveSettle(long recordId) throws Exception {
		return customSettleDao.haveSettle(recordId);
	}

	@Override
	public CustomSettleDTO getCustomSettleById(long recordId) throws Exception {
		return customSettleDao.getCustomSettleById(recordId);
	}

	@Override
	public int memPaySettle(long recordId, String payMemo) throws Exception {
		return customSettleDao.memPaySettle(recordId, payMemo);
	}
}
