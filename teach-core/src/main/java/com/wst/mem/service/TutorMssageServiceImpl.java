/*
 * TutorMssageServiceImpl.java Created on 2017年10月30日 下午4:45:33
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
import com.hiwi.common.dao.Page;
import com.wst.mem.dao.TutorMssageDao;
import com.wst.service.mem.dto.TutorMssageDTO;
import com.wst.service.mem.service.TutorMssageService;

/**
 * 导师信息Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class TutorMssageServiceImpl implements TutorMssageService {

	@Resource
	private TutorMssageDao tutorMssageDao;

	@Override
	public Page<TutorMssageDTO> findPaging(Page<TutorMssageDTO> pageParam, TutorMssageDTO tutorMssageDTO)
			throws Exception {
		return tutorMssageDao.findPaging(pageParam, tutorMssageDTO);
	}

}
