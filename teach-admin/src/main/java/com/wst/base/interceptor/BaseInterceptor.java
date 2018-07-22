/*
 * @(#)BaseInterceptor.java 2015-3-11下午1:51:54
 * Copyright (c) 2016 HeWei Technology Co.Ltd. All rights reserved.
 */
package com.wst.base.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.Globals;
import com.hiwi.common.util.JsonUtils;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.sm.util.OperatorUtil;

/**
 * 基本拦截器
 * 
 * @author liqg
 * @date 2016年9月7日下午4:57:22
 * @version 1.0
 */

public class BaseInterceptor implements HandlerInterceptor {

	private static HiwiLog log = HiwiLogFactory.getLogger(BaseInterceptor.class);
    private static String[] permissionSkipPattern = { "/d-admin/login", "/d-admin/auth" }; // 检查权限过滤条件
    private static String noPermissionRedirecUrl = "/d-admin/login"; // 无权限访问转向页面链接

    public static String webCtx = ""; // web服务器地址

    /*
     * 方法前拦截 (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#
     * preHandle (javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (StringUtils.isBlank(webCtx)) {
            webCtx = Globals.getCtx(request);
        }

        String requestIp = Globals.getRealIP(request); // 客户端IP
        String url = request.getServletPath(); // 访问路径
        JSONObject jsonObject = Globals.getRequestParamsJson(request);

        OperatorDTO dto = OperatorUtil.getOperator(request); // 当前登录用户

        // 记录请求日志
        StringBuilder reqLog = new StringBuilder();
        if (dto == null) {
            reqLog.append("登录用户=,").append(",访问=").append(url).append(",请求IP为=").append(requestIp).append("，请求参数：")
                    .append(jsonObject.toJSONString());
        } else {
            reqLog.append("登录用户=").append(dto.getOpLoginName()).append(",访问=").append(url).append(",请求IP为=")
                    .append(requestIp).append("，请求参数：").append(jsonObject.toJSONString());
        }
        log.debug(reqLog);

        // 效验请求是否需要检查权限
        boolean isCheck = true;
        for (String pattern : permissionSkipPattern) {
            if (url.equals(pattern)) {
                isCheck = false;
                break;
            }
        }
        if (isCheck && dto == null) {
            if (Globals.isAjaxRequest(request)) { // ajax请求
                response.setCharacterEncoding("UTF-8");
                response.setHeader("sessionstatus", "timeout");
                response.setContentType("text/x-json;charset=utf-8");
                response.setHeader("Cache-Control", "no-cache");
                PrintWriter out = response.getWriter();
                out.write("sessionTimeout");
                out.flush();
                out.close();
                return false;
            } else {
                request.getRequestDispatcher(noPermissionRedirecUrl).forward(request, response);
                return false;
            }
        }
        return true;
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelandview) throws Exception {

    }

    /**
     * 返回处理（用于处理异常）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        if (exception != null) {
            log.error("======请求拦截器=====（捕获异常）", exception);
            if (Globals.isAjaxRequest(request)) { // ajax请求
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/x-json;charset=utf-8");
                response.setHeader("Cache-Control", "no-cache");
                PrintWriter out = response.getWriter();
                ResultDTO<String> rr = new ResultDTO<String>();
                rr.set(ResultCodeEnum.ERROR_HANDLE, "服务器异常！请稍后重试！");
                out.write(JsonUtils.getJson(rr));
                out.close();
            }
        }
    }

}
