/*
 * LessonCustomController.java Created on 2017年9月29日 上午10:37:17
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.FileErrorDTO;
import com.hiwi.common.dto.FileInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.AmountUtils;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.base.helper.ExcelUtils;
import com.wst.constant.UserConstant.ExamineStatusEnmu;
import com.wst.constant.UserConstant.ValidStatusEnmu;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomClassVideoDtlDTO;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.MssageService;
import com.wst.service.mem.service.TutorService;
import com.wst.service.mem.service.UserService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.service.ConfirmOrderService;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.service.IndustryService;
import com.wst.sm.util.OperatorUtil;

/**
 * 定制课课程管理Controller
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/lesson/custom")
public class LessonCustomController {

	private static HiwiLog log = HiwiLogFactory.getLogger(LessonCustomController.class);

	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;

	@Reference(version = "0.0.1")
	private ConfirmOrderService confirmOrderService;

	@Reference(version = "0.0.1")
	private CustomClassService customClassService;

	@Reference(version = "0.0.1")
	private IndustryService industryService;

	@Reference(version = "0.0.1")
	private UserService userService;

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	@Reference(version = "0.0.1")
	private PublicClassService publicClassService;

	@Reference(version = "0.0.1")
	private MssageService mssageService;

	/**
	 * 课程管理首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String lessonIndex(HttpServletRequest request, ModelMap modelMap) throws Exception {
		List<IndustryDTO> industryList = industryService.findIndustryList();
		modelMap.put("industryList", industryList);
		return "lesson/lessonCustom";
	}

	/**
	 * 课件上传页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("readyFileIndex")
	public String readyFileIndex(ModelMap modelMap) throws Exception {
		List<IndustryDTO> industryList = industryService.findIndustryList();
		modelMap.put("industryList", industryList);
		return "lesson/readyFileUpload";
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
		List<IndustryDTO> industryList = industryService.findIndustryList();
		UserDTO userDTO = new UserDTO();
		userDTO.setStatus(1);
		List<UserDTO> userList = userService.findPaging(null, userDTO).getList();
		modelMap.put("industryList", industryList);
		modelMap.put("userList", userList);
		return "lesson/form/addLessonCustom";
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
		List<IndustryDTO> industryList = industryService.findIndustryList();
		UserDTO userDTO = new UserDTO();
		userDTO.setStatus(1);
		List<UserDTO> userList = userService.findPaging(null, userDTO).getList();
		modelMap.put("industryList", industryList);
		modelMap.put("userList", userList);
		return "lesson/form/editLessonCustom";
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
		return "lesson/form/watchCustomVideo";
	}

	/**
	 * 查看课程页面
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("viewInitForm")
	public String viewInitForm(HttpServletRequest request, ModelMap modelMap) throws Exception {
		return "lesson/form/viewLessonCustom";
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageParam
	 * @param CustomDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findPaging")
	public Page<CustomDTO> findPaging(BootStrapTable<CustomDTO> pageParam, CustomDTO customDTO) throws Exception {
		return lessonCustomService.fingPaging(pageParam, customDTO);
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
	public ResultDTO<String> saveOrUpdateLesson(HttpServletRequest request, CustomDTO customDTO) throws Exception {

		/** 校验参数 */
		ResultDTO<String> resultDTO = this.lessonClassValidate(customDTO, 1);
		if (!resultDTO.isSuccess()) {
			return resultDTO;
		}
		OperatorDTO operatorDTO = OperatorUtil.getOperator(request);
		customDTO.setEndTime(DateUtils.addEndTime(customDTO.getEndTime()));

		/** 业务处理 */
		try {
			if (customDTO.getLessonId() > 0) {
				customDTO.setModifyOp(operatorDTO.getOpId());
				resultDTO = lessonCustomService.updateLesson(customDTO);
			} else {
				customDTO.setCreateOp(operatorDTO.getOpId());
				resultDTO = lessonCustomService.addLesson(customDTO);
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
	public CustomDTO getById(long lessonId) throws Exception {
		return lessonCustomService.getLessonById(lessonId);
	}

	/**
	 * 课时信息校验
	 * 
	 * @param prodAttrList
	 * @return
	 */
	public ResultDTO<String> lessonClassValidate(CustomDTO customDTO, Integer... args) throws Exception {
		ResultDTO<String> resultDTO = new ResultDTO<String>();
		if (null == customDTO) {
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "新增或修改课程时不能为空!");
		}
		if (StringUtils.isEmpty(customDTO.getLessonName())) {
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课程名称不能为空!");
		}
		if (customDTO.getIndustryId() == 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "行业ID不能为空!");
		}
		customDTO.setLessonPrice(Long.parseLong(AmountUtils.changeY2F(customDTO.getLessonPriceStr())));
		// 购买金额大于0，则购买金额必须大于产品优惠金额
		if (customDTO.getLessonPrice() <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "课程价格必须大于0");
		}
		if (null == customDTO.getAttrList() || customDTO.getAttrList().size() <= 0) {
			return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "课程至少有一个课时");
		}

		customDTO.setIndustry(industryService.getIndustryById(customDTO.getIndustryId()).getIndustryName());
		// 课时时长
		long classSum = 0;
		for (CustomClassDTO customClassDTO : customDTO.getAttrList()) {
			// TODO 课时名称前端已做校验,如果为空认为是无效数据
			if (StringUtils.isEmpty(customClassDTO.getClassName())) {
				continue;
			}
			if (customClassDTO.getTutorId() <= 0) {
				return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "请选择导师!");
			}

			// 计划上课时间
			if (customClassDTO.getPlanTime() == null) {
				return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "计划上课时间不能为空!");
			}
			if (customClassDTO.getPlanTime().getTime() < customDTO.getBeginTime().getTime()) {
				return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "计划上课时间不能为小于课程开始时间!");
			}
			// 计划上课时间不能小于现在
			if (customClassDTO.getClassStatus() <= 1) {
				Date planTime = DateUtils.addEndTime(customClassDTO.getPlanTime());
				if (planTime.before(new Date())) {
					return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "计划上课时间不能小于当前时间");
				}
			}

			// 课时时长
			if (customClassDTO.getPlanDuration() <= 0) {
				return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "课时时长不能为空!");
			}
			// 课程结束或完结，用实际上课时长计算
			if (customClassDTO.getClassStatus() == 3 || customClassDTO.getClassStatus() == 4) {
				classSum += customClassDTO.getRealDuration();
			} else {
				classSum += customClassDTO.getPlanDuration();
			}

		}
		Iterator<CustomClassDTO> it = customDTO.getAttrList().iterator();
		while (it.hasNext()) {
			if (it.next().getClassName() == null) {
				it.remove();
			}
		}
		if (customDTO.getTotalClass() < classSum) {
			return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "课时时长不能大于总课时");
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "校验成功");
	}

	/**
	 * 购买定制课
	 * 
	 * @param request
	 * @param lessonId
	 *            课程id
	 * @param payType
	 *            支付方式（1-线上；2-线下）
	 * @param orderNote
	 *            订单备注
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("buyLesson")
	public ResultDTO<String> buyLesson(HttpServletRequest request, long lessonId, long payType, String orderNote)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (lessonId <= 0) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "请选择课程");
		}
		ResultDTO<OrderDTO> orderResult = confirmOrderService.createCustomLessonOrder(lessonId, payType, orderNote);
		if (!orderResult.isSuccess()) {
			return result.set(orderResult.getErrCode(), orderResult.getErrDesc());
		}
		return result.set(ResultCodeEnum.SUCCESS, "成功");
	}

	/**
	 * 授课管理首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("giveIndex")
	public String giveIndex(HttpServletRequest request) throws Exception {
		return "lesson/giveLessons";
	}

	/**
	 * 获取授课记录列表
	 * 
	 * @param pageParam
	 *            分页参数
	 * @param lessonId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @param tutorId
	 *            导师ID
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findGiveLessonsPaging")
	public Page<CustomClassDTO> findGiveLessonsPaging(BootStrapTable<CustomClassDTO> pageParam, String lessonName,
			String userLoginAccount, String tutorLoginAccount, Date beginTime, Date endTime) throws Exception {
		endTime = DateUtils.addEndTime(endTime);
		return customClassService.findGiveLessonsPaging(pageParam, lessonName, userLoginAccount, tutorLoginAccount, beginTime, endTime);
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
		lessonCustomService.updateLessonRcmd(lessonId, lessonRcmdStr);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	/**
	 * 定制课程模板导入
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "ImportLesson")
	public ResultDTO<List<FileErrorDTO>> ImportLesson(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue = "0") long editLessonId) throws Exception {
		ResultDTO<List<FileErrorDTO>> resultDTO = new ResultDTO<>();
		// 错误信息
		List<FileErrorDTO> errorList = new ArrayList<>();

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile lessonCustomExcel = standardRequest.getFile("lessonCustomExcel");
			ResultDTO<List<List<String>>> lessonCustom = ExcelUtils.readExcel(lessonCustomExcel, 0, 1);
			if (!lessonCustom.isSuccess()) {
				FileErrorDTO fileErrorDTO = new FileErrorDTO();
				fileErrorDTO.setErrorMsg("请传入有效文件！");
				errorList.add(fileErrorDTO);
				return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
			}
			List<List<String>> lessonInfo = lessonCustom.getResult();
			if (lessonInfo == null || lessonInfo.size() <= 0) {
				FileErrorDTO fileErrorDTO = new FileErrorDTO();
				fileErrorDTO.setErrorMsg("请传入有效文件！");
				errorList.add(fileErrorDTO);
				return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
			}
			OperatorDTO operatorDTO = OperatorUtil.getOperator(request);
			if (editLessonId <= 0) {
				return addLessonImport(lessonInfo, errorList, operatorDTO);
			} else {
				return editLessonImport(lessonInfo, errorList, editLessonId);
			}
		} else {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg("请传入有效文件！");
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}

	}

	/**
	 * 课程新增导入
	 * 
	 * @param lessonInfo
	 * @param errorList
	 * @param operatorDTO
	 * @return
	 */
	private ResultDTO<List<FileErrorDTO>> addLessonImport(List<List<String>> lessonInfo, List<FileErrorDTO> errorList,
			OperatorDTO operatorDTO) throws Exception {
		ResultDTO<List<FileErrorDTO>> resultDTO = new ResultDTO<>();
		CustomDTO customDTO = new CustomDTO();
		List<CustomClassDTO> attrList = new ArrayList<>();
		List<TutorDTO> tutorList = null;
		for (int i = 0; i < lessonInfo.size(); i++) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			List<String> lessonDetail = lessonInfo.get(i);
			log.debug("第" + (i + 1) + "数据:" + JsonUtils.getJson(lessonDetail));
			// 课程信息，模板格式2个字段
			if (i < 9) {
				if (lessonDetail == null || lessonDetail.size() != 2) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：数据格式不合法");
					errorList.add(fileErrorDTO);
					continue;
				}
				String str = lessonDetail.get(1);
				if (i == 0) {
					// 课程名称
					CustomDTO custom = lessonCustomService.getLessonByLessonName(str);
					if (custom != null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程名称已被占用");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setLessonName(str);
				} else if (i == 1) {
					// 学生
					UserDTO userDTO = userService.getUserByAccount(str);
					if (userDTO == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：用户不存在");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setUserId(userDTO.getUserId());
				} else if (i == 2) {
					// 课程类型
					long lessonType = 0;
					if (StringUtils.equals(str, "定制课")) {
						lessonType = 1;
					} else if (StringUtils.equals(str, "实习课")) {
						lessonType = 2;
					} else {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程分类不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setLessonType(lessonType);
				} else if (i == 3) {
					// 课程价格
					if (!NumberUtils.isNumber(str)) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程价格不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setLessonPrice(Long.parseLong(AmountUtils.changeY2F(str)));
				} else if (i == 4) {
					// 所属行业
					IndustryDTO industryDTO = industryService.getIndustryByName(str);
					if (industryDTO == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：所属行业不合法");
						errorList.add(fileErrorDTO);
						continue;
					}

					TutorDTO tutorQuery = new TutorDTO();
					tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
					tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
					tutorQuery.setIndustryId(industryDTO.getIndustryId());
					tutorList = tutorService.findPaging(null, tutorQuery).getList();

					customDTO.setIndustry(industryDTO.getIndustryName());
					customDTO.setIndustryId(industryDTO.getIndustryId());
				} else if (i == 5) {
					// 课程总课时
					if (!NumberUtils.isNumber(str)) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程总课时不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setTotalClass(Long.parseLong(str));
				} else if (i == 6) {
					// 课程开始时间
					Date beginTime = null;
					try {
						beginTime = DateUtils.getDateParse(str, DateUtils.YYYY_MM_DD_HH_MM_SS);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (beginTime == null || beginTime.before(DateUtils.getIntradayZeroTime())) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程开始时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}

					customDTO.setBeginTime(beginTime);
				} else if (i == 7) {
					// 课程结束时间
					Date endTime = null;
					try {
						endTime = DateUtils.getDateParse(str, DateUtils.YYYY_MM_DD_HH_MM_SS);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (endTime == null || customDTO.getBeginTime() == null
							|| endTime.getTime() <= customDTO.getBeginTime().getTime()) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程结束时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setEndTime(endTime);
				} else if (i == 8) {
					// 课程介绍
					customDTO.setLessonRcmdStr(str);
				}
			}
			// 课程下课时信息，模板格式6个字段
			if (i > 9) {
				if ((lessonDetail == null || lessonDetail.size() != 6) && NumberUtils.isNumber(lessonDetail.get(0))) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：数据格式不合法");
					errorList.add(fileErrorDTO);
					continue;
				}

				if (NumberUtils.isNumber(lessonDetail.get(0))) {
					// 课时
					CustomClassDTO customClassDTO = new CustomClassDTO();
					// 课时名称
					customClassDTO.setClassName(lessonDetail.get(1));
					// 导师
					String tutorName = lessonDetail.get(2);
					TutorDTO tutorDTO = tutorService.getTutorByAccount(tutorName);
					if (tutorDTO == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：导师不存在");
						errorList.add(fileErrorDTO);
						continue;
					}

					if (tutorList == null || tutorList.size() == 0) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：该行业未配置导师");
						errorList.add(fileErrorDTO);
						continue;
					}

					boolean haveTutor = false;
					for (TutorDTO tutor : tutorList) {
						if (tutor.getTutorId() == tutorDTO.getTutorId()) {
							haveTutor = true;
						}
					}

					if (!haveTutor) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：导师与所选行业不匹配");
						errorList.add(fileErrorDTO);
						continue;
					}

					customClassDTO.setTutorId(tutorDTO.getTutorId());
					customClassDTO.setTutorName(tutorDTO.getRealName());
					// 计划上课时间
					Date planTime = null;
					try {
						planTime = DateUtils.getDateParse(lessonDetail.get(3), DateUtils.YYYY_MM_DD);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (planTime == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：计划上课时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customClassDTO.setPlanTime(planTime);
					// 计划课时
					if (!NumberUtils.isNumber(lessonDetail.get(4))) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：计划时长不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customClassDTO.setPlanDuration(Long.parseLong(lessonDetail.get(4)));
					customClassDTO.setClassRcmd(lessonDetail.get(4));

					attrList.add(customClassDTO);
				}
			}
		}
		if (errorList.size() > 0) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		if (attrList.size() <= 0) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg("请传入有效文件！");
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		customDTO.setAttrList(attrList);
		ResultDTO<String> resultStr = this.lessonClassValidate(customDTO);
		if (!resultStr.isSuccess()) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg(resultStr.getErrDesc());
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		/** 业务处理 */
		customDTO.setCreateOp(operatorDTO.getOpId());
		lessonCustomService.addLesson(customDTO);
		return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
	}

	/**
	 * 课程编辑导入
	 * 
	 * @param lessonInfo
	 * @param errorList
	 * @return
	 */
	private ResultDTO<List<FileErrorDTO>> editLessonImport(List<List<String>> lessonInfo, List<FileErrorDTO> errorList,
			long editLessonId) throws Exception {
		ResultDTO<List<FileErrorDTO>> resultDTO = new ResultDTO<>();
		CustomDTO customDTO = lessonCustomService.getLessonById(editLessonId);
		if (customDTO == null) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg("请传入有效文件！");
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		// 课时集合
		List<CustomClassDTO> newAttrList = new ArrayList<>();
		List<TutorDTO> tutorList = null;
		for (int i = 0; i < lessonInfo.size(); i++) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			List<String> lessonDetail = lessonInfo.get(i);
			log.debug("第" + (i + 1) + "数据:" + JsonUtils.getJson(lessonDetail));
			// 课程信息，模板格式2个字段
			if (i < 5) {
				if (lessonDetail == null || lessonDetail.size() != 2) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：数据格式不合法");
					errorList.add(fileErrorDTO);
					continue;
				}
				String str = lessonDetail.get(1);
				if (i == 0) {
					// 课程类型
					long lessonType = 0;
					if (StringUtils.equals(str, "定制课")) {
						lessonType = 1;
					} else if (StringUtils.equals(str, "实习课")) {
						lessonType = 2;
					} else {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程分类不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setLessonType(lessonType);
				} else if (i == 1) {
					// 所属行业
					IndustryDTO industryDTO = industryService.getIndustryByName(str);
					if (industryDTO == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：所属行业不合法");
						errorList.add(fileErrorDTO);
						continue;
					}

					TutorDTO tutorQuery = new TutorDTO();
					tutorQuery.setExamineStatus(ExamineStatusEnmu.PASS_EXAMINE.status);
					tutorQuery.setValidStatus(ValidStatusEnmu.HAS_EFFECTIVE.status);
					tutorQuery.setIndustryId(industryDTO.getIndustryId());
					tutorList = tutorService.findPaging(null, tutorQuery).getList();

					customDTO.setIndustry(industryDTO.getIndustryName());
					customDTO.setIndustryId(industryDTO.getIndustryId());
				} else if (i == 2) {
					// 课程开始时间
					Date beginTime = null;
					try {
						beginTime = DateUtils.getDateParse(str, DateUtils.YYYY_MM_DD_HH_MM_SS);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (beginTime == null || beginTime.before(DateUtils.getIntradayZeroTime())) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程开始时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}

					customDTO.setBeginTime(beginTime);
				} else if (i == 3) {
					// 课程结束时间
					Date endTime = null;
					try {
						endTime = DateUtils.getDateParse(str, DateUtils.YYYY_MM_DD_HH_MM_SS);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (endTime == null || customDTO.getBeginTime() == null
							|| endTime.getTime() <= customDTO.getBeginTime().getTime()) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：课程结束时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customDTO.setEndTime(endTime);
				} else if (i == 4) {
					// 课程介绍
					customDTO.setLessonRcmdStr(str);
				}
			}
			// 课程下课时信息，模板格式6个字段
			if (i > 5) {
				if ((lessonDetail == null || lessonDetail.size() != 6) && NumberUtils.isNumber(lessonDetail.get(0))) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：数据格式不合法");
					errorList.add(fileErrorDTO);
					continue;
				}
				if (NumberUtils.isNumber(lessonDetail.get(0))) {
					// 课时
					CustomClassDTO customClassDTO = new CustomClassDTO();
					// 课时名称
					customClassDTO.setClassName(lessonDetail.get(1));
					// 导师
					String tutorName = lessonDetail.get(2);
					TutorDTO tutorDTO = tutorService.getTutorByAccount(tutorName);
					if (tutorDTO == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：导师不存在");
						errorList.add(fileErrorDTO);
						continue;
					}

					if (tutorList == null || tutorList.size() == 0) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：该行业未配置导师");
						errorList.add(fileErrorDTO);
						continue;
					}

					boolean haveTutor = false;
					for (TutorDTO tutor : tutorList) {
						if (tutor.getTutorId() == tutorDTO.getTutorId()) {
							haveTutor = true;
						}
					}

					if (!haveTutor) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：导师与所选行业不匹配");
						errorList.add(fileErrorDTO);
						continue;
					}

					customClassDTO.setTutorId(tutorDTO.getTutorId());
					customClassDTO.setTutorName(tutorDTO.getRealName());
					// 计划上课时间
					Date planTime = null;
					try {
						planTime = DateUtils.getDateParse(lessonDetail.get(3), DateUtils.YYYY_MM_DD);
					} catch (Exception e) {
						log.error("时间转换异常：", e);
					}
					if (planTime == null) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：计划上课时间不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customClassDTO.setPlanTime(planTime);
					// 计划课时
					if (!NumberUtils.isNumber(lessonDetail.get(4))) {
						fileErrorDTO.setDataNum(i);
						fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：计划时长不合法");
						errorList.add(fileErrorDTO);
						continue;
					}
					customClassDTO.setPlanDuration(Long.parseLong(lessonDetail.get(4)));
					customClassDTO.setClassRcmd(lessonDetail.get(4));

					newAttrList.add(customClassDTO);
				}
			}
		}
		if (errorList.size() > 0) {
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		if (newAttrList.size() <= 0) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg("请传入有效文件！");
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}
		List<CustomClassDTO> attrList = customDTO.getAttrList();
		if (newAttrList.size() < attrList.size()) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			fileErrorDTO.setErrorMsg("课程课时不合法！");
			errorList.add(fileErrorDTO);
			return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
		}

		// 课时校验，已完结课时不能编辑
		List<CustomClassDTO> customClassList = new ArrayList<>();
		for (int i = 0; i < newAttrList.size(); i++) {
			if ((attrList.size() - 1) > i) {
				CustomClassDTO customClassDTO = attrList.get(i);
				// 约课以后，不能编辑
				if (customClassDTO.getDateTime() != null) {
					customClassList.add(customClassDTO);
					continue;
				}
				CustomClassDTO newCustomClassDTO = newAttrList.get(i);
				newCustomClassDTO.setClassId(customClassDTO.getClassId());
				customClassList.add(newCustomClassDTO);
			} else {
				customClassList.add(newAttrList.get(i));
			}
		}

		customDTO.setAttrList(customClassList);
		ResultDTO<String> resultStr = this.lessonClassValidate(customDTO);
		if (!resultStr.isSuccess()) {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, resultStr.getErrDesc());
		}

		lessonCustomService.updateLesson(customDTO);
		lessonCustomService.updateLessonRcmd(customDTO.getLessonId(), customDTO.getLessonRcmdStr());
		return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
	}

	/**
	 * 通过课时ID获取视频列表
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findVideoByClassId")
	public ResultDTO<List<CustomClassVideoDtlDTO>> findVideoByClassId(long classId) throws Exception {
		ResultDTO<List<CustomClassVideoDtlDTO>> result = new ResultDTO<List<CustomClassVideoDtlDTO>>();
		List<CustomClassVideoDtlDTO> list = customClassService.findVideoByClassId(classId);
		return result.set(ResultCodeEnum.SUCCESS, "", list);
	}

	/**
	 * 备课文件上传
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("readyFileUpload")
	public ResultDTO<String> readyFileUpload(HttpServletRequest request, long classId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		CustomClassDTO customClass = customClassService.getCustomClassById(classId);
		if (customClass == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课时不存在");
		}
		CustomDTO custom = lessonCustomService.getLessonById(customClass.getLessonId());

		String readyFileStr = "";
		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest userRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile readyFile = userRequest.getFile("readyFile");
			if (readyFile != null && StringUtils.isNotEmpty(readyFile.getOriginalFilename())) {
				FileInfoDTO resume = FileUtils.uploadFile(readyFile);
				if (resume != null) {
					readyFileStr = resume.getUrl();
				}
			}
		} else {
			readyFileStr = customClass.getReadyFile();
		}

		publicClassService.readyFileUpload(classId, readyFileStr);
		mssageService.baseSendMssage(customClass.getTutorId(), custom.getUserId(), custom.getLessonId() + "", 1, 3);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageParam
	 * @param CustomDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPagingByUserId")
	public Page<CustomDTO> findPagingByUserId(BootStrapTable<CustomDTO> pageParam, CustomDTO customDTO)
			throws Exception {
		if (customDTO.getTutorId() > 0) {
			return lessonCustomService.fingPagingByTutorId(pageParam, customDTO);
		}

		customDTO.setBuy(true);
		return lessonCustomService.fingPaging(pageParam, customDTO);
	}
}
