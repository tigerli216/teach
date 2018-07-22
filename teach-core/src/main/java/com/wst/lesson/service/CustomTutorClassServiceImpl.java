/*
 * CustomTutorClassServiceImpl.java Created on 2017年10月13日 下午1:52:38
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

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.lesson.dao.CustomTutorClassDao;
import com.wst.service.lesson.dto.CustomTutorClassDTO;
import com.wst.service.lesson.service.CustomTutorClassService;

/**
 * 定制课程导师课时计划服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class CustomTutorClassServiceImpl implements CustomTutorClassService {

	@Resource
	private CustomTutorClassDao customTutorClassDao;
	
	@Override
	public CustomTutorClassDTO getByLessonIdTutorId(long lessonId, long tutorId) throws Exception {
		return customTutorClassDao.getCustomTutorClass(lessonId, tutorId);
	}

}
