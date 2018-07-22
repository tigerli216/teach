/*
 * OperatorServiceImpl.java Created on 2016年10月24日 下午3:08:20
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

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.hiwi.common.util.CommonFuntions;
import com.wst.entity.sm.RelOpOrg;
import com.wst.entity.sm.RelOpRole;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.service.OperatorService;
import com.wst.sm.dao.OperatorDao;
import com.wst.sm.dao.RelOpOrgDao;
import com.wst.sm.dao.RelOpRoleDao;

/**
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class OperatorServiceImpl implements OperatorService {

    @Resource
    private OperatorDao operatorDao;
    @Resource
    private RelOpOrgDao relOpOrgDao;
    @Resource
    private RelOpRoleDao relOpRoleDao;

    @Override
    public Page<OperatorDTO> findOperatorPaging(Page<OperatorDTO> pageParam, OperatorDTO operatorDTO) throws Exception {
        return operatorDao.findOperatorPaging(pageParam, operatorDTO);
    }

    @Override
    public long save(OperatorDTO operatorDTO) throws Exception {
        long opId = operatorDao.save(operatorDTO);
        // 处理账户与组织关系
        if (StringUtils.isNotEmpty(operatorDTO.getOrgIds())) {
            String[] arrOrgId = operatorDTO.getOrgIds().split(",");
            for (String orgId : arrOrgId) {
                if (StringUtils.isEmpty(orgId)) {
                    continue;
                }
                RelOpOrg rel = new RelOpOrg();
                rel.setOpId(opId);
                rel.setOrgId(Long.parseLong(orgId));
                rel.setStatus(0);
                rel.setCreateOpId(operatorDTO.getCreateOpId());
                rel.setCreateTime(operatorDTO.getCreateTime());
                relOpOrgDao.save(rel);
            }
        }

        // 处理账户与角色关系
        if (StringUtils.isNotEmpty(operatorDTO.getRoleIds())) {
            String[] arrRoleId = operatorDTO.getRoleIds().split(",");
            for (String roleId : arrRoleId) {
                if (StringUtils.isEmpty(roleId)) {
                    continue;
                }
                RelOpRole rar = new RelOpRole();
                rar.setCreateOpId(operatorDTO.getCreateOpId());
                rar.setCreateTime(operatorDTO.getCreateTime());
                rar.setOpId(opId);
                rar.setStatus(0);
                rar.setRoleId(Long.parseLong(roleId));
                relOpRoleDao.save(rar);
            }
        }
        return opId;
    }

    @Override
    public OperatorDTO getByOpLoginName(String opLoginName) throws Exception {
        return operatorDao.getByLoginName(opLoginName);
    }

    @Override
    public OperatorDTO getById(long opId) throws Exception {
        return operatorDao.getById(opId);
    }

    @Override
    public int updateOperator(OperatorDTO operatorDTO) throws Exception {
        // 更新操作员信息
        operatorDao.updateOperator(operatorDTO);
        // 更新操作员与组织关系信息
        updateOperatorOrgInfo(operatorDTO);
        // 更新操作员与角色关系信息
        updateOperatorRoleInfo(operatorDTO);
        return 0;
    }

    /**
     * 更新账户组织信息
     * 
     * @param operator
     */
    private void updateOperatorOrgInfo(OperatorDTO operator) throws Exception {
        // 取得账户关联原有组织
        List<String> preOrgIds = this.relOpOrgDao.findRelOpOrgIdsByOpId(operator.getOpId());
        String orgIds = "";
        if (CommonFuntions.isNotEmptyObject(operator.getOrgIds())) {
            orgIds = operator.getOrgIds();
        }

        String[] arrOrgId = orgIds.split(",");
        for (String orgIdStr : arrOrgId) {
            if (StringUtils.isEmpty(orgIdStr)) {
                continue;
            }
            if (preOrgIds.contains(orgIdStr)) { // 未变更,不处理
                preOrgIds.remove(orgIdStr);
                continue;
            } else { // 新增关系
                RelOpOrg rel = new RelOpOrg();
                rel.setOpId(operator.getOpId());
                rel.setOrgId(Long.valueOf(orgIdStr));
                rel.setStatus(0);
                rel.setCreateOpId(operator.getModifyOpId());
                rel.setCreateTime(operator.getModifyTime());
                this.relOpOrgDao.save(rel);
            }
        }

        // 删除取消关联组织
        for (String preOrgId : preOrgIds) {
            relOpOrgDao.deleteRelOpOrg(operator.getOpId(), Long.valueOf(preOrgId));
        }
    }

    /**
     * 更新账户角色信息
     * 
     * @param operator
     */
    private void updateOperatorRoleInfo(OperatorDTO operator) throws Exception {
        String roleIds = "";
        if (CommonFuntions.isNotEmptyObject(operator.getRoleIds())) {
            roleIds = operator.getRoleIds();
        }

        // 取得账户关联原有角色
        List<String> preRoleIds = this.relOpRoleDao.findRelOpRoleIdsByOpId(operator.getOpId());
        String[] arrRoleId = roleIds.split(",");
        for (String roleIdStr : arrRoleId) {
            if (StringUtils.isEmpty(roleIdStr)) {
                continue;
            }
            if (preRoleIds.contains(roleIdStr)) { // 未变更,不处理
                preRoleIds.remove(roleIdStr);
                continue;
            } else { // 新增关系
                RelOpRole rel = new RelOpRole();
                rel.setOpId(operator.getOpId());
                rel.setRoleId(Long.valueOf(roleIdStr));
                rel.setStatus(0);
                rel.setCreateOpId(operator.getModifyOpId());
                rel.setCreateTime(operator.getModifyTime());
                relOpRoleDao.save(rel);
            }
        }

        // 删除取消关联角色
        for (String preRoleId : preRoleIds) {
            relOpRoleDao.deleteRelOpOrg(operator.getOpId(), Long.valueOf(preRoleId));
        }
    }

    @Override
    public void updateOpStatus(String opIds, Long status) throws Exception {
        String[] ids = opIds.split(",");
        for (String opId : ids) {
            operatorDao.updateOpStatus(Long.valueOf(opId), status);
        }
    }

    @Override
    public int updateOperatorPwd(OperatorDTO operatorDTO) throws Exception {
        operatorDao.updateOperator(operatorDTO);
        return 0;
    }
}
