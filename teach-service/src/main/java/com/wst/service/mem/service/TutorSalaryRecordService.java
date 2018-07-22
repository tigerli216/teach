/*
 * TutorSalaryRecordService.java Created on 2017年9月27日 下午9:07:14
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
package com.wst.service.mem.service;

import com.wst.service.mem.dto.TutorSalaryRecordDTO;

/**
 * 导师薪资调整记录Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface TutorSalaryRecordService {

	/**
	 * 新增导师薪资调整
	 * 
     * @param tutorSalaryRecordDTO
	 * @return
	 * @throws Exception
	 */
	public long save(TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception;
}
