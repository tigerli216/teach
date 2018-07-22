/*
 * CustomLessonDao.java Created on 2017年9月27日 上午10:45:48
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
import com.wst.service.operate.dto.CustomLessonDTO;

/**
 * 运营定制辅导课程Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class CustomLessonDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 获取运营定制辅导课分页数据
	 * 
	 * @param pageParam
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public Page<CustomLessonDTO> findPaging(Page<CustomLessonDTO> pageParam, CustomLessonDTO customLessonDTO)
			throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, lesson_name, lesson_cycle, class_num, ");
		sql.append("	   lesson_abstract, lesson_rcmd, target_mem, lesson_pic, ");
		sql.append("	   is_top, weight, create_time, modify_time, status ");
		sql.append("  from operate_custom_lesson ");
		sql.append(" where 1 = 1 ");

		if (customLessonDTO != null) {
			if (customLessonDTO.getLessonId() > 0) {
				sql.append(" and lesson_id = :lessonId ");
			}
			if (StringUtils.isNotEmpty(customLessonDTO.getLessonName())) {
				sql.append(" and lesson_name like concat('%',:lessonName,'%')");
			}
			if (StringUtils.isNotEmpty(customLessonDTO.getLessonCycle())) {
				sql.append(" and lesson_cycle = :lessonCycle ");
			}
			if (StringUtils.isNotEmpty(customLessonDTO.getClassNum())) {
				sql.append(" and class_num = :classNum ");
			}
			if (StringUtils.isNotEmpty(customLessonDTO.getTargetMem())) {
				sql.append(" and target_mem like concat ('%',:targetMem,'%')");
			}
			if (customLessonDTO.getIsTop() > 0) {
				sql.append(" and is_top = :isTop ");
			}
			if (customLessonDTO.getWeight() > 0) {
				sql.append(" and weight = :weight ");
			}
			if (customLessonDTO.getStatus() > 0) {
				sql.append(" and status = :status ");
			}
			sql.append(" order by status asc, is_top desc, weight desc, create_time desc ");
		}

		return baseDao.findPaging(sql.toString(), pageParam, customLessonDTO, CustomLessonDTO.class);
	}

	/**
	 * 新增运营定制辅导课程
	 * 
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public long save(CustomLessonDTO customLessonDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into operate_custom_lesson ( ");
		sql.append("	lesson_name, lesson_cycle, class_num, ");
		sql.append("	lesson_abstract, target_mem, lesson_pic, ");
		sql.append("	is_top, weight, create_time, status ) ");
		sql.append("  values ( ");
		sql.append("	:lessonName, :lessonCycle, :classNum, ");
		sql.append("	:lessonAbstract, :targetMem, :lessonPic, ");
		sql.append("	:isTop, :weight, now(), :status) ");

		return baseDao.insertInto(sql.toString(), customLessonDTO, "lesson_id");
	}

	/**
	 * 修改运营定制辅导课程
	 * 
	 * @param customLessonDTO
	 * @return
	 * @throws Exception
	 */
	public int update(CustomLessonDTO customLessonDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update operate_custom_lesson ");
		sql.append("   set lesson_name = :lessonName, lesson_cycle = :lessonCycle,  ");
		sql.append("	   class_num = :classNum, lesson_abstract = :lessonAbstract,  ");
		sql.append("	   status = :status, target_mem = :targetMem, lesson_pic = :lessonPic, ");
		sql.append("	   is_top = :isTop, weight = :weight, modify_time = now() ");
		sql.append(" where lesson_id = :lessonId ");

		return baseDao.executeSQL(sql.toString(), customLessonDTO);
	}

	/**
	 * 根据运营定制辅导课程ID删除运营定制辅导课程
	 * 
	 * @return
	 * @throws Exception
	 */
	public int delete(long lessonId) throws Exception {
		String sql = "delete from operate_custom_lesson where lesson_id = :lessonId";

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据运营定制辅导课程ID获取运营定制辅导课程
	 * 
	 * @return
	 * @throws Exception
	 */
	public CustomLessonDTO getCustomLessonById(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, lesson_name, lesson_cycle, class_num, ");
		sql.append("	   lesson_abstract, lesson_rcmd, target_mem, lesson_pic, ");
		sql.append("	   is_top, weight, create_time, modify_time, status ");
		sql.append("  from operate_custom_lesson ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.get(sql.toString(), params, CustomLessonDTO.class);
	}

	/**
	 * 获取运营定制辅导课程集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomLessonDTO> findCustomLessons() throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, lesson_name, lesson_cycle, class_num, ");
		sql.append("	   lesson_abstract, lesson_rcmd, target_mem, lesson_pic, ");
		sql.append("	   is_top, weight, create_time, modify_time, status ");
		sql.append("  from operate_custom_lesson ");
		sql.append(" where status = 1 ");

		return baseDao.findList(sql.toString(), null, CustomLessonDTO.class);
	}

	/**
	 * 修改课程说明
	 * 
	 * @param lessonId
	 * @param lessonRcmdStr
	 * @return
	 * @throws Exception
	 */
	public int updateLessonRcmd(long lessonId, String lessonRcmdStr) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update operate_custom_lesson ");
		sql.append("   set lesson_rcmd = :lessonRcmdStr ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("lessonRcmdStr", lessonRcmdStr);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 修改课程简介
	 * 
	 * @param lessonId
	 * @param lessonAbstract
	 * @return
	 * @throws Exception
	 */
	public int updateLessonAbstract(long lessonId, String lessonAbstract) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update operate_custom_lesson ");
		sql.append("   set lesson_abstract = :lessonAbstract ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("lessonAbstract", lessonAbstract);

		return baseDao.executeSQL(sql.toString(), params);
	}
}
