/*
 * GlobalConstant.java Created on 2017年6月23日 下午2:09:19
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
package com.wst.constant;

/**
 * 全局应用常量
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public enum ApplicationConstant {

    /**
     * 核心系统
     */
    TEACH_CORE("WST在线教育核心系统", "TEACH_CORE"),

    /**
     * 对外API系统（包括微信）
     */
    TEACH_API("WST在线教育对外API系统", "TEACH_API"),

    /**
     * 运营后台系统
     */
    TEACH_ADMIN("WST在线教育运营后台系统", "TEACH_ADMIN");

    /**
     * 应用（系统）名
     */
    private String name;

    /**
     * 应用（系统）编码
     */
    private String code;

    private ApplicationConstant(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
