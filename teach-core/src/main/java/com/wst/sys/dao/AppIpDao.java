/*
 * AppIpDao.java Created on 2017年3月29日 上午11:30:14
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
package com.wst.sys.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.CommonFuntions;
import com.hiwi.common.util.StringUtils;
import com.wst.service.sys.dto.AppIpDTO;

/**
 * 第三方应用IP配置Dao
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
@Repository
public class AppIpDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 新增第三方应用IP配置
     * 
     * @param appIpDTO
     * @throws Exception
     */
    public void save(AppIpDTO appIpDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into sys_app_ip (accs_id, ip_addr, ip_type, create_time, create_op, status) ");
        sql.append("values(:accsId, :ipAddr, :ipType, now(), :createOp, :status)");

        baseDao.insertInto(sql.toString(), appIpDTO, "config_id");
    }

    /**
     * 更新第三方应用IP配置
     * 
     * @param appIpDTO
     * @throws Exception
     */
    public void update(AppIpDTO appIpDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_app_ip ");
        sql.append("   set ip_addr = :ipAddr, ip_type = :ipType, update_time = now(), ");
        sql.append("       update_op = :updateOp, status = :status ");
        sql.append(" where config_id = :configId");

        baseDao.executeSQL(sql.toString(), appIpDTO);
    }

    /**
     * 第三方应用IP配置信息分页查询
     * 
     * @param pageParam
     * @param appIpDTO
     * @return
     * @throws Exception
     */
    public Page<AppIpDTO> findPage(Page<AppIpDTO> pageParam, AppIpDTO appIpDTO) throws Exception {
        if (null == pageParam && null == appIpDTO) {
            return null;
        }
        StringBuilder sql = new StringBuilder();

        sql.append("select sso.op_login_name createOpName, so.op_login_name updateOpName, ");
        sql.append("       sai.config_id, sai.accs_id, sai.ip_addr, sai.ip_type, sai.create_time, ");
        sql.append("       sai.create_op, sai.update_time, sai.update_op, sai.status ");
        sql.append("  from sys_app_ip sai");
        sql.append("  left join sso_operator sso on sso.op_id = sai.create_op ");
        sql.append("  left join sso_operator so on so.op_id = sai.update_op ");
        sql.append(" where 1 = 1 ");

        if (StringUtils.isNotEmpty(appIpDTO.getCreateOpName())) {
            sql.append(" and sso.op_login_name = :createOpName ");
        }
        if (StringUtils.isNotEmpty(appIpDTO.getUpdateOpName())) {
            sql.append(" and so.op_login_name = :updateOpName ");
        }
        if (appIpDTO.getStatus() > 0) {
            sql.append(" and sai.status = :status ");
        }
        if (appIpDTO.getAccsId() > 0) {
            sql.append(" and sai.accs_id = :accsId ");
        }
        if (appIpDTO.getIpType() > 0) {
            sql.append(" and sai.ip_type = :ipType ");
        }
        if (StringUtils.isNotEmpty(appIpDTO.getIpAddr())) {
            sql.append(" and sai.ip_addr = :ipAddr ");
        }
        if (CommonFuntions.isNotEmptyObject(appIpDTO.getCreateTime())) {
            sql.append(" and sai.create_time >= :createTime ");
        }

        return baseDao.findPaging(sql.toString(), pageParam, appIpDTO, AppIpDTO.class);
    }

    /**
     * 根据配置ID获取所有应用IP配置
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public AppIpDTO getAllIpByConfigId(long configId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select sai.config_id, sai.accs_id, sai.ip_addr, sai.ip_type, sai.create_time, ");
        sql.append("       sai.create_op, sai.update_time, sai.update_op, sai.status ");
        sql.append("  from sys_app_ip sai");
        sql.append(" where config_id = :configId");

        Map<String, Long> param = new HashMap<String, Long>(1);
        param.put("configId", configId);

        return baseDao.get(sql.toString(), param, AppIpDTO.class);
    }
}
