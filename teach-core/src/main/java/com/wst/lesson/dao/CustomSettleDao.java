/*
 * CustomSettleDao.java Created on 2017年10月11日 下午4:28:40
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
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.lesson.dto.CustomSettleDTO;

/**
 * 定制课程结算记录dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class CustomSettleDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 获取定制课程结算记录分页数据
	 * 
	 * @param pageParam
	 * @param customSettleDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomSettleDTO> findPaging(Page<CustomSettleDTO> pageParam, CustomSettleDTO customSettleDTO)
			throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select cs.record_id, cs.tutor_id, cs.lesson_id, cs.lesson_name, cs.class_id, ");
		sql.append("	   cs.class_name, cs.settle_duration, cs.settle_price, cs.create_time, cs.settle_status, ");
		sql.append("	   cs.pay_status, cs.pay_memo, mt.real_name, mt.portrait_img_url, mt.login_account, mt.email ");
		sql.append("  from lesson_custom_settle cs ");
		sql.append("  left join mem_tutor mt on cs.tutor_id = mt.tutor_id ");
		sql.append(" where 1 = 1 ");

		if (customSettleDTO != null) {
			if (customSettleDTO.getRecordId() > 0) {
				sql.append(" and cs.record_id = :recordId ");
			}
			if (customSettleDTO.getTutorId() > 0) {
				sql.append(" and cs.tutor_id = :tutorId ");
			}
			if (customSettleDTO.getLessonId() > 0) {
				sql.append(" and cs.lesson_id = :lessonId ");
			}
			if (StringUtils.isNotEmpty(customSettleDTO.getLessonName())) {
				sql.append(" and cs.lesson_name like concat('%', :lessonName, '%') ");
			}
			if (customSettleDTO.getClassId() > 0) {
				sql.append(" and cs.class_id = :classId ");
			}
			if (StringUtils.isNotEmpty(customSettleDTO.getClassName())) {
				sql.append(" and cs.class_name like concat('%', :className, '%') ");
			}
			if (customSettleDTO.getSettleStatus() > 0) {
				sql.append(" and cs.settle_status = :settleStatus ");
			}
			if (customSettleDTO.getBeginTime() != null) {
				sql.append(" and cs.create_time > :beginTime ");
			}
			if (customSettleDTO.getEndTime() != null) {
				sql.append(" and cs.create_time < :endTime ");
			}
			if (customSettleDTO.getPayStatus() > 0) {
				sql.append(" and cs.pay_status = :payStatus ");
			}
			// 导师姓名
			if (StringUtils.isNotEmpty(customSettleDTO.getRealName())) {
				sql.append(" and mt.real_name like concat('%', :realName, '%') ");
			}
			// 导师账号
			if (StringUtils.isNotEmpty(customSettleDTO.getLoginAccount())) {
				sql.append(" and mt.login_account like concat('%', :loginAccount, '%') ");
			}
		}

		sql.append(" order by cs.record_id desc ");

		return baseDao.findPaging(sql.toString(), pageParam, customSettleDTO, CustomSettleDTO.class);
	}

	/**
	 * 新增定制课程结算记录
	 * 
	 * @param customSettleDTO
	 * @return
	 * @throws Exception
	 */
	public long saveCustomSettle(CustomSettleDTO customSettleDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_custom_settle ( ");
		sql.append("	   tutor_id, lesson_id, lesson_name, class_id, class_name, ");
		sql.append("	   settle_duration, settle_price, create_time, settle_status,");
		sql.append("       pay_status) ");
		sql.append("values ( ");
		sql.append("	   :tutorId, :lessonId, :lessonName, :classId, :className, ");
		sql.append("	   :settleDuration, :settlePrice, :createTime, :settleStatus,");
		sql.append("       :payStatus) ");

		return baseDao.insertInto(sql.toString(), customSettleDTO, "record_id");
	}

	/**
	 * 获取导师结算金额
	 * 
	 * @param tutorId
	 *            导师ID
	 * @param settleType
	 *            结算类型，1-未结算，2-已结算
	 * @return
	 * @throws Exception
	 */
	public long getSettle(long tutorId, long settleType) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select sum(settle_duration * settle_price) ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where tutor_id = :tutorId ");
		sql.append("   and settle_status = :settleType ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("tutorId", tutorId);
		params.put("settleType", settleType);

		return baseDao.get(sql.toString(), params, Long.class);
	}

	/**
	 * 查询导师未结算记录
	 * 
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public List<CustomSettleDTO> findNotSettleCustomSettle(long tutorId, long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select record_id, tutor_id, lesson_id, lesson_name, class_id, class_name, ");
		sql.append("       settle_duration, settle_price, create_time, settle_status ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where settle_status = 1 ");
		sql.append("   and tutor_id = :tutorId ");
		sql.append("   and lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("tutorId", tutorId);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, CustomSettleDTO.class);
	}

	/**
	 * 已结算
	 * 
	 * @param recordId
	 *            记录ID
	 * @return
	 * @throws Exception
	 */
	public int haveSettle(long recordId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_settle ");
		sql.append("   set settle_status = 2 ");
		sql.append(" where record_id = :recordId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("recordId", recordId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 获取结算的课程
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public Page<CustomSettleDTO> findLesson(Page<CustomSettleDTO> pageParams, long tutorId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where tutor_id = :tutorId  ");
		sql.append(" group by lesson_id ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("tutorId", tutorId);

		return baseDao.findPaging(sql.toString(), pageParams, params, CustomSettleDTO.class);
	}

	/**
	 * 通过课程ID获取结算列表
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public List<CustomSettleDTO> findByLesson(long lessonId, long tutorId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select record_id, tutor_id, lesson_id, lesson_name, class_id, class_name, ");
		sql.append("       settle_duration, settle_price, create_time, settle_status, pay_status, ");
		sql.append("	   pay_memo ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and tutor_id = :tutorId ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("lessonId", lessonId);
		params.put("tutorId", tutorId);

		return baseDao.findList(sql.toString(), params, CustomSettleDTO.class);
	}

	/**
	 * 通过ID获取薪资结算信息
	 * 
	 * @param recordId
	 * @return
	 * @throws Exception
	 */
	public CustomSettleDTO getCustomSettleById(long recordId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select record_id, tutor_id, lesson_id, lesson_name, class_id, class_name, ");
		sql.append("       settle_duration, settle_price, create_time, settle_status, pay_status, ");
		sql.append("	   pay_memo ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where record_id = :recordId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("recordId", recordId);

		return baseDao.get(sql.toString(), params, CustomSettleDTO.class);
	}

	/**
	 * 人为结算
	 * 
	 * @param recordId
	 * @param payMemo
	 * @return
	 * @throws Exception
	 */
	public int memPaySettle(long recordId, String payMemo) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_settle ");
		sql.append("   set pay_status = 2, pay_memo = :payMemo ");
		sql.append(" where record_id = :recordId");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("recordId", recordId);
		params.put("payMemo", payMemo);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 通过课程ID获取未结算记录
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<CustomSettleDTO> findNoSttleByLessonId(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select record_id, tutor_id, lesson_id, lesson_name, class_id, class_name, ");
		sql.append("       settle_duration, settle_price, create_time, settle_status, pay_status, ");
		sql.append("       pay_memo ");
		sql.append("  from lesson_custom_settle ");
		sql.append(" where settle_status = 1 ");
		sql.append("   and lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, CustomSettleDTO.class);
	}
}
