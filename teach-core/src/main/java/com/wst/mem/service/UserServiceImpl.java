/*
 * UserServiceImpl.java Created on 2017年9月27日 下午4:58:11
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
package com.wst.mem.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.security.Base64;
import com.hiwi.common.util.security.MD5Util;
import com.wst.mem.dao.UserDao;
import com.wst.service.mem.dto.UserDTO;
import com.wst.service.mem.service.UserService;
import com.wst.service.notify.dto.MailNotifyDTO;
import com.wst.service.notify.service.MailNotifyService;

/**
 * 用户Service实现类
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private MailNotifyService mailNotifyService;

    @Override
    public Page<UserDTO> findPaging(Page<UserDTO> pageParam, UserDTO userDTO) throws Exception {
        return userDao.findPaging(pageParam, userDTO);
    }

    @Override
    public UserDTO getUserById(long userId) throws Exception {
        return userDao.getUserById(userId);
    }

    @Override
    public int updateUser(UserDTO user) throws Exception {
        return userDao.updateUser(user);
    }

    @Override
    public int updateUser(long userId, String realName, String nickName, String resume, String offer) throws Exception {
        return userDao.updateUser(userId, realName, nickName, resume, offer);
    }

    @Override
    public UserDTO getUserByAccount(String loginAccount) throws Exception {
        return userDao.getUserByAccount(loginAccount);
    }

    @Override
    public UserDTO getUserByAccountOfStatus(String loginAccount, long status) throws Exception {
        return userDao.getUserByAccountOfStatus(loginAccount, status);
    }

    @Override
    public ResultDTO<Map<String, Object>> register(UserDTO userDto) throws Exception {
        ResultDTO<Map<String, Object>> result = new ResultDTO<Map<String, Object>>();

        userDto.setRegTime(new Date()); // 注册时间
        userDto.setUserType(1);
        userDto.setSubUserType(1);
        userDto.setUserLevel(1);
        userDto.setPassword(MD5Util.MD5(userDto.getPassword()));
        userDto.setStatus(2);
        userDto.setSex(1);

        // 新增用户
        long userId = userDao.addUser(userDto);

        // 添加邮件参数
        MailNotifyDTO mailNotifyDTO = new MailNotifyDTO();
        mailNotifyDTO.setToAccs(userDto.getEmail());

        String recommendCode = Base64.encode(String.valueOf(userId).getBytes("utf-8"));// Base64加密

        Map<String, Object> bodyParms = new HashMap<String, Object>();
        bodyParms.put("emailAddress", userDto.getEmail());
        bodyParms.put("recommendCode", recommendCode);
        bodyParms.put("roleType", userDto.getRoleType());
        mailNotifyDTO.setBodyParms(bodyParms);

        // 推送注册激活邮件
        this.mailNotifyService.sendRegisterActivationMail(mailNotifyDTO);

        // 用户注册成功,请登录邮箱完成激活
        return result.set(ResultCodeEnum.SUCCESS, "User registration is successful, please login email activation！");
    }

    @Override
    public ResultDTO<String> activate(String userId) throws Exception {
        ResultDTO<String> result = new ResultDTO<String>();
        userId = new String(Base64.decode(userId), "utf-8"); // Base64解密

        UserDTO user = this.userDao.getUserById(Long.parseLong(userId));
        if (user == null) { // 帐号未注册
            return result.set(ResultCodeEnum.ERROR_HANDLE, "Account not registered!");
        }
        // 用户已激活
        if (user.getStatus() == 1L) { // 该邮箱已激活
            return result.set(ResultCodeEnum.ERROR_HANDLE, "This mailbox has been activated!");
        }

        user.setStatus(1L); // 修改用户激活状态
        this.userDao.updateUserStatus(user.getUserId(), user.getStatus());

        return result.set(ResultCodeEnum.SUCCESS, "User activation success, please login!"); // 用户激活成功，请登陆使用！
    }

    @Override
    public int updatePassword(long userId, String password) throws Exception {
        // 密码加密
        password = MD5Util.MD5(password);
        return userDao.updatePassword(userId, password);
    }

    @Override
    public ResultDTO<UserDTO> userLogin(String loginAccount, String password) throws Exception {
        ResultDTO<UserDTO> result = new ResultDTO<UserDTO>();

        UserDTO user = this.userDao.getUserByAccount(loginAccount);

        return result.set(ResultCodeEnum.SUCCESS, "succeed", user);
    }

    @Override
    public UserDTO getUserByDeputyLoginAccount(String deputyLoginAccount) throws Exception {
        return userDao.getUserByDeputyLoginAccount(deputyLoginAccount);
    }

    @Override
    public int updateDeputyLoginAccount(long userId, String deputyLoginAccount, String portraitImgUrl)
            throws Exception {
        return userDao.updateDeputyLoginAccount(userId, deputyLoginAccount, portraitImgUrl);
    }

}
