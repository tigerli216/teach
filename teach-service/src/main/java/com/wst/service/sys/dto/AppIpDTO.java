/*
 * AppIpDTO.java Created on 2017年3月25日 下午2:13:56
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
package com.wst.service.sys.dto;

import com.wst.entity.sys.AppIp;

/**
 * 第三方应用IP配置
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class AppIpDTO extends AppIp {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人账号
     */
    private String createOpName;

    /**
     * 修改人账号
     */
    private String updateOpName;

    /**
     * 状态（1-有效；2-无效）
     */
    private String statusStr;

    /**
     * IP类型（1-白名单；2-黑名单）
     */
    private String ipTypeStr;

    public String getCreateOpName() {
        return createOpName;
    }

    public void setCreateOpName(String createOpName) {
        this.createOpName = createOpName;
    }

    public String getUpdateOpName() {
        return updateOpName;
    }

    public void setUpdateOpName(String updateOpName) {
        this.updateOpName = updateOpName;
    }

    public String getStatusStr() {
        if (status == 0) {
            statusStr = "";
        } else if (status == 1) {
            statusStr = "有效";
        } else if (status == 2) {
            statusStr = "无效";
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getIpTypeStr() {
        if (ipType == 0) {
            ipTypeStr = "";
        } else if (ipType == 1) {
            ipTypeStr = "白名单";
        } else if (ipType == 2) {
            ipTypeStr = "黑名单";
        }
        return ipTypeStr;
    }

    public void setIpTypeStr(String ipTypeStr) {
        this.ipTypeStr = ipTypeStr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
