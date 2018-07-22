/*
 * PracticeLessonDao.java Created on 2017年9月27日 下午2:55:51
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
package com.wst.operate.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.operate.dto.PracticeLessonDTO;

/**
 * 运营实习职位Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class PracticeLessonDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取运营实习职位分页数据
     * 
     * @param pageParam
     * @param customLessonDTO
     * @return
     * @throws Exception
     */
    public Page<PracticeLessonDTO> findPaging(Page<PracticeLessonDTO> pageParam, PracticeLessonDTO practiceLessonDTO)
            throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select lesson_id, practice_name, company_rcmd, ");
        sql.append("	   practice_rcmd, industry_rcmd, company_country,  ");
        sql.append("	   company_location, job_pic, practice_duration, ");
        sql.append("	   is_top, weight, create_time, modify_time, status, ");
        sql.append("	   lesson_abstract ");
        sql.append("  from operate_practice_lesson ");
        sql.append(" where 1 = 1 ");

        if (practiceLessonDTO != null) {
            if (practiceLessonDTO.getLessonId() > 0) {
                sql.append(" and lesson_id = :lessonId ");
            }
            if (StringUtils.isNotEmpty(practiceLessonDTO.getPracticeName())) {
                sql.append(" and practice_name like concat('%', :practiceName, '%') ");
            }
            if (practiceLessonDTO.getCompanyCountry() > 0) {
                sql.append(" and company_country = :companyCountry ");
            }
            if (practiceLessonDTO.getIsTop() > 0) {
                sql.append(" and is_top = :isTop ");
            }
            if (practiceLessonDTO.getWeight() > 0) {
                sql.append(" and weight = :weight ");
            }
            if (practiceLessonDTO.getStatus() > 0) {
                sql.append(" and status = :status ");
            }
            sql.append(" order by status asc, is_top desc, weight desc, create_time desc ");
        }
        return baseDao.findPaging(sql.toString(), pageParam, practiceLessonDTO, PracticeLessonDTO.class);
    }

    /**
     * 新增运营实习职位
     * 
     * @param practiceLessonDTO
     * @return
     * @throws Exception
     */
    public long save(PracticeLessonDTO practiceLessonDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into operate_practice_lesson ( ");
        sql.append("	   practice_name, status, lesson_abstract, ");
        sql.append("	   industry_rcmd, company_country, company_location, ");
        sql.append("	   job_pic, practice_duration, is_top, ");
        sql.append("	   weight, create_time) ");
        sql.append("values ( ");
        sql.append("	   :practiceName, :status, :lessonAbstract, ");
        sql.append("	   :industryRcmd, :companyCountry, :companyLocation, ");
        sql.append("	   :jobPic, :practiceDuration, :isTop, ");
        sql.append("	   :weight, now()) ");

        return baseDao.insertInto(sql.toString(), practiceLessonDTO, "lesson_id");
    }

    /**
     * 修改运营实习职位
     * 
     * @param practiceLessonDTO
     * @return
     * @throws Exception
     */
    public int update(PracticeLessonDTO practiceLessonDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update operate_practice_lesson ");
        sql.append("   set practice_name = :practiceName, industry_rcmd = :industryRcmd, ");
        sql.append("	   company_country = :companyCountry, company_location = :companyLocation,  ");
        sql.append("	   job_pic = :jobPic, practice_duration = :practiceDuration, ");
        sql.append("	   is_top = :isTop, weight = :weight, modify_time =  now(), ");
        sql.append("       status = :status, lesson_abstract = :lessonAbstract ");
        sql.append(" where lesson_id = :lessonId ");

        return baseDao.executeSQL(sql.toString(), practiceLessonDTO);
    }

    /**
     * 根据运营实习职位ID获取运营实习职位
     * 
     * @param lessonId
     * @return
     * @throws Exception
     */
    public PracticeLessonDTO getPracticeById(long lessonId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select lesson_id, practice_name, company_rcmd, ");
        sql.append("	   practice_rcmd, industry_rcmd, company_country,  ");
        sql.append("	   company_location, job_pic, practice_duration, ");
        sql.append("	   is_top, weight, create_time, modify_time, status, ");
        sql.append("	   lesson_abstract ");
        sql.append("  from operate_practice_lesson ");
        sql.append(" where lesson_id = :lessonId ");

        Map<String, Long> params = new HashMap<>(1);
        params.put("lessonId", lessonId);

        return baseDao.get(sql.toString(), params, PracticeLessonDTO.class);
    }

    /**
     * 根据运营实习职位ID删除运营实习职位
     * 
     * @param lessonId
     * @return
     * @throws Exception
     */
    public int delete(long lessonId) throws Exception {
        String sql = "delete from operate_practice_lesson where lesson_id = :lessonId";

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("lessonId", lessonId);

        return baseDao.executeSQL(sql, params);
    }

    /**
     * 获取运营实习职位集合
     * 
     * @return
     * @throws Exception
     */
    public List<PracticeLessonDTO> findPracticeLessons() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select lesson_id, practice_name, company_rcmd, ");
        sql.append("	   practice_rcmd, industry_rcmd, company_country,  ");
        sql.append("	   company_location, job_pic, practice_duration, ");
        sql.append("	   is_top, weight, create_time, modify_time, status, ");
        sql.append("	   lesson_abstract ");
        sql.append("  from operate_practice_lesson ");
        sql.append(" where status = 1 ");

        return baseDao.findList(sql.toString(), null, PracticeLessonDTO.class);
    }

    /**
     * 更新富文本
     * 
     * @param lessonId
     * @param companyRcmdStr
     * @param practiceRcmdStr
     * @return
     * @throws Exception
     */
    public int updateRcmdStr(long lessonId, String companyRcmdStr, String practiceRcmdStr) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update operate_practice_lesson ");
        sql.append("   set company_rcmd = :companyRcmdStr, ");
        sql.append("       practice_rcmd = :practiceRcmdStr ");
        sql.append(" where lesson_id = :lessonId ");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("lessonId", lessonId);
        params.put("companyRcmdStr", companyRcmdStr);
        params.put("practiceRcmdStr", practiceRcmdStr);

        return baseDao.executeSQL(sql.toString(), params);
    }
}
