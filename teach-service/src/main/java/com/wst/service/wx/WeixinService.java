/*
 * WeixinService.java Created on 2017年10月24日 上午10:17:38
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
package com.wst.service.wx;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 微信能力处理
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface WeixinService {

    /**
     * 获取access_token, 不强制刷新access_token
     * 
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception;

    /**
     * 获取微信用户信息
     * 
     * @param code
     * @return
     * @throws Exception
     */
    public WxMpUser getWxMpUser(String code) throws Exception;

    /**
     * 微信授权地址
     * 
     * @param redirectURI
     * @return
     * @throws Exception
     */
    public String oauth2buildAuthorizationUrl(String redirectURI) throws Exception;

}
