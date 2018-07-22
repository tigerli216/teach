/*
 * TutorMssageDao.java Created on 2017年10月12日 上午9:54:54
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.entity.mem.TutorMssage;
import com.wst.service.mem.dto.TutorMssageDTO;

/**
 * 导师消息Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class TutorMssageDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 保存数据
	 * 
	 * @param tutorMssage
	 * @return
	 * @throws Exception
	 */
	public long save(TutorMssage tutorMssage) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into mem_tutor_mssage (op_id, send_user, msg_type, msg_source, busi_type, ");
		sql.append("       tutor_id, msg_title, msg_content, msg_status, send_time, begin_time, end_time)");
		sql.append(" value (:opId, :sendUser, :msgType, :msgSource, :busiType, ");
		sql.append("       :tutorId, :msgTitle, :msgContent, :msgStatus, :sendTime, :beginTime, :endTime)");

		return baseDao.insertInto(sql.toString(), tutorMssage, "msg_id");
	}

	/**
	 * 分页获取导师信息列表
	 * 
	 * @param pageParam
	 * @param tutorMssageDTO
	 * @return
	 * @throws Exception
	 */
	public Page<TutorMssageDTO> findPaging(Page<TutorMssageDTO> pageParam, TutorMssageDTO tutorMssageDTO)
			throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select msg_id, op_id, send_user, msg_type, msg_source, busi_type, tutor_id, ");
		sql.append("       msg_title, msg_content, msg_status, send_time, begin_time, end_time ");
		sql.append("  from mem_tutor_mssage s");
		sql.append(" where 1 = 1 ");

		if (tutorMssageDTO != null) {
			if (tutorMssageDTO.getMsgId() > 0) {
				sql.append(" and msg_id = :msgId");
			}
			if (tutorMssageDTO.getOpId() > 0) {
				sql.append(" and op_id = :opId");
			}
			if (tutorMssageDTO.getMsgType() > 0) {
				sql.append(" and msg_type = :msgType");
			}
			if (tutorMssageDTO.getTutorId() > 0) {
				sql.append(" and tutor_id = :tutorId");
			}
			if (tutorMssageDTO.getMsgStatus() > 0) {
				sql.append(" and msg_status = :msgStatus");
			}
			if (StringUtils.isNotEmpty(tutorMssageDTO.getMsgSource())) {
				sql.append(" and msg_source = :msgSource");
			}
		}
		sql.append(" order by send_time desc ");

		return baseDao.findPaging(sql.toString(), pageParam, tutorMssageDTO, TutorMssageDTO.class);
	}
}
