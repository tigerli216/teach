/*
 * UserController.java Created on 2017年9月27日 下午4:47:26
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
package com.wst.mem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.FileInfoDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.file.FileUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.UserService;
import com.wst.service.sys.dto.RegionDTO;
import com.wst.service.sys.dto.RegionSchoolDTO;
import com.wst.service.sys.service.RegionSchoolService;
import com.wst.service.sys.service.RegionService;

/**
 * 用户管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/mem/user")
public class UserController {

    @Reference(version = "0.0.1")
    private UserService userService;
    
    @Reference(version = "0.0.1")
    private RegionService regionService;
    
    @Reference(version = "0.0.1")
    private RegionSchoolService regionSchoolService;

    /**
     * 用户信息总览首页
     * 
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "summaryIndex")
    public String summaryIndex(ModelMap modelMap) throws Exception {
		List<RegionDTO> regionList = regionService.findRegionList();
		modelMap.put("regionList", regionList);
        return "mem/userSummary";
    }

    /**
     * 用户信息管理首页
     * 
     * @param modelMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "index")
    public String user(ModelMap modelMap) throws Exception {
    	List<RegionDTO> regionList = regionService.findRegionList();
    	modelMap.put("regionList", regionList);
    	return "mem/user";
    }
    
    /**
     * 查看学生详情页面
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("viewInitForm")
    public String viewInitForm(HttpServletRequest request, ModelMap modelMap) throws Exception {
        return "mem/form/viewUser";
    }

    /**
     * 获取用户分页数据
     * 
     * @param pageParam
     * @param userDTO
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "findPaging")
    public Page<UserDTO> findPaging(BootStrapTable<UserDTO> pageParam, UserDTO userDTO) throws Exception {
        return userService.findPaging(pageParam, userDTO);
    }

    /**
     * 通过用户ID获取用户信息
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "getUser")
    public UserDTO getUser(long userId) throws Exception {
        UserDTO userDTO = userService.getUserById(userId);
        return userDTO;
    }

    /**
     * 修改用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "edit")
    public ResultDTO<String> updateUser(HttpServletRequest request, UserDTO user) throws Exception {
        ResultDTO<String> result = new ResultDTO<>();

        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest userRequest = (StandardMultipartHttpServletRequest) request;
            MultipartFile resumeFile = userRequest.getFile("resumeFile");
            if (resumeFile != null && StringUtils.isNotEmpty(resumeFile.getOriginalFilename())) {
                FileInfoDTO resume = FileUtils.uploadFile(resumeFile);
                if (resume != null) {
                    user.setResume(resume.getUrl());
                }
            }
            MultipartFile offerFile = userRequest.getFile("offerFile");
            if (offerFile != null && StringUtils.isNotEmpty(offerFile.getOriginalFilename())) {
                FileInfoDTO offer = FileUtils.uploadFile(offerFile);
                if (offer != null) {
                    user.setOffer(offer.getUrl());
                }
            }
        }

        userService.updateUser(user.getUserId(), user.getRealName(), user.getNickName(), user.getResume(),
                user.getOffer());
        return result.set(ResultCodeEnum.SUCCESS, "修改成功");
    }
    
    /**
     * 通过地区名称获取学校集合
     * 
     * @param request
     * @param regionName
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "findRegionSchoolList")
    public List<RegionSchoolDTO> findRegionSchoolList(HttpServletRequest request, String regionName) throws Exception {
    	return regionSchoolService.findRegionSchoolList(regionName);
    }
}
