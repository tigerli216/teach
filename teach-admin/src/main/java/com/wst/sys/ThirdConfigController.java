/*
 * ThirdConfigController.java Created on 2017年3月29日 下午4:14:13
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sys.dto.AppAccessDTO;
import com.wst.service.sys.dto.AppIpDTO;
import com.wst.service.sys.service.AppAccessService;
import com.wst.service.sys.service.AppIpService;
import com.wst.sm.util.OperatorUtil;

/**
 * 第三方应用配置Controller
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/third/config")
public class ThirdConfigController {

    @Reference(version = "0.0.1")
    private AppAccessService appAccessService;
    @Reference(version = "0.0.1")
    private AppIpService appIpService;

    /**
     * 第三方应用接入配置首页
     * 
     * @param userId
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request) throws Exception {
        return "sys/thirdConfig";
    }

    /**
     * 保存/更新第三方应用接入配置
     * 
     * @param appAccessDTO
     * @throws Exception
     */
/*  @ResponseBody
    @RequestMapping("saveorUpdate")
    public ResultDTO<String> saveorUpdate(AppAccessDTO appAccessDTO) throws Exception {

        return appAccessService.saveorUpdate(appAccessDTO);
    }*/

    /**
     * 更新应用秘钥
     * 
     * @param accsId
     * @param appSecret
     *            秘钥
     * @param opId
     * @throws Exception
     */
/*  @ResponseBody
    @RequestMapping("upAppSecret")
    public ResultDTO<String> upAppSecret(long accsId, HttpServletRequest request) throws Exception {
        return appAccessService.upAppSecret(accsId, OperatorUtil.getOperator(request).getOpId());
    }*/

    /**
     * 第三方应用接入配置信息分页查询
     * 
     * @param pageParam
     * @param appAccess
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findPage")
    public Page<AppAccessDTO> findPage(BootStrapTable<AppAccessDTO> pageParam, AppAccessDTO appAccess) throws Exception {
        return appAccessService.findPage(pageParam, appAccess);
    }

    /**
     * 根据Id获取第三方应用接入配置信息（包括有效和禁用状态）
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getAllById")
    public AppAccessDTO getAllById(long accsId) throws Exception {
        return appAccessService.getAllById(accsId);
    }

    /**
     * 新增、更新第三方应用IP配置
     * 
     * @param appIpDTO
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("saveOrUpdateAppIp")
    public ResultDTO<String> saveOrUpdateAppIp(HttpServletRequest request, AppIpDTO appIpDTO) throws Exception {
        OperatorDTO operatorDTO = OperatorUtil.getOperator(request);
        if (appIpDTO.getConfigId() == 0) {
            appIpDTO.setCreateOp(operatorDTO.getOpId());
            appIpDTO.setStatus(1);
        } else {
            appIpDTO.setUpdateOp(operatorDTO.getOpId());
        }
        return appIpService.saveOrUpdate(appIpDTO);
    }

    /**
     * 第三方应用IP配置信息分页查询
     * 
     * @param pageParam
     * @param appIpDTO
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findAppIpPage")
    public Page<AppIpDTO> findAppIpPage(BootStrapTable<AppIpDTO> pageParam, AppIpDTO appIpDTO) throws Exception {
        return appIpService.findPage(pageParam, appIpDTO);
    }

    /**
     * 根据配置ID获取所有应用IP配置
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getAllIpByConfigId")
    public AppIpDTO getAllIpByConfigId(long configId) throws Exception {
        return appIpService.getAllIpByConfigId(configId);
    }
}
