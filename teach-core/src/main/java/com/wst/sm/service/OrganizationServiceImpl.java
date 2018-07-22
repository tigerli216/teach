/*
 * organizationServiceImpl.java Created on 2016年10月25日 下午8:16:03
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
import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.OrganizationDTO;
import com.wst.service.sm.service.OrganizationService;
import com.wst.sm.dao.OrganizationDao;

/**
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationDao organizationDao;

    @Override
    public long save(OrganizationDTO organizationDTO) throws Exception {
        return organizationDao.save(organizationDTO);
    }

    @Override
    public List<OrganizationDTO> findOrganizationList() throws Exception {
        return organizationDao.findOrganizationList();
    }

    @Override
    public List<OrganizationDTO> findOrganizationListByParentId(long parentId) throws Exception {
        return organizationDao.findOrganizationListByParentId(parentId);
    }

    @Override
    public OrganizationDTO getOrganizationByOrgId(long orgId) throws Exception {
        return organizationDao.getOrganizationByOrgId(orgId);
    }

    @Override
    public Page<OrganizationDTO> findOrganizationPaging(Page<OrganizationDTO> pageParam, OrganizationDTO organizationDTO)
            throws Exception {
        return organizationDao.findOrganizationPaging(pageParam, organizationDTO);
    }

    @Override
    public int updateOrganization(OrganizationDTO organizationDTO) throws Exception {
        return organizationDao.updateOrganization(organizationDTO);
    }

}
