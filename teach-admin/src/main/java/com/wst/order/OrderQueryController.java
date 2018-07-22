/*
 * OrderCenterController.java Created on 2017年9月26日 下午4:51:55
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.order;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.DateUtils;
import com.hiwi.common.util.file.ExportHelper;
import com.hiwi.common.web.page.BootStrapTable;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.PayStatusEnum;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.service.OrderService;

/**
 * 订单查询Controller
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */

@Controller
@RequestMapping("d-admin/order")
public class OrderQueryController {

	@SuppressWarnings("unused")
	private static HiwiLog log = HiwiLogFactory.getLogger(OrderQueryController.class);

	@Reference(version = "0.0.1")
	private OrderService orderService;

	/**
	 * 修改支付方式
	 * 
	 * @param orderCode
	 * @param payType
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/pay")
	public ResultDTO<String> pay(String orderCode, long payType) throws Exception {
		return orderService.updatePayType(orderCode, payType);
	}

	/**
	 * 公开课管理首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("public/index")
	public String publicIndex(ModelMap modelMap) throws Exception {
		return "order/publicClassOrder";
	}

	/**
	 * 定制课管理首页
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("custom/index")
	public String customIndex(ModelMap modelMap) throws Exception {
		return "order/customClassOrder";
	}

	/**
	 * 获取分页列表
	 * 
	 * @param pageParam
	 * @param orderDTO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findPaging")
	public Page<OrderDTO> findPaging(BootStrapTable<OrderDTO> pageParam, OrderDTO orderDTO) throws Exception {
		if (CommonFuntions.isNotEmptyObject(orderDTO.getEndTime())) {
			orderDTO.setEndTime(DateUtils.addEndTime(orderDTO.getEndTime()));
		}
		return orderService.findOrderPaging(pageParam, orderDTO);
	}

	/**
	 * 查询订单导出
	 * 
	 * @param pageParam
	 * @param oilRgeOrderDTO
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping("exportOrder")
	public void exportOrder(OrderDTO orderDTO, HttpServletResponse response) throws Exception {
		if (CommonFuntions.isNotEmptyObject(orderDTO.getEndTime())) {
			orderDTO.setEndTime(DateUtils.addEndTime(orderDTO.getEndTime()));
		}
		if (StringUtils.isNotEmpty(orderDTO.getOrderName())) {
			orderDTO.setOrderName(orderDTO.getOrderName().trim());
		}
		if (StringUtils.isNotEmpty(orderDTO.getLoginAccount())) {
			orderDTO.setLoginAccount(orderDTO.getLoginAccount().trim());
		}

		// 设置需要导出字段
		StringBuffer fileFirstLine = new StringBuffer();
		String[] exportFieldName = null;
		/** 设置头部信息 */
		fileFirstLine.append("会员id,会员账号,用户名,订单ID,订单名称,订单编码,订单类型,订单原价,订单折扣价");
		fileFirstLine.append(",付款途径,订单状态,支付状态,订单创建时间,订单付款时间,订单成交时间,订单附言,付款备注");
		String firstLine = fileFirstLine.toString();

		/** 设置显示条数 */
		BootStrapTable<OrderDTO> pageParam = new BootStrapTable<OrderDTO>();
		pageParam.setLimit(5000);

		/** 设置值属性 */
		exportFieldName = new String[] { "userId", "loginAccount", "realName", "orderId", "orderName", "orderCode",
				"orderTypeStr", "origPriceStr", "discountPriceStr", "payTypeStr", "orderStatusStr", "payStatusStr",
				"createTime", "payTime", "dealTime", "orderNotes", "payNotes" };

		/** 分页查询 */
		Page<OrderDTO> page = orderService.findOrderPaging(pageParam, orderDTO);

		// 设置导出文件名
		ExportHelper.writerCsvFileSetHeaderForUTF8("订单信息-".toString() + DateUtils.getNowTime(), response);
		for (int i = 0; i < page.getPageCount(); i++) {
			// 从第二行开始去掉文件抬头
			if (page.getPage() > 1) {
				firstLine = "";
			}

			// 导出数据
			String csvStr = ExportHelper.getExportCsvStr(firstLine, (List) page.getList(), exportFieldName);
			// 导出文件内容,如果不存在最后一行了则关闭输出流
			boolean isHasNext = page.getPage() + 1 <= page.getPageCount();
			ExportHelper.writerCsvFileContent(csvStr, response, !isHasNext);

			if (isHasNext) {
				page.setPage(page.getPage() + 1);
				pageParam.setPage(i + 2);
				page = orderService.findOrderPaging(pageParam, orderDTO);
			} else {
				break;
			}
		}
	}

	/**
	 * 支付成功控制
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "orderPay")
	public ResultDTO<String> orderPay(String orderCode, String notes, long status) throws Exception {
		ResultDTO<String> result = new ResultDTO<String>();

		OrderDTO order = orderService.getOrderByCode(orderCode);
		if (order == null) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "订单不存在");
		}

		if (order.getOrderStatus() != OrderStatusEnum.HAS_CONFIRM.status
				|| order.getPayStatus() != PayStatusEnum.NO_PAY.status) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "订单状态不合法");
		}

		if (order.getPayType() == 1) {
			return result.set(ResultCodeEnum.ERROR_HANDLE, "订单付款方式不合法");
		}

		result = orderService.orderPay(order, notes, status);

		return result;
	}

}