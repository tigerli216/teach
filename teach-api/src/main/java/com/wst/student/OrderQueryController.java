/*
 * OrderQueryController.java Created on 2017年10月12日 上午10:58:03
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
package com.wst.student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.DateUtils;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.base.helper.ConvertToMapHelper;
import com.wst.base.helper.UserHelper;
import com.wst.constant.OrderConstant.OrderPayTypeEnum;
import com.wst.constant.OrderConstant.OrderStatusEnum;
import com.wst.constant.OrderConstant.OrderSubTypeEnmu;
import com.wst.constant.OrderConstant.OrderTypeEnmu;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.CustomDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.dto.PublicDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.LessonCustomService;
import com.wst.service.lesson.service.LessonPublicService;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.TutorService;
import com.wst.service.order.dto.OrderDTO;
import com.wst.service.order.service.OrderService;
import com.wst.service.order.service.PaymentService;

/**
 * 订单查询
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/API/student/orderQuery/")
public class OrderQueryController {

	@Reference(version = "0.0.1")
	private OrderService orderService;
	@Reference(version = "0.0.1")
	private LessonCustomService lessonCustomService;
	@Reference(version = "0.0.1")
	private LessonPublicService lessonPublicService;
	@Reference(version = "0.0.1")
	private PaymentService paymentService;
	@Reference(version = "0.0.1")
	private CustomClassService customClassService;
	@Reference(version = "0.0.1")
	private TutorService tutorService;

	/**
	 * 获取用户定制课订单
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @param orderSubType(1-vip定制辅导；2-精品实习)
	 * @return
	 */
	@RequestMapping("customOrder")
	public ResultDTO<List<Map<String, Object>>> customOrder(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "1") long orderSubType) throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		Page<OrderDTO> pageParam = new Page<OrderDTO>();
		pageParam.setPage(page);
		pageParam.setPageSize(pageSize);
		OrderDTO orderQuery = new OrderDTO();
		orderQuery.setUserId(user.getUserId());
		orderQuery.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);
		if (orderSubType == 1) {
			orderQuery.setOrderType(OrderTypeEnmu.CUSTOM_CLASS_ORDER.type);
			orderQuery.setOrderSubType(OrderSubTypeEnmu.CUSTOM_CLASS.type);
		} else if (orderSubType == 2) {
			orderQuery.setOrderType(OrderTypeEnmu.CUSTOM_CLASS_ORDER.type);
			orderQuery.setOrderSubType(OrderSubTypeEnmu.INTERNSHIP_CLASS.type);
		} else {
			// 订单类型不合法
			return result.set(ResultCodeEnum.ERROR_INVALID_PARAMS, "The order type is not valid",
					new ArrayList<Map<String, Object>>());
		}
		List<Map<String, Object>> resultOrderList = new ArrayList<>();
		List<OrderDTO> orderList = orderService.findOrderPaging(pageParam, orderQuery).getList();
		if (orderList != null && orderList.size() > 0) {
			for (OrderDTO order : orderList) {
				Map<String, Object> map = new HashMap<>(12);
				map.put("orderType", orderSubType);
				map.put("orderCode", order.getOrderCode());
				map.put("orderName", order.getOrderName());
				map.put("createTime", order.getCreateTime());
				map.put("payStatus", order.getPayStatus());
				map.put("payType", order.getPayType());
				map.put("discountPrice", order.getDiscountPriceStr());
				// 定制课程查询课程信息
				CustomDTO customDTO = lessonCustomService.getLessonById(order.getBusiId());
				if (customDTO != null) {
					map.put("lessonName", customDTO.getLessonName());
					/* map.put("lessonRcmd", customDTO.getLessonRcmdStr()); */
					map.put("industryName", customDTO.getIndustry());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					map.put("endTime", sdf.format(customDTO.getEndTime()));
					map.put("lessonCycle",
							DateUtils.getMonthSpace(customDTO.getBeginTime(), customDTO.getEndTime()) + 1);
					if (customDTO.getAttrList() != null) {
						map.put("totalClass", customDTO.getAttrList().size());
					}
				}
				resultOrderList.add(map);
			}
		}

		return result.set(ResultCodeEnum.SUCCESS, "", resultOrderList);
	}

	/**
	 * 获取用户网课订单
	 * 
	 * @param dto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("publicOrder")
	public ResultDTO<List<Map<String, Object>>> publicOrder(CommonParamsDTO dto,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize)
			throws Exception {
		ResultDTO<List<Map<String, Object>>> result = new ResultDTO<>();
		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		Page<OrderDTO> pageParam = new Page<OrderDTO>();
		pageParam.setPage(page);
		pageParam.setPageSize(pageSize);
		OrderDTO orderQuery = new OrderDTO();
		orderQuery.setUserId(user.getUserId());
		orderQuery.setOrderStatus(OrderStatusEnum.HAS_CONFIRM.status);
		orderQuery.setOrderType(OrderTypeEnmu.PUBLIC_CLASS_ORDER.type);
		List<Map<String, Object>> resultOrderList = new ArrayList<>();
		List<OrderDTO> orderList = orderService.findOrderPaging(pageParam, orderQuery).getList();
		if (orderList != null && orderList.size() > 0) {
			for (OrderDTO order : orderList) {
				Map<String, Object> map = new HashMap<>(9);
				map.put("orderCode", order.getOrderCode());
				map.put("orderName", order.getOrderName());
				map.put("createTime", order.getCreateTime());
				map.put("payStatus", order.getPayStatus());
				map.put("discountPrice", order.getDiscountPriceStr());
				// 查询网课信息
				PublicDTO publicDTO = lessonPublicService.getLessonById(order.getBusiId());
				if (publicDTO != null) {
					map.put("lessonName", publicDTO.getLessonName());
					map.put("lessonPic", publicDTO.getLessonPic());
				}
				resultOrderList.add(map);
			}
		}

		return result.set(ResultCodeEnum.SUCCESS, "", resultOrderList);
	}

	/**
	 * 获取订单详情
	 * 
	 * @param dto
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getOrderDtl")
	public ResultDTO<Map<String, Object>> getOrderDtl(CommonParamsDTO dto, String orderCode) throws Exception {
		ResultDTO<Map<String, Object>> result = new ResultDTO<>();
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isEmpty(orderCode)) {
			// 订单号不能为空
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The order number cannot be empty", resultMap);
		}
		UserDTO user = UserHelper.getUserDTO(dto);
		if (user == null) {
			return result.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		OrderDTO orderDTO = orderService.getOrderByCode(orderCode);
		if (orderDTO == null) {
			// 订单不存在
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Order does not exist", resultMap);
		}
		if (orderDTO.getUserId() != user.getUserId()) {
			// 订单不合法
			return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Order is not legal", resultMap);
		}
		String[] keys = new String[] { "orderCode", "orderName", "createTime", "discountPrice", "payStatus" };
		String[] attrNames = new String[] { "orderCode", "orderName", "createTime", "discountPriceStr", "payStatus" };
		resultMap = ConvertToMapHelper.convertToMap(orderDTO, keys, attrNames);
		if (orderDTO.getOrderType() == OrderTypeEnmu.PUBLIC_CLASS_ORDER.type) {
			resultMap.put("orderType", 2);
		} else if (orderDTO.getOrderType() == OrderTypeEnmu.CUSTOM_CLASS_ORDER.type) {
			resultMap.put("orderType", orderDTO.getOrderSubType() == OrderSubTypeEnmu.CUSTOM_CLASS.type ? 1 : 3);
		}
		// 支付方式
		resultMap.put("payType", 1);
		if (orderDTO.getPayType() == OrderPayTypeEnum.ON_PAY.type) { // 线上支付
			resultMap.put("payType", 2);
		}
		/** 课程信息 **/
		// 课程名称
		resultMap.put("lessonName", "");
		// 课程介绍
		resultMap.put("lessonRcmd", "");
		// 课程费用
		resultMap.put("lessonPrice", "");
		// 课程导师
		resultMap.put("tutorName", "");
		// 课程周期
		resultMap.put("lessonCycle", "");
		// 总课时
		resultMap.put("totalClassTime", "");
		// 定制课二维码
		resultMap.put("adviserCode", "");
		// 课程ID
		resultMap.put("lessonId", "");
		// 总课节
		resultMap.put("totalClass", "");
		if (orderDTO.getOrderType() == OrderTypeEnmu.PUBLIC_CLASS_ORDER.type) {
			PublicDTO publicDTO = lessonPublicService.getLessonById(orderDTO.getBusiId());
			if (publicDTO != null) {
				resultMap.put("lessonId", publicDTO.getLessonId());
				resultMap.put("lessonName", publicDTO.getLessonName());
				resultMap.put("lessonRcmd", publicDTO.getLessonRcmdStr());
				resultMap.put("lessonPrice", publicDTO.getDiscountPriceStr());
				resultMap.put("lessonPic", publicDTO.getLessonPic());
				List<PublicClassDTO> publicClassList = publicDTO.getAttrList();
				resultMap.put("totalClass", publicClassList.size());
			}
		} else if (orderDTO.getOrderType() == OrderTypeEnmu.CUSTOM_CLASS_ORDER.type) {
			CustomDTO customDTO = lessonCustomService.getLessonById(orderDTO.getBusiId());
			if (customDTO != null) {
				resultMap.put("lessonId", customDTO.getLessonId());
				resultMap.put("lessonName", customDTO.getLessonName());
				resultMap.put("lessonRcmd", customDTO.getLessonRcmdStr());
				resultMap.put("lessonPrice", customDTO.getLessonPriceStr());
				resultMap.put("lessonCycle",
						DateUtils.getMonthSpace(customDTO.getBeginTime(), customDTO.getEndTime()) + 1);
				List<CustomClassDTO> customClassList = customDTO.getAttrList();
				resultMap.put("totalClass", customClassList.size());
				resultMap.put("totalClassTime", customDTO.getTotalClass());
				resultMap.put("adviserCode", customDTO.getAdviserCode());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				resultMap.put("beginTime", sdf.format(customDTO.getBeginTime()));
				resultMap.put("endTime", sdf.format(customDTO.getEndTime()));
				// 正在进行的课时信息
				CustomClassDTO customClassDTO = customClassService.getCustomClassById(customDTO.getCurrentClass());
				if (customClassDTO != null) {
					TutorDTO tutorDTO = tutorService.getTutorById(customClassDTO.getTutorId());
					if (tutorDTO != null) {
						resultMap.put("tutorName", tutorDTO.getRealName());
					}
				}
			}
		}

		return result.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}

	/**
	 * 查询订单支付状态
	 * 
	 * @param dto
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findPayStatus")
	public ResultDTO<Map<String, Object>> findPayStatus(CommonParamsDTO dto, String orderCode) throws Exception {
		ResultDTO<Map<String, Object>> resultDTO = new ResultDTO<>();
		Map<String, Object> resultMap = new HashMap<>();
		if (StringUtils.isEmpty(orderCode)) {
			// 订单号不能为空
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "The order number cannot be empty");
		}
		UserDTO userDTO = UserHelper.getUserDTO(dto);
		if (userDTO == null) {
			return resultDTO.set(ResultCodeEnum.ERROR_NOT_ALLOW, "");
		}
		OrderDTO orderDTO = orderService.getOrderByCode(orderCode);
		if (orderDTO == null) {
			// 订单不存在
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "Order does not exist");
		}
		if (orderDTO.getUserId() != userDTO.getUserId()) {
			// 用户不合法
			return resultDTO.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "User illegal");
		}
		resultMap.put("payStatus", orderDTO.getPayStatus());

		return resultDTO.set(ResultCodeEnum.SUCCESS, "", resultMap);
	}

	public static void main(String[] args) {

		// Map<String, Integer> map = new HashMap<>();
		// List<String> list = new ArrayList<>();
		// List<String> list1 = Arrays.asList("aa", "bbb", "ccc");
		// list = list1.stream().filter(s -> s.length() > 2).map(s -> map.put(s,
		// s)).collect(Collectors.toList());
		// System.out.println(list.toString());
		// System.out.println(map.toString());

		List<String> arrayList = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			arrayList.add("A" + i);
		}
		long a = System.currentTimeMillis();
		for (String item : arrayList) {
			System.out.print(item);
		}
		long a1 = System.currentTimeMillis();

		arrayList.forEach(item -> System.out.print(item));
		long a2 = System.currentTimeMillis();
		System.out.println();
		System.out.println("耗时1-------：" + (a1 - a));
		System.out.println("耗时2-------：" + (a2 - a1));
	}
}
