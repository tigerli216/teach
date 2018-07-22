/*
 * UserDao.java Created on 2017年9月27日 下午5:00:33
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
import com.wst.service.mem.dto.UserDTO;

/**
 * 用户Dao
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Repository
public class UserDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 获取用户信息分页
     * 
     * @param pageParam
     * @param userDTO
     * @return
     * @throws Exception
     */
    public Page<UserDTO> findPaging(Page<UserDTO> pageParam, UserDTO userDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select user_id, login_account, password, bind_mobile, ");
        sql.append("	   email, reg_type, user_type, sub_user_type, user_level, ");
        sql.append("	   pwd_security_level, deputy_login_source, ");
        sql.append("	   deputy_login_account, portrait_img_url, ");
        sql.append("	   nick_name, real_name, sex, country, ");
        sql.append("	   school, grade, finish_time, resume,offer, ");
        sql.append("	   reg_time, recommend_code, pay_password, status ");
        sql.append("  from mem_user ");
        sql.append(" where 1 = 1 ");

        if (userDTO != null) {
            // 用户ID
            if (userDTO.getUserId() > 0) {
                sql.append(" and user_id = :userId ");
            }
            // 登陆账号
            if (StringUtils.isNotEmpty(userDTO.getLoginAccount())) {
                sql.append(" and login_account = :loginAccount ");
            }
            // 绑定手机
            if (StringUtils.isNotEmpty(userDTO.getBindMobile())) {
                sql.append(" and bind_mobile = :bindMobile ");
            }
            // 邮箱
            if (StringUtils.isNotEmpty(userDTO.getEmail())) {
                sql.append(" and email = :email ");
            }
            // 注册方式
            if (userDTO.getRegType() > 0) {
                sql.append(" and reg_type = :regType ");
            }
            // 用户级别
            if (userDTO.getUserLevel() > 0) {
                sql.append(" and user_level = :userLevel ");
            }
            // 昵称
            if (StringUtils.isNotEmpty(userDTO.getNickName())) {
                sql.append(" and nick_name = :nickName ");
            }
            // 真实姓名
            if (StringUtils.isNotEmpty(userDTO.getRealName())) {
                sql.append(" and real_name = :realName ");
            }
            // 性别
            if (userDTO.getSex() > 0) {
                sql.append(" and sex = :sex ");
            }
            // 国家
            if (StringUtils.isNotEmpty(userDTO.getCountry())) {
                sql.append(" and country = :country ");
            }
            // 学校
            if (StringUtils.isNotEmpty(userDTO.getSchool())) {
                sql.append(" and school = :school ");
            }
            // 年级
            if (userDTO.getGrade() > 0) {
                sql.append(" and grade = :grade ");
            }
            // 状态
            if (userDTO.getStatus() > 0) {
                sql.append(" and status = :status ");
            }
        }
        sql.append(" order by login_account");

        return baseDao.findPaging(sql.toString(), pageParam, userDTO, UserDTO.class);
    }

    /**
     * 通过用户ID获取用户信息
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public UserDTO getUserById(long userId) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select user_id, login_account, password, bind_mobile, ");
        sql.append("	   email, reg_type, user_type, sub_user_type, user_level, ");
        sql.append("	   pwd_security_level, deputy_login_source, ");
        sql.append("	   deputy_login_account, portrait_img_url, ");
        sql.append("	   nick_name, real_name, sex, country, ");
        sql.append("	   school, grade, finish_time, resume, offer, ");
        sql.append("	   reg_time, recommend_code, pay_password, status ");
        sql.append("  from mem_user ");
        sql.append(" where user_id = :userId ");

        Map<String, Long> params = new HashMap<>(1);
        params.put("userId", userId);

        return baseDao.get(sql.toString(), params, UserDTO.class);
    }

    /**
     * 修改用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public int updateUser(UserDTO user) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_user ");
        sql.append("   set sex = :sex, grade = :grade, real_name = :realName, ");
        sql.append("	   finish_time = :finishTime, country = :country, ");
        sql.append("       bind_mobile = :bindMobile, school = :school ");
        sql.append(" where user_id = :userId ");

        return baseDao.executeSQL(sql.toString(), user);
    }

    /**
     * 修改用户信息
     * 
     * @param userId
     *            用户ID
     * @param realName
     *            真实姓名
     * @param nickName
     *            昵称
     * @param resume
     *            简历
     * @param offer
     * @return
     * @throws Exception
     */
    public int updateUser(long userId, String realName, String nickName, String resume, String offer) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_user ");
        sql.append("   set real_name = :realName, nick_name = :nickName ");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(resume)) {
            sql.append(" , resume = :resume ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(offer)) {
            sql.append(" , offer = :offer ");
        }
        sql.append("  where user_id = :userId ");

        Map<String, Object> params = new HashMap<>(5);
        params.put("userId", userId);
        params.put("realName", realName);
        params.put("nickName", nickName);
        params.put("resume", resume);
        params.put("offer", offer);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 通过用户登录帐号获取用户信息
     * 
     * @param loginAccount
     * @return
     * @throws Exception
     */
    public UserDTO getUserByAccount(String loginAccount) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select user_id, login_account, password, bind_mobile, ");
        sql.append("	   email, reg_type, user_type, sub_user_type, user_level, ");
        sql.append("	   pwd_security_level, deputy_login_source, ");
        sql.append("	   deputy_login_account, portrait_img_url, ");
        sql.append("	   nick_name, real_name, sex, country, ");
        sql.append("	   school, grade, finish_time, resume,offer, ");
        sql.append("	   reg_time, recommend_code, pay_password, status ");
        sql.append("  from mem_user ");
        sql.append(" where login_account = :loginAccount ");
        sql.append(" order by user_id desc ");

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("loginAccount", loginAccount);

        return baseDao.get(sql.toString(), params, UserDTO.class);
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
    public UserDTO getUserByAccountOfStatus(String loginAccount, long status) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select user_id, login_account, password, bind_mobile, ");
        sql.append("	   email, reg_type, user_type, sub_user_type, user_level, ");
        sql.append("	   pwd_security_level, deputy_login_source, ");
        sql.append("	   deputy_login_account, portrait_img_url, ");
        sql.append("	   nick_name, real_name, sex, country, ");
        sql.append("	   school, grade, finish_time, resume,offer, ");
        sql.append("	   reg_time, recommend_code, pay_password, status ");
        sql.append("  from mem_user ");
        sql.append(" where login_account = :loginAccount ");
        sql.append("   and status = :status ");
        sql.append(" order by user_id desc ");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("loginAccount", loginAccount);
        params.put("status", status);

        return baseDao.get(sql.toString(), params, UserDTO.class);
    }

    /**
     * 新增会员
     * 
     * @param userDTO
     * @return
     * @throws Exception
     */
    public long addUser(UserDTO userDTO) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("insert into mem_user (login_account, password, bind_mobile, email, reg_type, ");
        sql.append("       portrait_img_url, user_type, sub_user_type, user_level, pwd_security_level, ");
        sql.append("       deputy_login_account, deputy_login_source, nick_name, real_name, reg_time, ");
        sql.append("       sex, status, country, school, grade, finish_time) ");
        sql.append("values (:loginAccount, :password, :bindMobile, :email, :regType, ");
        sql.append("       :portraitImgUrl, :userType, :subUserType, :userLevel, :pwdSecurityLevel, ");
        sql.append("       :deputyLoginAccount, :deputyLoginSource, :nickName, :realName, now(), ");
        sql.append("       :sex, :status, :country, :school, :grade, :finishTime) ");

        return baseDao.insertInto(sql.toString(), userDTO, "user_id");
    }

    /**
     * 修改用户状态
     * 
     * @param userId
     *            用户ID
     * @param status
     *            状态，1-有效；2-失效
     * @throws Exception
     */
    public void updateUserStatus(long userId, long status) throws Exception {
        String sql = "update mem_user set status = :status where user_id = :userId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("userId", userId);
        params.put("status", status);

        baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 修改用户级别
     * 
     * @param userId
     *            用户ID
     * @param userLevel
     *            级别，1-有效；2-失效
     * @throws Exception
     */
    public void updateUserLevel(long userId, long userLevel) throws Exception {
        String sql = "update mem_user set user_level = :userLevel where user_id = :userId";

        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("userId", userId);
        params.put("userLevel", userLevel);

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
    public int updatePassword(long userId, String password) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_user ");
        sql.append("   set password = :password ");
        sql.append(" where user_id = :userId");

        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("password", password);
        params.put("userId", userId);

        return baseDao.executeSQL(sql.toString(), params);
    }

    /**
     * 根据第三方账号查询用户信息
     * 
     * @param deputyLoginAccount
     * @return
     * @throws Exception
     */
    public UserDTO getUserByDeputyLoginAccount(String deputyLoginAccount) throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("select user_id, login_account, password, bind_mobile, ");
        sql.append("       email, reg_type, user_type, sub_user_type, user_level, ");
        sql.append("       pwd_security_level, deputy_login_source, ");
        sql.append("       deputy_login_account, portrait_img_url, ");
        sql.append("       nick_name, real_name, sex, country, ");
        sql.append("       school, grade, finish_time, resume,offer, ");
        sql.append("       reg_time, recommend_code, pay_password, status ");
        sql.append("  from mem_user ");
        sql.append(" where deputy_login_account = :deputyLoginAccount ");
        sql.append("   and status = 1 ");
        sql.append(" order by user_id desc ");

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("deputyLoginAccount", deputyLoginAccount);

        return baseDao.get(sql.toString(), params, UserDTO.class);
    }

    /**
     * 修改第三方登录账号
     * 
     * @param userId
     *            用户ID
     * @param deputyLoginAccount
     *            第三方登录账号
     * @return
     * @throws Exception
     */
    public int updateDeputyLoginAccount(long userId, String deputyLoginAccount, String portraitImgUrl)
            throws Exception {
        StringBuilder sql = new StringBuilder();

        sql.append("update mem_user ");
        sql.append("   set deputy_login_account = :deputyLoginAccount, deputy_login_source = 1");
        if (StringUtils.isNotEmpty(portraitImgUrl)) {
            sql.append(" , portrait_img_url = :portraitImgUrl ");
        }
        sql.append(" where user_id = :userId");

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("deputyLoginAccount", deputyLoginAccount);
        params.put("portraitImgUrl", portraitImgUrl);
        params.put("userId", userId);

        return baseDao.executeSQL(sql.toString(), params);
    }

}
