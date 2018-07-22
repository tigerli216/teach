/*
 * RelOpOrgServiceImpl.java Created on 2016年10月26日 下午4:58:46
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
package com.wst.sm.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.service.sm.dto.RelOpOrgDTO;
import com.wst.service.sm.service.RelOpOrgService;
import com.wst.sm.dao.RelOpOrgDao;

/**
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class RelOpOrgServiceImpl implements RelOpOrgService {

    @Resource
    private RelOpOrgDao relOpOrgDao;

    @Override
    public List<RelOpOrgDTO> findRelOpOrgListByOpId(long opId) throws Exception {
        return relOpOrgDao.findRelOpOrgListByOpId(opId);
    }

}
