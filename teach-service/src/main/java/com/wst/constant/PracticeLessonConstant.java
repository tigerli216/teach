/*
 * PracticeLessonConstant.java Created on 2017年10月27日 下午6:22:45
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
 * 运营实习职位枚举
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class PracticeLessonConstant {

    /**
     * 运营实习职位有效状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PracticeLessonStatusEnmu {
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

        private PracticeLessonStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
    
    /**
     * 运营实习职位公司分类（国内/国外）
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PracticeLessonCompanyCountryEnmu {
        /**
         * 国内
         */
        OPEN(1, "国内"),
        
        /**
         * 国外
         */
        CLOSE(2, "国外");

        /**
         * 状态
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private PracticeLessonCompanyCountryEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }
    
    /**
     * 运营实习职位是否置顶
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PracticeLessonIsTopEnum {
        /**
         * 否
         */
        NO(1, "否"),

        /**
         * 是
         */
        YES(2, "是");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private PracticeLessonIsTopEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
