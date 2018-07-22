/*
 * LessonCustomConstant.java Created on 2017年9月27日 下午1:41:29
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
 * 课程常量配置
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public class LessonCustomConstant {

    /**
     * 课程类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum LessonCustomTypeEnmu {
        /**
         * 定制课
         */
        CUSTOM_CLASS(1, "定制课"),

        /**
         * 实习课
         */
        INTERNSHIP_CLASS(2, "实习课");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private LessonCustomTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 课时状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum CustomClassStatusEnum {
        /**
         * 未上课
         */
        NOT_START(1, "未上课"),

        /**
         * 开始上课
         */
        UN_DERWAY(2, "开始上课"),

        /**
         * 课时结束
         */
        CLASS_END(3, "课时结束"),

        /**
         * 已完结
         */
        HAS_END(4, "已完结");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private CustomClassStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

    /**
     * 约课用户类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum DateMemTypeEnum {
        /**
         * 学生
         */
        STUDENT(1, "学生"),

        /**
         * 导师
         */
        TEACHER(2, "导师");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private DateMemTypeEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 确认状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ConfirmStatusEnum {
        /**
         * 未确认
         */
        NO_CONFIRM(1, "未确认"),

        /**
         * 已确认
         */
        HAS_CONFIRM(2, "已确认");

        private ConfirmStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;
    }

    /**
     * 结算状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum SettleStatusEnum {
        /**
         * 未结算
         */
        NO_SETTLE(1, "未结算"),

        /**
         * 已结算
         */
        HAS_SETTLE(2, "已结算");

        private SettleStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;
    }

    /**
     * 课程状态描述
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum RealCustomStatusEnum {
        /**
         * 待约课
         */
        UN_HAS_CONFIRM(1, "待约课"),

        /**
         * 待确认约课
         */
        HAS_CANCEL(2, "待确认约课"),

        /**
         * 已约课
         */
        HAS_CLASS(3, "已约课"),

        /**
         * 开始上课
         */
        UN_CLASS(4, "开始上课"),

        /**
         * 课程结束
         */
        UN_END(5, "课程结束"),

        /**
         * 课程完结
         */
        HAS_END(6, "课程完结");

        private RealCustomStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;
    }
    
    /**
     * 付款状态
     * 
     * @author <a href="mailto:guanyp@hiwitech.com">guanyp</a>
     * @version 1.0
     */
    public enum SettlePayStatusEnum {
        /**
         * 未付款
         */
        NO_PAY(1, "未付款"),

        /**
         * 已付款
         */
        HAS_PAY(2, "已付款");

        private SettlePayStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;
    }
}
