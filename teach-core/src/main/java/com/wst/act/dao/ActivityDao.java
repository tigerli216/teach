/*
 * ActivityDao.java Created on 2017年9月28日 上午11:01:52
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
import com.wst.service.act.dto.ActivityDTO;

/**
 * 活动Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class ActivityDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取活动信息分页数据
     * 
     * @param pageParam
     * @param activityDTO
     * @return
     * @throws Exception
     */
    public Page<ActivityDTO> findPaging(Page<ActivityDTO> pageParam, ActivityDTO activityDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select act_id, act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo, status ");
        sql.append("  from operate_activity ");
        sql.append(" where 1 = 1 ");

        if (activityDTO != null) {
            if (activityDTO.getActId() > 0) {
                sql.append(" and act_id = :actId ");
            }
            if (StringUtils.isNotEmpty(activityDTO.getActName())) {
                sql.append(" and act_name like concat('%', :actName, '%') ");
            }
            if (StringUtils.isNotEmpty(activityDTO.getActCode())) {
                sql.append(" and act_code like concat('%', :actCode, '%') ");
            }
            if (activityDTO.getActType() > 0) {
                sql.append(" and act_type = :actType ");
            }
            if (activityDTO.getStatus() > 0) {
                sql.append(" and status = :status ");
            }
        }

        sql.append(" order by status asc, act_id desc ");

        return baseDao.findPaging(sql.toString(), pageParam, activityDTO, ActivityDTO.class);
    }

    /**
     * 通过活动编码获取活动信息
     * 
     * @param actCode
     * @return
     * @throws Exception
     */
    public ActivityDTO getActByActCode(String actCode) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select act_id, act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo ");
        sql.append("  from operate_activity ");
        sql.append(" where act_code = :actCode ");

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("actCode", actCode);

        return baseDao.get(sql.toString(), params, ActivityDTO.class);
    }

    /**
     * 新增活动
     * 
     * @return
     * @throws Exception
     */
    public long saveActivity(ActivityDTO activityDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into operate_activity ( ");
        sql.append("	   act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo, status) ");
        sql.append("values ( ");
        sql.append("       :actName, :actCode, :actExplain, :actType, ");
        sql.append("       :expandConfig, :beginTime, :endTime, :memo, 1) ");

        return baseDao.insertInto(sql.toString(), activityDTO, "act_id");
    }

    /**
     * 修改活动
     * 
     * @return
     * @throws Exception
     */
    public int updateActivity(ActivityDTO activityDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update operate_activity ");
        sql.append("   set act_name = :actName, act_code = :actCode,  ");
        sql.append("	   act_explain = :actExplain, act_type = :actType,  ");
        sql.append("	   expand_config = :expandConfig, begin_time = :beginTime, ");
        sql.append("	   end_time = :endTime, memo = :memo ");
        sql.append(" where act_id = :actId ");

        return baseDao.executeSQL(sql.toString(), activityDTO);
    }

    /**
     * 获取状态为有效的活动配置的数量
     * 
     * @param actId
     * @return
     * @throws Exception
     */
    public long getCountByActId(long actId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select count(1) ");
        sql.append("  from operate_activity_extract oae, operate_activity oa ");
        sql.append(" where oae.act_id = oa.act_id ");
        sql.append("   and oae.valid_time > now() ");
        sql.append("   and oa.act_id = :actId ");

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("actId", actId);

        return baseDao.get(sql.toString(), params, Long.class);
    }

    /**
     * 根据活动ID删除活动
     * 
     * @param actId
     * @return
     * @throws Exception
     */
    public int deleteActivity(long actId) throws Exception {
        String sql = "delete from operate_activity where act_id = :actId";

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("actId", actId);

        return baseDao.executeSQL(sql, params);
    }

    /**
     * 根据活动ID获取活动信息
     * 
     * @param actId
     * @return
     * @throws Exception
     */
    public ActivityDTO getActById(long actId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select act_id, act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo ");
        sql.append("  from operate_activity ");
        sql.append(" where act_id = :actId ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("actId", actId);

        return baseDao.get(sql.toString(), params, ActivityDTO.class);
    }

    /**
     * 获取有效的活动集合
     * 
     * @return
     * @throws Exception
     */
    public List<ActivityDTO> findActList() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select act_id, act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo ");
        sql.append("  from operate_activity ");
        sql.append(" where end_time > now() ");

        return baseDao.findList(sql.toString(), null, ActivityDTO.class);
    }

    /**
     * 活动开启或关闭
     * 
     * @param status
     * @return
     * @throws Exception
     */
    public int upActStatus(long status, long actId) throws Exception {
        String sql = "update operate_activity set status = :status where act_id = :actId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("status", status);
        params.put("actId", actId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 根据活动类型查询有效活动
     * 
     * @param actType
     * @return
     * @throws Exception
     */
    public ActivityDTO getActByActType(long actType) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select act_id, act_name, act_code, act_explain, act_type, ");
        sql.append("	   expand_config, begin_time, end_time, memo ");
        sql.append("  from operate_activity ");
        sql.append(" where end_time > now() ");
        sql.append("   and act_type = :actType ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("actType", actType);

        return baseDao.get(sql.toString(), params, ActivityDTO.class);
    }
}
