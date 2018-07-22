/*
 * ActivityControllrt.java Created on 2017年9月28日 上午10:33:19
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
package com.wst.act;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.StringUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.service.act.dto.ActivityDTO;
import com.wst.service.act.service.ActivityService;

/**
 * 活动管理控制器
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/act")
public class ActivityController {

	@Reference(version = "0.0.1")
	private ActivityService activityService;

	/**
	 * 运营活动管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public String index(ModelMap modelMap) throws Exception {
		return "act/activity";
	}
	/**
	 * 获取活动信息分页数据
	 * 
	 * @param pageParam
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<ActivityDTO> findPaging(BootStrapTable<ActivityDTO> pageParam, ActivityDTO activityDTO)
			throws Exception {
		return activityService.findPaging(pageParam, activityDTO);
	}

	/**
	 * 新增或修改活动
	 * 
	 * @param activityDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("saveOrUpdateAct")
	public ResultDTO<String> saveOrUpdateAct(ActivityDTO activityDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();

		if (StringUtils.isEmpty(activityDTO.getActName())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "活动名称不能为空");
		}

		if (activityDTO.getBeginTime() == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "活动开始时间不能为空");
		}

		if (activityDTO.getEndTime() == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "活动结束时间不能为空");
		}

		if (activityDTO.getEndTime().getTime() < activityDTO.getBeginTime().getTime()) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "结束时间不能小于开始时间");
		}

		if (StringUtils.isEmpty(activityDTO.getActCode())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "订单编码不能为空");
		}

		ActivityDTO actDTO = activityService.getActByActCode(activityDTO.getActCode());
		if (actDTO != null && actDTO.getActId() != activityDTO.getActId()) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "活动编码已存在,请重新输入!");
		}

		activityDTO.setEndTime(DateUtils.addEndTime(activityDTO.getEndTime()));

		// 修改
		if (activityDTO.getActId() > 0) {
			activityService.updateActivity(activityDTO);
			return result.set(ResultCodeEnum.SUCCESS, "修改活动成功");
		} else { // 新增
			activityService.saveActivity(activityDTO);
			return result.set(ResultCodeEnum.SUCCESS, "新增活动成功");
		}
	}

	/**
	 * 根据活动ID删除活动
	 * 
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "deleteActivity")
	public ResultDTO<String> deleteActivity(long actId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		// 根据actId获取状态为有效的活动提取码配置数量
		long activityCount = activityService.getCountByActId(actId);
		// 如果数量大于0,不能删除
		if (activityCount > 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "该活动关联的活动提取码配置记录状态为有效,不能被删除");
		} else {
			activityService.deleteActivity(actId);
			return result.set(ResultCodeEnum.SUCCESS, "", "删除成功");
		}
	}

	/**
	 * 通过活动Id获取活动信息
	 * 
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getAct")
	public ActivityDTO getAct(long actId) throws Exception {
		return activityService.getActById(actId);
	}

	/**
	 * 活动开启或关闭
	 * 
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "actStartOrClose")
	public ResultDTO<String> actStartOrClose(long status, long actId) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		if (status == 0 || actId == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "缺少必要参数");
		}
		activityService.upActStatus(status, actId);
		return result.set(ResultCodeEnum.SUCCESS, "调整成功");
	}
}
