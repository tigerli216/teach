/*
 * ActivityExtractDao.java Created on 2017年9月28日 下午4:00:50
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
package com.wst.act.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.act.dto.ActivityExtractDTO;

/**
 * 活动提取码配置Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class ActivityExtractDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 活动提取码信息分页数据
	 * 
	 * @param pageParam
	 * @param activityExtractDTO
	 * @return
	 * @throws Exception
	 */
	public Page<ActivityExtractDTO> findPaging(Page<ActivityExtractDTO> pageParam,
			ActivityExtractDTO activityExtractDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select ae.config_id, ae.act_id, ae.lesson_id, ae.class_ids, ");
		sql.append("	   ae.extract_code, ae.user_id, ae.user_ip, ae.create_time, ");
		sql.append("	   ae.use_time, ae.valid_time, p.lesson_name, u.login_account, ");
		sql.append("       oa.act_name ");
		sql.append("  from operate_activity_extract ae ");
		sql.append("  join lesson_public p on ae.lesson_id = p.lesson_id ");
		sql.append("  join operate_activity oa on ae.act_id = oa.act_id ");
		sql.append("  left join mem_user u on ae.user_id = u.user_id ");
		sql.append(" where 1 = 1 ");

		if (activityExtractDTO != null) {
			if (activityExtractDTO.getActId() > 0) {
				sql.append(" and ae.config_id = :configId ");
			}
			if (activityExtractDTO.getActId() > 0) {
				sql.append(" and ae.act_id = :actId ");
			}
			if (activityExtractDTO.getLessonId() > 0) {
				sql.append(" and ae.lesson_id = :lessonId ");
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getExtractCode())) {
				sql.append(" and ae.extract_code = :extractCode ");
			}
			if (activityExtractDTO.getUserId() > 0) {
				sql.append(" and ae.user_id = :userId ");
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getLoginAccount())) {
				sql.append(" and u.login_account like concat('%', :loginAccount, '%') ");
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getLessonName())) {
				sql.append(" and p.lesson_name like concat('%', :lessonName, '%') ");
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getActName())) {
				sql.append(" and oa.act_name like concat('%', :actName, '%') ");
			}
			if (activityExtractDTO.getUseStatus() == 1) {
				sql.append(" and ae.user_id = 0 ");
			}
			if (activityExtractDTO.getUseStatus() == 2) {
				sql.append(" and ae.user_id > 0 ");
			}
		}

		sql.append(" order by ae.config_id desc ");

		return baseDao.findPaging(sql.toString(), pageParam, activityExtractDTO, ActivityExtractDTO.class);
	}

	/**
	 * 新增活动提取码
	 * 
	 * @return
	 * @throws Exception
	 */
	public long saveActivityExtract(ActivityExtractDTO activityExtractDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into  operate_activity_extract ( ");
		sql.append("	   act_id, lesson_id, class_ids, extract_code, ");
		sql.append("	   user_id, user_ip, create_time, use_time, valid_time) ");
		sql.append("values ( ");
		sql.append("	   :actId, :lessonId, :classIds, :extractCode, ");
		sql.append("	   :userId, :userIp, now(), :useTime, :validTime) ");

		return baseDao.insertInto(sql.toString(), activityExtractDTO, "config_id");
	}

	/**
	 * 通过课程ID获取活动提取码
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<ActivityExtractDTO> findByLessonId(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select config_id, act_id, lesson_id, class_ids, extract_code, ");
		sql.append("	   user_id, user_ip, create_time, use_time, valid_time ");
		sql.append("  from operate_activity_extract ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append(" and valid_time > now() ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, ActivityExtractDTO.class);
	}

	/**
	 * 通过用户ID和课程ID获取在有效期内的提取码
	 * 
	 * @param lessonId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ActivityExtractDTO> findBylessonIduserId(long lessonId, long userId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select config_id, act_id, lesson_id, class_ids, extract_code, ");
		sql.append("	   user_id, user_ip, create_time, use_time, valid_time ");
		sql.append("  from operate_activity_extract ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and user_id = :userId ");
		sql.append("   and valid_time > now() ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("lessonId", lessonId);
		params.put("userId", userId);

		return baseDao.findList(sql.toString(), params, ActivityExtractDTO.class);
	}

	/**
	 * 通过活动提取码获取提取码信息
	 * 
	 * @param extractCode
	 * @return
	 * @throws Exception
	 */
	public ActivityExtractDTO getByExtractCode(String extractCode) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select config_id, act_id, lesson_id, class_ids, extract_code, ");
		sql.append("	   user_id, user_ip, create_time, use_time, valid_time ");
		sql.append("  from operate_activity_extract ");
		sql.append(" where extract_code = :extractCode ");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("extractCode", extractCode);

		return baseDao.get(sql.toString(), params, ActivityExtractDTO.class);
	}

	/**
	 * 使用提取码
	 * 
	 * @param userId
	 * @param userIp
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	public int useExtractCode(long userId, String userIp, long configId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update operate_activity_extract ");
		sql.append("   set user_id = :userId, user_ip = :userIp , use_time = now() ");
		sql.append(" where config_id = :configId ");

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("userId", userId);
		params.put("userIp", userIp);
		params.put("configId", configId);

		return baseDao.executeSQL(sql.toString(), params);
	}
}
