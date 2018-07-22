/*
 * LessonFileUpload.java Created on 2017年11月20日 下午5:50:33
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
package com.wst.lesson;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.FileInfoDTO;
import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.file.FileUtils;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.lesson.service.LessoonFileOnloadService;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.utils.ImageZipFileProcess;

/**
 * 课程文件上传Controller
 * 
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/lesson/fileOnload")
public class LessonFileUploadController {

	private static HiwiLog log = HiwiLogFactory.getLogger(LessonPublicController.class);

	@Reference(version = "0.0.1")
	private LessoonFileOnloadService lessoonFileOnloadService;

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;

	@Reference(version = "0.0.1")
	private PublicClassService publicClassService;

	/**
	 * 直播课课件上传
	 * 
	 * @param request
	 * @param lessonId
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("affirmLoad")
	public ResultDTO<String> affirmFileonload(HttpServletRequest request, long lessonId, long classId)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		if (!(request instanceof StandardMultipartHttpServletRequest)) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传直播课件!");
		}

		/**
		 * 上传课件文件到服务器
		 */
		StandardMultipartHttpServletRequest fileRequest = (StandardMultipartHttpServletRequest) request;
		MultipartFile coursewareFile = fileRequest.getFile("pptFile");
		if (coursewareFile == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传直播课件!");
		}
		FileInfoDTO coursewareFileInfo = FileUtils.uploadFile(coursewareFile);
		if (coursewareFileInfo == null || StringUtils.isEmpty(coursewareFileInfo.getUrl())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课件文件上传失败,请联系系统管理员");
		}
		log.info("====== 课件上传服务器后返回的Url地址：" + coursewareFileInfo.getUrl() + " ======");
		List<String> unzipFilePaths = new ArrayList<String>();
		CloseableHttpClient httpClient = null;
		try {
			/**
			 * Office 365上传文件转图片
			 */
			httpClient = HttpClients.createDefault();
			String office365Url = "https://ow365.cn/?i=13958&ssl=1&info=1&words=100&furl="
					+ coursewareFileInfo.getUrl();
			log.info("====== office365Url地址：" + office365Url + " ======");
			HttpPost httpPost = new HttpPost(office365Url);
			CloseableHttpResponse office365Resp = httpClient.execute(httpPost);
			HttpEntity office365Entity = office365Resp.getEntity();

			/**
			 * 从office 365拉下转换后图片,解压缩存储
			 */
			InputStream coursewareImgIo = office365Entity.getContent();
			unzipFilePaths.addAll(ImageZipFileProcess.unzipImageFile(coursewareImgIo));
		} catch (Exception e) {
			log.error("课件文件上传失败,请联系系统管理员", e);
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课件文件上传失败,请联系系统管理员");
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}

		if (unzipFilePaths.size() == 0) {
			log.error("课件文件上传失败,转换后图片文件为空");
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课件文件上传失败,请联系系统管理员");
		}

		StringBuilder imgUrls = new StringBuilder();
		for (String unzipFilePath : unzipFilePaths) {
			imgUrls.append(unzipFilePath).append(",");
		}

		lessoonFileOnloadService.lessonFileOnload(lessonId, classId, imgUrls.substring(0, imgUrls.length() - 1));

		return result.set(ResultCodeEnum.SUCCESS, "课件文件上传成功");
	}

	/**
	 * 根据课程Id查询课时
	 */
	@ResponseBody
	@RequestMapping("getClassByLessonId")
	public ResultDTO<List<PublicClassDTO>> findPublicClassByLessonId(long lessonId) throws Exception {
		ResultDTO<List<PublicClassDTO>> resultDTO = new ResultDTO<List<PublicClassDTO>>();
		List<PublicClassDTO> publicClassList = lessoonFileOnloadService.findPublicClassList(lessonId);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "", publicClassList);
	}

	/**
	 * 通过课程ID获取直播课课件图片
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findImgList")
	public Map<String, Object> findImgList(long lessonId) throws Exception {
		return publicClassService.findImgList(lessonId);
	}

	/**
	 * 图片删除
	 * 
	 * @param lessonId
	 * @param picId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "imgDelete")
	public ResultDTO<String> imgDelete(long lessonId, String img) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		result = publicClassService.imgDelete(lessonId, img);

		return result;
	}

	// ===================== 产品图片处理 ===================
	/**
	 * 图片（批量，多次）上传
	 * 
	 * @param file
	 * @param suppId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("imgUpload")
	public String imgUpload(@RequestParam("file") MultipartFile file, long lessonId) throws Exception {
		if (file == null || lessonId == 0) {
			return "参数错误";
		}
		ImageInfoDTO imageInfo = FileUtils.uploadImage(file);
		if (CommonFuntions.isNotEmptyObject(imageInfo)) {
			if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
				publicClassService.updateLivePic(lessonId, imageInfo.getPicUrl());
			}
		}

		return "success";
	}
}
