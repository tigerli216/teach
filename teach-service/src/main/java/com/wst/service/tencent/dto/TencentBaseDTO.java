/*
 * TencentBaseDTO.java Created on 2017年11月2日 上午10:04:08
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
package com.wst.service.tencent.dto;

import java.io.Serializable;

/**
 * 腾讯视频点播基础信息
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class TencentBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    private String secretId;

    /**
     * 应用key
     */
    private String secretKey;
    
    /**
     * api域名
     */
    private String baseUrl;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String toString() {
        return "TencentBaseDTO [secretId=" + secretId + ", secretKey=" + secretKey + ", baseUrl=" + baseUrl + "]";
    }
}
