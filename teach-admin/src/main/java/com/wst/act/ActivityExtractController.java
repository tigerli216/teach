/*
 * ActivityExtractController.java Created on 2017年9月28日 下午3:10:22
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

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.wst.base.helper.ExportHelper;
import com.wst.constant.LessonPublicConstant.ShelfStatusEnum;
import com.wst.service.act.dto.ActivityDTO;
import com.wst.service.act.dto.ActivityExtractDTO;
import com.wst.service.act.service.ActivityExtractService;
import com.wst.service.act.service.ActivityService;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.LessonPublicService;

/**
 * 活动提取码管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/actExtract")
public class ActivityExtractController {
	/** 提取码数量最大值 **/
	private long numMax = 500;

	@Reference(version = "0.0.1")
	private ActivityExtractService activityExtractService;

	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;

	@Reference(version = "0.0.1")
	private ActivityService activityService;

	/**
	 * 运营活动提取码管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "index")
	public String index(ModelMap modelMap) throws Exception {
		List<ActivityDTO> actList = activityService.findActList();
		PublicDTO publicDTO = new PublicDTO();
		publicDTO.setShelfStatus(ShelfStatusEnum.UP.status);
		publicDTO.setHomePage(true);
		List<PublicDTO> publicList = lessonPublicService.findPaging(null, publicDTO).getList();
		modelMap.put("actList", actList);
		modelMap.put("publicList", publicList);
		return "act/activityExtract";
	}

	/**
	 * 活动提取码信息分页数据
	 * 
	 * @param pageParam
	 * @param tutorDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<ActivityExtractDTO> findPaging(BootStrapTable<ActivityExtractDTO> pageParam,
			ActivityExtractDTO activityExtractDTO) throws Exception {
		return activityExtractService.findPaging(pageParam, activityExtractDTO);
	}

	/**
	 * 生成活动提取码
	 * 
	 * @param activityExtractDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "createExtract")
	public ResultDTO<String> createActivityExtract(ActivityExtractDTO activityExtractDTO) throws Exception {
		ResultDTO<String> result = new ResultDTO<>();
		// 参数校验
		if (activityExtractDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "请求参数不能为空");
		}

		// 有效期校验
		if (activityExtractDTO.getValidTime() == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "有效期不能为空");
		}
		Date now = new Date();
		if (activityExtractDTO.getValidTime().getTime() < now.getTime()) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "有效期不能小于现在");
		}

		// 检验活动ID是否合法
		if (activityExtractDTO.getActId() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "活动ID不能为空");
		}

		ActivityDTO activityDTO = activityService.getActById(activityExtractDTO.getActId());
		if (activityDTO == null) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "活动ID不合法");
		}

		// 校验课程ID是否合法
		if (activityExtractDTO.getLessonId() == 0) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课程ID不能为空");
		}

		PublicDTO publicDTO = lessonPublicService.getLessonById(activityExtractDTO.getLessonId());
		if (publicDTO == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "课程ID不合法");
		}

		// 检验活动提取码是否合法
		if (activityExtractDTO.getActivityExtractNum() <= 0 || activityExtractDTO.getActivityExtractNum() > numMax) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "活动提取码数量为正整数且不大于500");
		}

		// 校验课时IDS是否合法
		if (StringUtils.isEmpty(activityExtractDTO.getClassIds())) {
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "课时IDS不能为空");
		}

		String classIds = activityExtractDTO.getClassIds();
		classIds = classIds.replace(",", "|");
		activityExtractDTO.setClassIds(classIds);
		result = activityExtractService.createActivityExtract(activityExtractDTO);
		return result;
	}

	/**
	 * 提取码信息导出
	 * 
	 * @param pageParam
	 * @param oilRgeOrderDTO
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping("exportExtractCode")
	public void exportExtractCode(ActivityExtractDTO activityExtractDTO, HttpServletResponse response)
			throws Exception {
		if (activityExtractDTO != null) {
			if (StringUtils.isNotEmpty(activityExtractDTO.getActName())) {
				activityExtractDTO.setActName(activityExtractDTO.getActName().trim());
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getLessonName())) {
				activityExtractDTO.setLessonName(activityExtractDTO.getLessonName().trim());
			}
			if (StringUtils.isNotEmpty(activityExtractDTO.getLoginAccount())) {
				activityExtractDTO.setLoginAccount(activityExtractDTO.getLoginAccount().trim());
			}
		}

		// 导出表头
		String tblHeader = "";
		String[] tblContent = null;

		tblHeader = "配置ID,活动名称,课程名称,课时IDS,提取码,学生账号,用户IP地址,创建时间,使用时间,有效期";
		tblContent = new String[] { "configId", "actName", "lessonName", "classIds", "extractCode", "loginAccount",
				"userIp", "createTimeStr", "useTimeStr", "validTimeStr" };

		// 设置导出文件名
		String fileName = "提取码信息-" + DateUtils.convertDateToString(new Date(), DateUtils.YYYY_MM_DD) + ".csv";

		ExportHelper.writerCsvFileSetHeaderForUTF8(fileName, response);

		// 查询数据
		BootStrapTable<ActivityExtractDTO> pageParam = new BootStrapTable<ActivityExtractDTO>();
		pageParam.setPageSize(500);
		Page<ActivityExtractDTO> page = activityExtractService.findPaging(pageParam, activityExtractDTO);
		for (int i = 0; i < page.getPageCount(); i++) {
			// 从第二行开始去掉文件抬头
			if (page.getPage() > 1) {
				tblHeader = "";
			}

			// 导出数据
			String csvStr = ExportHelper.getExportCsvStr(tblHeader, (List) page.getList(), tblContent);
			// 导出文件内容,如果不存在最后一行了则关闭输出流
			boolean isHasNext = page.getPage() + 1 <= page.getPageCount();
			ExportHelper.writerCsvFileContent(csvStr, response, !isHasNext);

			if (isHasNext) {
				page.setPage(page.getPage() + 1);
				page = activityExtractService.findPaging(pageParam, activityExtractDTO);
			} else {
				break;
			}
		}
	}
}