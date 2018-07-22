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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.security.MD5Util;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.service.OperatorService;
import com.wst.service.sm.service.RelOpOrgService;
import com.wst.service.sm.service.RelOpRoleService;
import com.wst.sm.util.OperatorUtil;

/**
 * 操作员控制器
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/operator")
public class OperatorController {

    private static HiwiLog log = HiwiLogFactory.getLogger(OperatorController.class);

    @Reference(version = "0.0.1")
    private OperatorService operatorService;
    @Reference(version = "0.0.1")
    private RelOpOrgService relOpOrgService;
    @Reference(version = "0.0.1")
    private RelOpRoleService relOpRoleService;

    /**
     * 首页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, ModelMap modelMap) throws Exception {
        return "sm/operator";
    }

    /**
     * 获取分页列表
     * 
     * @param pageParam
     * @param operatorQuery
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findPaging")
    public Page<OperatorDTO> findPaging(BootStrapTable<OperatorDTO> pageParam, OperatorDTO operatorQuery)
            throws Exception {
        return operatorService.findOperatorPaging(pageParam, operatorQuery);
    }

    /**
     * 编辑（保存或更新）
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("edit")
    public ResultDTO<String> edit(HttpServletRequest request, OperatorDTO operatorDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (CommonFuntions.isEmptyObject(operatorDTO.getOpName())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "姓名为空");
        }
        if (operatorDTO.getOpId() == 0 && CommonFuntions.isEmptyObject(operatorDTO.getPassword())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "初始密码为空");
        }
        // 当前登录人
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (operatorDTO.getOpId() == 0) {
            if (CommonFuntions.isEmptyObject(operatorDTO.getOpLoginName())) {
                return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "登录名为空");
            }
            OperatorDTO operator = operatorService.getByOpLoginName(operatorDTO.getOpLoginName());
            if (CommonFuntions.isNotEmptyObject(operator) && operator.getOpId() > 0) {
                return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "登录用户已存在");
            }
            operatorDTO.setPassword(MD5Util.MD5(operatorDTO.getPassword()));
            operatorDTO.setCreateOpId(oper.getOpId());
            operatorDTO.setCreateTime(new Date());
            operatorService.save(operatorDTO);
        } else {
            if (StringUtils.isNotBlank(operatorDTO.getPassword())) {
                operatorDTO.setPassword(MD5Util.MD5(operatorDTO.getPassword()));
            }
            operatorDTO.setModifyOpId(oper.getOpId());
            operatorDTO.setModifyTime(new Date());
            operatorService.updateOperator(operatorDTO);
        }
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

    /**
     * 根据操作员ID获取操作员信息
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getById")
    public OperatorDTO getById(long opId) throws Exception {
        // 操作员信息
        OperatorDTO operator = operatorService.getById(opId);
        if (CommonFuntions.isNotEmptyObject(operator)) {
            // 用户组织
            operator.setRelOpOrgList(relOpOrgService.findRelOpOrgListByOpId(opId));
            // 用户角色
            operator.setRelOpRoleList(relOpRoleService.findRelOpRoleListByOpId(opId));
        }
        return operator;
    }

    /**
     * 用户密码修改接口（登录)
     * 
     * @param newPassword
     *            新密码
     * @param oldPassword
     *            旧密码
     * @param againPassword
     *            确认新密码
     * @param pwdType
     *            密码类型， 登录密码：login 支付密码：pay
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("modifyPwd")
    public ResultDTO<String> modifyPwd(String oldPassword, String newPassword, String againPassword,
            HttpServletRequest request) throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        OperatorDTO oper = OperatorUtil.getOperator(request);
        String LoginName = oper.getOpLoginName();
        if (StringUtils.isBlank(oldPassword))
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "旧密码不能为空", "");

        if (6 > oldPassword.length() || 16 < oldPassword.length())
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "旧密码不能小于6位 大于16位!", "");

        if (StringUtils.isBlank(newPassword))
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "新密码不能为空", "");

        if (6 > newPassword.length() || 16 < newPassword.length())
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "新密码不能小于6位 大于16位!", "");

        if (StringUtils.equals(oldPassword, newPassword))
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "原密码不能与新密码相同", "");

        if (StringUtils.isBlank(againPassword))
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "确认密码不能为空", "");

        if (6 > againPassword.length() || 16 < againPassword.length())
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "确认密码不能小于6位 大于16位!", "");

        if (!StringUtils.equals(newPassword, againPassword))
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "新密码与确认密码不相同", "");
        OperatorDTO operatorDTO = operatorService.getByOpLoginName(LoginName);
        if (CommonFuntions.isEmptyObject(operatorDTO)) {
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "账号不存在");
        }
        String pass = operatorDTO.getPassword();
        // 密码 加密
        oldPassword = MD5Util.MD5(oldPassword);
        if (!StringUtils.equals(pass, oldPassword)) {
            return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "密码输入错误");
        }
        // 密码 加密
        newPassword = MD5Util.MD5(newPassword);
        operatorDTO.setPassword(newPassword);
        operatorService.updateOperatorPwd(operatorDTO);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "修改密码成功", "");
    }

    /**
     * 修改操作员状态
     * 
     * @param opIds
     * @param status
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateOpStatus")
    public ResultDTO<String> updateOpStatus(String opIds, Long status, HttpServletRequest request) {
        ResultDTO<String> resultDTO = new ResultDTO<String>();
        try {
            // 登录帐号
            OperatorDTO oper = OperatorUtil.getOperator(request);
            if (null == oper) {
                return resultDTO.set(ResultCodeEnum.ERROR_UNKNOW, "你还未登陆");
            }
            /** 参数校验 */
            if (CommonFuntions.isEmptyObject(opIds)) {
                resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "参数为空");
                return resultDTO;
            }
            operatorService.updateOpStatus(opIds, status);
        } catch (Exception e) {
            log.error("删除操作员发生系统异常", e);
            resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "操作系统异常");
        }
        return resultDTO.set(ResultCodeEnum.SUCCESS, "操作成功");
    }

    /**
     * 编辑（保存或更新）
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("edit1")
    public ResultDTO<String> edit1(HttpServletRequest request, OperatorDTO operatorDTO) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (CommonFuntions.isEmptyObject(operatorDTO.getOpName())) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "姓名为空");
        }
        // 当前登录人
        OperatorDTO oper = OperatorUtil.getOperator(request);
        if (oper.getOpId() == 0) {
            return result.set(ResultCodeEnum.ERROR_HANDLE, "修改失败");
        } else {
            operatorDTO.setModifyOpId(oper.getOpId());
            operatorDTO.setModifyTime(new Date());
            operatorService.updateOperator(operatorDTO);
        }
        return result.set(ResultCodeEnum.SUCCESS, "");
    }

}
