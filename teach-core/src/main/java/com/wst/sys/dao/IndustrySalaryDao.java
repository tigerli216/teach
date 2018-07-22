/*
 * IndustrySalaryDao.java Created on 2017年9月26日 上午11:57:22
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
import com.wst.service.sys.dto.IndustrySalaryDTO;

/**
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class IndustrySalaryDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 新增导师行业薪资配置
     * 
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public long addIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into sys_industry_salary(industry_id, base_salary, ");
        sql.append("	   create_time, create_op, status) ");
        sql.append(" values (:industryId, :baseSalary,");
        sql.append("	    now(), :createOp, :status) ");

        return baseDao.insertInto(sql.toString(), industrySalaryDTO, "config_id");
    }

    /**
     * 修改
     * 
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public int updateIndustrySalary(IndustrySalaryDTO industrySalaryDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update sys_industry_salary ");
        sql.append("   set base_salary = :baseSalary, modify_time = now(),");
        sql.append("	   modify_op = :modifyOp, status = :status ");
        sql.append(" where config_id = :configId ");

        return baseDao.executeSQL(sql.toString(), industrySalaryDTO);
    }

    /**
     * 通过行业ID获取导师行业薪资配置
     * 
     * @param industryId
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalaryByIndustryId(long industryId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select config_id, industry_id, base_salary, status, ");
        sql.append("	   create_time, create_op, modify_time, modify_op ");
        sql.append("  from sys_industry_salary ");
        sql.append(" where industry_id = :industryId ");
        sql.append("   and status = 1 ");

        Map<String, Long> params = new HashMap<>(1);
        params.put("industryId", industryId);

        return baseDao.get(sql.toString(), params, IndustrySalaryDTO.class);
    }

    /**
     * 获取导师行业薪资配置分页
     * 
     * @param pageParam
     * @param industrySalaryDTO
     * @return
     * @throws Exception
     */
    public Page<IndustrySalaryDTO> findPaging(Page<IndustrySalaryDTO> pageParam, IndustrySalaryDTO industrySalaryDTO)
            throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select ins.config_id, ins.industry_id, i.industry_name, ");
        sql.append("	   ins.base_salary, ins.status, ins.create_time, ");
        sql.append("	   ins.create_op, ins.modify_time, ins.modify_op ");
        sql.append("  from sys_industry_salary ins ");
        sql.append("  join sys_industry i on ins.industry_id = i.industry_id ");
        sql.append(" where 1 = 1 ");

        if (industrySalaryDTO != null) {
            if (industrySalaryDTO.getConfigId() > 0) {
                sql.append(" and ins.config_id = :configId ");
            }
            if (industrySalaryDTO.getIndustryId() > 0) {
                sql.append(" and ins.industry_id = :industryId ");
            }
            if (industrySalaryDTO.getCreateOp() > 0) {
                sql.append(" and ins.create_op = :createOp ");
            }
            if (industrySalaryDTO.getModifyOp() > 0) {
                sql.append(" and ins.modify_op = :modifyOp ");
            }
            if (industrySalaryDTO.getStatus() > 0) {
                sql.append(" and ins.status = :status ");
            }
        }

        sql.append(" order by ins.config_id desc ");

        return baseDao.findPaging(sql.toString(), pageParam, industrySalaryDTO, IndustrySalaryDTO.class);
    }

    /**
     * 通过配置ID删除导师行业薪资配置
     * 
     * @param configId
     * @return
     * @throws Exception
     */
    public int deleteByConfigId(long configId) throws Exception {
        String sql = "delete from sys_industry_salary where config_id = :configId";

        Map<String, Long> params = new HashMap<>(1);
        params.put("configId", configId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 通过配置ID获取导师行业薪资配置
     * 
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalaryByConfigId(long configId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select ins.config_id, ins.industry_id, i.industry_name, ");
        sql.append("	   ins.base_salary, ins.status, ins.create_time, ");
        sql.append("	   ins.create_op, ins.modify_time, ins.modify_op ");
        sql.append("  from sys_industry_salary ins  ");
        sql.append("  join sys_industry i on ins.industry_id = i.industry_id ");
        sql.append(" where ins.config_id = :configId");

        Map<String, Long> params = new HashMap<>(1);
        params.put("configId", configId);

        return baseDao.get(sql.toString(), params, IndustrySalaryDTO.class);
    }

    /**
     * 通过行业编码获取导师行业薪资配置
     * 
     * @param industryCode
     *            行业编码
     * @return
     * @throws Exception
     */
    public IndustrySalaryDTO getIndustrySalaryByIndustryCode(String industryCode) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select sis.config_id, sis.industry_id, sis.base_salary, sis.status, ");
        sql.append("       sis.create_time, sis.create_op, sis.modify_time, sis.modify_op ");
        sql.append("  from sys_industry_salary sis");
        sql.append(" inner join sys_industry si on si.industry_id = sis.industry_id");
        sql.append(" where sis.status = 1 ");
        sql.append("   and si.industry_code = :industryCode ");

        Map<String, String> params = new HashMap<>(1);
        params.put("industryCode", industryCode);

        return baseDao.get(sql.toString(), params, IndustrySalaryDTO.class);
    }
}
