/*
 * ActivityServiceImpl.java Created on 2017年9月28日 上午10:54:21
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
package com.wst.act.service;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.wst.act.dao.ActivityDao;
import com.wst.service.act.dto.ActivityDTO;
import com.wst.service.act.service.ActivityService;

/**
 * 活动管理Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class ActivityServiceImpl implements ActivityService {

	@Resource
	private ActivityDao activityDao;

	@Override
	public Page<ActivityDTO> findPaging(Page<ActivityDTO> pageParam, ActivityDTO activityDTO) throws Exception {
		return activityDao.findPaging(pageParam, activityDTO);
	}

	@Override
	public ActivityDTO getActByActCode(String actCode) throws Exception {
		return activityDao.getActByActCode(actCode);
	}

	@Override
	public long saveActivity(ActivityDTO activityDTO) throws Exception {
		return activityDao.saveActivity(activityDTO);
	}

	@Override
	public int updateActivity(ActivityDTO activityDTO) throws Exception {
		return activityDao.updateActivity(activityDTO);
	}

	@Override
	public long getCountByActId(long actId) throws Exception {
		return activityDao.getCountByActId(actId);
	}

	@Override
	public int deleteActivity(long actId) throws Exception {
		return activityDao.deleteActivity(actId);
	}

	@Override
	public ActivityDTO getActById(long actId) throws Exception {
		return activityDao.getActById(actId);
	}

	@Override
	public List<ActivityDTO> findActList() throws Exception {
		return activityDao.findActList();
	}

	@Override
	public int upActStatus(long status, long actId) throws Exception {
		return activityDao.upActStatus(status, actId);
	}

	@Override
	public ActivityDTO getActByActType(long actType) throws Exception {
		return activityDao.getActByActType(actType);
	}

}
