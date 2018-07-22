/*
 * orderDao.java Created on 2017年9月26日 下午4:51:29
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
package com.wst.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.entity.order.Order;
import com.wst.service.order.dto.OrderDTO;

/**
 * 订单管理Dao（订单表，bm_order）
 * 
 * @author <a href="mailto:pengcc@hiwitech.com">pengchengcheng</a>
 * @version 1.0
 */
@Repository
public class OrderDao {

	@Resource
	private BaseDao baseDao;

	/**
	 * 保存订单
	 * 
	 * @param order
	 *            订单信息
	 * @return
	 * @throws Exception
	 */
	public long save(Order order) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("insert into bm_order(order_code, order_name, order_type, order_sub_type, ");
		sql.append("       user_id, busi_id, orig_price, discount_price, create_time, ");
		sql.append("       pay_type, order_status, pay_status, order_notes,pay_notes, valid_time) ");
		sql.append("values(:orderCode, :orderName, :orderType, :orderSubType, ");
		sql.append("       :userId, :busiId, :origPrice, :discountPrice, now(), ");
		sql.append("       :payType, :orderStatus, :payStatus, :orderNotes, :payNotes, :validTime) ");

		return baseDao.insertInto(sql.toString(), order, "order_id");
	}

	/**
	 * 切换付款方式
	 * 
	 * @param orderCode
	 * @param payType
	 * @return
	 * @throws Exception
	 */
	public int updatePayType(String orderCode, long payType) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update bm_order ");
		sql.append("   set pay_type = :payType ");
		sql.append(" where order_code = :orderCode ");

		Map<String, Object> params = new HashMap<>(2);
		params.put("payType", payType);
		params.put("orderCode", orderCode);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 查询订单分页数据
	 * 
	 * @param pageParam
	 *            分页参数
	 * @param order
	 *            订单相关查询参数
	 * @return
	 * @throws Exception
	 */
	public Page<OrderDTO> findOrderPaging(Page<OrderDTO> pageParam, OrderDTO order) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select m.real_name realName, m.login_account, m.user_level, ");
		sql.append("       o.order_id, o.order_code, o.order_name, o.order_type, ");
		sql.append("       o.order_sub_type, o.user_id, o.busi_id, o.orig_price, ");
		sql.append("       o.discount_price, o.create_time, o.pay_time, o.deal_time, valid_time, ");
		sql.append("       o.pay_type, o.order_status, o.pay_status, o.order_notes, o.pay_notes ");
		sql.append("  from bm_order o ");
		sql.append("  join mem_user m on o.user_id = m.user_id ");
		sql.append(" where 1 = 1 ");

		// 开始查询时间
		if (order.getBeginTime() != null) {
			sql.append(" and o.create_time >= :beginTime ");
		}
		// 查询结束时间
		if (order.getEndTime() != null) {
			sql.append(" and o.create_time <= :endTime ");
		}
		if (StringUtils.isNotEmpty(order.getOrderName())) {
			sql.append(" and o.order_name like :orderName ");
		}

		// 支付方式
		if (order.getPayType() > 0) {
			sql.append(" and o.pay_type = :payType");
		}
		// 会员账号
		if (StringUtils.isNotEmpty(order.getLoginAccount())) {
			order.setLoginAccount("%" + order.getLoginAccount() + "%");
			sql.append(" and (m.login_account like :loginAccount or m.bind_mobile like :loginAccount) ");
		}
		// 用户Id
		if (order.getUserId() > 0) {
			sql.append(" and o.user_id = :userId");
		}
		// 订单Id
		if (order.getOrderId() > 0) {
			sql.append(" and o.order_id = :orderId");
		}
		// 业务Id
		if (order.getBusiId() > 0) {
			sql.append(" and o.busi_id = :busiId");
		}
		// 订单编码
		if (StringUtils.isNotEmpty(order.getOrderCode())) {
			sql.append(" and o.order_code = :orderCode");
		}
		// 订单类型
		if (order.getOrderType() > 0) {
			sql.append(" and o.order_type = :orderType");
		}
		// 订单子类型
		if (order.getOrderSubType() > 0) {
			sql.append(" and o.order_sub_type = :orderSubType");
		}
		// 订单状态
		if (order.getOrderStatus() > 0) {
			sql.append(" and o.order_status= :orderStatus ");
		}
		// 用户级别
		if (order.getUserLevel() > 0) {
			sql.append(" and m.user_level = :userLevel ");
		}
		// 支付状态
		if (order.getPayStatus() > 0) {
			sql.append(" and o.pay_status= :payStatus ");
		}

		// 订单支付时间
		if (order.getPayBeginTime() != null) {
			sql.append(" and o.pay_time >= :payBeginTime");
		}

		if (order.getPayEndTime() != null) {
			sql.append(" and o.pay_time <= :payEndTime");
		}

		sql.append(" order by o.order_id desc");

		return baseDao.findPaging(sql.toString(), pageParam, order, OrderDTO.class);
	}

	/**
	 * 根据订单号获取订单信息
	 * 
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public OrderDTO getOrderByCode(String orderCode) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select order_id, order_code, order_name, order_type, order_sub_type, ");
		sql.append("       user_id, busi_id, orig_price, discount_price, create_time, ");
		sql.append("       pay_time, deal_time, pay_type, order_status, pay_status, ");
		sql.append("       order_notes, pay_notes, valid_time ");
		sql.append("  from bm_order o ");
		sql.append(" where order_code = :orderCode");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("orderCode", orderCode);

		return baseDao.get(sql.toString(), params, OrderDTO.class);
	}

	/**
	 * 支付成功
	 * 
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public long paySuccess(String orderCode) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update bm_order ");
		sql.append("   set pay_status = 2, pay_time = now(), deal_time = now() ");
		sql.append(" where order_code = :orderCode ");

		Map<String, String> params = new HashMap<String, String>(2);
		params.put("orderCode", orderCode);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 通过用户ID和课程ID来获取网课订单
	 * 
	 * @param userId
	 *            用户名称
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	public OrderDTO getByLessonIdUSerId(long userId, long lessonId, long orderType) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select order_id, order_code, order_name, order_type, order_sub_type, ");
		sql.append("       user_id, busi_id, orig_price, discount_price, create_time, pay_time, ");
		sql.append("       deal_time, pay_type, order_status, pay_status, order_notes, pay_notes, valid_time");
		sql.append("  from bm_order o ");
		sql.append(" where order_type = :orderType "); // 网课订单 网课订单
		sql.append("   and order_status = 1 "); // 订单状态 已确认
		sql.append("   and user_id = :userId "); // 用户ID
		sql.append("   and busi_id = :lessonId "); // 课程ID

		Map<String, Long> params = new HashMap<String, Long>(3);
		params.put("userId", userId);
		params.put("lessonId", lessonId);
		params.put("orderType", orderType);

		return baseDao.get(sql.toString(), params, OrderDTO.class);
	}

	/**
	 * 取消订单
	 * 
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public int cancelOrder(String orderCode) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update bm_order ");
		sql.append("   set order_status = 2");
		sql.append(" where order_code = :orderCode ");

		Map<String, String> params = new HashMap<String, String>(1);
		params.put("orderCode", orderCode);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 支付失败
	 * 
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public long payFail(String orderCode) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("update bm_order ");
		sql.append("   set pay_status = 4, pay_time = now(), order_status = 4 ");
		sql.append(" where order_code = :orderCode ");

		Map<String, String> params = new HashMap<String, String>(2);
		params.put("orderCode", orderCode);

		return baseDao.executeSQL(sql.toString(), params);
	}

	/**
	 * 通过用户ID和课程ID来获取网课订单
	 * 
	 * @param userId
	 *            用户名称
	 * @param lessonId
	 *            课程ID
	 * @return
	 * @throws Exception
	 */
	public OrderDTO getByLessonIdUserId(long userId, long lessonId, long orderType) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select order_id, order_code, order_name, order_type, order_sub_type, valid_time, ");
		sql.append("       user_id, busi_id, orig_price, discount_price, create_time, pay_time, ");
		sql.append("       deal_time, pay_type, order_status, pay_status, order_notes, pay_notes, valid_time ");
		sql.append("  from bm_order ");
		sql.append(" where order_type = :orderType ");
		sql.append("   and user_id = :userId ");
		sql.append("   and busi_id = :lessonId ");
		sql.append(" order by create_time desc ");
		sql.append(" limit 1 ");

		Map<String, Long> params = new HashMap<String, Long>(3);
		params.put("userId", userId);
		params.put("lessonId", lessonId);
		params.put("orderType", orderType);

		return baseDao.get(sql.toString(), params, OrderDTO.class);
	}

	/**
	 * 关联课程表，课程最迟时间 获取定制课订单集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderDTO> findLessons() throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select bo.user_id, max(lc.end_time) end_time ");
		sql.append("  from bm_order bo ");
		sql.append("  join lesson_custom lc ");
		sql.append("    on bo.busi_id = lc.lesson_id ");
		sql.append(" where bo.order_type = 2 ");
		sql.append("   and bo.order_status = 1 ");
		sql.append("   and bo.pay_status = 2 ");
		sql.append(" group by bo.user_id ");

		return baseDao.findList(sql.toString(), null, OrderDTO.class);
	}
}
