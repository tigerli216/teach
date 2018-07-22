/*
 * AdController.java Created on 2017年9月26日 下午8:18:10
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
import com.wst.service.operate.dto.AdDTO;
import com.wst.service.operate.service.AdService;

/**
 * 广告管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/operate/ad")
public class AdController {

	@Reference(version = "0.0.1")
	private AdService adService;

	/**
	 * 广告管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("index")
	public String index() throws Exception {
		return "operate/ad";
	}

	/**
	 * 获取广告分页数据
	 * 
	 * @param pageParam
	 * @param adDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "finndPaging")
	public Page<AdDTO> findPaging(BootStrapTable<AdDTO> pageParam, AdDTO adDTO) throws Exception {
		return adService.findPaging(pageParam, adDTO);
	}

	// 保存/修改公告信息
	@ResponseBody
	@RequestMapping(value = "saveOrUpdate")
	public ResultDTO<String> saveOrUpdate(HttpServletRequest request, AdDTO adDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		result = adDTOMatch(adDTO);
		if (!result.isSuccess()) {
			return result;
		}

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile adPicFile = standardRequest.getFile("adPicFile");
			if (adPicFile == null) {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传广告图片");
			}
			// 广告图片
			ImageInfoDTO imageInfo = FileUtils.uploadImage(adPicFile);
			if (CommonFuntions.isNotEmptyObject(imageInfo)) {
				if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
					adDTO.setAdPic(imageInfo.getPicUrl());
				}
			}
		} else {
			if (adDTO.getAdId() > 0) {
				AdDTO ad = adService.getAdById(adDTO.getAdId());
				if (StringUtils.isEmpty(ad.getAdPic())) {
					return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传广告图片");
				}
				adDTO.setAdPic(ad.getAdPic());
			} else {
				return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请上传广告图片");
			}
		}

		// 修改
		if (adDTO.getAdId() > 0) {
			result = adService.update(adDTO);
		} else { // 保存
			result = adService.save(adDTO);
		}
		return result;
	}

	/**
	 * 根据广告ID删除广告
	 * 
	 * @param adId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public ResultDTO<String> delete(long adId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (adId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "广告ID不能为空");
		}
		result = adService.delete(adId);
		return result;
	}

	/**
	 * 通过广告ID获取广告信息
	 * 
	 * @param adId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getAd")
	public AdDTO getAd(long adId) throws Exception {
		return adService.getAdById(adId);
	}

	/**
	 * 广告参数校验
	 * 
	 * @param ad
	 * @return
	 * @throws Exception
	 */
	private ResultDTO<String> adDTOMatch(AdDTO adDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		if (adDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}
		if (adDTO.getAdPosition() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "广告位不能为空");
		}
		if (StringUtils.isEmpty(adDTO.getAdUrl())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "广告链接为空");
		}
		adDTO.setAdUrl(adDTO.getAdUrl().trim());
		if (StringUtils.isEmpty(adDTO.getAdUrl()) || !StringUtils.isUrl(adDTO.getAdUrl())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "广告链接格式不合法");
		}
		if (StringUtils.isEmpty(adDTO.getAdName())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "广告名称不能为空");
		}
		if (adDTO.getWeight() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "权重值不能为空");
		}
		if (adDTO.getValidTime() == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "有效期不能为空");
		}
		if (adDTO.getAdStatus() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "状态不能为空");
		}

		return result.set(ResultCodeEnum.SUCCESS, "");
	}
}
