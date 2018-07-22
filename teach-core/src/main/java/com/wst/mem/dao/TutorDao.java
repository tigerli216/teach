/*
 * TutorDao.java Created on 2017年9月27日 下午7:53:22
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
package com.wst.mem.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.StringUtils;
import com.wst.service.mem.dto.TutorDTO;

/**
 * 导师Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class TutorDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取导师分页数据
     * 
     * @param pageParam
     * @param tutorDTO
     * @return
     * @throws Exception
     */
    public Page<TutorDTO> findPaging(Page<TutorDTO> pageParam, TutorDTO tutorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select tutor_id, login_account, password, bind_mobile, email, ");
        sql.append("	   reg_type, nick_name, portrait_img_url, tutor_type, ");
        sql.append("	   linkedin_uri, graduate_school, newly_job, industry_id, ");
        sql.append("	   industry_name, tutor_remark, real_name, position, ");
        sql.append("       receive_type, receive_account, sex, country, tutor_salary, ");
        sql.append("	   contract_url, tutor_record, create_time, examine_status, ");
        sql.append("       examine_time, examine_op, valid_status ");
        sql.append("  from mem_tutor ");
        sql.append(" where 1 = 1");

        if (tutorDTO != null) {
            if (tutorDTO.getTutorId() > 0) {
                sql.append(" and tutor_id = :tutorId ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getLoginAccount())) {
                sql.append(" and login_account = :loginAccount ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getBindMobile())) {
                sql.append(" and bind_mobile = :bindMobile ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getEmail())) {
                sql.append(" and email = :email ");
            }
            if (tutorDTO.getRegType() > 0) {
                sql.append(" and reg_type = :regType ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getNickName())) {
                sql.append(" and nick_name = :nickName ");
            }
            if (tutorDTO.getIndustryId() > 0) {
                sql.append(" and industry_id = :industryId ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getReceiveAccount())) {
                sql.append(" and receive_account = :receiveAccount ");
            }
            if (tutorDTO.getExamineStatus() > 0) {
                sql.append(" and examine_status = :examineStatus ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getRealName())) {
                sql.append(" and real_name = :realName ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getPosition())) {
                sql.append(" and position = :position ");
            }
            if (tutorDTO.getSex() > 0) {
                sql.append(" and sex = :sex ");
            }
            if (StringUtils.isNotEmpty(tutorDTO.getCountry())) {
                sql.append(" and country = :country ");
            }
            if (tutorDTO.getValidStatus() > 0) {
                sql.append(" and valid_status = :validStatus ");
            }
            if (tutorDTO.getBeginTime() != null) {
            	sql.append(" and create_time > :beginTime ");
            }
            if (tutorDTO.getEndTime() != null) {
                sql.append(" and create_time < :endTime ");
            }
        }

        sql.append(" order by convert(real_name USING gbk)");

        return baseDao.findPaging(sql.toString(), pageParam, tutorDTO, TutorDTO.class);
    }

    /**
     * 通过导师ID获取导师信息
     * 
     * @param tutorId
     * @return
     * @throws Exception
     */
    public TutorDTO getTutorById(long tutorId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select tutor_id, login_account, password, bind_mobile, email, ");
        sql.append("       reg_type, nick_name, portrait_img_url, tutor_type, ");
        sql.append("       linkedin_uri, graduate_school, newly_job, industry_id, ");
        sql.append("       industry_name, tutor_remark, real_name, position, ");
        sql.append("       receive_type, receive_account, sex, country, tutor_salary, ");
        sql.append("       contract_url, tutor_record, create_time, examine_status, ");
        sql.append("       examine_time, examine_op, valid_status ");
        sql.append("  from mem_tutor ");
        sql.append(" where tutor_id = :tutorId");

        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("tutorId", tutorId);

        return baseDao.get(sql.toString(), params, TutorDTO.class);
    }

    /**
     * 更新审核状态
     * 
     * @param examineStatus
     * @return
     * @throws Exception
     */
    public int updateExamineStatus(long tutorId, long examineStatus) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_tutor ");
        sql.append("   set examine_status = :examineStatus ");
        sql.append(" where tutor_id = :tutorId ");

        Map<String, Long> params = new HashMap<>(2);
        params.put("examineStatus", examineStatus);
        params.put("tutorId", tutorId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 导师薪资调整
     * 
     * @param tutorId
     * @param tutorSalary
     * @return
     * @throws Exception
     */
    public int updateTutorSalary(long tutorId, long tutorSalary) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_tutor ");
        sql.append("   set tutor_salary = :tutorSalary ");
        sql.append(" where tutor_id = :tutorId ");

        Map<String, Long> params = new HashMap<>(2);
        params.put("tutorSalary", tutorSalary);
        params.put("tutorId", tutorId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 通过用户登录帐号获取用户信息
     * 
     * @param loginAccount
     * @return
     * @throws Exception
     */
    public TutorDTO getTutorByAccount(String loginAccount) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select tutor_id, login_account, password, bind_mobile, email, ");
        sql.append("       reg_type, nick_name, portrait_img_url, tutor_type, ");
        sql.append("       linkedin_uri, graduate_school, newly_job, industry_id, ");
        sql.append("       industry_name, tutor_remark, real_name, position, ");
        sql.append("       receive_type, receive_account, sex, country, tutor_salary, ");
        sql.append("       contract_url, tutor_record, create_time, examine_status, ");
        sql.append("       examine_time, examine_op, valid_status ");
        sql.append("  from mem_tutor ");
        sql.append(" where login_account = :loginAccount ");
        sql.append(" order by tutor_id desc ");

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("loginAccount", loginAccount);

        return baseDao.get(sql.toString(), params, TutorDTO.class);
    }

    /**
     * 通过用户登录帐号和用户状态获取用户信息
     * 
     * @param loginAccount
     * 
     * @param status
     *            1:有效 2：失效
     * @return
     * @throws Exception
     */
    public TutorDTO getTutorByAccountOfStatus(String loginAccount, long validStatus) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select tutor_id, login_account, password, bind_mobile, email, ");
        sql.append("       reg_type, nick_name, portrait_img_url, tutor_type, ");
        sql.append("       linkedin_uri, graduate_school, newly_job, industry_id, ");
        sql.append("       industry_name, tutor_remark, real_name, position, ");
        sql.append("       receive_type, receive_account, sex, country, tutor_salary, ");
        sql.append("       contract_url, tutor_record, create_time, examine_status, ");
        sql.append("       examine_time, examine_op, valid_status ");
        sql.append("  from mem_tutor ");
        sql.append(" where login_account = :loginAccount ");
        sql.append("   and valid_status = :validStatus ");
        sql.append(" order by tutor_id desc ");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("loginAccount", loginAccount);
        params.put("validStatus", validStatus);

        return baseDao.get(sql.toString(), params, TutorDTO.class);
    }

    /**
     * 新增导师
     * 
     * @return
     * @throws Exception
     */
    public long addTutor(TutorDTO tutor) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into mem_tutor ( ");
        sql.append("	login_account, password, bind_mobile, email, reg_type, ");
        sql.append("	nick_name, portrait_img_url, tutor_type, linkedin_uri, graduate_school, ");
        sql.append("	newly_job, industry_id, industry_name, tutor_remark, real_name, position, receive_type, ");
        sql.append("	receive_account, sex, country, tutor_salary, contract_url, tutor_record, ");
        sql.append("	examine_status, create_time, valid_status) ");
        sql.append("values ( ");
        sql.append("	:loginAccount, :password, :bindMobile, :email, :regType, ");
        sql.append("	:nickName, :portraitImgUrl, :tutorType, :linkedinUri, :graduateSchool, ");
        sql.append("	:newlyJob, :industryId, :industryName, :tutorRemark, :realName, :position, :receiveType, ");
        sql.append("	:receiveAccount, :sex, :country, :tutorSalary, :contractUrl, :tutorRecord, ");
        sql.append("	:examineStatus, :createTime, :validStatus ) ");

        return baseDao.insertInto(sql.toString(), tutor, "tutor_id");
    }

    /**
     * 修改用户状态
     * 
     * @param tutorId
     *            用户ID
     * @param validStatus
     *            状态，1-有效；2-失效
     * @throws Exception
     */
    public void updateTutorStatus(long tutorId, long validStatus) throws Exception {
        String sql = "update mem_tutor set valid_status = :validStatus where tutor_id = :tutorId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("tutorId", tutorId);
        params.put("validStatus", validStatus);

        baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 修改登录密码
     * 
     * @param loginAccount
     * @param payPassword
     * @return
     * @throws Exception
     */
    public int updatePassword(long tutorId, String password) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_tutor ");
        sql.append("   set password = :password ");
        sql.append(" where tutor_id = :tutorId");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("password", password);
        params.put("tutorId", tutorId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 修改导师个人信息
     * 
     * @param tutor
     * @return
     * @throws Exception
     */
    public int updateTutor(TutorDTO tutorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_tutor ");
        sql.append("   set receive_account = :receiveAccount, sex = :sex, ");
        sql.append("	   country = :country, bind_mobile = :bindMobile, ");
        sql.append("	   linkedin_uri = :linkedinUri, graduate_school = :graduateSchool, ");
        sql.append("       newly_job = :newlyJob, real_name = :realName, ");
        sql.append("	   position = :position, receive_type = :receiveType ");
        sql.append(" where tutor_id = :tutorId ");

        return baseDao.executeSQL(sql.toString(), tutorDTO);
    }

    /**
     * 修改导师信息后台
     * 
     * @param tutorDTO
     * @return
     * @throws Exception
     */
    public int upTutor(TutorDTO tutorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_tutor ");
        sql.append("   set tutor_remark = :tutorRemark, tutor_salary = :tutorSalary, ");
        sql.append("	   tutor_record = :tutorRecord ");
        if (StringUtils.isNotEmpty(tutorDTO.getContractUrl())) {
            sql.append("   , contract_url = :contractUrl ");
        }
        sql.append(" where tutor_id = :tutorId ");

        return baseDao.executeSQL(sql.toString(), tutorDTO);
    }

    /**
     * 修改导师头像地址
     * 
     * @param tutorId
     * @param imgUrl
     * @return
     * @throws Exception
     */
    public long upTutorPortrait(long tutorId, String imgUrl) throws Exception {
        String sql = "update mem_tutor set portrait_img_url = :imgUrl where tutor_id = :tutorId";

        Map<String, Object> params = new HashMap<>(2);
        params.put("tutorId", tutorId);
        params.put("imgUrl", imgUrl);

        return baseDao.executeSQL(sql, params);
    }

}
