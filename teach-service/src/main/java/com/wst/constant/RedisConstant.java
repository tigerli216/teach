/*
 * RedisConstant.java Created on 2017年6月20日 上午11:34:32
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
 * Redis相关常量
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class RedisConstant {

	/** 课时操作reids key */
	/**
	 * 课时操作限制key
	 */
	public final static String CLASS_LIMIT_ = "CLASS_LIMIT_";

	/**
	 * 设置失效时间180秒
	 */
	public final static long CLASS_LIMIT_EXPIRE = 180L;

	/**
	 * 课时取消次数key
	 */
	public final static String CLASS_CANCEL_NUMBER_ = "CLASS_CANCEL_NUMBER_";

	/** 回调相关 */
	/**
	 * 在线支付回调，防止重发回调key(完整的key:前缀+第三方交易号)
	 */
	public final static String PAY_CALLBACK_ = "pay_callback_";

	/**
	 * 失效时间:60秒过期
	 */
	public final static long PAY_CALLBACK_EXPIRE = 60;

	/** 订单相关 */
	/**
	 * 生成订单编码(完整的key:前缀+年+月+日+订单类型)
	 */
	public final static String REDIS_ORDER_CODE_ = "redis_order_code_";

	/**
	 * 限制下单(完整的key:前缀+用户Id)
	 */
	public final static String LIMIT_ORDER_ = "limit_order_";

	/**
	 * 当前用户支付限制,支付完成之前不能重发支付(完整的key:前缀+用户Id/前缀+订单编码)
	 */
	public final static String LIMIT_PAY_ = "limit_pay_";

	/**
	 * 微信授权，自动登录成功首页地址
	 */
	public final static String WX_INDEX_URL = "WX_INDEX_URL";

	/**
	 * 微信授权，登录页地址
	 */
	public final static String WX_LOGIN_URL = "WX_LOGIN_URL";

	/** 直播相关 */
	/**
	 * 存放群组对应的流信息
	 */
	public final static String LIVE_GROUP = "LIVE_GROUP_";
	/**
	 * 存放群组对应的流信息（2天后过期）
	 */
	public final static long LIVE_GROUP_EXPIRE = 60 * 60 * 24 * 2;
	/**
	 * 用户签名缓存KEY
	 */
	public final static String LIVE_USER_SIG = "LIVE_USER_SIG_";

	/**
	 * 学生是否需要等级更新
	 */
	public final static String USER_LEVEL_UPDATE_ = "USER_LEVEL_UPDATE_";

	/**
	 * 网课课时操作限制key
	 */
	public final static String PUBLIC_CLASS_LIMIT_ = "PUBLIC_CLASS_LIMIT_";

	/**
	 * 唯一标识与账号
	 */
	public final static String LIVE_THEONE_ACCOUNT_ = "LIVE_THEONE_ACCOUNT_";

	/**
	 * 直播课禁言
	 */
	public final static String LIVE_GROUP_SILENCE_ = "LIVE_GROUP_SILENCE_";
	public final static long LIVE_GROUP_SILENCE_EXPIRE = 60 * 60 * 24;
}
