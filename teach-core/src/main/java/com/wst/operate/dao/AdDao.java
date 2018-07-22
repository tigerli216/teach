/*
 * AdDao.java Created on 2017年9月26日 下午8:27:56
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
package com.wst.operate.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.operate.dto.AdDTO;

/**
 * 广告营销DAO
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class AdDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取广告分页数据
     * 
     * @param pageParam
     * @param adDTO
     * @return
     * @throws Exception
     */
    public Page<AdDTO> findPaging(Page<AdDTO> pageParam, AdDTO adDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select ad_id, ad_position, ad_pic, ad_url, ");
        sql.append("	   ad_name, weight, create_time, ");
        sql.append("	   modify_time, valid_time, ad_status ");
        sql.append("  from operate_ad ");
        sql.append(" where 1 = 1 ");

        if (adDTO != null) {
            if (adDTO.getAdId() > 0) {
                sql.append(" and ad_id = :adId ");
            }
            if (adDTO.getAdPosition() > 0) {
                sql.append(" and ad_position = :adPosition ");
            }
            if (StringUtils.isNotEmpty(adDTO.getAdName())) {
                sql.append(" and ad_name like concat('%', :adName, '%') ");
            }
            if (adDTO.getAdStatus() > 0) {
                sql.append(" and ad_status = :adStatus ");
            }
            if (adDTO.isHomePage()) {
                sql.append(" and valid_time > now() ");
            }
            if (adDTO.getAdPosition() > 0) {
                sql.append(" and ad_position = :adPosition ");
            }
            if (StringUtils.isNotEmpty(adDTO.getAdUrl())) {
            	sql.append(" and ad_url like concat('%', :adUrl, '%') ");
            }
        }

        sql.append(" order by ad_status asc, weight desc ");

        return baseDao.findPaging(sql.toString(), pageParam, adDTO, AdDTO.class);
    }

    /**
     * 新增广告
     * 
     * @param adDTO
     * @return
     * @throws Exception
     */
    public long save(AdDTO adDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into operate_ad (ad_position, ad_pic, ad_url, ad_name, ");
        sql.append("	   weight, create_time, valid_time, ad_status) ");
        sql.append("values(:adPosition, :adPic, :adUrl, :adName, ");
        sql.append("	   :weight, now(), :validTime, :adStatus) ");

        return baseDao.insertInto(sql.toString(), adDTO, "ad_id");
    }

    /**
     * 修改广告
     * 
     * @param adDTO
     * @return
     * @throws Exception
     */
    public int update(AdDTO adDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update operate_ad ");
        sql.append("   set ad_position = :adPosition, ad_pic = :adPic, ad_name = :adName, ");
        sql.append("	   ad_url = :adUrl, weight = :weight, modify_time = now(), ");
        sql.append("	   valid_time = :validTime, ad_status = :adStatus ");
        sql.append(" where ad_id = :adId ");

        return baseDao.executeSQL(sql.toString(), adDTO);
    }

    /**
     * 根据广告ID获取广告DTO
     * 
     * @param adId
     * @return
     * @throws Exception
     */
    public AdDTO getAdById(long adId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select ad_id, ad_position, ad_pic, ad_url, ad_name, weight, ");
        sql.append("	   create_time, modify_time, valid_time, ad_status ");
        sql.append("  from operate_ad ");
        sql.append(" where ad_id = :adId ");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("adId", adId);

        return baseDao.get(sql.toString(), params, AdDTO.class);
    }

    /**
     * 根据广告ID删除广告
     * 
     * @param adId
     * @return
     * @throws Exception
     */
    public int delete(long adId) throws Exception {
        String sql = "delete from operate_ad where ad_id = :adId";

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("adId", adId);

        return baseDao.executeSQL(sql, params);
    }

    /**
     * 获取广告集合
     * 
     * @return
     * @throws Exception
     */
    public List<AdDTO> findAds() throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select ad_id, ad_position, ad_pic, ad_url, ad_name, weight, ");
        sql.append("	   create_time, modify_time, valid_time, ad_status ");
        sql.append("  from operate_ad ");
        sql.append(" where ad_status = 1 ");
        sql.append("   and valid_time > now() ");

        return baseDao.findList(sql.toString(), null, AdDTO.class);
    }
}
