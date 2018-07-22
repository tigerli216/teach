/*
 * CustomLessonController.java Created on 2017年9月27日 上午10:32:52
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
import com.wst.service.operate.dto.CustomLessonDTO;
import com.wst.service.operate.service.CustomLessonService;

/**
 * 运营定制辅导课程控制器
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/operate/custom")
public class CustomLessonController {

	@Reference(version = "0.0.1")
	private CustomLessonService customLessonService;

	/**
	 * 获取运营定制辅导课管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index() throws Exception {
		return "operate/customLesson";
	}

	/**
	 * 获取运营定制辅导课程分页数据
	 * 
	 * @param pageParam
	 * @param adDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "finndPaging")
	public Page<CustomLessonDTO> findPaging(BootStrapTable<CustomLessonDTO> pageParam, CustomLessonDTO customLessonDTO)
			throws Exception {
		return customLessonService.findPaging(pageParam, customLessonDTO);
	}

	// 保存/修改运营定制辅导课程信息
	@ResponseBody
	@RequestMapping(value = "saveOrUpdate")
	public ResultDTO<String> saveOrUpdate(HttpServletRequest request, CustomLessonDTO customLessonDTO)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (customLessonDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile lessonPicFile = standardRequest.getFile("lessonPicFile");
			if (lessonPicFile == null) {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程图片");
			}
			// 广告图片
			ImageInfoDTO imageInfo = FileUtils.uploadImage(lessonPicFile);
			if (CommonFuntions.isNotEmptyObject(imageInfo)) {
				if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
					customLessonDTO.setLessonPic(imageInfo.getPicUrl());
				}
			}
		} else {
			if (customLessonDTO.getLessonId() > 0) {
				CustomLessonDTO customLesson = customLessonService.getCustomLessonById(customLessonDTO.getLessonId());
				if (StringUtils.isEmpty(customLesson.getLessonPic())) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程图片");
				}
				customLessonDTO.setLessonPic(customLesson.getLessonPic());
			} else {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程图片");
			}
		}

		// 修改
		if (customLessonDTO.getLessonId() > 0) {
			result = customLessonService.update(customLessonDTO);
		} else { // 保存
			result = customLessonService.save(customLessonDTO);
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
		result = customLessonService.delete(lessonId);
		return result;
	}

	/**
	 * 通过ID获取运营定制辅导课程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getCustomLesson")
	public CustomLessonDTO getCustomLesson(long lessonId) throws Exception {
		return customLessonService.getCustomLessonById(lessonId);
	}

	/**
	 * 更新课程说明
	 * 
	 * @param lessonRcmdStr
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateLessonRcmd")
	public ResultDTO<String> updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		lessonRcmdStr = lessonRcmdStr.replace("<p></p>", "");
		customLessonService.updateLessonRcmd(lessonId, lessonRcmdStr);
		return result.set(ResultCodeEnum.SUCCESS, "更新成功");
	}

	/**
	 * 更新课程简介
	 * 
	 * @param lessonId
	 * @param lessonAbstract
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateLessonAbstract")
	public ResultDTO<String> updateLessonAbstract(long lessonId, String lessonAbstract) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		// lessonAbstract = lessonAbstract.replace("<p></p>", "");
		customLessonService.updateLessonAbstract(lessonId, lessonAbstract);
		return result.set(ResultCodeEnum.SUCCESS, "更新成功");
	}
}
