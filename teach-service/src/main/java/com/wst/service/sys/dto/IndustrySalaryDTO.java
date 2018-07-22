/*
 * IndustrySalaryDTO.java Created on 2017年9月26日 上午10:31:54
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

import com.hiwi.common.util.AmountUtils;
import com.wst.entity.sys.IndustrySalary;

/**
 * 对应实体类(IndustrySalary)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class IndustrySalaryDTO extends IndustrySalary {

    private static final long serialVersionUID = 1L;

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 状态Str
     */
    private String statusStr;
    
    /**
     * 基本薪资Str
     */
    private String baseSalaryStr;

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getStatusStr() {
        if (status == 1) {
            statusStr = "有效";
        } else if (status == 2) {
            statusStr = "无效";
        }
        return statusStr;
    }

    public void setBaseSalaryStr(String baseSalaryStr) {
		this.baseSalaryStr = baseSalaryStr;
	}

	public String getBaseSalaryStr() throws Exception {
    	baseSalaryStr = AmountUtils.changeF2YString(baseSalary);
		return baseSalaryStr;
	}

	@Override
    public String toString() {
        return super.toString();
    }
}
