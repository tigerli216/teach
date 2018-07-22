/*
 * CustomClassDao.java Created on 2017年9月29日 上午10:54:10
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.lesson.dto.CustomClassDTO;

/**
 * 定制课课时查询dao
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class CustomClassDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 新增定制课课时信息
	 * 
	 * @param productAttr
	 * @return
	 * @throws Exception
	 */
	public long insertLessonClass(CustomClassDTO customClassDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_custom_class (lesson_id, class_name, class_rcmd, ");
		sql.append("       tutor_id, ready_file, class_summary, plan_duration, real_duration, ");
		sql.append("       date_time, confirm_time, begin_time, end_time, room_id, movie_url, ");
		sql.append("       class_status, plan_time) ");
		sql.append("values (:lessonId, :className, :classRcmd, ");
		sql.append("       :tutorId, :readyFile, :classSummary, :planDuration, :realDuration, ");
		sql.append("       :dateTime, :confirmTime, :beginTime, :endTime, :roomId, :movieUrl, ");
		sql.append("       1, :planTime)");

		return baseDao.insertInto(sql.toString(), customClassDTO, "class_id");
	}

	/**
	 * 删除课时属性
	 * 
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public int deleteLessonClass(long lessonId) throws Exception {
		String sql = "delete from lesson_custom_class where lesson_id = :lessonId";

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.executeSQL(sql, params);
	}

	/**
	 * 根据lessonId和classId删除课时信息
	 * 
	 * @param lessonId
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public int deleteLessonClassOfId(long lessonId, String classIds) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_custom_class_his (class_id, lesson_id, class_name, class_rcmd, ");
		sql.append("       tutor_id, ready_file, class_summary, plan_duration, real_duration, ");
		sql.append("       date_time, confirm_time, begin_time, end_time, room_id, movie_url, ");
		sql.append("       class_status, plan_time) ");
		sql.append("select class_id, lesson_id, class_name, class_rcmd, ");
		sql.append("       tutor_id, ready_file, class_summary, plan_duration, real_duration, ");
		sql.append("       date_time, confirm_time, begin_time, end_time, room_id, movie_url, ");
		sql.append("       class_status, plan_time ");
		sql.append("  from lesson_custom_class ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and class_id not in (" + classIds + ") ");

		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("lessonId", lessonId);

		baseDao.executeSQL(sql.toString(), params);

		sql.delete(0, sql.length());

		sql.append("delete from lesson_custom_class ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and class_id not in (" + classIds + ") ");

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据定制课课程id获取定制课课时信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findLessonClassList(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lc.class_id, lc.lesson_id, lc.class_name, lc.class_rcmd, ");
		sql.append("       lc.tutor_id, lc.ready_file, lc.class_summary, lc.plan_duration, ");
		sql.append("       lc.real_duration, lc.date_time, lc.begin_time, lc.end_time, ");
		sql.append("       lc.confirm_time, lc.room_id, lc.movie_url, lc.class_status, ");
		sql.append("       lc.plan_time, mt.real_name as tutorName, mt.login_account ");
		sql.append("  from lesson_custom_class lc ");
		sql.append("  left join mem_tutor mt on lc.tutor_id = mt.tutor_id");
		sql.append(" where lesson_id = :lessonId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, CustomClassDTO.class);
	}

	/**
	 * 根据课程ID和导师ID查询大于课时ID未结束的课时
	 * 
	 * @param classId
	 *            课时ID
	 * @param lessonId
	 *            课程ID
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findNotFinishCustomClass(long classId, long lessonId, long tutorId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lc.class_id, lc.lesson_id, lc.class_name, lc.class_rcmd, lc.tutor_id, ");
		sql.append("       lc.ready_file, lc.class_summary, lc.plan_duration, lc.real_duration, ");
		sql.append("       lc.date_time, lc.begin_time, lc.end_time, lc.confirm_time, lc.room_id, ");
		sql.append("       lc.movie_url, lc.class_status, lc.plan_time ");
		sql.append("  from lesson_custom_class lc ");
		sql.append(" where lc.class_status in (1, 2)");
		sql.append("   and lc.lesson_id = :lessonId ");
		if (tutorId > 0) {
			sql.append(" and lc.tutor_id = :tutorId ");
		}

		Map<String, Long> params = new HashMap<String, Long>(3);
		params.put("classId", classId);
		params.put("lessonId", lessonId);
		params.put("tutorId", tutorId);

		return baseDao.findList(sql.toString(), params, CustomClassDTO.class);
	}

	/**
	 * 获取导师ID课时列表（课程ID相同的合并）
	 * 
	 * @param tutorId
	 * @return
	 * @throws Exception
	 */
	public Page<CustomClassDTO> findLessons(Page<CustomClassDTO> param, CustomClassDTO customClassDTO)
			throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lesson_id ");
		sql.append("  from lesson_custom_class ");
		sql.append(" where tutor_id = :tutorId ");
		sql.append(" group by lesson_id ");

		return baseDao.findPaging(sql.toString(), param, customClassDTO, CustomClassDTO.class);
	}

	/**
	 * 根据课时ID查询课时信息
	 * 
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	public CustomClassDTO getCustomClass(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, ");
		sql.append("       tutor_id, ready_file, class_summary, plan_duration, ");
		sql.append("       real_duration, date_time, begin_time, end_time, ");
		sql.append("       confirm_time, room_id, movie_url, class_status, plan_time ");
		sql.append("  from lesson_custom_class ");
		sql.append(" where class_id = :classId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.get(sql.toString(), params, CustomClassDTO.class);
	}

	/**
	 * 修改约课时间
	 * 
	 * @param classId
	 *            课时ID
	 * @param dateTime
	 *            约课时间
	 * @return
	 * @throws Exception
	 */
	public int updateDateTime(long classId, Date dateTime) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set date_time = :dateTime ");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("classId", classId);
		params.put("dateTime", dateTime);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 确认上课
	 * 
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	public int confirmAttendClass(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set confirm_time = now(), class_status = 4 ");
		sql.append(" where class_id = :classId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 取消约课
	 * 
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	public int cancelClass(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set date_time = null, begin_time = null, class_status = 1 ");
		sql.append(" where class_id = :classId ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 修改课时信息
	 * 
	 * @param classId
	 *            课时ID
	 * @param classRcmd
	 *            课时介绍
	 * @param planDuration
	 *            计划时长
	 * @param beginTime
	 *            开始时间
	 * @return
	 * @throws Exception
	 */
	public int updateClass(long classId, String className, Date beginTime) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set class_name = :className, plan_time = :beginTime ");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("classId", classId);
		params.put("className", className);
		params.put("beginTime", beginTime);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据课时ID修改课时信息
	 * 
	 * @param classId
	 *            课时ID
	 * @param customClassDTO
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public int updateLessonClassById(CustomClassDTO customClassDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set class_name = :className, class_rcmd = :classRcmd, ");
		sql.append("       tutor_id = :tutorId, ready_file = :readyFile, ");
		sql.append("       plan_duration = :planDuration, movie_url = :movieUrl, ");
		sql.append("	   plan_time = :planTime ");
		sql.append(" where class_id = :classId ");

		return baseDao.executeSQL(sql.toString(), customClassDTO);
	}

	/**
	 * 课时确认结束
	 * 
	 * @param classId
	 *            课时ID
	 * @param realDuration
	 *            实际时长
	 * @return
	 * @throws Exception
	 */
	public int classConfirmFinish(long classId, long realDuration) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set real_duration = :realDuration, end_time = now(), class_status = 3 ");
		sql.append(" where class_id = :classId ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("classId", classId);
		params.put("realDuration", realDuration);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 课时小结
	 * 
	 * @param classId
	 *            课时ID
	 * @param classSummary
	 *            课时小结
	 * @return
	 * @throws Exception
	 */
	public int classBriefSummary(long classId, String classSummary) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set class_summary = :classSummary ");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("classId", classId);
		params.put("classSummary", classSummary);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 上传文件
	 * 
	 * @param classId
	 *            课时ID
	 * @param readyFile
	 *            上传文件
	 * @return
	 * @throws Exception
	 */
	public int uploadReadyFile(long classId, String readyFile) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set ready_file = :readyFile ");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("classId", classId);
		params.put("readyFile", readyFile);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 开始上课
	 * 
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	public int attendClass(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set begin_time = now(), class_status = 2 ");
		sql.append(" where class_id = :classId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 分页查询授课记录
	 * 
	 * @param page
	 *            分页信息
	 * @param lessonId
	 *            课程ID
	 * @param userId
	 *            用户ID
	 * @param tutorId
	 *            导师ID
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public Page<CustomClassDTO> findGiveLessonsPaging(Page<CustomClassDTO> page, String lessonName, String userLoginAccount,
			String tutorLoginAccount, Date beginTime, Date endTime) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.class_name, lcc.ready_file, lcc.class_summary, ");
		sql.append("       lcc.plan_duration, lcc.real_duration, lcc.date_time, lcc.begin_time, ");
		sql.append("       lcc.end_time, lcc.confirm_time, lcc.room_id, lcc.movie_url, ");
		sql.append("       lc.lesson_id, lc.lesson_type, lc.lesson_name, lc.total_class, ");
		sql.append("       lc.finish_class, lc.end_time, mu.real_name userName, mu.email userEmail, ");
		sql.append("       mt.real_name tutorName, mt.email tutorEmail, lcc.plan_time ");
		sql.append("  from lesson_custom_class lcc ");
		sql.append(" inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append(" inner join mem_user mu on mu.user_id = lc.user_id ");
		sql.append(" inner join mem_tutor mt on mt.tutor_id = lcc.tutor_id ");
		sql.append(" where class_status in (3, 4) ");

		if (StringUtils.isNotEmpty(lessonName)) {
			sql.append(" and lc.lesson_name like :lessonName ");
		}
		if (StringUtils.isNotEmpty(userLoginAccount)) {
			sql.append(" and mu.email like :userEmail ");
		}
		if (StringUtils.isNotEmpty(tutorLoginAccount)) {
			sql.append(" and mt.email like :tutorEmail ");
		}
		if (beginTime != null) {
			sql.append(" and lcc.end_time >= :beginTime");
		}
		if (endTime != null) {
			sql.append(" and lcc.end_time <= :endTime");
		}
		sql.append(" order by lc.lesson_id desc, lcc.class_id desc");

		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("lessonName", "%" + lessonName + "%");
		params.put("userEmail", "%" + userLoginAccount + "%");
		params.put("tutorEmail", "%" + tutorLoginAccount + "%");
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);

		return baseDao.findPaging(sql.toString(), page, params, CustomClassDTO.class);
	}

	/**
	 * 视频地址上传
	 * 
	 * @param classId
	 * @param movieUrl
	 * @return
	 * @throws Exception
	 */
	public int upMovieUrl(long classId, String movieUrl) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set movie_url = :movieUrl");
		sql.append(" where class_id = :classId");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("classId", classId);
		params.put("movieUrl", movieUrl);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 生成房间号
	 * 
	 * @param classId
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public long addMovieId(long classId, long roomId) throws Exception {
		String sql = "update lesson_custom_class set room_id = :roomId where class_id = :classId";

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("classId", classId);
		params.put("roomId", roomId);

		return baseDao.executeSQL(sql.toString(), params);
	}
	
	/**
	 * 根据房间ID获取课时信息
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public CustomClassDTO getByRoomId(long roomId) throws Exception {
	    StringBuilder sql = new StringBuilder();

        sql.append("select class_id, lesson_id, class_name, class_rcmd, ");
        sql.append("       tutor_id, ready_file, class_summary, plan_duration, ");
        sql.append("       real_duration, date_time, begin_time, end_time, ");
        sql.append("       confirm_time, room_id, movie_url, class_status, plan_time ");
        sql.append("  from lesson_custom_class ");
        sql.append("  where room_id = :roomId");
        sql.append("  order by class_id desc");
        sql.append("  limit 1");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("roomId", roomId);
        
        return baseDao.get(sql.toString(), params, CustomClassDTO.class);
	}

	/**
	 * 放入开始上课
	 * 
	 * @param classId
	 *            课时ID
	 * @return
	 * @throws Exception
	 */
	public int upBeginTime(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_custom_class ");
		sql.append("   set begin_time = now() ");
		sql.append(" where class_id = :classId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据定制课课程id获取导师集合
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<Long> findTutorIdList(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select distinct tutor_id ");
		sql.append("  from lesson_custom_class ");
		sql.append(" where lesson_id = :lessonId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, Long.class);
	}

	/**
	 * 查询课时未确认且超过N小时的课时
	 * 
	 * @param hour
	 *            小时
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassByNHourNotConfirm(long hour) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.lesson_id, lcc.tutor_id, lc.user_id ");
		sql.append("  from lesson_custom_class lcc ");
		sql.append(" inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append(" where lcc.class_status = 3 ");
		sql.append("   and lcc.end_time <= subdate(now(), interval " + hour + " hour) ");

		return baseDao.findList(sql.toString(), null, CustomClassDTO.class);
	}

	/**
	 * 查询离计划上课时长还有N天的课时
	 * 
	 * @param day
	 *            天
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByPlanTimeNDay(long day) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.lesson_id, lcc.tutor_id, lc.user_id ");
		sql.append("  from lesson_custom_class lcc ");
		sql.append(" inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append(" where lcc.date_time is null ");
		sql.append("   and lcc.plan_time = :planTime ");

		// 获取当前日期
        Calendar planTime = Calendar.getInstance();
        planTime.set(Calendar.HOUR_OF_DAY, 0); // 小时
        planTime.set(Calendar.MINUTE, 0); // 分钟
        planTime.set(Calendar.SECOND, 0); // 秒
        planTime.set(Calendar.MILLISECOND, 0); // 毫秒
		
        planTime.add(Calendar.DAY_OF_MONTH, (int) day);
        
	    Map<String, Date> params = new HashMap<String, Date>(1);
	    params.put("planTime", planTime.getTime());
		
		return baseDao.findList(sql.toString(), params, CustomClassDTO.class);
	}

	/**
	 * 查询上节课结束后，N天未再次上课
	 * 
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByNDayNotClass(long day) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.lesson_id, lcc.tutor_id, lc.user_id ");
		sql.append("  from lesson_custom_class lcc ");
		sql.append("  inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append("  where ((lc.current_class != lcc.class_id and lc.total_class != lc.finish_class) ");
		sql.append("    or (lc.current_class = lcc.class_id and lcc.date_time is not null and lcc.date_time <> '' ))");
		sql.append("    and lcc.class_id in ( select max(class_id) from lesson_custom_class");
		sql.append("                where end_time is not null and end_time <> '' group by lesson_id) ");
		sql.append("    and date_format(lcc.end_time,'%Y-%m-%d') = subdate(date_format(now(),'%Y-%m-%d'),interval "
				+ day + " day)");

		return baseDao.findList(sql.toString(), null, CustomClassDTO.class);
	}

	/**
	 * 课程购买后，开始时间已过N天未上课
	 * 
	 * @param day
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByPayNDayNotClass(long day) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.lesson_id, lcc.tutor_id, lc.user_id");
		sql.append("  from lesson_custom_class lcc ");
		sql.append("  inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append("  where lc.current_class = lcc.class_id and lc.finish_class = 0 ");
		sql.append("    and lc.buy_time is not null and lc.buy_time <> '' ");
		sql.append("    and lcc.date_time is not null and lcc.date_time <> ''");
		sql.append("    and date_format(lc.begin_time,'%Y-%m-%d') = subdate(date_format(now(),'%Y-%m-%d'),interval "
				+ day + "  day) ");

		return baseDao.findList(sql.toString(), null, CustomClassDTO.class);
	}

	/**
	 * 已约课节，离上课还有N小时
	 * 
	 * @param hour
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findCustomClassListByDataTimeNHour(long hour) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lcc.class_id, lcc.lesson_id, lcc.tutor_id, lc.user_id ");
		sql.append("  from lesson_custom_class lcc ");
		sql.append(" inner join lesson_custom lc on lc.lesson_id = lcc.lesson_id ");
		sql.append(" inner join lesson_custom_class_date ccd on ccd.class_id = lcc.class_id ");
		sql.append(" where lcc.class_status < 3 "); // 未结束
		sql.append("   and lcc.date_time between now() and adddate(now(), interval " + hour + " hour) ");
		sql.append("   and ccd.confirm_status = 2 "); // 双方已确认约课
		sql.append("   and ccd.cancel_time is null "); // 约课未取消

		return baseDao.findList(sql.toString(), null, CustomClassDTO.class);
	}

	/**
	 * 根据课程ID和导师ID查询大于课时ID未结束的课时
	 * 
	 * @param classId
	 *            课时ID
	 * @param lessonId
	 *            课程ID
	 * @param tutorId
	 *            导师ID
	 * @return
	 * @throws Exception
	 */
	public List<CustomClassDTO> findNotFinishCustom(long lessonId, long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lc.class_id, lc.lesson_id, lc.class_name, lc.class_rcmd, lc.tutor_id, ");
		sql.append("       lc.ready_file, lc.class_summary, lc.plan_duration, lc.real_duration, ");
		sql.append("       lc.date_time, lc.begin_time, lc.end_time, lc.confirm_time, lc.room_id, ");
		sql.append("       lc.movie_url, lc.class_status, lc.plan_time ");
		sql.append("  from lesson_custom_class lc ");
		sql.append(" where lc.class_status < 4");
		sql.append("   and lc.lesson_id = :lessonId ");
		sql.append("   and lc.class_id != :classId ");
		
		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("lessonId", lessonId);
		params.put("classId", classId);

		return baseDao.findList(sql.toString(), params, CustomClassDTO.class);
	}
}
