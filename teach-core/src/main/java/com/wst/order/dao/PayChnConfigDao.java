/*
 * PayChnConfigDao.java Created on 2016年9月26日 下午10:34:56
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
package com.wst.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.order.dto.PayChnConfigDTO;

/**
 * 第三方支付渠道配置Dao
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@Repository
public class PayChnConfigDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 根据终端类型、支付通道获取渠道配置
     * 
     * @param sourceChn
     *            终端类型
     * @param payChn
     *            支付通道
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_pay_chn_config")
    public PayChnConfigDTO getPayConfig(int sourceChn, int payChn) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name, pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_chn, :paySourceChn paySourceChn");
        sql.append("  from bm_pay_chn_config ");
        sql.append(" where pay_chn_status = 1");
        sql.append("   and pay_source_chn = :paySourceChn");
        sql.append("   and pay_chn = :payChn ");
        sql.append(" order by pay_chn_id desc");

        Map<String, Integer> params = new HashMap<String, Integer>(2);
        params.put("paySourceChn", sourceChn);
        params.put("payChn", payChn);

        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    /**
     * 获取所有状态的支付通道
     * 
     * @param sourceChn
     * @param payChn
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_pay_chn_config_all")
    public PayChnConfigDTO getAllPayConfig(int sourceChn, int payChn) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name, pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, :paySourceChn paySourceChn ");
        sql.append("  from bm_pay_chn_config ");
        sql.append("   and pay_source_chn = :paySourceChn");
        sql.append("   and pay_chn = :payChn ");
        sql.append(" order by pay_chn_id desc");

        Map<String, Integer> params = new HashMap<String, Integer>(2);
        params.put("paySourceChn", sourceChn);
        params.put("payChn", payChn);

        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    /**
     * 根据订单号查询第三方支付渠道配置
     * 
     * @param orderCode
     *            订单号
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_pay_chn_config")
    public PayChnConfigDTO getPayChnConfigByOrderCode(String orderCode, long tradeType) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select payChnConfig.pay_chn_id, payChnConfig.pay_chn_name, payChnConfig.pay_chn_code, ");
        sql.append("       payChnConfig.pay_chn_status, payChnConfig.pay_chn_params, ");
        sql.append("       payChnConfig.pay_source_chn, payChnConfig.pay_chn ");
        sql.append("  from bm_pay_chn_config payChnConfig ");
        sql.append(" inner join bm_payment payment on payment.pay_chn_id = payChnConfig.pay_chn_id ");
        sql.append(" where payment.order_code = :orderCode");
 
        if (tradeType > 0) {
            sql.append(" and payment.trade_type = :tradeType");
        }

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("orderCode", orderCode);
        params.put("tradeType", tradeType);

        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    /**
     * 根据交易流水号查询第三方支付渠道配置
     * 
     * @param orderCode
     *            订单号
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_pay_chn_config")
    public PayChnConfigDTO getPayChnConfigByPayNo(String payNo, long tradeType) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select payChnConfig.pay_chn_id, payChnConfig.pay_chn_name, payChnConfig.pay_chn_code, ");
        sql.append("       payChnConfig.pay_chn_status, payChnConfig.pay_chn_params, ");
        sql.append("       payChnConfig.pay_source_chn, payChnConfig.pay_chn");
        sql.append("  from bm_pay_chn_config payChnConfig ");
        sql.append(" inner join bm_payment payment on payment.pay_chn_id = payChnConfig.pay_chn_id ");
        sql.append(" where payment.pay_no = :payNo");
 
        if (tradeType > 0) {
            sql.append(" and payment.trade_type = :tradeType");
        }

        Map<String, Object> params = new HashMap<>(2);
        params.put("payNo", payNo);
        params.put("tradeType", tradeType);

        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    /**
     * 根据ID获取渠道配置
     * 
     * @param chnId
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_pay_chn_config")
    public PayChnConfigDTO getPayConfigById(long chnId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name, pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_source_chn, pay_chn ");
        sql.append("  from bm_pay_chn_config ");
        sql.append(" where pay_chn_id = :chnId ");
        sql.append(" order by pay_chn_id desc");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("chnId", chnId);

        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    /**
     * 查询第三方充值渠道配置分页数据
     * 
     * @param pageParam
     * @param rechargeChnConfigDTO
     * @return
     * @throws Exception
     */
    public Page<PayChnConfigDTO> findChnConfigPaging(Page<PayChnConfigDTO> pageParam, PayChnConfigDTO payChnConfigDTO)
            throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name, pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_source_chn, pay_chn ");
        sql.append("  from bm_pay_chn_config ");
        
        return baseDao.findPaging(sql.toString(), pageParam, payChnConfigDTO, PayChnConfigDTO.class);
    }

    /**
     * 根据渠道配置ID查询渠道配置信息
     * 
     * @param payChnId
     * @return
     * @throws Exception
     */
    public PayChnConfigDTO findChnConfigByPayChnId(long payChnId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name,pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_source_chn, pay_chn ");
        sql.append("  from bm_pay_chn_config ");
        sql.append(" where pay_chn_id = :chnId ");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("chnId", payChnId);
        
        return baseDao.get(sql.toString(), params, PayChnConfigDTO.class);
    }

    public List<PayChnConfigDTO> payChnConfigList() throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name,pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_source_chn, pay_chn");
        sql.append("  from bm_pay_chn_config ");

        return baseDao.findList(sql.toString(), null, PayChnConfigDTO.class);
    }

    public List<PayChnConfigDTO> payChnConfigListTrue() throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select pay_chn_id, pay_chn_name,pay_chn_code, pay_chn_status, ");
        sql.append("       pay_chn_params, pay_source_chn, pay_chn");
        sql.append("  from bm_pay_chn_config ");
        sql.append(" where pay_chn_status = 1 ");

        return baseDao.findList(sql.toString(), null, PayChnConfigDTO.class);
    }

    public void updateChnStatusByPayChnId(long chnID, long chnStatus, long opId) {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update bm_pay_chn_config ");
        sql.append("   set pay_chn_status = :pay_chn_status, pay_chn_id = :pay_chn_id ");
        sql.append(" where pay_chn_id = :pay_chn_id");

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("pay_chn_status", chnStatus);
        params.put("pay_chn_id", chnID);

        baseDao.executeSQL(sql.toString(), params);
    }
}
