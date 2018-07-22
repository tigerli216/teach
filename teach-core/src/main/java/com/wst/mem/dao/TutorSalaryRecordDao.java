/*
 * TutorSalaryRecordDao.java Created on 2017年9月27日 下午9:10:20
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.mem.dto.TutorSalaryRecordDTO;

/**
 * 导师薪资记录调整DAO
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class TutorSalaryRecordDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 新增导师薪资调整
	 * 
	 * @param tutorSalaryRecordDTO
	 * @return
	 * @throws Exception
	 */
	public long save(TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into mem_tutor_salary_record ( ");
		sql.append("	   tutor_id, befor_salary, end_salary, ");
		sql.append("	   change_explain, change_time, change_op) ");
		sql.append("values (:tutorId, :beforSalary, :endSalary, ");
		sql.append("	   :changeExplain, now(), :changeOp) ");

		return baseDao.insertInto(sql.toString(), tutorSalaryRecordDTO, "record_id");
	}

	/**
	 * 根据导师ID 获取最新一次调薪
	 * 
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public TutorSalaryRecordDTO getTutorSalaryRecord(long tutorId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select record_id, tutor_id, befor_salary, end_salary, ");
		sql.append("       change_explain, change_time, change_op ");
		sql.append("  from mem_tutor_salary_record ");
		sql.append(" where tutor_id = :tutorId ");
		sql.append(" order by record_id desc limit 1");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("tutorId", tutorId);

		return baseDao.get(sql.toString(), params, TutorSalaryRecordDTO.class);
	}

	/**
	 * 获取导师薪资调整列表
	 * 
	 * @param pageParam
	 * @param tutorSalaryRecordDTO
	 * @return
	 * @throws Exception
	 */
	public Page<TutorSalaryRecordDTO> findTutorSalaryPaging(Page<TutorSalaryRecordDTO> pageParam,
			TutorSalaryRecordDTO tutorSalaryRecordDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select tsr.record_id, tsr.tutor_id, tsr.befor_salary, ");
		sql.append("       tsr.end_salary,tsr.change_explain, tsr.change_time, ");
		sql.append("       tsr.change_op, mt.real_name ");
		sql.append("  from mem_tutor_salary_record tsr");
		sql.append("  join mem_tutor mt ");
		sql.append("    on tsr.tutor_id = mt.tutor_id ");
		sql.append(" where 1 = 1 ");

		if (tutorSalaryRecordDTO != null) {
			if (tutorSalaryRecordDTO.getTutorId() > 0) {
				sql.append(" and tsr.tutor_id = :tutorId ");
			}

			if (tutorSalaryRecordDTO.getBeginTime() != null) {
				sql.append(" and tsr.change_time > :beginTime ");
			}

			if (tutorSalaryRecordDTO.getEndTime() != null) {
				sql.append(" and tsr.change_time < :endTime ");
			}
		}
		sql.append(" order by tsr.record_id desc ");

		return baseDao.findPaging(sql.toString(), pageParam, tutorSalaryRecordDTO, TutorSalaryRecordDTO.class);
	}
}
