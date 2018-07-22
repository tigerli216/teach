/*
 * OperatorController.java Created on 2016年10月24日 下午4:22:56
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
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.OrganizationDTO;
import com.wst.service.sm.dto.ZtreeDTO;
import com.wst.service.sm.service.OrganizationService;
import com.wst.sm.util.OperatorUtil;

/**
 * 组织控制器
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/org")
public class OrganizationController {

    @Reference(version = "0.0.1")
    private OrganizationService organizationService;

    /**
     * 首页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap) throws Exception {
        modelMap.addAttribute("orgTree", JsonUtils.getJson(organizationTree(true)));
        return "sm/organization";
    }

    /**
     * 获取组织树
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findOrganizationTree")
    public List<ZtreeDTO> findOrganizationTree(boolean treeOpen) throws Exception {
        return organizationTree(treeOpen);
    }

    /**
     * 根据组织ID获取信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getOrganizationByOrgId")
    public OrganizationDTO getOrganizationByOrgId(long orgId) throws Exception {
        return organizationService.getOrganizationByOrgId(orgId);
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
    public ResultDTO<String> edit(HttpServletRequest request, OrganizationDTO organizationDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (CommonFuntions.isEmptyObject(organizationDTO.getOrgName())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "组织名称为空");
        }
        if (organizationDTO.getOrgId() == 0 && organizationDTO.getParentId() == 0) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "父标识为空");
        }
        // 当前登录人
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (organizationDTO.getOrgId() == 0) {
            organizationDTO.setStatus(0);
            organizationDTO.setCreateOpId(oper.getOpId());
            organizationDTO.setCreateTime(new Date());
            organizationService.save(organizationDTO);
        } else {
            organizationDTO.setModifyOpId(oper.getOpId());
            organizationDTO.setModifyTime(new Date());
            organizationService.updateOrganization(organizationDTO);
        }
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    /**
     * 获取分页列表
     * 
     * @param pageParam
     * @param orgQuery
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findPaging")
    public Page<OrganizationDTO> findPaging(BootStrapTable<OrganizationDTO> pageParam, OrganizationDTO orgQuery)
            throws Exception {
        return organizationService.findOrganizationPaging(pageParam, orgQuery);
    }

    /**
     * 组织树
     * 
     * @param treeOpen
     * @return
     * @throws Exception
     */
    private List<ZtreeDTO> organizationTree(boolean treeOpen) throws Exception {
        List<OrganizationDTO> orgList = organizationService.findOrganizationList();
        List<ZtreeDTO> ztreeList = new ArrayList<ZtreeDTO>();
        for (OrganizationDTO organizationDTO : orgList) {
            ZtreeDTO ztreeDTO = new ZtreeDTO();
            ztreeDTO.setId(organizationDTO.getOrgId());
            ztreeDTO.setpId(organizationDTO.getParentId());
            ztreeDTO.setName(organizationDTO.getOrgName());
            ztreeDTO.setOpen(treeOpen);
            ztreeList.add(ztreeDTO);
        }
        return ztreeList;
    }
}
