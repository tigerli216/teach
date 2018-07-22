/*
 * TutorController.java Created on 2017年9月27日 下午7:35:24
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.FileInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.TutorSalaryRecordDTO;
import com.wst.service.mem.service.TutorSalaryRecordService;
import com.wst.service.mem.service.TutorService;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.service.IndustryService;
import com.wst.service.sys.service.RegionService;
import com.wst.sm.util.OperatorUtil;

/**
 * 导师管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/mem/tutor")
public class TutorController {

	@Reference(version = "0.0.1")
	private TutorService tutorService;

	@Reference(version = "0.0.1")
	private TutorSalaryRecordService tutorSalaryRecordService;

	@Reference(version = "0.0.1")
	private IndustryService industryService;

	@Reference(version = "0.0.1")
	private RegionService regionService;

	/**
	 * 导师信息总览首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("summaryIndex")
	public String tutorSummary(ModelMap modelMap) throws Exception {
		List<IndustryDTO> industryList = industryService.findIndustryList();
		List<RegionDTO> regionList = regionService.findRegionList();
		modelMap.put("industryList", industryList);
		modelMap.put("regionList", regionList);
		return "mem/tutorSummary";
	}

	/**
	 * 导师信息管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String tutor(ModelMap modelMap) throws Exception {
		List<IndustryDTO> industryList = industryService.findIndustryList();
		List<RegionDTO> regionList = regionService.findRegionList();
		modelMap.put("industryList", industryList);
		modelMap.put("regionList", regionList);
		return "mem/tutor";
	}

	/**
	 * 注册导师审核首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("examineIndex")
	public String tutorExamine(ModelMap modelMap) throws Exception {
		return "mem/tutor_examine";
	}

	/**
	 * 获取导师分页数据
	 * 
	 * @param pageParam
	 * @param userDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<TutorDTO> findPaging(BootStrapTable<TutorDTO> pageParam, TutorDTO tutorDTO) throws Exception {
		return tutorService.findPaging(pageParam, tutorDTO);
	}

	/**
	 * 注册导师审核
	 * 
	 * @param tutorId
	 * @param examineStatus
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "examine")
	public ResultDTO<String> examine(HttpServletRequest request, long confirm, long tutorId, long tutorSalary,
			String changeExplain) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 校验tutorId是否合法
		TutorDTO tutor = tutorService.getTutorById(tutorId);
		if (tutor == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "导师ID不合法");
		}
		tutorService.updateExamineStatus(tutorId, confirm);

		if (tutorSalary <= 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "导师薪资不能小于等于");
		}
		tutorService.updateTutorSalary(tutorId, tutorSalary);
		OperatorDTO operatorDTO = OperatorUtil.getOperator(request);
		TutorSalaryRecordDTO tutorSalaryRecordDTO = new TutorSalaryRecordDTO();
		// 调整前薪资
		tutorSalaryRecordDTO.setBeforSalary(tutor.getTutorSalary());
		tutorSalaryRecordDTO.setChangeExplain(changeExplain);
		tutorSalaryRecordDTO.setChangeOp(operatorDTO.getOpId());
		// 调整后薪资
		tutorSalaryRecordDTO.setEndSalary(tutorSalary);
		tutorSalaryRecordDTO.setTutorId(tutorId);
		tutorSalaryRecordService.save(tutorSalaryRecordDTO);

		return result.set(ResultCodeEnum.SUCCESS, "审核完成");
	}

	/**
	 * 修改导师信息
	 * 
	 * @param tutorId
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "updateTutor")
	public ResultDTO<String> updateTutor(HttpServletRequest request, TutorDTO tutorDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 导师薪资不能小于等于0
		if (tutorDTO.getTutorSalary() <= 0) {
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS,
					"The salary of a mentor cannot be less than or equal to 0");
		}

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest userRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile contractFile = userRequest.getFile("contractFile");
			if (contractFile != null && StringUtils.isNotEmpty(contractFile.getOriginalFilename())) {
				FileInfoDTO contract = FileUtils.uploadFile(contractFile);
				if (contract != null) {
					tutorDTO.setContractUrl(contract.getUrl());
				}
			}
		}

		TutorDTO tutor = tutorService.getTutorById(tutorDTO.getTutorId());
		tutorService.upTutor(tutorDTO);

		OperatorDTO operatorDTO = OperatorUtil.getOperator(request);

		if (tutorDTO.getTutorSalary() == tutor.getTutorSalary()) {
			return result.set(ResultCodeEnum.SUCCESS, "success");
		}

		TutorSalaryRecordDTO tutorSalaryRecordDTO = new TutorSalaryRecordDTO();
		// 调整前薪资
		tutorSalaryRecordDTO.setBeforSalary(tutor.getTutorSalary());
		// 调整说明
		tutorSalaryRecordDTO.setChangeExplain(tutorDTO.getChangeExplain());
		// 调整人
		tutorSalaryRecordDTO.setChangeOp(operatorDTO.getOpId());
		// 调整后薪资
		tutorSalaryRecordDTO.setEndSalary(tutorDTO.getTutorSalary());
		// 导师ID
		tutorSalaryRecordDTO.setTutorId(tutorDTO.getTutorId());
		// 生成导师薪资更改记录
		tutorSalaryRecordService.save(tutorSalaryRecordDTO);

		return result.set(ResultCodeEnum.SUCCESS, "success");
	}

	/**
	 * 通过导师ID获取导师信息
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getTutor")
	public TutorDTO getTutor(long tutorId) throws Exception {
		return tutorService.getTutorById(tutorId);
	}

	/**
	 * 获取导师薪资调整列表
	 * 
	 * @param pageParam
	 * @param tutorSalaryRecordDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findTutorSalaryPaging")
	public Page<TutorSalaryRecordDTO> findTutorSalaryPaging(BootStrapTable<TutorSalaryRecordDTO> pageParam,
			TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception {
		return tutorService.findTutorSalaryPaging(pageParam, tutorSalaryRecordDTO);
	}
}
