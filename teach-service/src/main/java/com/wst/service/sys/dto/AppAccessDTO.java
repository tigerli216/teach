/*
 * AppAccessDTO.java Created on 2017年3月25日 下午2:13:26
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

import com.wst.entity.sys.AppAccess;

/**
 * App访问信息
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class AppAccessDTO extends AppAccess {

    private static final long serialVersionUID = 1L;

    /**
     * 操作人登录账号
     */
    private String opLoginName;

    /**
     * 操作人姓名
     */
    private String opName;

    /**
     * 状态（1-正常；2-禁用）
     */
    private String statusStr;

    public String getOpLoginName() {
        return opLoginName;
    }

    public void setOpLoginName(String opLoginName) {
        this.opLoginName = opLoginName;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getStatusStr() {
        if (status == 0) {
            statusStr = "";
        } else if (status == 1) {
            statusStr = "正常";
        } else if (status == 2) {
            statusStr = "禁用";
        }
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
