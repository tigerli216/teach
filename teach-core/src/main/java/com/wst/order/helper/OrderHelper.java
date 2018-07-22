/*
 * ProductPriceHelper.java Created on 2016年10月8日 上午11:09:43
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.order.helper;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.DateUtils;
import com.wst.constant.PayConstant.PayChnEnum;
import com.wst.constant.RedisConstant;

/**
 * 订单帮助工具类
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class OrderHelper {

    private static HiwiLog log = HiwiLogFactory.getLogger(OrderHelper.class);

    /**
     * 生成订单编号 生成规则2（订单类型） + 16（年后两位） + 09（月） + 27（日） + 5位数字（从10000开始的当日订单数）
     * 
     * @param busiCode
     * @return
     */
    public synchronized static String getOrderCode(long orderType) {
        if (orderType <= 0) {
            log.error("生成订单编码错误，传入orderType为空");
            return null;
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
        String yearStr = String.valueOf(calendar.get(Calendar.YEAR) - 2000); // 年份
        String monthStr = String.valueOf(calendar.get(Calendar.MONTH) + 1); // 月份
        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)); // 日

        String redisKey = RedisConstant.REDIS_ORDER_CODE_ + yearStr + "_" + monthStr + "_" + dayStr + "_" + orderType; // redis中的key
        boolean isExist = RedisClient.exists(redisKey);
        long orderNum = 0; // 订单数
        if (isExist) {
            orderNum = RedisClient.incr(redisKey);
        } else { // 当天订单数数据还不存在
            orderNum = 11111;
            RedisClient.set(redisKey, orderNum);
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTime(DateUtils.getIntradayZeroTime());
            nextDay.add(Calendar.DATE, 1);

            RedisClient.expire(redisKey, (nextDay.getTimeInMillis() - calendar.getTimeInMillis()) / 1000); // 当天24点后自动失效
        }
        // 订单编号，12位
        String orderCod = String.valueOf(orderType) + yearStr + monthStr + dayStr + String.valueOf(orderNum);

        return orderCod;
    }

    /**
     * 生成交易流水号 生成规则（支付类型） + 16（年后两位） + 09（月） + 27（日） + 3位数字（从10000开始的当日交易数）
     * 
     * @param payType(1：支付宝，2：微信，3：微信公众号)
     * @return
     */
    public synchronized static String getTradeNo(long payType) {
        if (payType <= 0) {
            log.error("生成交易流水号错误，传入payType为空");
            return null;
        }
        String payTypeStr = StringUtils.EMPTY;
        if (payType == PayChnEnum.WX_APP.chn) {
            payTypeStr = "PP";
        } else if (payType == PayChnEnum.WX_JS_PAY.chn) {
            payTypeStr = "PJ";
        } else if (payType == PayChnEnum.ALIPAY.chn) {
            payTypeStr = "PA";
        } else if (payType == 9) {// 线下
            payTypeStr = "PL";
        } else {
            log.error("生成交易流水号错误，传入orderType=" + payType);
            return null;
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
        String yearStr = String.valueOf(calendar.get(Calendar.YEAR) - 2000); // 年份
        String monthStr = String.valueOf(calendar.get(Calendar.MONTH) + 1); // 月份
        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)); // 日

        String redisKey = payTypeStr + yearStr + "_" + monthStr + "_" + dayStr; // redis中的key
        boolean isExist = RedisClient.exists(redisKey);
        long orderNum = 0; // 订单数
        if (isExist) {
            orderNum = RedisClient.incr(redisKey);
        } else { // 当天订单数数据还不存在
            orderNum = 100;
            RedisClient.set(redisKey, orderNum);
            Calendar nextDay = Calendar.getInstance();
            nextDay.setTime(DateUtils.getIntradayZeroTime());
            nextDay.add(Calendar.DATE, 1);

            RedisClient.expire(redisKey, (nextDay.getTimeInMillis() - calendar.getTimeInMillis()) / 1000); // 当天24点后自动失效
        }
        // 交易流水号，12位
        String tradeNo = payTypeStr + yearStr + monthStr + dayStr + String.valueOf(orderNum);

        return tradeNo;
    }
}
