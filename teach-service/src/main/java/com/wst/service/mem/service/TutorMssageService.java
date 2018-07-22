/*
 * TutorMssageService.java Created on 2017年10月30日 下午4:42:29
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

import com.hiwi.common.dao.Page;
import com.wst.service.mem.dto.TutorMssageDTO;

/**
 * 导师消息
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface TutorMssageService {

	/**
	 * 分页获取导师消息列表
	 * 
	 * @param pageParam
	 * @param tutorMssageDTO
	 * @return
	 * @throws Exception
	 */
	Page<TutorMssageDTO> findPaging(Page<TutorMssageDTO> pageParam, TutorMssageDTO tutorMssageDTO) throws Exception;
}
