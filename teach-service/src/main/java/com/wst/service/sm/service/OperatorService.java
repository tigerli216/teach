/*
 * OperatorService.java Created on 2016年10月24日 下午3:07:53
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
package com.wst.service.sm.service;

import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.OperatorDTO;

/**
 * 操作员服务接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface OperatorService {

    /**
     * 分页查询操作员信息
     * 
     * @param pageParam
     *            分页参数
     * @param operatorDTO
     *            操作员相关查询参数
     * @return
     * @throws Exception
     */
    public Page<OperatorDTO> findOperatorPaging(Page<OperatorDTO> pageParam, OperatorDTO operatorDTO) throws Exception;

    /**
     * 保存数据
     * 
     * @param operatorDTO
     *            操作员信息
     * @return
     * @throws Exception
     */
    public long save(OperatorDTO operatorDTO) throws Exception;

    /**
     * 根据登录名获取操作员信息
     * 
     * @param opLoginName
     *            登录名
     * @return
     * @throws Exception
     */
    public OperatorDTO getByOpLoginName(String opLoginName) throws Exception;

    /**
     * 根据操作员ID获取操作员信息
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public OperatorDTO getById(long opId) throws Exception;

    /**
     * 修改操作员信息
     * 
     * @param operatorDTO
     * @return
     * @throws Exception
     */
    public int updateOperator(OperatorDTO operatorDTO) throws Exception;

    /**
     * 修改操作员状态
     * 
     * @param opIds
     * @param status
     * @throws Exception
     */
    public void updateOpStatus(String opIds, Long status) throws Exception;

    /**
     * 修改操作员密码
     * 
     * @param operatorDTO
     * @return
     * @throws Exception
     */
    public int updateOperatorPwd(OperatorDTO operatorDTO) throws Exception;

}
