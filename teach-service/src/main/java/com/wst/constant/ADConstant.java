/*
 * ADConstant.java Created on 2017年10月27日 下午6:08:50
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
 * 广告营销枚举
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class ADConstant {

    /**
     * 广告营销有效状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ADStatusEnmu {
        /**
         * 开启
         */
        OPEN(1, "有效"),
        
        /**
         * 关闭
         */
        CLOSE(2, "无效");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private ADStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
