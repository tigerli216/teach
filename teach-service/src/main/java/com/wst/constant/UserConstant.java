/*
 * MemConstant.java Created on 2017年6月20日 上午11:33:32
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
 * 会员相关常量配置
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class UserConstant {

    /**
     * 导师类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum TutorTypeEnmu {
        /**
         * 普通
         */
        NORMAL_TUTOR(1, "普通");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private TutorTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 用户类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum UserTypeEnmu {
        /**
         * 个人账户
         */
        PERSON_USER(1, "个人账户"),

        /**
         * 企业账户
         */
        ENTERPRISE_USER(2, "企业账户");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private UserTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 用户子类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum SubUserTypeEnmu {
        /**
         * 普通用戶
         */
        NORMAL_USER(1, "普通用戶");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private SubUserTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 用户级别
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum UserLevelTypeEnmu {
        /**
         * 普通用戶
         */
        REG(1, "普通用户"),

        /**
         * vip
         */
        VIP(2, "VIP");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private UserLevelTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 用户性别
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum SexTypeEnmu {
        /**
         * 男
         */
        MAN(1, "男"),

        /**
         * 女
         */
        WOMAN(2, "女");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private SexTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum StatusEnum {
        /**
         * 有效
         */
        HAS_EFFECTIVE(1, "有效"),

        /**
         * 失效
         */
        NO_EFFECTIVE(2, "无效");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private StatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 注册方式
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum RegTypeEnmu {
        /**
         * 邮箱
         */
        EMAIL(1, "邮箱"),

        /**
         * 手机
         */
        MOBILE(2, "手机"),

        /**
         * 第三方
         */
        TPOS(3, "第三方");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private RegTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 收款方式
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ReceiveTypeEnmu {
        /**
         * 银行卡
         */
        BANK_CARD(1, "银行卡");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private ReceiveTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 导师状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ValidStatusEnmu {
        /**
         * 有效
         */
        HAS_EFFECTIVE(1, "有效"),

        /**
         * 失效
         */
        NO_EFFECTIVE(2, "无效");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private ValidStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

    /**
     * 审核状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum ExamineStatusEnmu {
        /**
         * 未审核
         */
        NOT_EXAMINE(1, "未审核"),

        /**
         * 审核通过
         */
        PASS_EXAMINE(2, "审核通过"),

        /**
         * 审核拒绝
         */
        REJECT_EXAMINE(3, "审核拒绝");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private ExamineStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

    /**
     * 消息类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum MsgTypeEnmu {
        /**
         * 系统消息
         */
        SYSTEM_MSG(1, "系统消息"),

        /**
         * 课程消息
         */
        LESSON_MSG(2, "课程消息"),

        /**
         * 结算消息
         */
        SETTLE_MSG(3, "结算消息");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private MsgTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 消息状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum MsgStatusEnmu {
        /**
         * 有效
         */
        HAS_EFFECTIVE(1, "有效"),

        /**
         * 失效
         */
        NO_EFFECTIVE(2, "失效");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private MsgStatusEnmu(int status, String name) {
            this.status = status;
            this.name = name;
        }
    }

}
