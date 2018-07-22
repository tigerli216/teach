/*
 * AdServiceImpl.java Created on 2017年9月26日 下午8:26:31
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
package com.wst.operate.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.operate.dao.AdDao;
import com.wst.service.operate.dto.AdDTO;
import com.wst.service.operate.service.AdService;

/**
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class AdServiceImpl implements AdService {

	@Resource
	private AdDao adDao;

	@Override
	public Page<AdDTO> findPaging(Page<AdDTO> pageParam, AdDTO adDTO) throws Exception {
		return adDao.findPaging(pageParam, adDTO);
	}

	@Override
	public ResultDTO<String> save(AdDTO adDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		adDao.save(adDTO); // 新增
		return result.set(ResultCodeEnum.SUCCESS, "新增成功");
	}

	@Override
	public ResultDTO<String> update(AdDTO adDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		adDao.update(adDTO); // 修改
		return result.set(ResultCodeEnum.SUCCESS, "修改成功");
	}

	@Override
	public ResultDTO<String> delete(long adId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		AdDTO adDTO = adDao.getAdById(adId);
		if (adDTO == null)
			return result.set(ResultCodeEnum.ERROR_HANDLE, "广告ID不合法");

		adDao.delete(adId); // 删除
		return result.set(ResultCodeEnum.SUCCESS, "删除成功");
	}

	@Override
	public AdDTO getAdById(long adId) throws Exception {
		return adDao.getAdById(adId);
	}

	@Override
	public List<Map<String, Object>> findAds(Page<AdDTO> param, boolean isHomePage) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<>();

		AdDTO ad = new AdDTO();
		ad.setHomePage(isHomePage); // 是否首页
		ad.setAdStatus(1); // 状态 1. 有效
		ad.setAdPosition(1); // 广告位（1-首页banner）
		Page<AdDTO> adPage = adDao.findPaging(param, ad);
		List<AdDTO> adDTOList = adPage.getList();

		if (adDTOList != null && adDTOList.size() > 0) {
			for (AdDTO adDTO : adDTOList) {
				Map<String, Object> adMap = new HashMap<>();
				adMap.put("adId", adDTO.getAdId()); // 广告id
				adMap.put("adPosition", adDTO.getAdPosition()); // 广告位
				adMap.put("adPic", adDTO.getAdPic()); // 广告图片
				adMap.put("adUrl", adDTO.getAdUrl()); // 广告链接
				adMap.put("adName", adDTO.getAdName()); // 广告名称

				resultList.add(adMap);
			}
		}
		return resultList;
	}
}
