/*
 * UserHelper.java Created on 2016年10月10日 下午8:51:02
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
package com.wst.base.helper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.session.SessionIdGenerator;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.security.MD5Util;
import com.wst.base.dto.CommonParamsDTO;
import com.wst.service.mem.dto.TutorDTO;
import com.wst.service.mem.dto.UserDTO;

/**
 * 用户信息管理帮助工具类
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
public class UserHelper {

    /**
     * 设置用户信息(学生)
     * 
     * @param dto
     * @param userDto
     */
    public static void setUser(CommonParamsDTO dto, UserDTO userDto) {
        if (null != dto.getAppSessionId()) {
            RedisClient.set(dto.getAppSessionId(), JsonUtils.getJsonWithName(userDto));
        }
        HttpSession session = dto.getRequest().getSession();
        session.setAttribute("userDTO", userDto);
    }

    /**
     * 设置用户信息(导师)
     * 
     * @param dto
     * @param tutorDto
     */
    public static void setTutor(CommonParamsDTO dto, TutorDTO tutorDto) {
        if (null != dto.getAppSessionId()) {
            RedisClient.set(dto.getAppSessionId(), JsonUtils.getJsonWithName(tutorDto));
        }

        HttpSession session = dto.getRequest().getSession();
        session.setAttribute("tutorDTO", tutorDto);
    }

    /**
     * 
     * 退出登录 将用户信息从redis中移除
     * 
     * @param appSessionId
     * @throws Exception
     */
    public static void delUserOrTutor(CommonParamsDTO dto) throws Exception {
        if (null != dto.getAppSessionId()) {
            RedisClient.del(dto.getAppSessionId());
        }
        HttpSession session = dto.getRequest().getSession();
        session.removeAttribute("userDTO");
        session.removeAttribute("tutorDTO");
    }
    
    /**
     * 删除用户session
     * @param dto
     * @throws Exception
     */
    public static void delUserSession(CommonParamsDTO dto) throws Exception {
        HttpSession session = dto.getRequest().getSession();
        session.removeAttribute("userDTO");
        session.removeAttribute("tutorDTO");
    }

    /**
     * 获取用户信息(学生)
     * 
     * @param dto
     * @return
     * @throws Exception
     */
    public static UserDTO getUserDTO(CommonParamsDTO dto) throws Exception {
        // 从Redis中获取
        if (null != dto.getAppSessionId()) {
            // 是导师的appSessionId返回空
            if (dto.getAppSessionId().endsWith("T")) {
                return null;
            }
            String userStr = RedisClient.get(dto.getAppSessionId());
            return JsonUtils.parseJson(userStr, UserDTO.class);
        }
        // 从session中获取
        HttpSession session = dto.getRequest().getSession();
        Object user = session.getAttribute("userDTO");
        if (user != null) {
            UserDTO userDTO = (UserDTO) user;
            return userDTO;
        }
        return null;
    }

    /**
     * 获取用户信息(导师)
     * 
     * @param dto
     * @return
     * @throws Exception
     */
    public static TutorDTO getTutorDTO(CommonParamsDTO dto) throws Exception {
        // 从Redis中获取
        if (null != dto.getAppSessionId()) {
            // 是学生的appSessionId返回空
            if (dto.getAppSessionId().endsWith("S")) {
                return null;
            }
            String userStr = RedisClient.get(dto.getAppSessionId());
            return JsonUtils.parseJson(userStr, TutorDTO.class);
        }
        // 从session中获取
        HttpSession session = dto.getRequest().getSession();
        Object tutor = session.getAttribute("tutorDTO");
        if (tutor != null) {
            TutorDTO tutorDTO = (TutorDTO) tutor;
            String appSessionId = SessionIdGenerator.generateSessionId(String.valueOf(tutorDTO.getTutorId())) + "_T";
            String tutorStr = RedisClient.get(appSessionId);
            if (StringUtils.isNotEmpty(tutorStr)) {
                return JsonUtils.parseJson(tutorStr, TutorDTO.class);
            }
            return tutorDTO;
        }
        return null;
    }

    /**
     * 将用户信息存入session
     * 
     * @param request
     * @param UserDTO
     * @return
     */
    public static String setUserSession(HttpServletRequest request, UserDTO dto) {
        HttpSession session = request.getSession();
        session.setAttribute("userDTO", dto);
        return session.getId();
    }

    /**
     * 将导师信息存入session
     * 
     * @param request
     * @param TutorDTO
     * @return
     */
    public static String setTutorSession(HttpServletRequest request, TutorDTO tutorDto) {
        HttpSession session = request.getSession();
        session.setAttribute("tutorDTO", tutorDto);
        return session.getId();
    }
    
    /**
     * 获取用户账号（腾讯云账号）
     * @param dto
     * @return
     * @throws Exception
     */
    public static String getUserAccount(CommonParamsDTO dto) throws Exception {
        String appSessonId = dto.getAppSessionId();
        if (StringUtils.isNotEmpty(appSessonId) && appSessonId.endsWith("T")) {
            // 导师端
            TutorDTO tutorDTO = UserHelper.getTutorDTO(dto);
            if (tutorDTO != null) {
                return tutorDTO.getLoginAccount() + "_T"; 
            }
        } else {
            // 学生端
            UserDTO userDTO = UserHelper.getUserDTO(dto);
            if (userDTO != null) {
                return userDTO.getLoginAccount() + "_S";
            }
        }
        return null;
    }

    // ====================== 签名 =====================
    /**
     * 生成签名
     * 
     * @param appSessionId
     * @return
     * @throws Exception
     */
    public static String generateSecret(String appSessionId) throws Exception {
        // 生成密钥
        String secret = System.currentTimeMillis() + appSessionId;
        secret = MD5Util.MD5(secret);

        RedisClient.set(appSessionId + "_secret", secret);

        return secret;
    }

    /**
     * 校验签名是否合法
     * 
     * @param requestParams
     * @return
     * @throws Exception
     */
    public static boolean checkSign(JSONObject requestParams) throws Exception {
        String appSessionId = requestParams.getString("appSessionId");
        String sign = requestParams.getString("sign");
        String secret = "";
        if (StringUtils.isBlank(appSessionId)) {
            secret = AppConstant.KEY;
        } else {
            secret = RedisClient.get(appSessionId + "_secret");
            if (StringUtils.isBlank(secret))
                return false;
        }
        if (StringUtils.isBlank(sign))
            return false;

        Map<String, Object> paramsMap = new TreeMap<>();
        Iterator<Entry<String, Object>> iter = requestParams.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, Object> entry = iter.next();
            String key = entry.getKey();
            if (key.equals("appSessionId") || key.equals("sign")) {
                continue;
            }
            paramsMap.put(key, entry.getValue());
        }

        StringBuilder signBody = new StringBuilder();
        Iterator<Entry<String, Object>> mapIter = paramsMap.entrySet().iterator();
        while (mapIter.hasNext()) {
            Entry<String, Object> entry = mapIter.next();
            signBody.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        String signBodyStr = secret + signBody.substring(0, signBody.length() - 1) + secret;
        if (sign.equals(MD5Util.MD5(signBodyStr)))
            return true;

        return false;
    }
}
