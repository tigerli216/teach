/*
 * organizationService.java Created on 2016年10月25日 下午8:15:35
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

import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.OrganizationDTO;

/**
 * 组织服务接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface OrganizationService {

    /**
     * 保存数据
     * 
     * @param organizationDTO
     * @return
     * @throws Exception
     */
    public long save(OrganizationDTO organizationDTO) throws Exception;

    /**
     * 查询所有有效组织
     * 
     * @return
     * @throws Exception
     */
    public List<OrganizationDTO> findOrganizationList() throws Exception;

    /**
     * 根据父ID查询所有有效组织
     * 
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<OrganizationDTO> findOrganizationListByParentId(long parentId) throws Exception;

    /**
     * 根据主键查询组织信息
     * 
     * @param orgId
     * @return
     * @throws Exception
     */
    public OrganizationDTO getOrganizationByOrgId(long orgId) throws Exception;

    /**
     * 分页查询信息
     * 
     * @param pageParam
     *            分页参数
     * @param organizationDTO
     *            相关查询参数
     * @return
     * @throws Exception
     */
    public Page<OrganizationDTO> findOrganizationPaging(Page<OrganizationDTO> pageParam, OrganizationDTO organizationDTO)
            throws Exception;

    /**
     * 更新信息
     * 
     * @param organizationDTO
     * @return
     * @throws Exception
     */
    public int updateOrganization(OrganizationDTO organizationDTO) throws Exception;

}
