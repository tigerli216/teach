/*
 * MssageDao.java Created on 2017年10月12日 上午9:54:18
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
import com.wst.entity.mem.Mssage;
import com.wst.service.mem.dto.MssageDTO;

/**
 * 学生消息Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class MssageDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 保存消息
	 * 
	 * @param mssage
	 * @return
	 * @throws Exception
	 */
	public long save(Mssage mssage) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("insert into mem_mssage (op_id, send_user, msg_type, msg_source, ");
		sql.append("       busi_type, user_id, msg_title, msg_content, msg_status, ");
		sql.append("       send_time, begin_time, end_time) ");
		sql.append(" value (:opId, :sendUser, :msgType, :msgSource, ");
		sql.append("       :busiType, :userId, :msgTitle, :msgContent, :msgStatus, ");
		sql.append("       :sendTime, :beginTime, :endTime)");

		return baseDao.insertInto(sql.toString(), mssage, "msg_id");
	}

	/**
	 * 分页查询消息
	 * 
	 * @param pageParam
	 * @param mssageDTO
	 * @return
	 * @throws Exception
	 */
	public Page<MssageDTO> pageFindMssage(Page<MssageDTO> pageParam, MssageDTO mssageDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select msg_id, op_id, send_user, msg_type, msg_source, busi_type, user_id, ");
		sql.append("       msg_title, msg_content, msg_status, send_time, begin_time, end_time ");
		sql.append("  from mem_mssage s ");
		sql.append(" where 1 = 1 ");

		if (mssageDTO != null) {
			if (mssageDTO.getMsgId() > 0) {
				sql.append(" and msg_id = :msgId");
			}
			if (mssageDTO.getOpId() > 0) {
				sql.append(" and op_id = :opId");
			}
			if (mssageDTO.getMsgType() > 0) {
				sql.append(" and msg_type = :msgType");
			}
			if (mssageDTO.getUserId() > 0) {
				sql.append(" and user_id = :userId");
			}
			if (mssageDTO.getMsgStatus() > 0) {
				sql.append(" and msg_status = :msgStatus");
			}
			if (StringUtils.isNotEmpty(mssageDTO.getMsgSource())) {
				sql.append(" and msg_source = :msgSource");
			}
		}
		sql.append(" order by send_time desc ");
		return baseDao.findPaging(sql.toString(), pageParam, mssageDTO, MssageDTO.class);
	}

}
