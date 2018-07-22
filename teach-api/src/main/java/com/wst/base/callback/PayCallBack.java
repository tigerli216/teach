/*
 * PayCallBack.java Created on 2017年10月11日 下午2:18:21
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
package com.wst.base.callback;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.wst.service.pay.service.PayResultService;

/**
 * 支付回调
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/callback/pay/")
public class PayCallBack {

    private static HiwiLog log = HiwiLogFactory.getLogger(PayCallBack.class);
    
    @Reference(version = "0.0.1")
    private PayResultService payResultService;
    
    /**
     * 微信支付回调
     * 
     * @param request
     * @param response
     */
    @RequestMapping("wxBack")
    public void wxPayCallback(HttpServletRequest request,HttpServletResponse response){
        try {
            BufferedReader reader = request.getReader();

            StringBuffer content = new StringBuffer();
            String line = ""; // 一行数据
            while ((line = reader.readLine()) != null)
                content.append(line);
            ResultDTO<String> result = payResultService.payCallbackResult(content.toString());
            if (!result.isSuccess()) {
                log.error("============微信充值回调失败======描述：" + result.getErrDesc());
            }
            response.setContentType("text/xml;charset=utf-8");
            StringBuffer responseContent = new StringBuffer();
            responseContent.append("<xml>");
            responseContent.append("<return_code>SUCCESS</return_code>");
            responseContent.append("<return_msg>OK</return_msg>");
            responseContent.append("</xml>");
            PrintWriter out = response.getWriter();
            out.print(responseContent.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("微信充值回调异常：", e);
        }
    }
}
