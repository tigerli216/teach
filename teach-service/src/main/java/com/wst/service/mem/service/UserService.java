/*
 * UserService.java Created on 2017年9月27日 下午4:56:26
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
package com.wst.service.mem.service;

import java.util.Map;

import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.mem.dto.UserDTO;

/**
 * 用户Service
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface UserService {

    /**
     * 获取用户分页数据
     * 
     * @param pageParam
     * @param userDTO
     * @return
     * @throws Exception
     */
    public Page<UserDTO> findPaging(Page<UserDTO> pageParam, UserDTO userDTO) throws Exception;

    /**
     * 通过用户ID获取用户信息
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public UserDTO getUserById(long userId) throws Exception;

    /**
     * 修改用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public int updateUser(UserDTO user) throws Exception;

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
    public int updateUser(long userId, String realName, String nickName, String resume, String offer) throws Exception;

    /**
     * 通过用户登录帐号获取用户信息
     * 
     * @param loginAccount
     * @return
     * @throws Exception
     */
    public UserDTO getUserByAccount(String loginAccount) throws Exception;

    /**
     * 通过用户登录帐号和用户状态获取用户信息
     * 
     * @param loginAccount
     * 
     * @param status
     *            1:有效 2:失效
     * @return
     * @throws Exception
     */
    public UserDTO getUserByAccountOfStatus(String loginAccount, long status) throws Exception;

    /**
     * 邮箱激活
     * 
     * @param loginAccount
     * @return
     * @throws Exception
     */
    public ResultDTO<String> activate(String loginAccount) throws Exception;

    /**
     * 用户注册
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public ResultDTO<Map<String, Object>> register(UserDTO user) throws Exception;

    /**
     * 找回密码
     * 
     * @param userId
     *            用户id
     * @param password
     *            登录密码
     * @return
     * @throws Exception
     */
    public int updatePassword(long userId, String password) throws Exception;

    /**
     * 用户登录和密码校验
     * 
     * @param loginAccount
     * @param password
     * @return
     * @throws Exception
     */
    public ResultDTO<UserDTO> userLogin(String loginAccount, String password) throws Exception;

    /**
     * 根据第三方账号查询用户信息
     * 
     * @param deputyLoginAccount
     * @return
     * @throws Exception
     */
    public UserDTO getUserByDeputyLoginAccount(String deputyLoginAccount) throws Exception;

    /**
     * 修改第三方登录账号
     * 
     * @param userId
     *            用户ID
     * @param deputyLoginAccount
     *            第三方登录账号
     * @param portraitImgUrl
     *            图像
     * @return
     * @throws Exception
     */
    public int updateDeputyLoginAccount(long userId, String deputyLoginAccount, String portraitImgUrl) throws Exception;
}
