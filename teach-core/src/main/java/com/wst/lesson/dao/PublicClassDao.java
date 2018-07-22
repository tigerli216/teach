/*
 * PublicClassDao.java Created on 2017年9月27日 下午4:49:48
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
import com.wst.service.lesson.dto.PublicClassDTO;

/**
 * 网课课时查询dao
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class PublicClassDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 新增网课课时信息
	 * 
	 * @param productAttr
	 * @return
	 * @throws Exception
	 */
	public long insertLessonClass(PublicClassDTO publicClassDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into lesson_public_class (lesson_id, class_name, class_rcmd, ");
		sql.append("       duration, is_free, begin_time, end_time, room_id, movie_url, ");
		sql.append("       class_status, movie_cover, courseware_pic) ");
		sql.append("values (:lessonId, :className, :classRcmd, ");
		sql.append("       :duration, :isFree, :beginTime, :endTime, :roomId, :movieUrl, ");
		sql.append("       :classStatus, :movieCover, :coursewarePic)");

		return baseDao.insertInto(sql.toString(), publicClassDTO, "class_id");
	}

	/**
	 * 删除课时属性
	 * 
	 * @param prodId
	 * @return
	 * @throws Exception
	 */
	public int deleteLessonClass(long lessonId) throws Exception {
		String sql = "delete from lesson_public_class where lesson_id = :lessonId";

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.executeSQL(sql.toString(), params);
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

		sql.append("insert into lesson_public_class_his (class_id, lesson_id, room_id, ");
		sql.append("       class_name, class_rcmd, duration, is_free, begin_time, end_time, ");
		sql.append("       group_id, movie_url, movie_cover, courseware_pic, class_status) ");
		sql.append("select class_id, lesson_id, room_id, ");
		sql.append("       class_name, class_rcmd, duration, is_free, begin_time, end_time, ");
		sql.append("       group_id, movie_url, movie_cover, courseware_pic, class_status ");
		sql.append("  from lesson_public_class ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and class_id not in (" + classIds + ") ");

		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("lessonId", lessonId);

		baseDao.executeSQL(sql.toString(), params);

		sql.delete(0, sql.length());

		sql.append("delete from lesson_public_class ");
		sql.append(" where lesson_id = :lessonId ");
		sql.append("   and class_id not in (" + classIds + ") ");

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据网课课程id获取网课课时信息
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public List<PublicClassDTO> findLessonClassList(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, duration, ");
		sql.append("       is_free, begin_time, end_time, group_id, movie_url, ");
		sql.append("	   class_status, movie_cover, courseware_pic, room_id ");
		sql.append("  from lesson_public_class ");
		sql.append(" where lesson_id = :lessonId");
		sql.append(" order by lesson_id desc ");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.findList(sql.toString(), params, PublicClassDTO.class);
	}

	/**
	 * 根据网课课程id获取网课课时信息,过滤掉上过的
	 * 
	 * @param lessonId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO getLiveClassByLessonId(long lessonId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, duration, ");
		sql.append("       is_free, begin_time, end_time, group_id, movie_url, ");
		sql.append("	   class_status, movie_cover, courseware_pic, room_id ");
		sql.append("  from lesson_public_class ");
		sql.append(" where lesson_id = :lessonId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("lessonId", lessonId);

		return baseDao.get(sql.toString(), params, PublicClassDTO.class);
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
	public int updateLessonClassById(PublicClassDTO publicClassDTO) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class ");
		sql.append("   set class_name = :className, class_rcmd = :classRcmd, ");
		sql.append("       duration = :duration, is_free = :isFree, ");
		sql.append("       movie_url = :movieUrl, begin_time = :beginTime, ");
		sql.append("       end_time = :endTime ");
		sql.append(" where class_id = :classId ");

		return baseDao.executeSQL(sql.toString(), publicClassDTO);
	}

	/**
	 * 根据id获取课时
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO findByClassId(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, duration, ");
		sql.append("       is_free, begin_time, end_time, group_id, movie_url, ");
		sql.append("       class_status, movie_cover, courseware_pic, room_id  ");
		sql.append("  from lesson_public_class ");
		sql.append(" where class_id = :classId");

		Map<String, Long> params = new HashMap<String, Long>(1);
		params.put("classId", classId);

		return baseDao.get(sql.toString(), params, PublicClassDTO.class);
	}

	/**
	 * 修改视频地址和封面地址
	 * 
	 * @param moveUrl
	 *            视频地址
	 * @param coverUrl
	 *            封面地址
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public long upMoveUrlById(String moveUrl, String coverUrl, long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class");
		sql.append("   set movie_url = :moveUrl,");
		sql.append("       movie_cover = :coverUrl");
		sql.append(" where class_id = :classId");

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("moveUrl", moveUrl);
		params.put("coverUrl", coverUrl);
		params.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 查询离上课还有N小时的直播课
	 * 
	 * @param hour
	 * @return
	 * @throws Exception
	 */
	public List<PublicClassDTO> findPublicClassListByNHourClass(long hour) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select lp.lesson_id, lp.tutor_id, lpc.class_id, bo.user_id ");
		sql.append("  from lesson_public_class lpc ");
		sql.append(" inner join lesson_public lp on lp.lesson_id = lpc.lesson_id ");
		sql.append(" inner join bm_order bo on bo.busi_id = lp.lesson_id ");
		sql.append(" where bo.order_type = 1 ");
		sql.append("   and bo.pay_status = 2 ");
		sql.append("   and bo.order_status = 1 ");
		sql.append("   and lp.lesson_type = 2 ");
		sql.append("   and lpc.class_status = 1 ");
		sql.append("   and lpc.begin_time <= adddate(now(), interval " + hour + " hour)");

		Map<String, Long> params = new HashMap<String, Long>(1);

		return baseDao.findList(sql.toString(), params, PublicClassDTO.class);
	}

	/**
	 * 通过课时ID获取课时信息
	 * 
	 * @param classId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO getById(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, duration, ");
		sql.append("       is_free, begin_time, end_time, group_id, movie_url, ");
		sql.append("       class_status, movie_cover, courseware_pic, room_id, group_id ");
		sql.append("  from lesson_public_class ");
		sql.append(" where class_id = :classId");

		Map<String, Long> param = new HashMap<String, Long>(1);
		param.put("classId", classId);

		return baseDao.get(sql.toString(), param, PublicClassDTO.class);
	}

	/**
	 * 保存房间ID
	 * 
	 * @param classId
	 * @param GroupId
	 * @return
	 * @throws Exception
	 */
	public int addGroupId(long classId, String groupId, String roomId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class ");
		sql.append("   set group_id = :groupId, room_id = :roomId");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("classId", classId);
		params.put("groupId", groupId);
		params.put("roomId", roomId);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 根据房间ID获取课时信息
	 * 
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public PublicClassDTO getByRoomId(long roomId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select class_id, lesson_id, class_name, class_rcmd, duration, ");
		sql.append("       is_free, begin_time, end_time, group_id, movie_url, ");
		sql.append("       class_status, movie_cover, courseware_pic, room_id, group_id ");
		sql.append("  from lesson_public_class ");
		sql.append("  where room_id = :roomId");
		sql.append("  order by class_id desc");
		sql.append("  limit 1");

		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("roomId", roomId);

		return baseDao.get(sql.toString(), params, PublicClassDTO.class);
	}

	/**
	 * 备课文件上传
	 * 
	 * @param classId
	 * @param readyFile
	 * @return
	 * @throws Exception
	 */
	public int readyFileUpload(long classId, String readyFile) throws Exception {
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
	 * 直播课课时结束
	 * 
	 * @return
	 * @throws Exception
	 */
	public int classFinish(long classId) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class ");
		sql.append("   set class_status = 3, end_time = now() ");
		sql.append("  where class_id = :classId ");

		Map<String, Long> param = new HashMap<String, Long>(1);
		param.put("classId", classId);

		return baseDao.executeSQL(sql.toString(), param);
	}

	/**
	 * 获取课时集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PublicClassDTO> LiveClassList() throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select pc.class_id, pc.lesson_id, pc.class_name, pc.class_rcmd, pc.duration, ");
		sql.append("       pc.is_free, pc.begin_time, pc.end_time, pc.group_id, pc.movie_url, ");
		sql.append("       pc.class_status, pc.movie_cover, pc.courseware_pic, pc.room_id, pc.group_id ");
		sql.append("  from lesson_public_class pc ");
		sql.append("  join lesson_public p ");
		sql.append("    on pc.lesson_id = p.lesson_id ");
		sql.append(" where p.lesson_type = 2 ");

		return baseDao.findList(sql.toString(), null, PublicClassDTO.class);
	}

	/**
	 * 修改课时状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public int updateClassStatus(long classId, long classStatus) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class ");
		sql.append("   set class_status = :classStatus ");
		sql.append(" where class_id = :classId ");

		Map<String, Long> params = new HashMap<String, Long>(2);
		params.put("classId", classId);
		params.put("classStatus", classStatus);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 更新直播课课件图片
	 * 
	 * @param classId
	 * @param coursewarePic
	 * @return
	 * @throws Exception
	 */
	public int updateCoursewarePic(long classId, String coursewarePic) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update lesson_public_class ");
		sql.append("   set courseware_pic = :coursewarePic ");
		sql.append(" where class_id = :classId ");

		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("classId", classId);
		params.put("coursewarePic", coursewarePic);

		return baseDao.executeSQL(sql.toString(), params);
	}
}
