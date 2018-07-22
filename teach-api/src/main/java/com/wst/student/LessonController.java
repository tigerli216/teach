/*
 * LessonController.java Created on 2017年10月16日 上午11:24:57
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
package com.wst.student;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.UserHelper;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;

/**
 * 上课相关管理
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/API/student/")
public class LessonController {

    @Reference(version = "0.0.1")
    private LessonCustomService lessonCustomService;
    @Reference(version = "0.0.1")
    private CustomClassService customClassService;
    @Reference(version = "0.0.1")
    private BusiConfigService busiConfigService;
    @Reference(version = "0.0.1")
    private PublicClassService publicClassService;

    /**
     * 确认上课
     * 
     * @param dto
     * @param classId
     *            课时id
     * @return
     * @throws Exception
     */
    @RequestMapping("confirmClass")
    public ResultDTO<String> confirmClass(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
            throws Exception {
        ResultDTO<String> result = new ResultDTO<>();
        if (classId <= 0) {
            // 课时不能为空
            return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Class hours should not be empty");
        }
        UserDTO userDTO = UserHelper.getUserDTO(dto);
        if (userDTO == null) {
            return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
        }
        return customClassService.confirmAttendClass(classId, userDTO.getUserId(), 1);
    }

    /**
     * 查看课时文件
     * 
     * @param dto
     * @param classId
     *            课时id
     * @return
     * @throws Exception
     */
    @RequestMapping("getReadyFile")
    public ResultDTO<String> findReadyFile(CommonParamsDTO dto, @RequestParam(defaultValue = "0") long classId)
            throws Exception {
        ResultDTO<String> resultDTO = new ResultDTO<>();
        if (classId <= 0) {
            // 课时不能为空
            return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Class hours should not be empty");
        }
        CustomClassDTO customClassDTO = customClassService.getCustomClassById(classId);
        if (customClassDTO == null) {
            // 课时不存在
            return resultDTO.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "Class does not exist");
        }

        return resultDTO.set(ResultCodeEnum.SUCCESS, "", customClassDTO.getReadyFile());
    }

    /**
     * 获取定制课咨询二维码
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getVipSeekCode")
    public ResultDTO<Map<String, String>> getVipSeekCode(CommonParamsDTO dto) throws Exception {
        ResultDTO<Map<String, String>> result = new ResultDTO<Map<String, String>>();
        Map<String, String> resultMap = new HashMap<String, String>(2);

        // 定制课咨询配置
        BusiConfigDTO busiConfigDTO = busiConfigService.getConfigByBusiType(1);
        if (busiConfigDTO != null) {
            String params = busiConfigDTO.getParams();
            if (params == null) {
                return result.set(ResultCodeEnum.ERROR_HANDLE, "未配置", resultMap);
            }
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 微信号
            resultMap.put("wechatNumber", jsonObject.getString("wechatNumber"));
            // 二维码路径
            resultMap.put("qRCodeUrl", jsonObject.getString("qRCodeUrl"));
        }

        return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
    }
    
    /**
     * 获取直播课视频地址
     * @param classId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("live/getLiveUrl")
    public ResultDTO<String> getLiveUrl(long classId) throws Exception {
        
        String url = publicClassService.getLiveUrl(classId);
        
        return new ResultDTO<String>().set(ResultCodeEnum.SUCCESS, "", url);
    }
}
