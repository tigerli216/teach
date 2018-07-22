/*
 * WeixinServiceImpl.java Created on 2017年10月24日 上午10:14:57
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
package com.wst.wx.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.wst.service.wx.WeixinService;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class WeixinServiceImpl implements WeixinService {

    @Resource
    private WxMpService wxMpService;

    @Override
    public String getAccessToken() throws Exception {
        return wxMpService.getAccessToken();
    }

    @Override
    public WxMpUser getWxMpUser(String code) throws Exception {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        if (!wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken)) {
            return null;
        }
        WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        return wxMpUser;
    }

    @Override
    public String oauth2buildAuthorizationUrl(String redirectURI) throws Exception {
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, "snsapi_userinfo", null);
    }

}
