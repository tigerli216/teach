/*
 * AuthServiceImpl.java Created on 2016年9月28日 下午2:30:15
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
package com.wst.sm.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.util.security.MD5Util;
import com.wst.entity.sm.Operator;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.ResourceDTO;
import com.wst.service.sm.service.AuthService;
import com.wst.sm.dao.OperatorDao;
import com.wst.sm.dao.ResourceDao;

/**
 * 后台操作员登录及退出管理类
 * 
 * @author <a href="mailto:liul@hiwitech.com">Louis Liu</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class AuthServiceImpl implements AuthService {

    @Resource
    private OperatorDao opDao;

    @Resource
    private ResourceDao resourceDao;

    @Override
    public ResultDTO<OperatorDTO> login(OperatorDTO dto, String sessionId) throws Exception {
        Operator op = this.opDao.getByLoginName(dto.getOpLoginName());
        ResultDTO<OperatorDTO> result = new ResultDTO<OperatorDTO>();
        if (op == null || op.getOpLoginName() == null) {
            return result.set(ResultCodeEnum.ERROR_AUTH, dto.getOpLoginName() + "不存在!");
        }

        if (op.getLockStatus() == 1) {
            return result.set(ResultCodeEnum.ERROR_AUTH, "帐号:" + dto.getOpLoginName() + "已被锁定!");
        }

        if (op.getStatus() == 1) {
            return result.set(ResultCodeEnum.ERROR_AUTH, "帐号:" + dto.getOpLoginName() + "已被禁用!");
        }

        String password = op.getPassword();
        String md5Pwd = MD5Util.MD5(dto.getPassword());
        if (!md5Pwd.equals(password)) {
            return result.set(ResultCodeEnum.ERROR_AUTH, "帐号密码错误");
        }
        return result.set(ResultCodeEnum.SUCCESS, "", (OperatorDTO) op);
    }

    @Override
    public List<ResourceDTO> getOperatorMenus(Long opId) throws Exception {
        List<ResourceDTO> resources = this.resourceDao.getByOpId(opId);

        List<ResourceDTO> rootTrees = new ArrayList<ResourceDTO>();
        for (ResourceDTO tree : resources) {
            if (tree.getParentId() == tree.getResourceId()) {
                rootTrees.add(tree);
                // continue;
            }
            for (ResourceDTO t : resources) {
                if (t.getParentId() == t.getResourceId()) {
                    continue;
                }
                if (t.getParentId() == tree.getResourceId()) {
                    if (tree.getChildrens() == null) {
                        List<ResourceDTO> myChildrens = new ArrayList<ResourceDTO>();
                        myChildrens.add(t);
                        tree.setChildrens(myChildrens);
                    } else {
                        tree.getChildrens().add(t);
                    }
                }
            }
        }

        return rootTrees;
    }

    @Override
    public ResultDTO<String> logout(String sessionId) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
