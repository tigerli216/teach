/*
 * LessonFileOnloadDao.java Created on 2017年11月23日 下午2:52:24
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;

/**
 * 课程文件上传
 * @author <a href="mailto:lanwc@hiwitech.com">lanweicheng</a>
 * @version 1.0
 */
@Repository
public class LessonFileOnloadDao {
    
    @Resource
    private BaseDao baseDao;
    
    /**
     * 保存课程图片的路径
     */
    public int findLessonClass(long lessonId , long classId , String courSewarePic) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update lesson_public_class ");
        sql.append("    set courseware_pic = :courSewarePic ");
        sql.append(" where lesson_id = :lessonId");
        sql.append("  and class_id = :classId");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("lessonId", lessonId);
        params.put("classId", classId);
        params.put("courSewarePic", courSewarePic);

        return  baseDao.executeSQL(sql.toString(), params);
    }
    
    /**
     * 查询网课课程名称
     * 
     */
    public List<PublicDTO> findPublicLessonList() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select lp.lesson_id, lp.lesson_name");
        sql.append("  from lesson_public lp");
        sql.append(" join lesson_public_class lpc on lpc.lesson_id = lp.lesson_id");
        sql.append(" where lp.lesson_type = 2");
        sql.append("   and lpc.class_status = 1");
        sql.append(" group by lp.lesson_id ");

        return baseDao.findList(sql.toString(), null, PublicDTO.class);
    }
    
    
    /**
     * 根据课程Id查询课时
     */
    public List<PublicClassDTO> findPublicClassList(long lessonId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select lpc.class_name, lpc.class_id");
        sql.append(" from lesson_public_class lpc");
        sql.append(" where lpc.lesson_id = :lessonId");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("lessonId", lessonId);
        
        return  baseDao.findList(sql.toString(), params, PublicClassDTO.class);
    }
}
