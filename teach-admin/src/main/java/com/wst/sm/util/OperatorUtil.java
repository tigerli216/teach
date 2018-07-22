/*
 * @(#)UserUtil.java 2016年9月7日上午10:21:21
 * Copyright 2016 gbtsoft, Inc. All rights reserved.
 */
package com.wst.sm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import com.hiwi.common.util.SpringUtils;
import com.wst.service.sm.dto.OperatorDTO;

/**
 * 系统管理员操作工具类
 * 
 * @author liqg
 * @date 2016年9月7日上午10:21:21
 * @version 1.0
 */

public class OperatorUtil {

    /**
     * 增加管理员至Session
     * 
     * @author liqg
     * @creationDate. 2016年9月7日 下午2:51:28
     * @param request
     * @param dto
     * @return sessionId
     */
    public static String addOperator(HttpServletRequest request, OperatorDTO dto) {
        HttpSession session = request.getSession();
        session.setAttribute("op", dto);

        return session.getId();
    }

    /**
     * 获取登陆管理员信息
     * 
     * @author liqg
     * @creationDate. 2016年9月7日 上午10:50:20
     * @param request
     * @return
     */
    public static OperatorDTO getOperator(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object operator = session.getAttribute("op");
        if (operator != null) {
            return (OperatorDTO) operator;
        }
        return null;
    }

    /**
     * 删除登陆管理员信息
     * 
     * @author liqg
     * @creationDate. 2016年9月7日 下午5:59:15
     * @param request
     * @return sessionId
     */
    public static String deleteOperator(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return session.getId();
    }

    /**
     * 根据sessionId删除
     * 
     * @author liqg
     * @creationDate. 2016年9月7日 下午2:53:02
     * @param sessionId
     */
    public static void deleteById(String sessionId) {
        RedisOperationsSessionRepository redisSession = SpringUtils.getBean(RedisOperationsSessionRepository.class);
        redisSession.delete(sessionId);
    }
}
