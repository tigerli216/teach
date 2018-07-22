/*
 * AppFaqService.java Created on 2016年10月31日 下午5:26:21
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
package com.wst.service.sys.service;

import java.util.List;

import com.wst.service.sys.dto.AppFaqDTO;

/**
 * app问题解答服务
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
public interface AppFaqService {

    /**
     * 根据 FAQ编码查询 （可不传参）
     * 
     * @param faqCode
     *            FAQ编码（可不传值）
     * @return
     * @throws Exception
     */
    public List<AppFaqDTO> findBy(String faqCode) throws Exception;
}
