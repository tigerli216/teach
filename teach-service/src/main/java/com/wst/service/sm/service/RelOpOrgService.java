/*
 * RelOpOrgService.java Created on 2016年10月26日 下午4:54:29
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
package com.wst.service.sm.service;

import java.util.List;

import com.wst.service.sm.dto.RelOpOrgDTO;

/**
 * 用户与组织关系服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface RelOpOrgService {

    /**
     * 根据用户ID获取用户关联组织
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public List<RelOpOrgDTO> findRelOpOrgListByOpId(long opId) throws Exception;

}
