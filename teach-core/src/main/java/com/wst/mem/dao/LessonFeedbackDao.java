/*
 * LessonFeedbackDao.java Created on 2017年10月10日 下午5:40:27
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
package com.wst.mem.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.mem.dto.LessonFeedbackDTO;

/**
 * 用户课程反馈Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class LessonFeedbackDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取用户课程反馈管理分页数据
     * 
     * @param pageParam
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public Page<LessonFeedbackDTO> findPaging(Page<LessonFeedbackDTO> pageParam, LessonFeedbackDTO lessonFeedbackDTO)
            throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select mlf.fb_id, mlf.user_id, mlf.lesson_type, mlf.lesson_id, ");
        sql.append("	   mlf.lesson_name, mlf.content, mlf.create_time, usr.login_account ");
        sql.append("  from mem_lesson_feedback mlf ");
        sql.append("  left join mem_user usr on mlf.user_id = usr.user_id ");
        sql.append(" where 1 = 1 ");

        if (lessonFeedbackDTO != null) {
            // 反馈ID
            if (lessonFeedbackDTO.getFbId() > 0) {
                sql.append(" and mlf.fb_id = :fbId ");
            }
            // 会员ID
            if (lessonFeedbackDTO.getUserId() > 0) {
                sql.append(" and mlf.user_id = :userId ");
            }
            // 课程类别
            if (lessonFeedbackDTO.getLessonType() > 0) {
                sql.append(" and mlf.lesson_type = :lessonType ");
            }
            // 课程ID
            if (lessonFeedbackDTO.getLessonId() > 0) {
                sql.append(" and mlf.lesson_id = :lessonId ");
            }
            // 课程名称
            if (StringUtils.isNotEmpty(lessonFeedbackDTO.getLessonName())) {
                sql.append(" and mlf.lesson_name like :lessonName ");
            }
            // 学生账号
            if (StringUtils.isNotEmpty(lessonFeedbackDTO.getLoginAccount())) {
                sql.append(" and usr.login_account like :loginAccount ");
            }
            //创建时间
            if(null != lessonFeedbackDTO.getCreateTime()){
                sql.append(" and mlf.create_time > :createTime");
            }
            /*//结束时间
            if(null != lessonFeedbackDTO.getCreateTime()){
                sql.append(" and mlf.create_time < :endTime");
            }*/
        }

        sql.append(" order by fb_id desc ");

        return baseDao.findPaging(sql.toString(), pageParam, lessonFeedbackDTO, LessonFeedbackDTO.class);
    }

    /**
     * 新增用户课程反馈
     * 
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public long save(LessonFeedbackDTO lessonFeedbackDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into mem_lesson_feedback ( ");
        sql.append("	   user_id, lesson_type, lesson_id, lesson_name, content, create_time) ");
        sql.append("values ( ");
        sql.append("	   :userId, :lessonType, :lessonId, :lessonName, :content, now()) ");

        return baseDao.insertInto(sql.toString(), lessonFeedbackDTO, "fb_id");
    }

    /**
     * 获取用户可反馈的课程列表
     * 
     * @param pageParam
     * @param lessonFeedbackDTO
     * @return
     * @throws Exception
     */
    public Page<LessonFeedbackDTO> findNotFeedbackLesson(Page<LessonFeedbackDTO> pageParam,
            LessonFeedbackDTO lessonFeedbackDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select lc.lesson_id, lc.lesson_name ");
        sql.append("  from lesson_custom lc ");
        sql.append("  join bm_order bo on bo.busi_id = lc.lesson_id ");
        sql.append(" where bo.order_type = 2 ");
        sql.append("   and bo.order_status = 1 ");
        sql.append("   and bo.pay_status = 2 ");
        sql.append("   and bo.user_id = :userId");

        return baseDao.findPaging(sql.toString(), pageParam, lessonFeedbackDTO, LessonFeedbackDTO.class);
    }

}
