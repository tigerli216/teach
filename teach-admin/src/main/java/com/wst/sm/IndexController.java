/*
 * MainConroller.java Created on 2016年9月21日 下午1:36:23
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
package com.wst.sm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.ResourceDTO;
import com.wst.service.sm.service.AuthService;
import com.wst.sm.util.OperatorUtil;

/**
 * Index控制器
 * 
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@Controller
@RequestMapping("d-admin")
public class IndexController {

    @Reference(version = "0.0.1")
    private AuthService authService;

    /**
     * 存放菜单的key
     */
    public static final String OP_MENUS_SESSION = "op_menus";
    
    /**
     * 登陆页
     * 
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("login")
    public String login(HttpServletRequest request) throws Exception {
        return "login";
    }

    /**
     * 登陆认证
     * 
     * @param request
     * @param entity
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("auth")
    public ResultDTO<OperatorDTO> auth(HttpServletRequest request, OperatorDTO dto) throws Exception {
        HttpSession session = request.getSession();
        ResultDTO<OperatorDTO> result = authService.login(dto, session.getId());
        if (result.isSuccess()) { // 存入session
            OperatorUtil.addOperator(request, result.getResult());
            result.setResult(null);
        }
        // 加载菜单信息
        return result;
    }

    /**
     * 首页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("index")
    public String main(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute(OP_MENUS_SESSION) == null) {
            OperatorDTO op = OperatorUtil.getOperator(request);
            List<ResourceDTO> res = this.authService.getOperatorMenus(op.getOpId());
            
            session.setAttribute(OP_MENUS_SESSION, res);
        }
        return "index";
    }
    

    /**
     * 主页
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("homePage")
    public String homePage(HttpServletRequest request) throws Exception {
    
        return "homePage";
    }
    
    

    /**
     * 下线
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("logout")
    public ResultDTO<String> logout(HttpServletRequest request) throws Exception {
        OperatorUtil.deleteOperator(request);
        
        ResultDTO<String> result = new ResultDTO<String>();
        return result.set(ResultCodeEnum.SUCCESS, "");
    }
}
