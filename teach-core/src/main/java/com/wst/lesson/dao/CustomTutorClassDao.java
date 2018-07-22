/*
 * CustomTutorClassDao.java Created on 2017年10月13日 下午1:53:33
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
import com.wst.entity.lesson.CustomTutorClass;
import com.wst.service.lesson.dto.CustomTutorClassDTO;

/**
 * 定制课程导师课时计划Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class CustomTutorClassDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 保存数据
     * 
     * @param customTutorClass
     * @return
     * @throws Exception
     */
    public long save(CustomTutorClass customTutorClass) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into lesson_custom_tutor_class ( ");
        sql.append("       lesson_id, tutor_id, total_class, finish_class) ");
        sql.append(" value (:lessonId, :tutorId, :totalClass, :finishClass) ");

        return baseDao.insertInto(sql.toString(), customTutorClass, "plan_id");
    }

    /**
     * 根据课程ID和导师ID查询导师上课时长
     * 
     * @param lessonId
     *            课程ID
     * @param tutorId
     *            导师ID
     * @return
     * @throws Exception
     */
    public CustomTutorClassDTO getCustomTutorClass(long lessonId, long tutorId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select plan_id, lesson_id, tutor_id, total_class, finish_class ");
        sql.append("  from lesson_custom_tutor_class ");
        sql.append(" where lesson_id = :lessonId ");
        sql.append("   and tutor_id = :tutorId ");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("lessonId", lessonId);
        params.put("tutorId", tutorId);

        return baseDao.get(sql.toString(), params, CustomTutorClassDTO.class);
    }

    /**
     * 更新导师已完成课时
     * 
     * @param planId
     *            计划ID
     * @param finishClass
     *            已完成课时
     * @return
     * @throws Exception
     */
    public int updateFinishClass(long planId, long finishClass) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update lesson_custom_tutor_class ");
        sql.append("   set finish_class = finish_class + :finishClass ");
        sql.append(" where plan_id = :planId");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("planId", planId);
        params.put("finishClass", finishClass);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 通过课程ID删除定制课程导师课时计划
     * 
     * @param lessonId
     * @return
     * @throws Exception
     */
    public int deleteByLessonId(long lessonId) throws Exception {
        String sql = "delete from lesson_custom_tutor_class where lesson_id = :lessonId";

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("lessonId", lessonId);

        return baseDao.executeSQL(sql.toString(), params);
    }
}
