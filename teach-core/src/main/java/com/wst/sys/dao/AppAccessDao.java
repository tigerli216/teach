/*
 * AppAccessDao.java Created on 2017年3月25日 下午2:14:57
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.sys.dto.AppAccessDTO;
import com.wst.service.sys.dto.AppIpDTO;

/**
 * 第三方应用接入Dao
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@Repository
public class AppAccessDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 根据appId获取接入信息
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_app_access_get_appId")
    public AppAccessDTO getByAppId(String appId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select accs_id, company, legal_person, address, ");
        sql.append("       contact_name, contact_mobile, app_name, ");
        sql.append("       app_id, app_secret, create_time, op_id, status ");
        sql.append("  from sys_app_access");
        sql.append(" where status = 1 ");
        sql.append("   and app_id = :appId ");

        Map<String, String> params = new HashMap<>(1);
        params.put("appId", appId);

        return baseDao.get(sql.toString(), params, AppAccessDTO.class);
    }

    /**
     * 获取白名单IP列表
     * 
     * @param accsId
     * @return
     * @throws Exception
     */
    @Cacheable(value = "cache_app_access_white_ip_list")
    public List<AppIpDTO> findWhiteIpList(long accsId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select config_id, accs_id, ip_addr, ip_type, create_time, ");
        sql.append("       create_op, update_time, update_op, status");
        sql.append("  from sys_app_ip");
        sql.append(" where status = 1 ");
        sql.append("   and ip_type = 1 ");
        sql.append("   and accs_id = :accsId ");

        Map<String, Long> params = new HashMap<>(1);
        params.put("accsId", accsId);

        return baseDao.findList(sql.toString(), params, AppIpDTO.class);
    }

    /**
     * 保存第三方应用接入配置
     * 
     * @param appAccessDTO
     * @throws Exception
     */
    public void saveAccess(AppAccessDTO appAccessDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into sys_app_access (company, legal_person, address, contact_name, ");
        sql.append("       contact_mobile, app_name, app_id, app_secret, create_time, op_id, status) ");
        sql.append("values(:company, :legalPerson, :address, :contactName, ");
        sql.append("       :contactMobile, :appName, :appId, :appSecret, now(), :opId, :status )");

        baseDao.insertInto(sql.toString(), appAccessDTO, "accs_id");
    }

    /**
     * 第三方应用接入配置状态变更
     * 
     * @param accsId
     * @param status
     * @param opId
     * @throws Exception
     */
    public void upStatus(long accsId, long status, long opId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_app_access ");
        sql.append("   set status = :status, op_id = :opId ");
        sql.append(" where accs_id = :accsId");

        Map<String, Long> param = new HashMap<String, Long>(3);
        param.put("accsId", accsId);
        param.put("status", status);
        param.put("opId", opId);

        baseDao.executeSQL(sql.toString(), param);
    }

    /**
     * 更新第三方应用接入配置
     * 
     * @param appAccessDTO
     * @throws Exception
     */
    public void updateAppAccess(AppAccessDTO appAccessDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_app_access ");
        sql.append("   set company = :company, legal_person = :legalPerson, ");
        sql.append("       address = :address, contact_name = :contactName, ");
        sql.append("       contact_mobile = :contactMobile, app_name = :appName, ");
        sql.append("       op_id = :opId, status = :status ");
        sql.append(" where accs_id = :accsId ");

        baseDao.executeSQL(sql.toString(), appAccessDTO);
    }

    /**
     * 更新应用秘钥
     * 
     * @param accsId
     * @param appSecret
     *            秘钥
     * @param opId
     * @throws Exception
     */
    public void upAppSecret(long accsId, String appSecret, long opId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_app_access ");
        sql.append("   set app_secret = :appSecret, op_id = :opId ");
        sql.append(" where accs_id = :accsId");

        Map<String, Object> param = new HashMap<String, Object>(3);
        param.put("accsId", accsId);
        param.put("appSecret", appSecret);
        param.put("opId", opId);

        baseDao.executeSQL(sql.toString(), param);
    }

    /**
     * 第三方应用接入配置信息分页查询
     * 
     * @param pageParam
     * @param appAccess
     * @return
     * @throws Exception
     */
    public Page<AppAccessDTO> findPage(Page<AppAccessDTO> pageParam, AppAccessDTO appAccess) throws Exception {
        if (null == pageParam && null == appAccess) {
            return null;
        }
        StringBuilder sql = new StringBuilder();

        sql.append("select so.op_login_name opLoginName, so.op_name opName, saa.accs_id, ");
        sql.append("       saa.company, saa.legal_person, saa.address, saa.contact_name, ");
        sql.append("       saa.contact_mobile, saa.app_name, saa.app_id, saa.app_secret, ");
        sql.append("       saa.create_time, saa.op_id, saa.status ");
        sql.append("  from sys_app_access saa ");
        sql.append("  left join sso_operator so on so.op_id = saa.op_id ");
        sql.append(" where 1 = 1 ");

        if (StringUtils.isNotEmpty(appAccess.getCompany())) {
            sql.append(" and saa.company like concat('%', :company, '%') ");
        }
        if (StringUtils.isNotEmpty(appAccess.getOpLoginName())) {
            sql.append(" and so.op_login_name = :opLoginName ");
        }
        if (StringUtils.isNotEmpty(appAccess.getOpName())) {
            sql.append(" and so.op_name = :opName ");
        }
        if (StringUtils.isNotEmpty(appAccess.getLegalPerson())) {
            sql.append(" and saa.legal_person = :legalPerson ");
        }
        if (StringUtils.isNotEmpty(appAccess.getContactMobile())) {
            sql.append(" and saa.contact_mobile = :contactMobile ");
        }
        if (StringUtils.isNotEmpty(appAccess.getContactName())) {
            sql.append(" and saa.contact_name = :contactName ");
        }
        if (StringUtils.isNotEmpty(appAccess.getAppName())) {
            sql.append(" and saa.app_name like concat('%', :appName, '%')");
        }
        if (StringUtils.isNotEmpty(appAccess.getAppId())) {
            sql.append(" and saa.app_id = :appId ");
        }
        if (StringUtils.isNotEmpty(appAccess.getAppSecret())) {
            sql.append(" and saa.app_secret = :appSecret ");
        }
        if (appAccess.getStatus() > 0) {
            sql.append(" and saa.status = :status ");
        }
        sql.append(" order by saa.accs_id desc ");

        return baseDao.findPaging(sql.toString(), pageParam, appAccess, AppAccessDTO.class);
    }

    /**
     * 根据Id获取第三方应用接入配置信息（包括有效和禁用状态）
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    public AppAccessDTO getAllById(long accsId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select accs_id, company, legal_person, address, ");
        sql.append("       contact_name, contact_mobile, app_name, app_id, ");
        sql.append("       app_secret, create_time, op_id, status ");
        sql.append("  from sys_app_access");
        sql.append(" where accs_id = :accsId");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("accsId", accsId);

        return baseDao.get(sql.toString(), params, AppAccessDTO.class);
    }

    /**
     * 根据appId获取第三方应用接入配置信息（包括有效和禁用状态）
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    public AppAccessDTO getAllByAppId(String appId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select accs_id, company, legal_person, address, ");
        sql.append("       contact_name, contact_mobile, app_name, app_id, ");
        sql.append("       app_secret, create_time, op_id, status ");
        sql.append("  from sys_app_access ");
        sql.append(" where app_id = :appId");

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("appId", appId);

        return baseDao.get(sql.toString(), params, AppAccessDTO.class);
    }
}
