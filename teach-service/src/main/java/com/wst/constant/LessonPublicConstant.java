/*
 * LessonPublicConstant.java Created on 2017年9月27日 上午11:47:37
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
 * 网课相关常量配置
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
public class LessonPublicConstant {

    /**
     * 课程类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum LessonPublicTypeEnmu {
        /**
         * 录播课
         */
        VIDEO_PLAY_CLASS(1, "录播课"),

        /**
         * 直播课
         */
        LIVE_PLAY_CLASS(2, "直播课");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private LessonPublicTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }

    /**
     * 访问权限
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum VisitAuthStatusEnmu {
        /**
         * 游客
         */
        VISITOR(1, "游客"),

        /**
         * 普通用户
         */
        NORMAL(2, "普通用户"),

        /**
         * vip
         */
        VIP(3, "VIP");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private VisitAuthStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 上架状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ShelfStatusEnum {
        /**
         * 已上架
         */
        UP(1, "已上架"),

        /**
         * 已下架
         */
        DOWN(2, "未上架");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private ShelfStatusEnum(int status, String name) {
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
    public enum IsTopStatusEnum {
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

        private IsTopStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

    /**
     * 是否免费
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum IsFreeStatusEnum {
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

        private IsFreeStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

    /**
     * 课时状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PublicClassStatusEnum {
        /**
         * 未开始
         */
        NOT_START(1, "未开始"),

        /**
         * 进行中
         */
        UN_DERWAY(2, "进行中"),

        /**
         * 已完结
         */
        HAS_END(3, "已完结");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private PublicClassStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }
}
