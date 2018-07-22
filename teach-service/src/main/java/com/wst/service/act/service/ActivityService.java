/*
 * ActivityService.java Created on 2017年9月28日 上午10:50:59
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
package com.wst.service.act.service;

import java.util.List;

import com.hiwi.common.dao.Page;
import com.wst.service.act.dto.ActivityDTO;

/**
 * 活动管理Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface ActivityService {

	/**
	 * 获取活动信息分页数据
	 * 
	 * @param pageParam
	 * @param activityDTO
	 * @return
	 * @throws Exception
	 */
	public Page<ActivityDTO> findPaging(Page<ActivityDTO> pageParam, ActivityDTO activityDTO) throws Exception;

	/**
	 * 通过活动编码获取活动信息
	 * 
	 * @param actCode
	 * @return
	 * @throws Exception
	 */
	public ActivityDTO getActByActCode(String actCode) throws Exception;

	/**
	 * 新增活动
	 * 
     * @param activityDTO
	 * @return
	 * @throws Exception
	 */
	public long saveActivity(ActivityDTO activityDTO) throws Exception;

	/**
	 * 修改活动
	 * 
     * @param activityDTO
	 * @return
	 * @throws Exception
	 */
	public int updateActivity(ActivityDTO activityDTO) throws Exception;

	/**
	 * 获取状态为有效的活动提取码配置的数量
	 * 
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public long getCountByActId(long actId) throws Exception;

	/**
	 * 根据活动ID删除活动
	 * 
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public int deleteActivity(long actId) throws Exception;

	/**
	 * 通过活动Id获取活动信息
	 * 
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	public ActivityDTO getActById(long actId) throws Exception;
	
	/**
	 * 获取有效的活动集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ActivityDTO> findActList() throws Exception;
	
	/**
	 * 活动开启或关闭
	 * 
	 * @param status
     * @param actId
	 * @return
	 * @throws Exception
	 */
	public int upActStatus(long status, long actId) throws Exception;
	
	/**
	 * 根据活动类型获取活动信息
	 * 
	 * @param actType
	 * @return
	 * @throws Exception
	 */
	public ActivityDTO getActByActType(long actType) throws Exception;
}
