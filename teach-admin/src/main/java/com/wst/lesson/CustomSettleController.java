/*
 * CustomSettleController.java Created on 2017年10月11日 下午4:04:31
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
package com.wst.lesson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.FileErrorDTO;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.base.helper.ExcelUtils;
import com.wst.base.helper.ExportHelper;
import com.wst.constant.LessonCustomConstant.SettleStatusEnum;
import com.wst.service.lesson.dto.CustomSettleDTO;
import com.wst.service.lesson.service.CustomSettleService;
import com.wst.service.mem.service.TutorService;

/**
 * 定制课程结算记录管理
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin/customSettle")
public class CustomSettleController {

	private static HiwiLog log = HiwiLogFactory.getLogger(CustomSettleController.class);

	@Reference(version = "0.0.1")
	private CustomSettleService customSettleService;

	@Reference(version = "0.0.1")
	private TutorService TutorService;

	/**
	 * 结算薪资管理首页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "settleIndex")
	public String settleIndex() throws Exception {
		return "settle/customSettle";
	}

	/**
	 * 薪资结算页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "settleAccounts")
	public String settle() throws Exception {
		return "settle/customSettleAccounts";
	}

	/**
	 * 获取定制课程结算记录分页数据
	 * 
	 * @param pageParam
	 * @param adDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "findPaging")
	public Page<CustomSettleDTO> findPaging(BootStrapTable<CustomSettleDTO> pageParam, CustomSettleDTO customSettleDTO)
			throws Exception {
		// 结束时间
		if (customSettleDTO.getEndTime() != null) {
			customSettleDTO.setEndTime(DateUtils.addEndTime(customSettleDTO.getEndTime()));
		}
		return customSettleService.findPaging(pageParam, customSettleDTO);
	}

	/**
	 * 强制结算
	 * 
	 * @param recordId
	 * @param notes
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "coerceSettle")
	public ResultDTO<String> coerceSettle(long recordId) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();
		customSettleService.haveSettle(recordId);
		return result.set(ResultCodeEnum.SUCCESS, "");
	}

	/**
	 * 导出结算记录
	 * 
	 * @param pageParam
	 * @param oilRgeOrderDTO
	 * @throws Exception
	 *//*
		 * @SuppressWarnings({ "unchecked", "rawtypes" })
		 * 
		 * @ResponseBody
		 * 
		 * @RequestMapping(value = "exportCustomSettle") public void
		 * exportCustomSettle(CustomSettleDTO customSettleDTO,
		 * HttpServletResponse response) throws Exception { // 导出表头 String
		 * tblHeader = ""; String[] tblContent = null;
		 * 
		 * // 记录ID,导师账号,课程名称,课时名称,结算时长（分钟）,结算单价（美元）,付款状态 tblHeader =
		 * "Record ID,Teacher Login Account,Lesson Name,Class Name,Settlement Time (minutes),Settlement Price (usd),Pay Status,Pay Memo"
		 * ; tblContent = new String[] { "recordId", "realName", "lessonName",
		 * "className", "settleDuration", "settlePrice", "payStatus", "payMemo"
		 * }; // 设置导出文件名 String fileName = "导师定制课薪资发放列表-" +
		 * DateUtils.convertDateToString(new Date(), DateUtils.YYYY_MM_DD) +
		 * ".csv";
		 * 
		 * ExportHelper.writerCsvFileSetHeaderForUTF8(fileName, response);
		 * 
		 * // 查询数据 BootStrapTable<CustomSettleDTO> pageParam = new
		 * BootStrapTable<CustomSettleDTO>(); pageParam.setPageSize(500);
		 * customSettleDTO.setSettleStatus(SettleStatusEnum.HAS_SETTLE.status);
		 * customSettleDTO.setPayStatus(2); customSettleDTO.setBeginTime(null);
		 * customSettleDTO.setEndTime(null); Page<CustomSettleDTO> page =
		 * customSettleService.findPaging(pageParam, customSettleDTO); for (int
		 * i = 0; i < page.getPageCount(); i++) { // 从第二行开始去掉文件抬头 if
		 * (page.getPage() > 1) { tblHeader = ""; }
		 * 
		 * // 导出数据 String csvStr = ExportHelper.getExportCsvStr(tblHeader,
		 * (List) page.getList(), tblContent); // 导出文件内容,如果不存在最后一行了则关闭输出流
		 * boolean isHasNext = page.getPage() + 1 <= page.getPageCount();
		 * ExportHelper.writerCsvFileContent(csvStr, response, !isHasNext);
		 * 
		 * if (isHasNext) { page.setPage(page.getPage() + 1); page =
		 * customSettleService.findPaging(pageParam, customSettleDTO); } else {
		 * break; } } }
		 */

	/**
	 * 定制课程模板导入
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "importCustomSettle")
	public ResultDTO<List<FileErrorDTO>> ImportLesson(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultDTO<List<FileErrorDTO>> resultDTO = new ResultDTO<>();
		// 错误信息
		List<FileErrorDTO> errorList = new ArrayList<>();

		if (request instanceof StandardMultipartHttpServletRequest) {
			StandardMultipartHttpServletRequest standardRequest = (StandardMultipartHttpServletRequest) request;
			MultipartFile customSettleExcel = standardRequest.getFile("customSettleExcel");
			ResultDTO<List<List<String>>> customSettle = ExcelUtils.readExcel(customSettleExcel, 0, 1);
			if (!customSettle.isSuccess()) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "请传入有效文件！");
			}
			List<List<String>> customSettleInfo = customSettle.getResult();
			if (customSettleInfo == null || customSettleInfo.size() <= 0) {
				return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "请传入有效文件！");
			}
			return editCustomSettle(customSettleInfo, errorList);
		} else {
			return resultDTO.set(ResultCodeEnum.ERROR_HANDLE, "请传入有效文件！", errorList);
		}
	}

	/**
	 * 课程编辑导入
	 * 
	 * @param lessonInfo
	 * @param errorList
	 * @return
	 */
	private ResultDTO<List<FileErrorDTO>> editCustomSettle(List<List<String>> customSettleInfo,
			List<FileErrorDTO> errorList) throws Exception {
		ResultDTO<List<FileErrorDTO>> resultDTO = new ResultDTO<>();
		// 结算记录集合
		for (int i = 0; i < customSettleInfo.size(); i++) {
			FileErrorDTO fileErrorDTO = new FileErrorDTO();
			List<String> customSettleDetail = customSettleInfo.get(i);
			log.debug("第" + (i + 1) + "数据:" + JsonUtils.getJson(customSettleDetail));
			if ((customSettleDetail == null || customSettleDetail.size() != 8)) {
				fileErrorDTO.setDataNum(i);
				fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：数据格式不合法");
				errorList.add(fileErrorDTO);
				continue;
			}
			// 当这一行的第一个是数字时
			if (NumberUtils.isNumber(customSettleDetail.get(0))) {
				CustomSettleDTO customSettle = new CustomSettleDTO();

				long recordId = Long.valueOf(customSettleDetail.get(0));
				CustomSettleDTO customSettleDTO = customSettleService.getCustomSettleById(recordId);
				if (customSettleDTO == null) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：记录不存在");
					errorList.add(fileErrorDTO);
					continue;
				}
				if (customSettleDTO.getSettleStatus() != 2) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：记录结算状态不合法（未结算）");
					errorList.add(fileErrorDTO);
					continue;
				}
				if (customSettleDTO.getPayStatus() != 1) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：记录付款状态不合法（已付款）");
					errorList.add(fileErrorDTO);
					continue;
				}
				customSettle.setRecordId(recordId);

				long payStatus = Long.valueOf(customSettleDetail.get(6));
				if (payStatus > 2 || payStatus <= 0) {
					fileErrorDTO.setDataNum(i);
					fileErrorDTO.setErrorMsg("第" + (i + 1) + "行处理失败：支付状态填写不正确");
					errorList.add(fileErrorDTO);
					continue;
				}
				customSettle.setPayStatus(payStatus);

				String payMemo = customSettleDetail.get(7);
				customSettle.setPayMemo(payMemo);

				if (payStatus == 2) {
					customSettleService.memPaySettle(recordId, payMemo);
				}
			}
		}
		return resultDTO.set(ResultCodeEnum.SUCCESS, "SUCCESS", errorList);
	}

	/**
	 * 导出结算记录
	 * 
	 * @param pageParam
	 * @param oilRgeOrderDTO
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping(value = "exportCustomSettle")
	public void exportCustomSettle(CustomSettleDTO customSettleDTO, HttpServletResponse response) throws Exception {
		// 导出表头
		String tblHeader = "";
		String[] tblContent = null;

		if (StringUtils.isNotEmpty(customSettleDTO.getRealName())) {
			customSettleDTO.setRealName(customSettleDTO.getRealName().trim());
		}
		if (StringUtils.isNotEmpty(customSettleDTO.getLessonName())) {
			customSettleDTO.setLessonName(customSettleDTO.getLessonName().trim());
		}
		if (StringUtils.isNotEmpty(customSettleDTO.getClassName())) {
			customSettleDTO.setClassName(customSettleDTO.getClassName().trim());
		}

		// 记录ID,导师账号,课程名称,课时名称,结算时长（分钟）,结算单价（美元）,付款状态
		tblHeader = "Record ID,Teacher Login Account,Lesson Name,Class Name,Settlement Time (minutes),Settlement Price (usd),Pay Status,Pay Memo";
		tblContent = new String[] { "recordId", "realName", "lessonName", "className", "settleDuration",
				"settlePriceStr", "payStatus", "payMemo" };
		// 设置导出文件名
		String fileName = "导师定制课薪资发放列表-" + DateUtils.convertDateToString(new Date(), DateUtils.YYYY_MM_DD) + ".xlsx";

		ExportHelper.writerXlsFileSetHeaderForUTF8(fileName, response);

		// 查询数据
		BootStrapTable<CustomSettleDTO> pageParam = new BootStrapTable<CustomSettleDTO>();
		pageParam.setPageSize(500);
		customSettleDTO.setSettleStatus(SettleStatusEnum.HAS_SETTLE.status);
		customSettleDTO.setPayStatus(1);
		customSettleDTO.setBeginTime(null);
		customSettleDTO.setEndTime(null);
		Page<CustomSettleDTO> page = customSettleService.findPaging(pageParam, customSettleDTO);
		for (int i = 0; i < page.getPageCount(); i++) {
			// 从第二行开始去掉文件抬头
			if (page.getPage() > 1) {
				tblHeader = "";
			}
			// 导出数据
			HSSFWorkbook Workbook = ExportHelper.exportExcel(fileName, tblHeader, tblContent, (List) page.getList());
			// 导出文件内容,如果不存在最后一行了则关闭输出流
			boolean isHasNext = page.getPage() + 1 <= page.getPageCount();
			ExportHelper.writerXlsFileContent(Workbook, response, !isHasNext);

			if (isHasNext) {
				page.setPage(page.getPage() + 1);
				page = customSettleService.findPaging(pageParam, customSettleDTO);
			} else {
				break;
			}
		}
	}

}
