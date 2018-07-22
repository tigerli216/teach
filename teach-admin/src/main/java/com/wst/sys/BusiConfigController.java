/*
 * BusiConfigController.java Created on 2017年10月25日 下午4:33:17
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

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;

/**
 * 业务系统配置
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/busiConfig")
public class BusiConfigController {

	@Reference(version = "0.0.1")
	private BusiConfigService busiConfigService;

	/**
	 * 首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request) throws Exception {
		return "sys/busiConfig";
	}

	/**
	 * 业务系统配置分页列表
	 * 
	 * @param page
	 * @param busiLimitDto
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<BusiConfigDTO> findPaging(BootStrapTable<BusiConfigDTO> page, BusiConfigDTO busiConfigDTO)
			throws Exception {
		return busiConfigService.findPaging(page, busiConfigDTO);
	}

	/**
	 * 保存或修改配置
	 * 
	 * @param busiConfigDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrUpdate")
	public ResultDTO<String> saveOrUpdate(HttpServletRequest request, BusiConfigDTO busiConfigDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (busiConfigDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}

		JSONObject json = new JSONObject();
		json.put("wechatNumber", busiConfigDTO.getWechatNumber());
		
		// 定制课咨询配置
		if (busiConfigDTO.getBusiType() == 1) {
			if (request instanceof StandardMultipartHttpServletRequest) {
				StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
				MultipartFile qRCodeFile = standardRequest.getFile("qRCodeFile");
				if (qRCodeFile == null) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传二维码图片");
				}
				ImageInfoDTO imageInfo = FileUtils.uploadImage(qRCodeFile);// 二维码图片
				if (CommonFuntions.isNotEmptyObject(imageInfo)) {
					if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
						json.put("qRCodeUrl", imageInfo.getPicUrl());
						busiConfigDTO.setParams(json.toJSONString());
					}
				}
			} else {
				if (busiConfigDTO.getConfigId() > 0) {
					BusiConfigDTO busiConfig = busiConfigService.getBusiConfigBuId(busiConfigDTO.getConfigId());
					if (busiConfig.getParams() == null) {
						return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传二维码图片");
					}
					busiConfigDTO.setParams(busiConfig.getParams());
				}else{
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传课程图片");
				}
			}
			
		} else { // 活动咨询配置
			json.put("nickName", busiConfigDTO.getNickName());
			busiConfigDTO.setParams(json.toString());
		}

		// 修改
		if (busiConfigDTO.getConfigId() > 0) {
			result = busiConfigService.update(busiConfigDTO);
		} else { // 新增
			result = busiConfigService.save(busiConfigDTO);
		}

		return result;
	}

	/**
	 * 修改配置状态
	 * 
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "upConfigStatus")
	public ResultDTO<String> upConfigStatus(long status, long configId, long busiType) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		if (status == 0 || configId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}

		// 开启
		if (status == 1) {
			BusiConfigDTO busiConfig = busiConfigService.getConfigByBusiType(busiType);
			if (busiConfig != null) {
				return result.set(ResultCodeEnum.ERROR_HANDLE, "已有开启配置，请先关闭");
			}
		}

		busiConfigService.upConfigStatus(status, configId);
		return result.set(ResultCodeEnum.SUCCESS, "修改成功");
	}

	/**
	 * 通过ID获取系统业务配置信息
	 * 
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getBusiConfigBuId")
	public BusiConfigDTO getBusiConfigBuId(long configId) throws Exception {
		return busiConfigService.getBusiConfigBuId(configId);
	}
}
