/*
 * RoleServiceImpl.java Created on 2016年10月25日 下午8:36:45
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

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dao.Page;
import com.wst.entity.sm.RelOrgRole;
import com.wst.entity.sm.RelResRole;
import com.wst.service.sm.dto.RelOrgRoleDTO;
import com.wst.service.sm.dto.RelResRoleDTO;
import com.wst.service.sm.dto.RoleDTO;
import com.wst.service.sm.service.RoleService;
import com.wst.sm.dao.RelOrgRoleDao;
import com.wst.sm.dao.RelResRoleDao;
import com.wst.sm.dao.RoleDao;

/**
 * 角色Service
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleDao roleDao;

    @Resource
    private RelOrgRoleDao relOrgRoleDao;

    @Resource
    private RelResRoleDao relResRoleDao;

    @Override
    public List<RoleDTO> findRoleList() throws Exception {
        return roleDao.findRoleList();
    }

    @Override
    public Page<RoleDTO> findRolePaging(Page<RoleDTO> pageParam, RoleDTO roleQuery) throws Exception {
        return roleDao.findRolePaging(pageParam, roleQuery);
    }

    @Override
    public RoleDTO findRoleById(long roleId) throws Exception {
        return roleDao.getByRoleId(roleId);
    }

    @Override
    public RoleDTO findRoleByPrentId(long parentId) throws Exception {
        return roleDao.getRoleByPrentId(parentId);
    }

    /**
     * 查看组织名是否重复
     * 
     * @param role
     * @return
     * @throws Exception
     */
    @Override
    public boolean ifRepeatName(RoleDTO role) throws Exception {
        boolean b = false;
        if (role.getRoleId() == 0) {
            int roleCount = roleDao.ifRepeatNameAdd(role);
            if (roleCount == 0) {
                b = true;
            }
        } else {
            int roleCount = roleDao.ifRepeatNameUpdate(role);
            if (roleCount == 0) {
                b = true;
            }
        }
        return b;
    }

    @Override
    public void addRole(RoleDTO role, String resIdListStr, String orgIdListStr) throws Exception {
        if (StringUtils.isEmpty(resIdListStr)) {
            resIdListStr = "";
        }
        if (StringUtils.isEmpty(orgIdListStr)) {
            orgIdListStr = "";
        }
        long roleId = roleDao.save(role);
        role.setRoleId(roleId);

        // 如果无父节点则默认为自身
        if (role.getParentId() == 0) {
            role.setRolePath("#" + role.getRoleId() + "#");
            role.setParentId(role.getRoleId());
        } else {
            role.setRolePath(role.getRolePath() + role.getRoleId() + "#");
        }
        roleDao.update(role);

        // 添加当前角色的所属权限
        String[] resIdList = resIdListStr.split(",");
        for (int i = 0; i < resIdList.length; i++) {
            if (StringUtils.isNotBlank(resIdList[i])) {
                RelResRole rrr = new RelResRole();
                rrr.setResourceId(Long.valueOf(resIdList[i]));
                rrr.setRoleId(role.getRoleId());
                rrr.setStatus(0);
                rrr.setCreateOpId(role.getCreateOpId());
                rrr.setCreateTime(role.getCreateTime());
                relResRoleDao.save(rrr);
            }
        }

        // 添加当前角色的所属组织
        String[] orgIdList = orgIdListStr.split(",");
        for (int i = 0; i < orgIdList.length; i++) {
            if (StringUtils.isNotBlank(orgIdList[i])) {
                RelOrgRole rrr = new RelOrgRole();
                rrr.setOrgId(Long.valueOf(orgIdList[i]));
                rrr.setRoleId(role.getRoleId());
                rrr.setStatus(0);
                rrr.setCreateOpId(role.getCreateOpId());
                rrr.setCreateTime(role.getCreateTime());
                relOrgRoleDao.save(rrr);
            }
        }
    }

    @Override
    public void updateRole(RoleDTO role, String resIdListStr, String orgIdListStr) throws Exception {
        updateResRoleInfo(role, resIdListStr);
        updateOrgRoleInfo(role, orgIdListStr);

        updateRoleBaseInfo(role);
    }

    /**
     * 更新角色菜单权限信息
     * 
     * @param role
     * @throws Exception
     */
    public void updateResRoleInfo(RoleDTO role, String resIdListStr) throws Exception {
        if (StringUtils.isEmpty(resIdListStr)) {
            resIdListStr = "";
        }

        // 取得角色原有权限
        List<String> resIds = new ArrayList<String>();
        List<String> preResIds = relResRoleDao.findResIdsByRoleId(role.getRoleId());
        if (null != preResIds && preResIds.size() > 0) {
            for (String resId : preResIds) {
                resIds.add(resId);
            }
        }

        String[] arrResId = resIdListStr.split(",");
        for (String resIdStr : arrResId) {
            if (StringUtils.isEmpty(resIdStr) || StringUtils.equals("undefined", resIdStr)) {
                continue;
            }
            Long resId = Long.parseLong(resIdStr);
            if (resIds.contains(resIdStr)) { // 未变更,不处理
                resIds.remove(resIdStr);
                continue;
            } else {
                RelResRole rrr = new RelResRole();
                rrr.setResourceId(resId);
                rrr.setRoleId(role.getRoleId());
                rrr.setStatus(0L);
                rrr.setCreateOpId(role.getModifyOpId());
                rrr.setCreateTime(role.getModifyTime());
                relResRoleDao.save(rrr);
            }
        }

        // 删除取消关联权限
        for (String resId : resIds) {
            RelResRole relResRole = this.relResRoleDao.findRelResRoleByRoleIdAndResId(role.getRoleId(),
                    Long.valueOf(resId));
            if (relResRole == null) {
                continue;
            }
            this.relResRoleDao.delete(relResRole);
        }
        // TODO 保存历史删除历史记录
        // RelResRoleHis relResRoleHis = (RelResRoleHis)
        // EntityBeanUtils.getHisEntity(relResRole);
        // relResRoleHis.setExpireOpId(role.getModifyOpId());
        // relResRoleHis.setExpireTime(role.getModifyTime());
        // this.relResRoleHisDao.save(relResRoleHis);
        // }
    }

    /**
     * 更新角色组织权限信息
     * 
     * @param role
     * @param orgIdListStr
     * @throws Exception
     */
    public void updateOrgRoleInfo(RoleDTO role, String orgIdListStr) throws Exception {
        if (StringUtils.isEmpty(orgIdListStr)) {
            orgIdListStr = "";
        }

        // 取得角色原有权限
        List<Long> orgIds = new ArrayList<Long>();
        List<RelOrgRole> preOrgIds = relOrgRoleDao.findOrgIdsByRoleId(role.getRoleId());
        if (null != preOrgIds && preOrgIds.size() > 0) {
            for (RelOrgRole relOrgRole : preOrgIds) {
                orgIds.add(relOrgRole.getOrgId());
            }
        }
        String[] arrOrgId = orgIdListStr.split(",");
        for (String orgIdStr : arrOrgId) {
            if (StringUtils.isEmpty(orgIdStr) || StringUtils.equals("undefined", orgIdStr)) {
                continue;
            }
            Long orgId = Long.parseLong(orgIdStr);
            if (orgIds.contains(orgId)) { // 未变更,不处理
                orgIds.remove(orgId);
                continue;
            } else {
                RelOrgRoleDTO rrr = new RelOrgRoleDTO();
                rrr.setOrgId(orgId);
                rrr.setRoleId(role.getRoleId());
                rrr.setStatus(0L);
                rrr.setCreateOpId(role.getModifyOpId());
                rrr.setCreateTime(role.getModifyTime());
                relOrgRoleDao.save(rrr);
            }
        }

        // 删除取消关联权限
        for (Long preOrgId : orgIds) {
            RelOrgRoleDTO relOrgRole = this.relOrgRoleDao.findRelOrgRoleByRoleIdAndOrgId(role.getRoleId(), preOrgId);
            if (relOrgRole == null) {
                continue;
            }
            // TODO 记录历史记录
            // RelOrgRoleHisDO relOrgRoleHis = (RelOrgRoleHisDO)
            // EntityBeanUtils.getHisEntity(relOrgRole);
            // relOrgRoleHis.setExpireOpId(role.getModifyOpId());
            // relOrgRoleHis.setExpireTime(role.getModifyTime());
            // this.relOrgRoleHisDao.save(relOrgRoleHis);
            this.relOrgRoleDao.delete(relOrgRole);
        }
    }

    /**
     * 添加历史记录
     * 
     * 
     * @param RoleDO
     * @throws Exception
     */
    public void updateRoleBaseInfo(RoleDTO role) throws Exception {
        RoleDTO dbRole = this.roleDao.getByRoleId(role.getRoleId());
        if (dbRole == null) { // 数据库数据不存在
            return;
        }

        // 更新当前表
        dbRole.setRoleName(role.getRoleName());
        dbRole.setRoleDesc(role.getRoleDesc());
        this.roleDao.update(dbRole);

        // TODO 保存历史记录
        // if (isRoleBaseInfoChange(role, dbRole)) {
        // 保存历史
        // RoleHisDO roleHis = (RoleHisDO)
        // EntityBeanUtils.getHisEntity(dbRole);
        // roleHis.setExpireOpId(role.getModifyOpId());
        // roleHis.setExpireTime(role.getModifyTime());
        // this.roleHisDao.save(roleHis);
        // 更新当前表
        // this.roleDao.updateRole(role);
        // }
    }

    @Override
    public List<Long> findRelResRoleByRoleId(RoleDTO role) throws Exception {
        List<RelResRoleDTO> rrrList = this.relResRoleDao.findRelResRoleByResIdAndRoleId(role.getRoleId());
        List<Long> resIdList = new ArrayList<Long>();
        if (rrrList != null && rrrList.size() != 0) {
            for (int i = 0; i < rrrList.size(); i++) {
                RelResRoleDTO rrr = rrrList.get(i);
                resIdList.add(rrr.getResourceId());
            }
        }
        return resIdList;
    }

    @Override
    public List<Long> findRelOrgRoleByRoleId(RoleDTO role) throws Exception {
        List<RelOrgRoleDTO> orgList = this.relResRoleDao.findRelOrgRoleByRoleId(role.getRoleId());
        List<Long> orgIdList = new ArrayList<Long>();
        if (orgList != null && orgList.size() != 0) {
            for (int i = 0; i < orgList.size(); i++) {
                RelOrgRoleDTO rrr = orgList.get(i);
                orgIdList.add(rrr.getOrgId());
            }
        }
        return orgIdList;
    }

    @Override
    public void enableOrDisableRole(String roleIds, Long status) throws Exception {
        String rIds[] = roleIds.split(",");
        for (int i = 0; i < rIds.length; i++) {
            roleDao.updateRoleByStatus(Long.valueOf(rIds[i]), status);
        }
    }

    /**
     * 判断组织基本信息是否发生变更
     * 
     * @param role
     * @param dbOrg
     * @return
     */
    // private boolean isRoleBaseInfoChange(RoleDTO role, RoleDTO dbOrg) {
    // String[] propertyNames = { "roleName", "roleDesc" };
    //
    // return EntityBeanUtils.compareProperty(role, dbOrg, propertyNames);
    // }
}
