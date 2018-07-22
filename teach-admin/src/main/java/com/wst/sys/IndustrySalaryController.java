/*
 * IndustrySalaryController.java Created on 2017年9月26日 上午10:14:21
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
package com.wst.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.dto.IndustrySalaryDTO;
import com.wst.service.sys.service.IndustrySalaryService;
import com.wst.service.sys.service.IndustryService;
import com.wst.sm.util.OperatorUtil;

/**
 * 导师行业基础薪资管理(增、删、改、查)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/industrySalary")
public class IndustrySalaryController {

	@Reference(version = "0.0.1")
	private IndustrySalaryService industrySalaryService;

	@Reference(version = "0.0.1")
	private IndustryService industryService;

	/**
	 * 导师行业基础薪资管理首页
	 * 
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public String publicIndex(HttpServletRequest request, ModelMap modelMap) throws Exception {
		List<IndustryDTO> industryList = industryService.findIndustryList();
		modelMap.put("industryList", industryList);
		return "sys/industrySalary";
	}

	/**
	 * 获取分页
	 * 
	 * @param pageParam
	 * @param industrySalaryDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<IndustrySalaryDTO> findPaging(BootStrapTable<IndustrySalaryDTO> pageParam,
			IndustrySalaryDTO industrySalaryDTO) throws Exception {
		return industrySalaryService.findPaging(pageParam, industrySalaryDTO);
	}

	/**
	 * 新增或修改
	 * 
	 * @param salDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrUpdate")
	public ResultDTO<String> addOrUpdate(HttpServletRequest request, IndustrySalaryDTO industrySalaryDTO)
			throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (industrySalaryDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "参数不能为空");
		}
		if (industrySalaryDTO.getBaseSalary() <= 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "基本薪资不能小于等于0");
		}

		OperatorDTO operatorDTO = OperatorUtil.getOperator(request);

		// 修改
		if (industrySalaryDTO.getConfigId() > 0) {
			industrySalaryDTO.setModifyOp(operatorDTO.getOpId());
			result = industrySalaryService.updateIndustrySalary(industrySalaryDTO);
		} else { // 新增
			industrySalaryDTO.setCreateOp(operatorDTO.getOpId());
			result = industrySalaryService.addIndustrySalary(industrySalaryDTO);
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public ResultDTO<String> deleteByConfigId(long configId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (configId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "配置ID不能为空");
		}
		IndustrySalaryDTO sdsDTO = industrySalaryService.getIndustrySalary(configId);
		if (sdsDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "配置ID不合法");
		}
		industrySalaryService.delete(configId);

		return result.set(ResultCodeEnum.SUCCESS, "删除成功");
	}

	/**
	 * 通过配置ID获取导师行业基础薪资信息
	 * 
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getIndustrySalary")
	public IndustrySalaryDTO getIndustrySalary(long configId) throws Exception {
		return industrySalaryService.getIndustrySalary(configId);
	}
}
