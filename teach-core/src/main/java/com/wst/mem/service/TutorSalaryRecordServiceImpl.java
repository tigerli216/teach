/*
 * TutorSalaryRecordServiceImpl.java Created on 2017年9月27日 下午9:09:26
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
package com.wst.mem.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.mem.dao.TutorSalaryRecordDao;
import com.wst.service.mem.dto.TutorSalaryRecordDTO;
import com.wst.service.mem.service.TutorSalaryRecordService;

/**
 * 导师薪资调整记录Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class TutorSalaryRecordServiceImpl implements TutorSalaryRecordService{

	@Resource
	private TutorSalaryRecordDao tutorSalaryRecordDao;
	
	@Override
	public long save(TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception {
		return tutorSalaryRecordDao.save(tutorSalaryRecordDTO);
	}
	
	
	
}
