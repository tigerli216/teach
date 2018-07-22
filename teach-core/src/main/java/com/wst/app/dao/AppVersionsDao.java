/*
 * AppVersionsDao.java Created on 2017年10月18日 下午3:41:33
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
package com.wst.app.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.service.app.dto.AppVersionsDTO;

/**
 * app信息Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class AppVersionsDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 根据类型，操作系统获取版本信息
     * 
     * @param userType
     *            用户类型
     * @param osname
     *            操作系统
     * @return
     * @throws Exception
     */
    public List<AppVersionsDTO> findByUtypeAndOsname(long userType, long osname) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select versions_id, user_type, osname, versions_name, versions, versions_size, ");
        sql.append("       update_content, download_url, is_forced_update, create_time");
        sql.append("  from app_versions");
        sql.append(" where user_type = :userType");
        sql.append("   and osname = :osname");
        sql.append(" order by create_time desc");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("userType", userType);
        params.put("osname", osname);

        return baseDao.findList(sql.toString(), params, AppVersionsDTO.class);
    }

}
