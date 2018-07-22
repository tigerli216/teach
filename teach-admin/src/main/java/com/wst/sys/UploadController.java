/*
 * BusiConfigController.java Created on 2017年10月25日 下午4:33:17
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hiwi.common.dto.ImageInfoDTO;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.file.FileUtils;

/**
 * 业务系统配置
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/")
public class UploadController {

	/**
	 * 上传图片返回路径
	 * 
	 * @param imgFile
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping(value =  "upload")
    public Map<String, Object> upload(@RequestParam("imgFile") MultipartFile imgFile) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<String> uriList = new ArrayList<>();
        resultMap.put("errno", 0);
        if (imgFile == null) {
            resultMap.put("errno", 1);
        } else {
            ImageInfoDTO imageInfo = FileUtils.uploadImage(imgFile);
            if (CommonFuntions.isNotEmptyObject(imageInfo)) {
                if (CommonFuntions.isNotEmptyObject(imageInfo.getPicUrl())) {
                    uriList.add(imageInfo.getPicUrl());
                }
            }
        }
        resultMap.put("data", uriList);
        return resultMap;
    }

}
