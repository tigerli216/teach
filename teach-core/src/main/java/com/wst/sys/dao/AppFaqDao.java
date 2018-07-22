/*
 * AppFaqDao.java Created on 2016年10月31日 下午5:05:45
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
package com.wst.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.service.sys.dto.AppFaqDTO;

/**
 * App QA
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Repository
public class AppFaqDao {

    @Resource
    private BaseDao baseDao;

    public List<AppFaqDTO> findByCode(String faqCode) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select faq_id, faq_code, title, content, create_time, update_time ");
        sql.append("  from app_faq ");
        sql.append(" where 1 = 1 ");
        
        Map<String, String> params = new HashMap<String, String>(1);
        if (StringUtils.isNotEmpty(faqCode)) {
            sql.append(" and faq_code = :faqCode ");

            params.put("faqCode", faqCode);
        }

        return baseDao.findList(sql.toString(), params, AppFaqDTO.class);
    }
}
