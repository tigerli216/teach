/*
 * CustomClassDateDao.java Created on 2017年10月11日 下午7:19:55
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
package com.wst.lesson.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.entity.lesson.CustomClassDate;
import com.wst.service.lesson.dto.CustomClassDateDTO;

/**
 * 课时约课DAO
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class CustomClassDateDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存约课信息
     * 
     * @param customClassDate
     *            约课信息
     * @return
     * @throws Exception
     */
    public long save(CustomClassDate customClassDate) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into lesson_custom_class_date ( ");
        sql.append("       class_id, date_time, create_time, date_mem_type, confirm_status) ");
        sql.append(" value (:classId, :dateTime, now(), :dateMemType, :confirmStatus) ");

        return baseDao.insertInto(sql.toString(), customClassDate, "record_id");
    }

    /**
     * 根据课时ID获取最新的约课信息
     * 
     * @param classId
     *            课时ID
     * @return
     * @throws Exception
     */
    public CustomClassDateDTO getCustomClassDateByClassId(long classId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select record_id, class_id, date_time, create_time, date_mem_type, ");
        sql.append("       confirm_status, cancel_time, cancel_user_id ");
        sql.append("  from lesson_custom_class_date ");
        sql.append(" where class_id = :classId ");
        sql.append(" order by record_id desc limit 1 ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("classId", classId);

        return baseDao.get(sql.toString(), params, CustomClassDateDTO.class);
    }

    /**
     * 约课记录确认
     * 
     * @param recordId
     *            记录ID
     * @return
     * @throws Exception
     */
    public int confirmClassStatus(long recordId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update lesson_custom_class_date ");
        sql.append("   set confirm_status = 2 ");
        sql.append(" where record_id = :recordId");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("recordId", recordId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 取消约课
     * 
     * @param recordId
     *            记录ID
     * @param cancelUserId
     *            取消用户ID
     * @return
     * @throws Exception
     */
    public int cancel(long recordId, long cancelUserId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update lesson_custom_class_date ");
        sql.append("   set cancel_time = now(), cancel_user_id = :cancelUserId");
        sql.append(" where record_id = :recordId");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("recordId", recordId);
        params.put("cancelUserId", cancelUserId);

        return baseDao.executeSQL(sql.toString(), params);
    }
}
