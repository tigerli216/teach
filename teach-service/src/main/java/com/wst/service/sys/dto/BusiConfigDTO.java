/*
 * ModuleDTO.java Created on 2016年9月25日 下午3:47:29
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
package com.wst.service.sys.dto;


import com.hiwi.common.util.EnumUtils;
import com.wst.constant.SysConfigConstant.BusiStatusEnum;
import com.wst.constant.SysConfigConstant.BusiTypeEnum;
import com.wst.entity.sys.BusiConfig;

/**
 * 公共服务插件配置对象
 * 
 * @author <a href="mailto:luozj@hiwitech.com">luozj</a>
 * @version 1.0
 */
public class BusiConfigDTO extends BusiConfig {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 微信号
     */
    private String wechatNumber;

    /**
     * 二维码地址
     */
    private String qRCodeUrl;
    
    /**
     * 业务类型（1-定制课咨询配置；2-活动咨询配置）
     */
    @SuppressWarnings("unused")
    private String busiTypeStr;
    
    /**
     * 状态（1-启用；2-禁用）
     */
    @SuppressWarnings("unused")
    private String statusStr;

    public String getBusiTypeStr() {
    	BusiTypeEnum busiTypeEnum = EnumUtils.getEnum(BusiTypeEnum.values(), "type", busiType);
		return busiTypeEnum == null ? "" : busiTypeEnum.name;
	}

	public void setBusiTypeStr(String busiTypeStr) {
		this.busiTypeStr = busiTypeStr;
	}

	public String getStatusStr() {
		BusiStatusEnum busiStatusEnum = EnumUtils.getEnum(BusiStatusEnum.values(), "status",
				status);
		return busiStatusEnum == null ? "" : busiStatusEnum.name;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWechatNumber() {
        return wechatNumber;
    }

    public void setWechatNumber(String wechatNumber) {
        this.wechatNumber = wechatNumber;
    }


    public String getqRCodeUrl() {
        return qRCodeUrl;
    }

    public void setqRCodeUrl(String qRCodeUrl) {
        this.qRCodeUrl = qRCodeUrl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
