/*
 * SysConfigConstant.java Created on 2017年11月2日 下午2:25:20
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
 * 系统配置常量
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class SysConfigConstant {

    /**
     * 腾讯视频账户信息编码
     */
    public static final String TENCENT_VIDEO_ACC = "TENCENT_VIDEO_ACC";

    public enum BusiTypeEnum {

        /**
         * 定制课咨询配置
         */
        CUSTOM_CONFIG(1, "定制课咨询配置"),

        /**
         * 活动咨询配置
         */
        ACTIVITY_CONFIG(2, "活动咨询配置"),

        /**
         * 系统配置
         */
        SYS_CONFIG(3, "系统配置");

        /**
         * 类型
         */
        public int type;

        /**
         * 名称
         */
        public String name;

        private BusiTypeEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

    }
    
    public enum BusiStatusEnum {
    	/**
    	 * 启用
    	 */
    	ENABLED(1,"启用"),
    	
    	/**
    	 * 禁用
    	 */
    	DISABLED(2,"禁用");
    	
    	public int status;
    	
    	public String name;
    	
    	private BusiStatusEnum(int status,String name){
    		this.status = status;
    		this.name = name;
    	}
    	
    }

}
