/*
 * PublicDao.java Created on 2017年9月27日 下午3:19:51
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
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;

/**
 * 网课查询dao
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class PublicDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 
	 * 分页查询网课课程
	 * 
	 * @param page
	 * @param holidayDTO
	 * @return
	 * @throws Exception
	 */
	public Page<PublicDTO> findPaging(Page<PublicDTO> page, PublicDTO publicDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select le.lesson_id, le.lesson_type, le.lesson_name, le.series_name, ");
		sql.append("       le.industry_id, le.industry, le.visit_auth, le.orig_price, ");
		sql.append("       le.discount_price, le.lesson_rcmd, le.tutor_id, le.tutor_rcmd, ");
		sql.append("       le.tutor_salary, le.shelf_status, le.valid_time, le.is_top, ");
		sql.append("       le.weight, le.create_time, le.create_op, le.modify_time, ");
		sql.append("       le.modify_op, le.lesson_pic, mt.real_name tutorName, le.valid_duration");
		sql.append("  from lesson_public le ");
		sql.append("  left join mem_tutor mt on le.tutor_id = mt.tutor_id ");
		sql.append(" where 1 = 1");

		// 课程分类
		if (publicDTO.getLessonType() > 0) {
			sql.append(" and le.lesson_type = :lessonType ");
		}

		// 系列名称
		if (StringUtils.isNotEmpty(publicDTO.getSeriesName())) {
			sql.append(" and le.series_name = :seriesName ");
		}

		// 行业ID
		if (publicDTO.getIndustryId() > 0) {
			sql.append(" and le.industry_id = :industryId ");
		}

		// 讲师ID
		if (publicDTO.getTutorId() > 0) {
			sql.append(" and le.tutor_id = :tutorId ");
		}

		// 讲师名称
		if (StringUtils.isNotEmpty(publicDTO.getTutorName())) {
			sql.append(" and mt.real_name like concat('%', :tutorName, '%')");
		}

		// 访问权限
		if (publicDTO.getVisitAuth() > 0) { // 游客
			sql.append(" and le.visit_auth <= :visitAuth ");
		}

		// 上架状态
		if (publicDTO.getShelfStatus() > 0) {
			sql.append(" and le.shelf_status = :shelfStatus ");
		}

		// 课程名称
		if (StringUtils.isNotEmpty(publicDTO.getLessonName())) { // 模糊搜索课程名
			sql.append(" and le.lesson_name like concat('%',:LessonName,'%')");
		}
		// sql.append(" and le.valid_time > now() "); // 有效期
		sql.append(" order by le.shelf_status, le.is_top desc, le.weight desc, le.create_time desc ");

		return baseDao.findPaging(sql.toString(), page, publicDTO, PublicDTO.class);
	}

	/**
	 * 根据网课课程id获取网课课程信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public PublicDTO getLessonById(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, lesson_type, lesson_name, series_name, industry_id, ");
		sql.append("       industry, visit_auth, orig_price, discount_price, lesson_rcmd,  ");
		sql.append("       tutor_id, tutor_rcmd, tutor_salary, shelf_status, is_top, valid_duration, ");
		sql.append("       weight, create_time, create_op, modify_time, modify_op, lesson_pic ");
		sql.append("  from lesson_public ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.get(sql.toString(), params, PublicDTO.class);
	}

	/**
	 * 新增网课课程
	 * 
	 * @param productDTO
	 * @return
	 * @throws Exception
	 */
	public long insertLesson(PublicDTO publicDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_public (lesson_type, lesson_name, series_name, industry_id, ");
		sql.append("       industry, visit_auth, orig_price, discount_price, lesson_rcmd, tutor_id, ");
		sql.append("       tutor_rcmd, tutor_salary, shelf_status, is_top, weight, ");
		sql.append("       create_time, create_op, lesson_pic, valid_duration)");
		sql.append("values (:lessonType, :lessonName, :seriesName, :industryId, ");
		sql.append("       :industry, :visitAuth, :origPrice, :discountPrice, :lessonRcmdStr, :tutorId, ");
		sql.append("       :tutorRcmd, :tutorSalary, :shelfStatus, :isTop, :weight, ");
		sql.append("       now(), :createOp, :lessonPic, :validDuration)");

		return baseDao.insertInto(sql.toString(), publicDTO, "lesson_id");
	}

	/**
	 * 修改网课课程信息
	 * 
	 * @param prodDTO
	 * @throws Exception
	 */
	public int updateLesson(PublicDTO publicDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public ");
		sql.append("   set lesson_type = :lessonType, lesson_name = :lessonName, ");
		sql.append("       series_name =:seriesName, industry_id = :industryId, ");
		sql.append("       industry = :industry, visit_auth = :visitAuth, ");
		sql.append("       orig_price = :origPrice, discount_price = :discountPrice, ");
		sql.append("       lesson_pic = :lessonPic, tutor_id = :tutorId, ");
		sql.append("       tutor_rcmd = :tutorRcmd, tutor_salary = :tutorSalary,");
		sql.append("       is_top = :isTop, weight = :weight, valid_duration = :validDuration, ");
		sql.append("       modify_time = now(), modify_op = :modifyOp ");
		sql.append(" where lesson_id = :lessonId ");

		return baseDao.executeSQL(sql.toString(), publicDTO);
	}

	/**
	 * 网课课程上架或下架
	 * 
	 * @param lessonId
	 * @param shelfStatus
	 * @throws Exception
	 */
	public int upOrDownoLesson(long lessonId, long shelfStatus) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public ");
		sql.append("   set shelf_status = :shelfStatus ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("shelfStatus", shelfStatus);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 获取网课集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PublicDTO> findPublicLessons() throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id, lesson_type, lesson_name, series_name, industry_id, ");
		sql.append("       industry, visit_auth, orig_price, discount_price, lesson_rcmd,  ");
		sql.append("       tutor_id, tutor_rcmd, tutor_salary, shelf_status, valid_time, is_top, ");
		sql.append("       weight, create_time, create_op, modify_time, modify_op, lesson_pic, valid_duration ");
		sql.append("  from lesson_public ");

		return baseDao.findList(sql.toString(), null, PublicDTO.class);
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

		sql.append("update lesson_public ");
		sql.append("   set lesson_rcmd = :lessonRcmdStr ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("lessonRcmdStr", lessonRcmdStr);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 获取导师网课列表 以课时状态2,1,3 和 开始时间排序
	 * 
	 * @param param
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public Page<PublicClassDTO> findMyPublic(Page<PublicClassDTO> page, long tutorId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select pc.lesson_id, p.lesson_name, pc.end_time, ");
		sql.append("       pc.class_status, pc.begin_time, pc.class_id ");
		sql.append("  from lesson_public_class pc");
		sql.append("  join lesson_public p on pc.lesson_id = p.lesson_id ");
		sql.append(" where p.tutor_id = :tutorId ");
		sql.append("   and lesson_type = 2 ");
		sql.append("   and shelf_status = 1 ");
		sql.append(" group by pc.lesson_id ");
		sql.append(" order by (case class_status when 2 then 1 when 1 then 2 when 3 then 3 end) asc, begin_time desc ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("tutorId", tutorId);

		return baseDao.findPaging(sql.toString(), page, params, PublicClassDTO.class);
	}

	/**
	 * 获取用户已购买网课
	 * 
	 * @param page
	 * @param publicDTO
	 * @return
	 * @throws Exception
	 */
	public Page<PublicDTO> findUserPublicPaging(Page<PublicDTO> page, long userId, String lessonName, long lessonType)
			throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select le.lesson_id, le.lesson_type, le.lesson_name, le.series_name, ");
		sql.append("       le.industry_id, le.industry, le.visit_auth, le.orig_price, ");
		sql.append("       le.discount_price, le.tutor_id, le.tutor_rcmd, ");
		sql.append("       le.tutor_salary, le.shelf_status, le.valid_time, le.is_top, ");
		sql.append("       le.weight, le.create_time, le.create_op, le.modify_time, ");
		sql.append("       le.modify_op, le.lesson_pic, le.valid_duration ");
		sql.append("  from lesson_public le ");
		sql.append("  join bm_order bo ");
		sql.append("    on le.lesson_id = bo.busi_id ");
		sql.append(" where bo.order_status = 1 ");
		sql.append("   and bo.pay_status = 2 ");
		sql.append("   and bo.user_id = :userId ");
		sql.append("   and le.valid_duration > now() ");

		if (StringUtils.isNotEmpty(lessonName)) {
			sql.append(" and le.lesson_name like :lessonName ");
		}

		if (lessonType > 0) {
			sql.append(" and le.lesson_type = :lessonType ");
		}

		Map<String, Object> param = new HashMap<String, Object>(3);
		param.put("userId", userId);
		param.put("lessonName", "%" + lessonName + "%");
		param.put("lessonType", lessonType);

		return baseDao.findPaging(sql.toString(), page, param, PublicDTO.class);
	}

	/**
	 * 更新网课导师介绍
	 * 
	 * @param lessonId
	 * @param lessonPic
	 * @return
	 * @throws Exception
	 */
	public int updateTutorRcmd(long lessonId, String tutorRcmd) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public ");
		sql.append("   set tutor_rcmd = :tutorRcmd ");
		sql.append(" where lesson_id = :lessonId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("lessonId", lessonId);
		params.put("tutorRcmd", tutorRcmd);

		return baseDao.executeSQL(sql.toString(), params);
	}
}
