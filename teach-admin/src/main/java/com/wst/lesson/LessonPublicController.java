/*
 * LessonPublicController.java Created on 2017年9月27日 上午10:43:11
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.NumValidatorUtils;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.constant.LessonPublicConstant.ShelfStatusEnum;
import com.wst.constant.UserConstant.ExamineStatusEnmu;
import com.wst.constant.UserConstant.ValidStatusEnmu;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.lesson.service.LessoonFileOnloadService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.service.TutorService;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.service.IndustryService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 网课课程管理Controller
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/lesson/public")
public class LessonPublicController {

	private static HiwiLog log = HiwiLogFactory.getLogger(LessonPublicController.class);

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	@Reference(version = "0.0.1")
	private IndustryService industryService;

	@Reference(version = "0.0.1")
	private TxVideoService txVideoService;

	@Reference(version = "0.0.1")
	private LessoonFileOnloadService lessoonFileOnloadService;

	/**
	 * 课程管理首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String lessonIndex(HttpServletRequest request, ModelMap modelMap) throws Exception {
		// 行业列表
		List<IndustryDTO> industrys = industryService.findIndustryList();
		modelMap.put("industrys", industrys);
		return "lesson/lessonPublic";
	}

	/**
	 * 网课文件上传页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("lessonFileOnLoad")
	public String lessonFileOnLoad(HttpServletRequest request, ModelMap modelMap) throws Exception {
		List<IndustryDTO> industrys = industryService.findIndustryList();
		modelMap.put("industrys", industrys);
		return "lesson/lessonFileOnLoad";
	}

	/**
	 * 新增课程页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addInitForm")
	public String addInitForm(HttpServletRequest request, ModelMap modelMap) throws Exception {
		// 行业列表
		List<IndustryDTO> industrys = industryService.findIndustryList();
		modelMap.put("industrys", industrys);

		// 导师列表
		TutorDTO tutorQuery = new TutorDTO();
		tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
		tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
		tutorQuery.setIndustryId(industrys.size() > 0 ? industrys.get(0).getIndustryId() : 0);

		List<TutorDTO> tutors = tutorService.findPaging(null, tutorQuery).getList();
		modelMap.put("tutors", tutors);

		return "lesson/form/addLessonPublic";
	}

	/**
	 * 编辑课程页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editInitForm")
	public String editInitForm(HttpServletRequest request, ModelMap modelMap) throws Exception {
		// 行业列表
		List<IndustryDTO> industrys = industryService.findIndustryList();
		modelMap.put("industrys", industrys);

		// 导师列表
		TutorDTO tutorQuery = new TutorDTO();
		tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
		tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
		tutorQuery.setIndustryId(industrys.size() > 0 ? industrys.get(0).getIndustryId() : 0);

		List<TutorDTO> tutors = tutorService.findPaging(null, tutorQuery).getList();
		modelMap.put("tutors", tutors);

		return "lesson/form/editLessonPublic";
	}

	/**
	 * 查看课程页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("viewInitForm")
	public String viewInitForm(HttpServletRequest request) throws Exception {
		return "lesson/form/viewLessonPublic";
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageParam
	 * @param PublicDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findPaging")
	public Page<PublicDTO> findPaging(BootStrapTable<PublicDTO> pageParam, PublicDTO publicDTO) throws Exception {
		return lessonPublicService.findPaging(pageParam, publicDTO);
	}

	/**
	 * 定制课查看视频
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("watchVideo")
	public String watchVideo(HttpServletRequest request) throws Exception {
		return "lesson/form/watchPublicVideo";
	}

	/**
	 * 新增或更新课程信息
	 * 
	 * @param request
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("saveOrUpdateLesson")
	public ResultDTO<String> saveOrUpdateLesson(HttpServletRequest request, PublicDTO publicDTO) throws Exception {
		ResultDTO<String> resultDTO = this.lessonClassValidate(publicDTO);
		if (!resultDTO.isSuccess()) {
			return resultDTO;
		}

		try {
			if (request instanceof StandardMultipartHttpServletRequest) {
				StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
				MultipartFile lessonPicFile = standardRequest.getFile("lessonPicFile");
				if (lessonPicFile == null) {
					return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程封面");
				}
				// 上传课程封面图片
				ImageInfoDTO imageInfo = FileUtils.uploadImage(lessonPicFile);
				if (imageInfo != null && StringUtils.isNotEmpty(imageInfo.getPicUrl())) {
					publicDTO.setLessonPic(imageInfo.getPicUrl());
				}
			} else {
				if (publicDTO.getLessonId() > 0) {
					PublicDTO dto = lessonPublicService.getLessonById(publicDTO.getLessonId());
					if (StringUtils.isEmpty(dto.getLessonPic())) {
						return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程封面");
					}
					publicDTO.setLessonPic(dto.getLessonPic());
				} else {
					return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程封面");
				}
			}

			/**
			 * 腾讯云视频转码（添加水印）
			 */
			for (PublicClassDTO publicClassDTO : publicDTO.getAttrList()) {
				if (!StringUtils.isEmpty(publicClassDTO.getFileId())) {
					boolean convertResult = txVideoService.convertVodFile(publicClassDTO.getFileId());
					if (convertResult) { // 视频地址转换
						String moiveUrl = publicClassDTO.getMovieUrl();
						moiveUrl = moiveUrl.replace("c57fe348vodgzp", "79a785f0vodtransgzp");
						moiveUrl = moiveUrl.substring(0, moiveUrl.lastIndexOf("/"));
						moiveUrl += "/v.f20.mp4";
						moiveUrl = moiveUrl.replace("http", "https");
						publicClassDTO.setMovieUrl(moiveUrl);
					}
				}
			}

			if (publicDTO.getLessonId() > 0) {
				lessonPublicService.updateLesson(publicDTO);
				resultDTO.set(ResultCodeEnum.SUCCESS, "修改成功");
			} else {
				publicDTO.setShelfStatus(ShelfStatusEnum.DOWN.status);
				resultDTO = lessonPublicService.addLesson(publicDTO);
			}

			return resultDTO;
		} catch (Exception e) {
			log.error("查询业务信息系统异常", e);
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "添加课程系统异常");
		}
	}

	/**
	 * 根据网课课程Id获取课程信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getById")
	public PublicDTO getById(long lessonId) throws Exception {
		return lessonPublicService.getLessonById(lessonId);
	}

	/**
	 * 根据课时id获取课时信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getClassByClassId")
	public PublicClassDTO getClassByClassId(long classId) throws Exception {
		return lessonPublicService.findPublicClassByClassId(classId);
	}

	/**
	 * 上架/下架
	 * 
	 * @param lessonIds
	 *            课程Id集合
	 * @param shelfStatus
	 *            课程状态
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("update")
	public ResultDTO<String> update(String lessonIds, long shelfStatus) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		lessonPublicService.upOrDownLesson(lessonIds, shelfStatus);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	/**
	 * 课时信息校验
	 * 
	 * @param prodAttrList
	 * @return
	 */
	private ResultDTO<String> lessonClassValidate(PublicDTO publicDTO) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();

		/**
		 * 验证金额输入正确性
		 */
		if (StringUtils.isEmpty(publicDTO.getOrigPriceStr())) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入课程原价!");
		}
		if (!NumberUtils.isNumber(publicDTO.getOrigPriceStr())) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入正确的课程原价!");
		}

		if (StringUtils.isEmpty(publicDTO.getDiscountPriceStr())) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入课程折扣价!");
		}
		if (!NumberUtils.isNumber(publicDTO.getDiscountPriceStr())) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入正确的课程折扣价!");
		}

		if (publicDTO.getValidDuration() <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入有效时长!");
		}

		publicDTO.setOrigPrice(Long.parseLong(AmountUtils.changeY2F(publicDTO.getOrigPriceStr())));
		publicDTO.setDiscountPrice(Long.parseLong(AmountUtils.changeY2F(publicDTO.getDiscountPriceStr())));
		// 原价与折扣价大于 0，则购买金额必须大于产品优惠金额
		if (publicDTO.getOrigPrice() < 0 || publicDTO.getDiscountPrice() < 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请输入正确的课程价格!");
		}
		if (publicDTO.getOrigPrice() < publicDTO.getDiscountPrice()) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "课程原价不能小于折扣价!");
		}

		// 设置所属行业
		publicDTO.setIndustry(industryService.getIndustryById(publicDTO.getIndustryId()).getIndustryName());

		/**
		 * 校验课时信息
		 */
		List<PublicClassDTO> publicClassList = publicDTO.getAttrList();
		if (publicClassList == null || publicClassList.size() == 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "请添加详细课程课时!");
		}

		for (PublicClassDTO publicClassDTO : publicClassList) {
			// TODO 课时名称前端已做校验,如果为空认为是无效数据
			if (StringUtils.isEmpty(publicClassDTO.getClassName())) {
				continue;
			}

			if (!NumValidatorUtils.isNotEmpty(publicClassDTO.getDuration())) {
				return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "课时时长不能为空!");
			}

			// 录播课必须填写播放视频地址
			if (publicDTO.getLessonType() == 1L) {
				if (StringUtils.isEmpty(publicClassDTO.getMovieUrl())) {
					return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "请上传课时视频!");
				}
			}

			if (publicDTO.getLessonType() == 2L) {
				if (publicClassDTO.getBeginTime().getTime() < System.currentTimeMillis()) {
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "课时开始时间不能小于当前时间");
				}
			}
		}

		return resultDTO.set(ResultCodeEnum.SUCCESS, "校验成功");
	}

	/**
	 * 根据行业id获取导师
	 * 
	 * @param request
	 * @param industryId
	 *            行业id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getTutorByIndustryId")
	public List<TutorDTO> getTutorByIndustryId(HttpServletRequest request, long industryId) throws Exception {
		TutorDTO tutorQuery = new TutorDTO();
		tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
		tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
		tutorQuery.setIndustryId(industryId);

		List<TutorDTO> tutorList = tutorService.findPaging(null, tutorQuery).getList();
		return tutorList;
	}

	/**
	 * 视频上传前获取签名接口
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("beforeUplodeMovie")
	public ResultDTO<String> beforeUplodeMovie() throws Exception {
		return txVideoService.getSignature();
	}

	/**
	 * 视频上传
	 * 
	 * @param moveUrl
	 *            视频URL
	 * @param coverUrl
	 *            封面URL
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("uplodeMovie")
	public ResultDTO<String> uplodeMovie(String moveUrl, String coverUrl, long classId) throws Exception {
		return lessonPublicService.upMoveUrlById(moveUrl, coverUrl, classId);
	}

	/**
	 * 更新课程介绍(富文本)
	 * 
	 * @param lessonId
	 * @param lessonRcmd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateLessonRcmd")
	public ResultDTO<String> updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		lessonRcmdStr = lessonRcmdStr.replace("<p></p>", "");
		lessonPublicService.updateLessonRcmd(lessonId, lessonRcmdStr);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	/**
	 * 获取学生已购买网课列表
	 * 
	 * @param pageParam
	 * @param PublicDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findUserPublicPaging")
	public Page<PublicDTO> findUserPublicPaging(BootStrapTable<PublicDTO> pageParam, long userId, String lessonName,
			long lessonType) throws Exception {
		return lessonPublicService.findUserPublicPaging(pageParam, userId, lessonName, lessonType);
	}

	/**
	 * 更新导师介绍(富文本)
	 * 
	 * @param lessonId
	 * @param lessonRcmd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateTutorRcmd")
	public ResultDTO<String> updateTutorRcmd(long lessonId, String tutorRcmd) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		// tutorRcmd = tutorRcmd.replace("<p></p>", "");
		lessonPublicService.updateTutorRcmd(lessonId, tutorRcmd);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}
}
