/*
 * RoleController.java Created on 2016年10月26日 上午10:44:36
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
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.ResourceDTO;
import com.wst.service.sm.dto.RoleDTO;
import com.wst.service.sm.dto.ZtreeDTO;
import com.wst.service.sm.service.ResourceService;
import com.wst.service.sm.service.RoleService;
import com.wst.sm.util.OperatorUtil;

/**
 * 角色控制器
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/role")
public class RoleController {

    private static HiwiLog log = HiwiLogFactory.getLogger(RoleController.class);

    @Reference(version = "0.0.1")
    private RoleService roleService;

    @Reference(version = "0.0.1")
    private ResourceService resService;

    /**
     * 首页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request) throws Exception {
        return "sm/role";
    }

    /**
     * 新增角色信息
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/addRole")
    public ResultDTO<String> addRole(@ModelAttribute RoleDTO role, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        // 无父节点直接返回
        if (role.getParentId() == 0) {
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "父Id为空");
        }
        // 当前登录人
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (role.getRoleId() == 0) {
            role.setStatus(0);
            role.setCreateOpId(oper.getOpId());
            role.setCreateTime(new Date());
        }
        // 角色名是否重复
        boolean b = roleService.ifRepeatName(role);
        if (b == false) {
            return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "角色名已存在");
        }
        // 上一级角色
        RoleDTO parentRoleDO = roleService.findRoleById(role.getParentId());

        // 供货商与上级一致
        role.setSupplierId(parentRoleDO.getSupplierId());
        // 级别,根据path获取
        if (CommonFuntions.isEmptyObject(parentRoleDO.getRolePath())) {
            // role.setRoleLevel(parentRoleDO.getRoleId());
            role.setRoleLevel(1);
            String rolePath = "#" + parentRoleDO.getRoleId() + "#";
            role.setRolePath(rolePath);
        } else {
            // role.setRoleLevel(((Integer)
            // (parentRoleDO.getRolePath().split("#").length)).longValue());
            role.setRoleLevel(parentRoleDO.getRoleLevel() + 1);
            role.setRolePath(parentRoleDO.getRolePath());
        }

        String resIdListStr = role.getResIdList();
        String orgIdListStr = role.getOrgIdList();

        roleService.addRole(role, resIdListStr, orgIdListStr);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "角色新增成功");
    }

    /**
     * 修改角色信息
     * 
     * @param role
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/updateRole")
    public ResultDTO<Object> updateRole(@ModelAttribute RoleDTO role, HttpServletRequest request) throws Exception {
        ResultDTO<Object> resultDTO = new ResultDTO<Object>();
        // 登录帐号
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (null == oper) {
            return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
        }
        if (role.getRoleId() == 0) {
            role.setModifyOpId(oper.getOpId());
            role.setModifyTime(new Date());
        }
        // TODO 验证操作权限

        // 角色名是否重复
        boolean b = roleService.ifRepeatName(role);
        if (b == false) {
            return resultDTO.set(ResultCodeEnum.ERROR_AUTH, "角色名已被使用");
        }

        String resIdListStr = role.getResIdList();
        String orgIdListStr = role.getOrgIdList();

        roleService.updateRole(role, resIdListStr, orgIdListStr);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "修改成功");
    }

    /**
     * 获取组织树
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findRoleTree")
    public List<ZtreeDTO> findRoleTree(boolean treeOpen) throws Exception {
        List<RoleDTO> roleList = roleService.findRoleList();
        List<ZtreeDTO> ztreeList = new ArrayList<ZtreeDTO>();
        for (RoleDTO roleDTO : roleList) {
            ZtreeDTO ztreeDTO = new ZtreeDTO();
            ztreeDTO.setId(roleDTO.getRoleId());
            ztreeDTO.setName(roleDTO.getRoleName());
            ztreeDTO.setOpen(treeOpen);
            ztreeDTO.setpId(roleDTO.getParentId());
            ztreeList.add(ztreeDTO);
        }
        return ztreeList;
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
    public Page<RoleDTO> findPaging(BootStrapTable<RoleDTO> pageParam, RoleDTO roleQuery) throws Exception {
        return roleService.findRolePaging(pageParam, roleQuery);
    }

    /**
     * 查询功能菜单首页信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findRoleById")
    public RoleDTO findRoleById(long roleId) throws Exception {
        return roleService.findRoleById(roleId);
    }

    /**
     * 查询功能菜单首页信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findRoleByParentId")
    public RoleDTO findRoleByParentId(long parentId) throws Exception {
        return roleService.findRoleByPrentId(parentId);
    }

    /**
     * 创建功能菜单树
     * 
     * @param treeOpen
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findTreeRes")
    public List<ZtreeDTO> findTreeRes(boolean treeOpen) throws Exception {
        List<ResourceDTO> reSourceList = resService.findResourceList();
        List<ZtreeDTO> ztreeList = new ArrayList<ZtreeDTO>();
        for (ResourceDTO resource : reSourceList) {
            ZtreeDTO ztreeDTO = new ZtreeDTO();
            ztreeDTO.setId(resource.getResourceId());
            ztreeDTO.setName(resource.getResourceTitle());
            ztreeDTO.setOpen(treeOpen);
            ztreeDTO.setpId(resource.getParentId());
            ztreeList.add(ztreeDTO);
        }
        return ztreeList;
    }

    /**
     * 通过角色Id查询角色拥有的菜单权限
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findRelResRoleByRoleId")
    public ResultDTO<List<Long>> findRelResRoleByRoleId(@ModelAttribute RoleDTO role, HttpServletRequest request)
            throws Exception {
        ResultDTO<List<Long>> resultDTO = new ResultDTO<List<Long>>();
        // 登录帐号
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (null == oper) {
            return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
        }
        // TODO 验证操作权限

        // 查询角色与菜单之间的关系信息
        List<Long> list = roleService.findRelResRoleByRoleId(role);
        resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功", list);
        return resultDTO;
    }

    /**
     * 通过角色Id查询角色拥有的组织
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/findRelOrgRoleByRoleId")
    public ResultDTO<List<Long>> findRelOrgRoleByRoleId(@ModelAttribute RoleDTO role, HttpServletRequest request)
            throws Exception {
        ResultDTO<List<Long>> resultDTO = new ResultDTO<List<Long>>();
        // 登录帐号
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (null == oper) {
            return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
        }
        // TODO 验证操作权限

        // 查询角色与组织之间的关系信息
        List<Long> list = roleService.findRelOrgRoleByRoleId(role);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功", list);
    }

    /**
     * 开启
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enableEvent")
    public ResultDTO<String> enableEvent(String roleIds, HttpServletRequest request) {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        try {
            // 登录帐号
            OperatorDTO oper = OperatorUtil.getOperator(request);
            if (null == oper) {
                return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
            }
            /** 参数校验 */
            if (CommonFuntions.isEmptyObject(roleIds)) {
                resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "参数为空");
                return resultDTO;
            }
            roleService.enableOrDisableRole(roleIds, 0L);
        } catch (Exception e) {
            log.error("角色启用发生系统异常", e);
            resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "操作系统异常");
        }
        return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
    }

    /**
     * 禁用
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/disableEvent")
    public ResultDTO<String> disableEvent(String roleIds, HttpServletRequest request) {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        try {
            // 登录帐号
            OperatorDTO oper = OperatorUtil.getOperator(request);
            if (null == oper) {
                return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
            }
            /** 参数校验 */
            if (CommonFuntions.isEmptyObject(roleIds)) {
                resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "参数为空");
                return resultDTO;
            }
            roleService.enableOrDisableRole(roleIds, 1L);
        } catch (Exception e) {
            log.error("角色禁用用发生系统异常", e);
            resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "操作系统异常");
        }
        return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
    }
}
