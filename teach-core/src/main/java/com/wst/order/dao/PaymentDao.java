/*
 * PaymentDao.java Created on 2017年10月12日 下午3:13:18
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.entity.order.Payment;
import com.wst.service.order.dto.PaymentDTO;

/**
 * 支付记录
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Component
public class PaymentDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 分页查询支付记录
     * 
     * @param paymentDTO
     * @return
     * @throws Exception
     */
    public Page<PaymentDTO> pageFindPayment(Page<PaymentDTO> pageParam, PaymentDTO paymentDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select pay_id, pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id ");
        sql.append("  from bm_payment");
        sql.append(" where 1 = 1");

        if (paymentDTO != null) {
            if (paymentDTO.getPayId() > 0) {
                sql.append(" and pay_id = :PayId");
            }
            if (StringUtils.isNotEmpty(paymentDTO.getPayNo())) {
                sql.append(" and pay_no = :payNo");
            }
            if (StringUtils.isNotEmpty(paymentDTO.getOrderCode())) {
                sql.append(" and order_code = :orderCode");
            }
            if (paymentDTO.getTradeSource() > 0) {
                sql.append(" and trade_source = :tradeSource");
            }
            if (paymentDTO.getTradeType() > 0) {
                sql.append(" and trade_type = :tradeType");
            }
            if (paymentDTO.getPayType() > 0) {
                sql.append(" and pay_type = :payType");
            }
            if (StringUtils.isNotEmpty(paymentDTO.getThridTradeNo())) {
                sql.append(" and thrid_trade_no = :thridTradeNo");
            }
            if (paymentDTO.getTradeStatus() > 0) {
                sql.append(" and trade_status = :tradeStatus");
            }
        }

        return baseDao.findPaging(sql.toString(), pageParam, paymentDTO, PaymentDTO.class);
    }

    /**
     * 修改支付状态
     * 
     * @param orderCode
     *            订单编码
     * @param tradeType
     *            交易类型：1-消费；2-退款；3-提现
     * @param outTradeNo
     *            第三方支付交易号
     * @param payStatus
     *            交易状态；1-未支付；2-支付中；3-成功；4-失败；5-关闭
     * @return
     * @throws Exception
     */
    public long upPayStatus(String orderCode, long tradeType, String outTradeNo, long payStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update bm_payment");
        sql.append("   set pay_time = now(), thrid_trade_no = :outTradeNo, trade_status = :payStatus");
        sql.append(" where order_code = :orderCode ");
        sql.append("   and trade_type = :tradeType ");

        Map<String, Object> params = new HashMap<>(6);
        params.put("orderCode", orderCode);
        params.put("tradeType", tradeType);
        params.put("outTradeNo", outTradeNo);
        params.put("payStatus", payStatus);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 修改支付状态
     * 
     * @param payId
     *            支付记录ID
     * @param tradeType
     *            交易类型：1-消费；2-退款；3-提现
     * @param outTradeNo
     *            第三方支付交易号
     * @param payStatus
     *            交易状态；1-未支付；2-支付中；3-成功；4-失败；5-关闭
     * @return
     * @throws Exception
     */
    public long upPayStatusByPayId(long payId, String outTradeNo, long payStatus) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update bm_payment");
        sql.append("   set pay_time = now(), thrid_trade_no = :outTradeNo, trade_status = :payStatus");
        sql.append(" where pay_id = :payId ");

        Map<String, Object> params = new HashMap<>(3);
        params.put("payId", payId);
        params.put("outTradeNo", outTradeNo);
        params.put("payStatus", payStatus);

        return baseDao.executeSQL(sql.toString(), params);
    }
    
    /**
     * 保存
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public long save(Payment entity) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into bm_payment(pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id)");
        sql.append("values(:payNo, :orderCode, :tradeSource, :tradeExplain, ");
        sql.append("       :payAmount, :payType, :tradeType, now(), :thridTradeNo, ");
        sql.append("       :tradeMemo, :tradeStatus, :payChnId)");

        return baseDao.insertInto(sql.toString(), entity, "pay_id");
    }

    /**
     * 根据id查询支付记录
     * 
     * @param payId
     * @return
     * @throws Exception
     */
    public PaymentDTO getPaymentById(long payId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_id, pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id ");
        sql.append("  from bm_payment");
        sql.append(" where pay_id = :payId");

        Map<String, Long> param = new HashMap<String, Long>(1);
        param.put("payId", payId);

        return baseDao.get(sql.toString(), param, PaymentDTO.class);
    }

    /**
     * 根据交易流水号查询支付记录
     * 
     * @param payNo
     * @return
     * @throws Exception
     */
    public PaymentDTO getPaymentByPayNo(String payNo) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_id, pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id ");
        sql.append("  from bm_payment");
        sql.append(" where pay_no = :payNo");

        Map<String, String> param = new HashMap<>(1);
        param.put("payNo", payNo);

        return baseDao.get(sql.toString(), param, PaymentDTO.class);
    }

    /**
     * 根据订单号，交易类型获取支付记录
     * 
     * @param orderCode
     *            订单编码
     * @param tradeType
     *            交易类型：1-消费；2-退款；3-提现
     * @return
     * @throws Exception
     */
    public List<PaymentDTO> getPayments(String orderCode, long tradeType) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_id, pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id ");
        sql.append("  from bm_payment");
        sql.append(" where order_code = :orderCode ");
  
        if (tradeType >= 0) {
            sql.append(" and trade_type = :tradeType");
        }

        Map<String, Object> params = new HashMap<>(3);
        params.put("orderCode", orderCode);
        params.put("tradeType", tradeType);

        return baseDao.findList(sql.toString(), params, PaymentDTO.class);
    }
    
    /**
     * 根据订单编码查询有效支付记录,最后一条支付记录认为是有效支付记录
     * 
     * @param payId
     * @return
     * @throws Exception
     */
    public PaymentDTO getPaymentByOrderCode(String orderCode) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_id, pay_no, order_code, trade_source, trade_explain, ");
        sql.append("       pay_amount, pay_type, trade_type, pay_time, thrid_trade_no, ");
        sql.append("       trade_memo, trade_status, pay_chn_id ");
        sql.append("  from bm_payment ");
        sql.append(" where order_code = :orderCode ");
        sql.append(" order by pay_id desc ");
        sql.append(" limit 1 ");

        Map<String, String> param = new HashMap<String, String>(1);
        param.put("orderCode", orderCode);

        return baseDao.get(sql.toString(), param, PaymentDTO.class);
    }
}
