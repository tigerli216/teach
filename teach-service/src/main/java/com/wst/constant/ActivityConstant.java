/*
 * ActivityConstant.java Created on 2017年10月26日 下午4:17:23
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
 * 营销活动枚举
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class ActivityConstant {

    /**
     * 活动类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ActivityTypeEnmu {
        /**
         * 提取码活动
         */
        EXTRACTION_CODE_ACTIVITY(1, "提取码活动");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private ActivityTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }
    
    /**
     * 活动状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ActivityStatusEnmu {
        /**
         * 开启
         */
        OPEN(1, "开启"),
        
        /**
         * 关闭
         */
        CLOSE(2, "关闭");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private ActivityStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
