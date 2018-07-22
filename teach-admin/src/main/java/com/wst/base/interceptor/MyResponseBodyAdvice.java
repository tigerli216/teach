/*
 * MyResponseBodyAdvice.java Created on 2016年10月12日 上午11:30:59
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
package com.wst.base.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.Globals;
import com.hiwi.common.util.JsonUtils;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.sm.util.OperatorUtil;

/**
 * spring的@ResponseBody切面拦截 用于打印或修改返回内容
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
//@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static HiwiLog log = HiwiLogFactory.getLogger(MyResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        // 异常（由拦截器捕获）
        if (body instanceof Map && ((Map) body).get("exception") != null) {
            return null;
        }

        // 转换为servletRequest
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        if (Globals.isAjaxRequest(servletRequest)) {    // ajax请求才打印日志
            String url = servletRequest.getServletPath(); // 请求地址
            String ip = Globals.getRealIP(servletRequest); // 请求IP

            try {
                String jsonBody = JsonUtils.getJson(body);

                OperatorDTO operator = OperatorUtil.getOperator(servletRequest);

                if (operator != null) {
                    log.debug(new StringBuilder("======APP返回拦截=====用户：").append(operator.getOpLoginName())
                            .append("，userId:").append(operator.getOpId()).append("，请求URL：").append(url).append("，请求IP：")
                            .append(ip).append("，返回内容：").append(jsonBody).toString());
                } else {
                    log.debug(new StringBuilder("======APP返回拦截=====请求URL：").append(url).append("，请求IP：").append(ip)
                            .append("，返回内容：").append(jsonBody).toString());
                }
            } catch (Exception e) {
                log.error("======APP返回拦截=====（异常）", e);
            }
        }

        return body;
    }

}
