/*
 * CustomLessonConstant.java Created on 2017年10月27日 下午6:14:06
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
 * 定制课程表枚举
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class CustomLessonConstant {

    /**
     * 广告营销有效状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum CustomLessonStatusEnmu {
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

        private CustomLessonStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
    
    /**
     * 是否置顶
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum IsTopEnmu {
        /**
         * 否
         */
    	NOT_TOP(1, "不置顶"),
        
        /**
         * 是
         */
        IS_TOP(2, "置顶");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private IsTopEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
