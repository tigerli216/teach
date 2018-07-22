/*
 * UserDTO.java Created on 2017年9月26日 上午10:28:18
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
package com.wst.service.mem.dto;

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.UserConstant.RegTypeEnmu;
import com.wst.constant.UserConstant.SexTypeEnmu;
import com.wst.constant.UserConstant.StatusEnum;
import com.wst.constant.UserConstant.UserLevelTypeEnmu;
import com.wst.constant.UserConstant.UserTypeEnmu;
import com.wst.entity.mem.User;

/**
 * 对应实体类(User)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class UserDTO extends User {

    private static final long serialVersionUID = 1L;

    /**
     * 可选值：导师端（tutor）T，学生端（student）S
     */
    private String roleType;

    /**
     * 学校ID
     */
    private long schoolId;

    /**
     * 地区ID
     */
    private long regionId;

    /**
     * 毕业时间Str
     */
    private String finishTimeStr;

    /**
     * 枚举注册类型
     * 
     * @return
     */
    public String getRegTypeStr() {
        RegTypeEnmu regTypeEnum = EnumUtils.getEnum(RegTypeEnmu.values(), "type", regType);
        return regTypeEnum == null ? "" : regTypeEnum.name;
    }

    /**
     * 枚举用户类型
     * 
     * @return
     */
    public String getUserTypeStr() {
        UserTypeEnmu userTypeEnmu = EnumUtils.getEnum(UserTypeEnmu.values(), "type", userType);
        return userTypeEnmu == null ? "" : userTypeEnmu.name;
    }

    /**
     * 枚举用户级别
     * 
     * @return
     */
    public String getUserLevelStr() {
        UserLevelTypeEnmu userLevelTypeEnmu = EnumUtils.getEnum(UserLevelTypeEnmu.values(), "type", userLevel);
        return userLevelTypeEnmu == null ? "" : userLevelTypeEnmu.name;
    }

    /**
     * 枚举用户状态
     * 
     * @return
     */
    public String getStatusStr() {
    	StatusEnum statusEnum = EnumUtils.getEnum(StatusEnum.values(), "status", status);
        return statusEnum == null ? "" : statusEnum.name;
    }

    /**
     * 枚举性别
     * 
     * @return
     */
    public String getSexStr() {
        SexTypeEnmu sexTypeEnmu = EnumUtils.getEnum(SexTypeEnmu.values(), "type", sex);
        return sexTypeEnmu == null ? "" : sexTypeEnmu.name;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(long schoolId) {
        this.schoolId = schoolId;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public String getFinishTimeStr() {
        return finishTimeStr;
    }

    public void setFinishTimeStr(String finishTimeStr) {
        this.finishTimeStr = finishTimeStr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
