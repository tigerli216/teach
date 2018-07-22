/*
 * ResourceController.java Created on 2016年10月27日 上午9:53:50
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
package com.wst.sm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.TypeReference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.ResourceDTO;
import com.wst.service.sm.dto.ZtreeDTO;
import com.wst.service.sm.service.ResourceService;
import com.wst.sm.util.OperatorUtil;

/**
 * 功能菜单控制器
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/resource")
public class ResourceController {

    @Reference(version = "0.0.1")
    private ResourceService resourceService;

    /**
     * 首页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap) throws Exception {
        modelMap.addAttribute("resourceTree", JsonUtils.getJson(resourceTree(true)));
        return "sm/resource";
    }

    /**
     * 获取功能菜单树
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findResourceTree")
    public List<ZtreeDTO> findResourceTree(boolean treeOpen) throws Exception {
        return resourceTree(treeOpen);
    }

    /**
     * 查询功能菜单首页信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findResourceById")
    public ResourceDTO findResourceById(long resourceId) throws Exception {
        return resourceService.getByResourceId(resourceId);
    }

    /**
     * 获取分页列表
     * 
     * @param pageParam
     * @param resourceQuery
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findPaging")
    public Page<ResourceDTO> findPaging(BootStrapTable<ResourceDTO> pageParam, ResourceDTO resourceQuery)
            throws Exception {
        return resourceService.findOResourcePaging(pageParam, resourceQuery);
    }

    /**
     * 功能菜单树
     * 
     * @param treeOpen
     * @return
     * @throws Exception
     */
    private List<ZtreeDTO> resourceTree(boolean treeOpen) throws Exception {
        List<ResourceDTO> resourceList = resourceService.findResourceList();
        List<ZtreeDTO> ztreeList = new ArrayList<ZtreeDTO>();
        for (ResourceDTO resourceDTO : resourceList) {
            ZtreeDTO ztreeDTO = new ZtreeDTO();
            ztreeDTO.setId(resourceDTO.getResourceId());
            ztreeDTO.setpId(resourceDTO.getParentId());
            ztreeDTO.setName(resourceDTO.getResourceTitle());
            ztreeDTO.setOpen(treeOpen);
            ztreeList.add(ztreeDTO);
        }
        return ztreeList;
    }

    /**
     * 编辑（保存或更新）
     * 
     * @param request
     * @param resourceDTO
     * @param pId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("edit")
    public ResultDTO<String> edit(HttpServletRequest request, ResourceDTO resourceDTO, long pId) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (CommonFuntions.isEmptyObject(resourceDTO.getResourceTitle())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "功能 名称为空");
        }
        if (resourceDTO.getResourceId() == 0 && pId == 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "父标识为空");
        }
        // 功能 名称是否存在
        if (resourceService.checkResourceTitle(resourceDTO) > 0) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "功能名称已存在");
        }
        // 功能路径是否存在
        if (CommonFuntions.isNotEmptyObject(resourceDTO.getMenuUrl())
                && resourceService.checkResourceUrl(resourceDTO) > 0) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "功能路径已存在");
        }
        resourceDTO.setParentId(pId);
        // 当前登录人
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (resourceDTO.getResourceId() == 0) {
            resourceDTO.setStatus(0);
            resourceDTO.setCreateOpId(oper.getOpId());
            resourceDTO.setCreateTime(new Date());
            resourceService.save(resourceDTO);
        } else {
            resourceDTO.setModifyOpId(oper.getOpId());
            resourceDTO.setModifyTime(new Date());
            resourceService.updateResource(resourceDTO);
        }
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    /**
     * 删除
     * 
     * @param resourceId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("delete")
    public ResultDTO<String> delete(HttpServletRequest request, long resourceId) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        List<ResourceDTO> resourceList = resourceService.findResourceListByParentId(resourceId);
        if (CommonFuntions.isNotEmptyObject(resourceList) && resourceList.size() > 0) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "此功能下存在数据，不能删除！");
        }
        resourceService.deleteResource(resourceId);
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    /**
     * 更新菜单顺序
     * @param request
     * @param menuSortlist
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("updateSort")
    public ResultDTO<String> updateSort(HttpServletRequest request, String menuSortlist) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        List<ResourceDTO> resourceList = JsonUtils.jsonToObject(menuSortlist, new TypeReference<List<ResourceDTO>>() {});
         resourceService.updateSort(resourceList);
        return result.set(ResultCodeEnum.SUCCESS, "");
    }
}
