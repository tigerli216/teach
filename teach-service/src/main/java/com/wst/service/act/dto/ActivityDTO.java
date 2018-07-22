/*
 * ActivityDTO.java Created on 2017年9月26日 上午10:40:32
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
package com.wst.service.act.dto;

import com.hiwi.common.util.EnumUtils;
import com.wst.constant.ActivityConstant.ActivityStatusEnmu;
import com.wst.constant.ActivityConstant.ActivityTypeEnmu;
import com.wst.entity.act.Activity;

/**
 * 对应实体表(Activity)
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class ActivityDTO extends Activity {

	private static final long serialVersionUID = 1L;

	/**
	 * 枚举活动类型
	 * 
	 * @return
	 */
	public String getActTypeStr() {
		ActivityTypeEnmu activityTypeEnmu = EnumUtils.getEnum(ActivityTypeEnmu.values(), "type", actType);
		return activityTypeEnmu == null ? "" : activityTypeEnmu.name;
	}

	/**
	 * 枚举状态
	 * 
	 * @return
	 */
	public String getStatusStr() {
		ActivityStatusEnmu activityStatusEnmu = EnumUtils.getEnum(ActivityStatusEnmu.values(), "status", status);
		return activityStatusEnmu == null ? "" : activityStatusEnmu.name;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
