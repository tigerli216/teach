/*
 * OrderConstant.java Created on 2017年6月20日 上午11:32:19
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
 * 订单相关常量配置
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class OrderConstant {

    /**
     * 订单类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum OrderTypeEnmu {
        /**
         * 网课订单
         */
        PUBLIC_CLASS_ORDER(1, "网课订单"),

        /**
         * 定制课订单
         */
        CUSTOM_CLASS_ORDER(2, "定制课订单"),

        /**
         * 会员购买订单
         */
        BUY_ORDER(3, "会员购买订单");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private OrderTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 订单子类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum OrderSubTypeEnmu {
        /**
         * 录播课
         */
        VIDEO_PLAY_CLASS(11, "录播课"),

        /**
         * 直播课
         */
        LIVE_PLAY_CLASS(12, "直播课"),

        /**
         * 定制课
         */
        CUSTOM_CLASS(21, "定制课"),

        /**
         * 实习课
         */
        INTERNSHIP_CLASS(22, "实习课");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private OrderSubTypeEnmu(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }

    /**
     * 订单状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum OrderStatusEnum {
        /**
         * 已确认
         */
        HAS_CONFIRM(1, "已确认"),

        /**
         * 已取消
         */
        HAS_CANCEL(2, "已取消"),

        /**
         * 无效
         */
        INVALID(3, "无效"),

        /**
         * 退款
         */
        RETURN_MONEY(4, "退款"),

        /**
         * 退货
         */
        RETURN_UP(5, "退货");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private OrderStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 支付状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PayStatusEnum {
        /**
         * 未付款
         */
        NO_PAY(1, "未付款"),

        /**
         * 已付款
         */
        HAS_PAY(2, "已付款"),

        /**
         * 已退款
         */
        HAS_REFUND(4, "已退款");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private PayStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 支付方式
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum OrderPayTypeEnum {
        /**
         * 线上
         */
        ON_PAY(1, "线上"),

        /**
         * 线下
         */
        DOWN_PAY(2, "线下");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private OrderPayTypeEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

}
