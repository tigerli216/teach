/*
 * AppVersionsController.java Created on 2017年10月18日 下午3:34:44
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
package com.wst.commonality;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.service.app.dto.AppVersionsDTO;
import com.wst.service.app.service.AppVersionsService;

/**
 * APP版本信息控制层
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API")
public class AppVersionsController {

    @Reference(version = "0.0.1")
    private AppVersionsService appVersionsService;

    /**
     * app版本信息
     * 
     * @param commonParamsDTO
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/AppVersions")
    public ResultDTO<Object> AppVersions(HttpServletRequest request, CommonParamsDTO dto) throws Exception {
        ResultDTO<Object> resultDTO = new ResultDTO<Object>();
        // 操作系统
        long osname = 0;
        if (StringUtils.equals("android", dto.getAppType().toLowerCase())) {
            osname = 1;
        }
        // 用户类型
        long userType = 0;
        if (StringUtils.equals(dto.getUserType(), "S")) {
            userType = 1;
        } else if (StringUtils.equals(dto.getUserType(), "T")) {
            userType = 2;
        }

        List<AppVersionsDTO> list = appVersionsService.findByUtypeAndOsname(userType, osname);
        if (list == null || list.size() == 0) {
            return resultDTO.set(ResultCodeEnum.SUCCESS, "", null);
        }
        AppVersionsDTO appVersionsDTO = list.get(0);
        // 比较版本号
        if (!isAppNewVersion(dto.getAppVersion(), appVersionsDTO.getVersions())) {
            return resultDTO.set(ResultCodeEnum.SUCCESS, "success", null);
        }
        String[] keys = new String[] { "downloadUrl", "isForcedUpdate", "updateContent", "versions", "versionsSize",
                "versionsName" };
        String[] attrNames = new String[] { "downloadUrl", "isForcedUpdate", "updateContent", "versions",
                "versionsSize", "versionsName" };
        Map<String, Object> verMap = ConvertToMapHelper.convertToMap(appVersionsDTO, keys, attrNames);
        return resultDTO.set(ResultCodeEnum.SUCCESS, "success", verMap);
    }

    /**
     * 判断是否为最新版本方法 将版本号根据.切分为int数组 比较
     * 
     * @param localVersion
     *            本地版本号
     * @param onlineVersion
     *            线上版本号
     * @return
     */
    private boolean isAppNewVersion(String localVersion, String onlineVersion) {
        if (localVersion.equals(onlineVersion)) {
            return false;
        }
        String[] localArray = localVersion.split("\\.");
        String[] onlineArray = onlineVersion.split("\\.");

        int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

        for (int i = 0; i < length; i++) {
            if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
                return true;
            } else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
                return false;
            }
            // 相等 比较下一组值
        }

        return true;
    }
}
