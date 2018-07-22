/*
 * LessonFeedbackController.java Created on 2017年10月10日 下午5:30:19
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
package com.wst.mem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.mem.dto.LessonFeedbackDTO;
import com.wst.service.mem.service.LessonFeedbackService;

/**
 * 用户课程反馈管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/lessonFeedback")
public class LessonFeedbackController {

	@Reference(version = "0.0.1")
	private LessonFeedbackService lessonFeedbackService;

	/**
	 * 用户课程反馈管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index(ModelMap modelMap) throws Exception {
		return "mem/lessonFeedback";
	}

	/**
	 * 获取用户课程反馈管理分页数据
	 * 
	 * @param pageParam
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<LessonFeedbackDTO> findPaging(BootStrapTable<LessonFeedbackDTO> pageParam,
			LessonFeedbackDTO lessonFeedbackDTO) throws Exception {
		if (StringUtils.isNotEmpty(lessonFeedbackDTO.getLoginAccount())) {
			lessonFeedbackDTO.setLoginAccount("%" + lessonFeedbackDTO.getLoginAccount() + "%");
		}
		
		if (StringUtils.isNotEmpty(lessonFeedbackDTO.getLessonName())) {
			lessonFeedbackDTO.setLessonName("%" + lessonFeedbackDTO.getLessonName() + "%");
		}
		
		return lessonFeedbackService.findPaging(pageParam, lessonFeedbackDTO);
	}

}
