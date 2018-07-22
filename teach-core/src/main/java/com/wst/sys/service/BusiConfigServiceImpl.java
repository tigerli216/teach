/*
 * BusiConfigServiceImpl.java Created on 2016年10月16日 下午5:43:11
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;
import com.wst.sys.dao.BusiConfigDao;

/**
 * 公用业务配置Service
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class BusiConfigServiceImpl implements BusiConfigService {

	@Resource
	private BusiConfigDao BusiConfigDao;

	@Override
	public List<BusiConfigDTO> getConfigList(BusiConfigDTO busiConfigDTO) throws Exception {
		return BusiConfigDao.findBusiConfigList(busiConfigDTO);
	}

	@Override
	public List<BusiConfigDTO> findConfigList(String busiCode) throws Exception {
		return BusiConfigDao.findBusiConfigList(busiCode);
	}

	@Override
	public Page<BusiConfigDTO> findPaging(Page<BusiConfigDTO> page, BusiConfigDTO busiConfigDTO) throws Exception {
		return BusiConfigDao.findPaging(page, busiConfigDTO);
	}

	@Override
	public ResultDTO<String> save(BusiConfigDTO busiConfigDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		BusiConfigDao.save(busiConfigDTO);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public ResultDTO<String> update(BusiConfigDTO busiConfigDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		BusiConfigDao.update(busiConfigDTO);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	@Override
	public int upConfigStatus(long status, long configId) throws Exception {
		return BusiConfigDao.updateStatus(status, configId);
	}

	@Override
	public BusiConfigDTO getBusiConfigBuId(long configId) throws Exception {
		BusiConfigDTO busiConfigDTO = BusiConfigDao.getBusiConfigById(configId);
		if (busiConfigDTO != null) {
			String params = busiConfigDTO.getParams();
			if (params != null) {
				JSONObject jsonObject = JSONObject.parseObject(params);
				busiConfigDTO.setWechatNumber(jsonObject.getString("wechatNumber"));
				if (busiConfigDTO.getBusiType() == 1) {// 定制课咨询配置
					busiConfigDTO.setqRCodeUrl(jsonObject.getString("qRCodeUrl"));
				} else if (busiConfigDTO.getBusiType() == 2) {
					busiConfigDTO.setNickName(jsonObject.getString("nickName"));
				}
			}
		}
		return busiConfigDTO;
	}

	@Override
	public BusiConfigDTO getConfigByBusiType(long busiType) throws Exception {
		return BusiConfigDao.getConfigByBusiType(busiType);
	}

}
