/*
 * ResourceService.java Created on 2016年10月26日 下午9:51:33
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
import com.wst.service.sm.dto.ResourceDTO;

/**
 * 功能菜单服务
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface ResourceService {

    /**
     * 保存数据
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long save(ResourceDTO resourceDTO) throws Exception;

    /**
     * 查询所有菜单
     * 
     * @return
     * @throws Exception
     */
    public List<ResourceDTO> findResourceList() throws Exception;

    /**
     * 根据主键查询信息
     * 
     * @param resourceId
     * @return
     * @throws Exception
     */
    public ResourceDTO getByResourceId(long resourceId) throws Exception;

    /**
     * 根据父ID查询子信息
     * 
     * @param parentId
     * @return
     * @throws Exception
     */
    public List<ResourceDTO> findResourceListByParentId(long parentId) throws Exception;

    /**
     * 分页查询信息
     * 
     * @param pageParam
     *            分页参数
     * @param resourceDTO
     *            相关查询参数
     * @return
     * @throws Exception
     */
    public Page<ResourceDTO> findOResourcePaging(Page<ResourceDTO> pageParam, ResourceDTO resourceDTO) throws Exception;

    /**
     * 校验功能名称是否存在
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long checkResourceTitle(ResourceDTO resourceDTO) throws Exception;

    /**
     * 校验功能路径是否存在
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public long checkResourceUrl(ResourceDTO resourceDTO) throws Exception;

    /**
     * 修改信息
     * 
     * @param resourceDTO
     * @return
     * @throws Exception
     */
    public int updateResource(ResourceDTO resourceDTO) throws Exception;

    /**
     * 根据主键删除功能菜单
     * 
     * @param resourceId
     * @return
     * @throws Exception
     */
    public int deleteResource(long resourceId) throws Exception;

    /**
     * 修改排序顺序
     * 
     * @param resourceList
     *            父Id
     * @throws Exception
     */
    public void updateSort(List<ResourceDTO> resourceList) throws Exception;

}
