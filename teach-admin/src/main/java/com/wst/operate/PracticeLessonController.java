/*
 * PracticeLessonController.java Created on 2017年9月27日 下午2:42:06
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
package com.wst.operate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.operate.dto.PracticeLessonDTO;
import com.wst.service.operate.service.PracticeLessonService;

/**
 * 运营实习职位管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/operate/practice")
public class PracticeLessonController {

	@Reference(version = "0.0.1")
	private PracticeLessonService practiceLessonService;

	/**
	 * 获取运营实习职位管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index() throws Exception {
		return "operate/practiceLesson";
	}

	/**
	 * 获取 运营实习职位分页数据
	 * 
	 * @param pageParam
	 * @param adDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "finndPaging")
	public Page<PracticeLessonDTO> findPaging(BootStrapTable<PracticeLessonDTO> pageParam,
			PracticeLessonDTO practiceLessonDTO) throws Exception {
		return practiceLessonService.findPaging(pageParam, practiceLessonDTO);
	}

	// 保存/修改运营定制辅导课程信息
	@ResponseBody
	@RequestMapping(value = "saveOrUpdate")
	public ResultDTO<String> saveOrUpdate(HttpServletRequest request, PracticeLessonDTO practiceLessonDTO)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (practiceLessonDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile jobPicFile = standardRequest.getFile("jobPicFile");
			if (jobPicFile == null) {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传职位图片");
			}
			// 广告图片
			ImageInfoDTO imageInfo = FileUtils.uploadImage(jobPicFile);
			if (CommonFuntions.isNotEmptyObject(imageInfo)) {
				if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
					practiceLessonDTO.setJobPic(imageInfo.getPicUrl());
				}
			}
		} else {
			if (practiceLessonDTO.getLessonId() > 0) {
				PracticeLessonDTO practiceLesson = practiceLessonService
						.getPracticeLessonById(practiceLessonDTO.getLessonId());
				if (StringUtils.isEmpty(practiceLesson.getJobPic())) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传职位图片");
				}
				practiceLessonDTO.setJobPic(practiceLesson.getJobPic());
			} else {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传职位图片");
			}
		}

		// 修改
		if (practiceLessonDTO.getLessonId() > 0) {
			result = practiceLessonService.update(practiceLessonDTO);
		} else { // 保存
			result = practiceLessonService.save(practiceLessonDTO);
		}
		return result;
	}

	/**
	 * 根据运营定制辅导课程ID删除运营定制辅导课程
	 * 
	 * @param adId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public ResultDTO<String> delete(long lessonId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (lessonId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课程ID不能为空");
		}
		result = practiceLessonService.delete(lessonId);
		return result;
	}

	/**
	 * 根据ID获取运营实习职位信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getPracticeLesson")
	public PracticeLessonDTO getPracticeLesson(long lessonId) throws Exception {
		return practiceLessonService.getPracticeLessonById(lessonId);
	}

	/**
	 * 更新富文本
	 * 
	 * @param lessonId
	 * @param companyRcmdStr
	 * @param practiceRcmdStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateRcmdStr")
	public ResultDTO<String> updateRcmdStr(long lessonId, String companyRcmdStr, String practiceRcmdStr)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		companyRcmdStr = companyRcmdStr.replace("<p></p>", "");
		practiceRcmdStr = practiceRcmdStr.replace("<p></p>", "");
		practiceLessonService.updateRcmdStr(lessonId, companyRcmdStr, practiceRcmdStr);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

}
