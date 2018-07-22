/*
 * customDao.java Created on 2017年9月29日 上午10:53:33
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.lesson.dto.CustomDTO;

/**
 * 定制课查询dao
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class CustomDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 分页查询定制课课程
	 * 
	 * @param page
	 * @param customDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomDTO> fingPaging(Page<CustomDTO> page, CustomDTO customDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select le.lesson_id, le.user_id, le.lesson_type, le.lesson_name, ");
		sql.append("       le.industry_id, le.industry, le.lesson_price, le.total_class, ");
		sql.append("       le.finish_class, le.current_class, le.lesson_rcmd, le.begin_time, ");
		sql.append("       le.end_time, le.create_time, le.create_op, le.modify_time, ");
		sql.append("       le.modify_op, le.buy_time, le.adviser_code, mu.login_account, ");
		sql.append("       bo.order_code, bo.pay_status ");

		sql.append("  from lesson_custom le ");
		sql.append("  join mem_user mu on le.user_id = mu.user_id");

		if (customDTO.isBuy() || customDTO.getPayStatus() > 0) {
			sql.append(" join bm_order bo ");
		} else {
			sql.append(" left join bm_order bo ");
		}
		sql.append("  on bo.user_id = le.user_id ");
		sql.append(" and bo.busi_id = le.lesson_id ");

		// 是否已购买
		if (customDTO.isBuy() || customDTO.getPayStatus() > 0) {
			// 已购买必须是已付款
			if (customDTO.getPayStatus() == 0) {
				customDTO.setPayStatus(2);
			}
			if (customDTO.getPayStatus() != 4) {
				sql.append(" and bo.order_status = 1 ");
			}
			sql.append(" and bo.order_type = 2 ");
			sql.append(" and bo.pay_status = :payStatus ");
		}

		sql.append(" where 1 = 1 ");
		// 课程名称
		if (StringUtils.isNotEmpty(customDTO.getLessonName())) {
			sql.append(" and le.lesson_name like concat('%', :LessonName, '%') ");
		}
		// 课程分类
		if (customDTO.getLessonType() > 0) {
			sql.append(" and le.lesson_type = :lessonType ");
		}
		// 用户帐号
		if (StringUtils.isNotEmpty(customDTO.getLoginAccount())) {
			sql.append(" and mu.login_account like concat('%', :loginAccount, '%')");
		}
		// 行业
		if (customDTO.getIndustryId() > 0) {
			sql.append(" and le.industry_id = :industryId");
		}
		// 开始时间
		if (customDTO.getBeginTime() != null) {
			sql.append(" and le.begin_time >= :beginTime");
		}
		// 结束时间
		if (customDTO.getEndTime() != null) {
			sql.append(" and le.end_time <= :endTime");
		}
		// 学生ID
		if (customDTO.getUserId() > 0) {
			sql.append(" and le.user_id = :userId ");
		}

		sql.append(" order by le.lesson_id desc ");

		return baseDao.findPaging(sql.toString(), page, customDTO, CustomDTO.class);
	}

	/**
	 * 根据定制课课程id获取定制课课程信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public CustomDTO getLessonById(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, user_id, lesson_type, lesson_name, industry_id, ");
		sql.append("       industry, lesson_price, total_class, finish_class, current_class, ");
		sql.append("       lesson_rcmd, begin_time, end_time, create_time, create_op, ");
		sql.append("       modify_time, modify_op, buy_time, adviser_code ");
		sql.append("  from lesson_custom ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.get(sql.toString(), params, CustomDTO.class);
	}

	/**
	 * 新增定制课课程
	 * 
	 * @param productDTO
	 * @return
	 * @throws Exception
	 */
	public long insertLesson(CustomDTO customDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_custom (lesson_type, lesson_name, industry_id, industry, ");
		sql.append("       adviser_code, lesson_price, total_class, finish_class, current_class, ");
		sql.append("       lesson_rcmd, user_id, begin_time, end_time, create_time, create_op )");
		sql.append("values (:lessonType, :lessonName, :industryId, :industry,  ");
		sql.append("       :adviserCode, :lessonPrice, :totalClass, :finishClass, :currentClass, ");
		sql.append("       :lessonRcmd, :userId, :beginTime, :endTime, now(), :createOp )");

		return baseDao.insertInto(sql.toString(), customDTO, "lesson_id");
	}

	/**
	 * 修改定制课课程信息
	 * 
	 * @param prodDTO
	 * @throws Exception
	 */
	public int updateLesson(CustomDTO customDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom ");
		sql.append("   set lesson_type = :lessonType, lesson_name = :lessonName, ");
		sql.append("       industry_id = :industryId, industry = :industry, ");
		sql.append("       lesson_price = :lessonPrice, modify_time = now(), ");
		sql.append("       total_class = :totalClass, modify_op = :modifyOp, ");
		sql.append("       user_id = :userId, begin_time = :beginTime, ");
		sql.append("       end_time = :endTime ");
		sql.append(" where lesson_id = :lessonId ");

		return baseDao.executeSQL(sql.toString(), customDTO);
	}

	/**
	 * 根据导师id分页查询定制课课程信息
	 * 
	 * @param page
	 * @param customDTO
	 *            tutorId 导师ID
	 * @return
	 * @throws Exception
	 */
	public Page<CustomDTO> fingPagingByTutorId(Page<CustomDTO> page, CustomDTO customDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select le.lesson_id, le.user_id, le.lesson_type, le.lesson_name, le.industry_id, ");
		sql.append("       le.industry, le.lesson_price, le.total_class, le.finish_class, le.current_class, ");
		sql.append("       le.lesson_rcmd, le.begin_time, le.end_time, le.create_time, le.create_op, ");
		sql.append("       le.modify_time, le.modify_op, le.buy_time, le.adviser_code, mu.login_account ");
		sql.append("  from lesson_custom le ");
		sql.append("  left join lesson_custom_class lc on lc.lesson_id = le.lesson_id ");
		sql.append("  join bm_order bo on bo.busi_id = le.lesson_id ");
		sql.append("  join mem_user mu on le.user_id = mu.user_id ");
		sql.append(" where lc.tutor_id = :tutorId ");
		sql.append("   and le.buy_time is not null ");
		sql.append("   and bo.order_status = 1 ");
		sql.append("   and bo.pay_status = 2 ");
		sql.append("   and bo.order_type = 2 ");

		if (StringUtils.isNoneEmpty(customDTO.getLessonName())) {
			customDTO.setLessonName("%" + customDTO.getLessonName() + "%");
			sql.append(" and le.lesson_name like :LessonName ");
		}

		if (customDTO.getLessonType() > 0) {
			sql.append(" and le.lesson_type = :lessonType ");
		}

		sql.append(" group by le.lesson_id ");
		sql.append(" order by le.create_time desc ");

		return baseDao.findPaging(sql.toString(), page, customDTO, CustomDTO.class);
	}

	/**
	 * 修改课程当前课时
	 * 
	 * @param lessonId
	 *            课程ID
	 * @param currentClass
	 *            当前课时
	 * @param finishClass
	 *            已用时长
	 * @return
	 * @throws Exception
	 */
	public int updateCurrentClass(long lessonId, long currentClass, long finishClass) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom ");
		sql.append("   set current_class = :currentClass, finish_class = finish_class + :finishClass ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(3);
		params.put("currentClass", currentClass);
		params.put("finishClass", finishClass);
		params.put("lessonId", lessonId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 查询有效时间内未结束的课程
	 * 
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	public List<CustomDTO> findUnclosedCustom(long lessonId, long userId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select le.lesson_id, le.user_id, le.lesson_type, le.lesson_name, le.industry_id, ");
		sql.append("       le.industry, le.lesson_price, le.total_class, le.finish_class, le.current_class, ");
		sql.append("       le.lesson_rcmd, le.begin_time, le.end_time, le.create_time, le.create_op, ");
		sql.append("       le.modify_time, le.modify_op, le.buy_time, le.adviser_code ");
		sql.append("  from lesson_custom le");
		sql.append("  left join lesson_custom_class lc on lc.lesson_id = le.lesson_id ");
		sql.append("  join bm_order bo on le.lesson_id = bo.busi_id ");
		sql.append(" where le.lesson_id <> :lessonId and le.user_id = :userId ");
		sql.append("   and le.begin_time < now() ");
		sql.append("   and le.end_time > now() ");
		sql.append("   and lc.class_status in (1, 2) ");
		sql.append("   and bo.order_status = 1 ");
		sql.append("   and bo.pay_status = 2 ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("lessonId", lessonId);
		params.put("userId", userId);

		return baseDao.findList(sql.toString(), params, CustomDTO.class);
	}

	/**
	 * 购买课程
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public int buyCustom(long lessonId) throws Exception {
		String sql = "update lesson_custom set buy_time = now() where lesson_id = :lessonId";

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 更新课程介绍（富文本）
	 * 
	 * @param lessonId
	 * @param lessonRcmdStr
	 * @return
	 * @throws Exception
	 */
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom ");
		sql.append("   set lesson_rcmd = :lessonRcmdStr ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("lessonRcmdStr", lessonRcmdStr);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 通过课程名称获取课程信息
	 * 
	 * @param lessonName
	 * @return
	 * @throws Exception
	 */
	public CustomDTO getLessonByLessonName(String lessonName) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, user_id, lesson_type, lesson_name, industry_id, ");
		sql.append("       industry, lesson_price, total_class, finish_class, current_class, ");
		sql.append("       lesson_rcmd, begin_time, end_time, create_time, create_op, ");
		sql.append("       modify_time, modify_op, buy_time, adviser_code ");
		sql.append("  from lesson_custom ");
		sql.append(" where lesson_name = :lessonName ");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("lessonName", lessonName);

		return baseDao.get(sql.toString(), params, CustomDTO.class);
	}
}
