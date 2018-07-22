/*
 * ResourceServiceImpl.java Created on 2016年10月26日 下午9:52:03
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
import com.wst.service.sm.dto.ResourceDTO;
import com.wst.service.sm.service.ResourceService;
import com.wst.sm.dao.ResourceDao;

/**
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private ResourceDao resourceDao;

    @Override
    public long save(ResourceDTO resourceDTO) throws Exception {
        return resourceDao.save(resourceDTO);
    }

    @Override
    public List<ResourceDTO> findResourceList() throws Exception {
        return resourceDao.findResourceList();
    }

    @Override
    public ResourceDTO getByResourceId(long resourceId) throws Exception {
        return resourceDao.getByResourceId(resourceId);
    }

    @Override
    public List<ResourceDTO> findResourceListByParentId(long parentId) throws Exception {
        return resourceDao.findResourceListByParentId(parentId);
    }

    @Override
    public Page<ResourceDTO> findOResourcePaging(Page<ResourceDTO> pageParam, ResourceDTO resourceDTO) throws Exception {
        return resourceDao.findOResourcePaging(pageParam, resourceDTO);
    }

    @Override
    public long checkResourceTitle(ResourceDTO resourceDTO) throws Exception {
        return resourceDao.checkResourceTitle(resourceDTO);
    }

    @Override
    public long checkResourceUrl(ResourceDTO resourceDTO) throws Exception {
        return resourceDao.checkResourceUrl(resourceDTO);
    }

    @Override
    public int updateResource(ResourceDTO resourceDTO) throws Exception {
        return resourceDao.updateResource(resourceDTO);
    }

    @Override
    public int deleteResource(long resourceId) throws Exception {
        return resourceDao.deleteResource(resourceId);
    }

    @Override
    public void updateSort(List<ResourceDTO> resourceList) throws Exception {
        for (int i = 0; i < resourceList.size(); i++) {
            ResourceDTO resource = resourceList.get(i);
            resourceDao.updateMenuSort(resource.getResourceId(), resource.getMenuSort());
        }
    }

}
