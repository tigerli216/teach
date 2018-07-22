/*
 * CustomClassVideoDao.java Created on 2017年11月25日 下午5:34:20
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

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.wst.entity.lesson.CustomClassVideo;
import com.wst.service.lesson.dto.CustomClassVideoDtlDTO;

/**
 * 课程视频拉取信息DAO
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
@Repository
public class CustomClassVideoDao {

    @Resource
    private BaseDao baseDao;
    
    /**
     * 记录待拉取视频源信息
     * 
     * @param customClassVideo
     * @return
     * @throws Exception
     */
    public long insertCustomClassVideo(CustomClassVideo customClassVideo) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into lesson_custom_class_video (class_id, vid, upload_time, download_num) ");
        sql.append("values (:classId, :vid, :uploadTime, 0)");

        return baseDao.insertInto(sql.toString(), customClassVideo, "video_id");
    }
    
    /**
     * 更新待拉取视频源信息
     * 
     * @param customClassVideo
     * @return
     * @throws Exception
     */
    public int updateCustomClassVideo(CustomClassVideo customClassVideo) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update lesson_custom_class_video ");
        sql.append("   set video_url = :videoUrl, download_time = :downloadTime, ");
        sql.append("       download_num = download_num + 1, download_remark = :downloadRemark ");
        sql.append(" where video_id = :videoId ");

        return baseDao.executeSQL(sql.toString(), customClassVideo);
    }
    
    /**
     * 更新待拉取视频源信息
     * 
     * @param customClassVideo
     * @return
     * @throws Exception
     */
    public int deleteCustomClassVideo(CustomClassVideo customClassVideo) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into lesson_custom_class_video_dtl (video_id, class_id, vid, ");
        sql.append("       upload_time, video_url, download_time, download_num, download_remark) ");
        sql.append("values (:videoId, :classId, :vid, ");
        sql.append("       :uploadTime, :videoUrl, :downloadTime, :downloadNum, :downloadRemark)");
        
        baseDao.insertInto(sql.toString(), customClassVideo, "his_id");
        
        sql.delete(0, sql.length());
        
        sql.append("delete from lesson_custom_class_video where video_id = :videoId ");

        return baseDao.executeSQL(sql.toString(), customClassVideo);
    }
    
    /**
     * 获取待处理数据
     * 
     * @return
     * @throws Exception
     */
    public List<CustomClassVideo> getPendingCustomClassVideo() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select video_id, class_id, vid, upload_time, video_url, ");
        sql.append("       download_time, download_num, download_remark  ");
        sql.append("  from lesson_custom_class_video ");
        sql.append(" where download_num < 5 "); // 最多获取5次
        sql.append("   and upload_time < :uploadTime"); // 获取5分钟之前的
        sql.append(" limit 50 ");

        Map<String, Date> params = new HashMap<String, Date>(1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -5);
        params.put("uploadTime", cal.getTime());
        
        return baseDao.findList(sql.toString(), params, CustomClassVideo.class);
    }
    
    /**
     * 通过课时ID获取视频列表
     * 
     * @param classId
     * @return
     * @throws Exception
     */
    public List<CustomClassVideoDtlDTO> findVideoByClassId(long classId) throws Exception {
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append("select his_id, video_id, class_id, vid, upload_time, ");
    	sql.append("       video_url, download_time, download_num, download_remark ");
    	sql.append("  from lesson_custom_class_video_dtl ");
    	sql.append(" where class_id = :classId ");
    	
    	Map<String, Long> param = new HashMap<String, Long>(1);
    	param.put("classId", classId);
    	
    	return baseDao.findList(sql.toString(), param, CustomClassVideoDtlDTO.class);
    }
}
