/*
 * RegionController.java Created on 2017年10月17日 下午3:05:27
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.service.sys.dto.IndustryDTO;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.IndustryService;
import com.wst.service.sys.service.RegionService;

/**
 * 地区控制层
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-app/API/region")
public class RegionController {

    @Reference(version = "0.0.1")
    private RegionService regionService;

    @Reference(version = "0.0.1")
    private IndustryService industryService;

    /**
     * 查询地区集合
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "findRegionList")
    public ResultDTO<List<Map<String, Object>>> findRegionList() throws Exception {
        ResultDTO<List<Map<String, Object>>> resultDTO = new ResultDTO<>();

        // 返回信息
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 地区集合
        List<RegionDTO> regionList = regionService.findRegionList();
        if (regionList != null && regionList.size() > 0) {
            for (RegionDTO regionDTO : regionList) {
                Map<String, Object> resultMap = new HashMap<>();
                // 设置转换参数
                String[] keys = new String[] { "regionName", "regionCode", "regionId" };
                String[] attrNames = new String[] { "regionName", "regionCode", "regionId" };
                resultMap = ConvertToMapHelper.convertToMap(regionDTO, keys, attrNames);
                resultList.add(resultMap);
            }
        }

        return resultDTO.set(ResultCodeEnum.SUCCESS, "success", resultList);
    }

    /**
     * 查询学校集合
     * 
     * @param regionCode
     *            地区编码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "findSchoolList")
    public ResultDTO<List<Map<String, Object>>> findSchoolList(String regionCode) throws Exception {
        ResultDTO<List<Map<String, Object>>> resultDTO = new ResultDTO<>();

        // 返回信息
        List<Map<String, Object>> resultList = new ArrayList<>();

        // 校验参数
        if (StringUtils.isEmpty(regionCode)) {
            return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Please select area", resultList);
        }

        // 学校集合
        List<RegionSchoolDTO> schoolList = regionService.findSchoolList(regionCode);
        if (schoolList != null && schoolList.size() > 0) {
            for (RegionSchoolDTO regionSchoolDTO : schoolList) {
                Map<String, Object> resultMap = new HashMap<>();
                // 设置转换参数
                String[] keys = new String[] { "regionName", "schoolName", "schoolExplain", "schoolAddr", "schoolId" };
                String[] attrNames = new String[] { "regionName", "schoolName", "schoolExplain", "schoolAddr",
                        "schoolId" };
                resultMap = ConvertToMapHelper.convertToMap(regionSchoolDTO, keys, attrNames);
                resultList.add(resultMap);
            }
        }

        return resultDTO.set(ResultCodeEnum.SUCCESS, "success", resultList);
    }

    /**
     * 获取行业列表
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/findIndustrys")
    public ResultDTO<List<Map<String, Object>>> findIndustrys(CommonParamsDTO dto) throws Exception {
        ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
        List<Map<String, Object>> resultList = new ArrayList<>();

        List<IndustryDTO> industryList = industryService.findIndustryList();
        if (industryList != null && industryList.size() > 0) {
            for (IndustryDTO industryDTO : industryList) {
                Map<String, Object> industryMap = new HashMap<>();
                industryMap.put("industryId", industryDTO.getIndustryId()); // 行业ID
                industryMap.put("industryName", industryDTO.getIndustryName()); // 行业名称
                industryMap.put("industryCode", industryDTO.getIndustryCode()); // 行业编码
                resultList.add(industryMap);
            }
        }
        return result.set(ResultCodeEnum.SUCCESS, "success", resultList);
    }

}
