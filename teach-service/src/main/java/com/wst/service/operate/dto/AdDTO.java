/*
 * AdDTO.java Created on 2017年9月26日 上午10:37:30
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
package com.wst.service.operate.dto;

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.ADConstant.ADStatusEnmu;
import com.wst.entity.operate.Ad;

/**
 * 对应实体类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class AdDTO extends Ad {

    private static final long serialVersionUID = 1L;

    /**
     * 是否首页数据 true 是首页，false 不是首页
     */
    private boolean isHomePage;

    public boolean isHomePage() {
        return isHomePage;
    }

    public void setHomePage(boolean isHomePage) {
        this.isHomePage = isHomePage;
    }

    public String getAdStatusStr() {
        ADStatusEnmu aDStatusEnmu = EnumUtils.getEnum(ADStatusEnmu.values(), "status", adStatus);
        return aDStatusEnmu != null ? aDStatusEnmu.name : "";
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
