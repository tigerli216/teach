/*
 * PayConstant.java Created on 2017年6月20日 下午2:17:27
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
 * 支付相关常量
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class PayConstant {

    /**
     * 交易来源
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum TradeSourceEnum {
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

        private TradeSourceEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }

    /**
     * 支付方式
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PayTypeEnum {
        /**
         * 微信
         */
        WEIXIN(1, "微信"),

        /**
         * 支付宝
         */
        ALIPAY(2, "支付宝");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private PayTypeEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 交易类型
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum TradeTypeEnum {
        /**
         * 消费
         */
        CONSUME(1, "消费"),

        /**
         * 退款
         */
        REFUND(2, "退款"),

        /**
         * 提现
         */
        WITHDRAW(3, "提现");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private TradeTypeEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    /**
     * 交易状态
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum TradeStatusEnum {
        /**
         * 未支付
         */
        NO_PAY(1, "未支付"),

        /**
         * 支付中
         */
        PAY_IN(2, "支付中"),

        /**
         * 成功
         */
        PAY_SUCCESS(3, "成功"),

        /**
         * 失败
         */
        PAY_FAIL(4, "失败"),

        /**
         * 关闭
         */
        PAY_CLOSE(5, "关闭");

        /**
         * 状态
         */
        public int status;

        /**
         * 名称
         */
        public String name;

        private TradeStatusEnum(int status, String name) {
            this.status = status;
            this.name = name;
        }

    }

    /**
     * 支付通道
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PaySourceChnEnum {
        /**
         * 微信
         */
        WEIXIN(1, "微信"),

        /**
         * 学生端
         */
        S_APP(2, "学生端"),

        /**
         * 导师端
         */
        T_APP(3, "导师端");

        /**
         * 支付通道
         */
        public int chn;

        /**
         * 通道名称
         */
        public String name;

        private PaySourceChnEnum(int chn, String name) {
            this.chn = chn;
            this.name = name;
        }
    }

    /**
     * 支付通道
     * 
     * @author <a href="mailto:yangc@hiwitech.com">yangc</a>
     * @version 1.0
     */
    public enum PayChnEnum {
        /**
         * 支付宝
         */
        ALIPAY(1, "支付宝"),

        /**
         * 微信
         */
        WX_APP(2, "微信"),

        /**
         * 微信网页支付
         */
        WX_JS_PAY(3, "微信网页支付");

        /**
         * 支付通道
         */
        public int chn;

        /**
         * 通道名称
         */
        public String name;

        private PayChnEnum(int chn, String name) {
            this.chn = chn;
            this.name = name;
        }
    }
}
