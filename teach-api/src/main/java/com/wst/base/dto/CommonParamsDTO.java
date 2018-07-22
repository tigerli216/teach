/*
 * CommonParamsDTO.java Created on 2016年10月10日 下午8:23:54
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
package com.wst.base.dto;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 公用参数DTO
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class CommonParamsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** APP公共参数 */
    private String appSessionId; // appSessionId
    private String appType; // 可选值：IOS，ANDROID，WIN
    private String appVersion; // app版本版本号

    /** 共享公共参数 */
    private String sign; // 签名字符串,加密方式MD5(key+submitTime+appSessionId)转小写,其中key由平台提供商提供
    private String submitTime; // 提交时间，格式yyyyMMhhHHmmss

    private String userType; // 用户类型，导师端（tutor）T，学生端（student）S

    private HttpServletRequest request;

    public CommonParamsDTO() {

    }

    public String getAppSessionId() {
        return appSessionId;
    }

    public void setAppSessionId(String appSessionId) {
        this.appSessionId = appSessionId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public HttpServletRequest getRequest() {
        if (null == request) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
