/*
 * IndustrySalaryServiceImpl.java Created on 2017年9月26日 上午11:20:24
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
package com.wst.sys.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.dto.IndustrySalaryDTO;
import com.wst.service.sys.service.IndustrySalaryService;
import com.wst.service.sys.service.IndustryService;
import com.wst.sys.dao.IndustrySalaryDao;

/**
 * 导师行业薪资管理Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class IndustrySalaryServiceImpl implements IndustrySalaryService {

	@Resource
	private IndustryService industryService;

	@Resource
	private IndustrySalaryDao industrySalaryDao;

	@Override
	public ResultDTO<String> addIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (industrySalaryDTO.getIndustryId() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "行业ID不能为空");
		}

		if (industrySalaryDTO.getCreateOp() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "创建人不能为空");
		}

		if (industrySalaryDTO.getStatus() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "状态不能为空");
		}

		IndustryDTO industryDTO = industryService.getIndustryById(industrySalaryDTO.getIndustryId());
		if (industryDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "行业ID不合法");
		}

		IndustrySalaryDTO idsDTO = industrySalaryDao.getIndustrySalaryByIndustryId(industrySalaryDTO.getIndustryId());
		if (idsDTO != null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "该行业已配置");
		}

		industrySalaryDao.addIndustrySalary(industrySalaryDTO);

		return result.set(ResultCodeEnum.SUCCESS, "添加成功");
	}

	@Override
	public ResultDTO<String> updateIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		if (industrySalaryDTO.getIndustryId() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "行业名称不能为空");
		}

		if (industrySalaryDTO.getConfigId() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "配置ID不能为空");
		}

		if (industrySalaryDTO.getStatus() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "状态不能为空");
		}

		IndustrySalaryDTO sdsDTO = industrySalaryDao.getIndustrySalaryByConfigId(industrySalaryDTO.getConfigId());
		if (sdsDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "配置ID不合法");
		}

		IndustrySalaryDTO idsDTO = industrySalaryDao.getIndustrySalaryByIndustryId(industrySalaryDTO.getIndustryId());
		if (idsDTO != null && idsDTO.getConfigId() != industrySalaryDTO.getConfigId()) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "该行业已配置");
		}

		industrySalaryDao.updateIndustrySalary(industrySalaryDTO);

		return result.set(ResultCodeEnum.SUCCESS, "修改成功");
	}

	@Override
	public Page<IndustrySalaryDTO> findPaging(Page<IndustrySalaryDTO> pageParam, IndustrySalaryDTO industrySalaryDTO)
			throws Exception {
		return industrySalaryDao.findPaging(pageParam, industrySalaryDTO);
	}

	@Override
	public IndustrySalaryDTO getIndustrySalary(long configId) throws Exception {
		return industrySalaryDao.getIndustrySalaryByConfigId(configId);
	}

	@Override
	public int delete(long configId) throws Exception {
		return industrySalaryDao.deleteByConfigId(configId);
	}

	@Override
	public IndustrySalaryDTO getIndustrySalaryByIndustryCode(String industryCode) throws Exception {
		return industrySalaryDao.getIndustrySalaryByIndustryCode(industryCode);
	}

	@Override
	public IndustrySalaryDTO getIndustrySalaryByIndustryId(long industryId) throws Exception {
		return industrySalaryDao.getIndustrySalaryByIndustryId(industryId);
	}
}
