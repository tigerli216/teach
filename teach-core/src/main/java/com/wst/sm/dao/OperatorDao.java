/*
 * OperatorDao.java Created on 2016年10月24日 下午3:54:27
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
package com.wst.sm.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.hiwi.common.dao.BaseDao;
import com.hiwi.common.dao.Page;
import com.wst.service.sm.dto.OperatorDTO;

/**
 * 操作员管理Dao
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
@Repository
public class OperatorDao {

    @Resource
    private BaseDao baseDao;

    /**
     * 根据登录帐号查询操作员信息
     * 
     * @param loginName
     *            操作员登录帐号
     * @return
     * @throws Exception
     */
    public OperatorDTO getByLoginName(String loginName) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select op_id, op_login_name, is_owner, own_op_id, password, ");
        sql.append("       org_id, status, lock_status, op_name ");
        sql.append("  from sso_operator ");
        sql.append(" where op_login_name = :loginName ");

        Map<String, String> params = new HashMap<>(1);
        params.put("loginName", loginName);

        return baseDao.get(sql.toString(), params, OperatorDTO.class);
    }

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
    public Page<OperatorDTO> findOperatorPaging(Page<OperatorDTO> pageParam, OperatorDTO operatorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select op.op_id, op.op_login_name, op.is_owner, op.own_op_id, op.password, ");
        sql.append("       op.org_id, op.status, op.lock_status, op.op_name, op.email, op.qq, ");
        sql.append("       op.address, op.mobile_phone, op.tele_phone, op.postcode, op.job_number, ");
        sql.append("       op.identity_type, op.identity_no, op.age, op.photo, op.memo, op.create_op_id, ");
        sql.append("       op.create_time, op.modify_op_id, op.modify_time, op.overdue_time, op.batch_id, ");
        sql.append("       op.is_admin, op.supplier_id, op.is_supplier_admin, op.next_reset_pwd_time, ");
        sql.append("       op.reset_pwd_notes, o.op_name createOpName, o.create_time createTime, org.org_name orgName");
        sql.append("  from sso_operator op");
        sql.append(" inner join sso_operator o on o.op_id = op.create_op_id");
        sql.append("  left join sso_rel_op_org relOrg on relOrg.op_id = op.op_id");
        sql.append("  left join sso_organization org on org.org_id = relOrg.org_id");
        sql.append(" where 1 = 1");
        
        if (StringUtils.isNotEmpty(operatorDTO.getOpLoginName())) {
            sql.append(" and op.op_login_name like concat('%', :opLoginName, '%')");
        }
        if (StringUtils.isNotEmpty(operatorDTO.getOpName())) {
            sql.append(" and op.op_name like concat('%', :opName, '%')");
        }
        if (operatorDTO.getStatus() > 0) {
            operatorDTO.setStatus(operatorDTO.getStatus() - 1);
            sql.append(" and op.status = :status");
        }
        sql.append(" order by op.op_id desc");
 
        return baseDao.findPaging(sql.toString(), pageParam, operatorDTO, OperatorDTO.class);
    }

    /**
     * 添加数据
     * 
     * @param operatorDTO
     * @return
     * @throws Exception
     */
    public long save(OperatorDTO operatorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("insert into sso_operator(op_login_name, is_owner, own_op_id, password, ");
        sql.append("       org_id, status, lock_status, op_name, email, qq, address, ");
        sql.append("       mobile_phone, tele_phone, postcode, job_number, identity_type, ");
        sql.append("       identity_no, age, photo, memo, create_op_id, create_time, modify_op_id, ");
        sql.append("       modify_time, overdue_time, batch_id, is_admin, supplier_id, is_supplier_admin, ");
        sql.append("       next_reset_pwd_time, reset_pwd_notes) ");
        sql.append(" value(:opLoginName, :isOwner, :ownOpId, :password, ");
        sql.append("       :orgId, :status, :lockStatus, :opName, :email, :qq, :address, ");
        sql.append("       :mobilePhone, :telePhone, :postcode, :jobNumber, :identityType, ");
        sql.append("       :identityNo, :age, :photo, :memo, :createOpId, :createTime, :modifyOpId, ");
        sql.append("       :modifyTime, :overdueTime, :batchId, :isAdmin, :supplierId, :isSupplierAdmin, ");
        sql.append("       :nextResetPwdTime, :resetPwdNotes) ");
        
        return baseDao.insertInto(sql.toString(), operatorDTO, "op_id");
    }

    /**
     * 根据操作员ID获取操作员信息
     * 
     * @param opId
     * @return
     * @throws Exception
     */
    public OperatorDTO getById(long opId) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("select op.op_id, op.op_login_name, op.is_owner, op.own_op_id, op.password, ");
        sql.append("       op.org_id, op.status, op.lock_status, op.op_name, op.email, op.qq, ");
        sql.append("       op.address, op.mobile_phone, op.tele_phone, op.postcode, op.job_number, ");
        sql.append("       op.identity_type, op.identity_no, op.age, op.photo, op.memo, op.create_op_id, ");
        sql.append("       op.create_time, op.modify_op_id, op.modify_time, op.overdue_time, op.batch_id,");
        sql.append("       op.is_admin, op.supplier_id, op.is_supplier_admin, op.next_reset_pwd_time, ");
        sql.append("       op.reset_pwd_notes, o.op_name createOpName, o.create_time createTime, ");
        sql.append("       org.org_name orgName, s.op_name modifyOpName ");
        sql.append("  from sso_operator op");
        sql.append(" inner join sso_operator o on o.op_id = op.create_op_id");
        sql.append("  left join sso_operator s on s.op_id = op.modify_op_id");
        sql.append("  left join sso_rel_op_org relOrg on relOrg.op_id = op.op_id");
        sql.append("  left join sso_organization org on org.org_id = relOrg.org_id");
        sql.append(" where op.op_id = :opId");
        
        Map<String, Long> params = new HashMap<String, Long>(1);
        params.put("opId", opId);
        
        return baseDao.get(sql.toString(), params, OperatorDTO.class);
    }

    /**
     * 修改操作员信息
     * 
     * @param operatorDTO
     * @return
     */
    public int updateOperator(OperatorDTO operatorDTO) throws Exception {
        StringBuilder sql = new StringBuilder();
        
        sql.append("update sso_operator ");
        sql.append("   set op_name = :opName, ");
        if (StringUtils.isNotEmpty(operatorDTO.getPassword())) {
            sql.append("   password = :password,");
        }
        sql.append("       email = :email, qq = :qq, tele_phone = :telePhone, ");
        sql.append("       mobile_phone = :mobilePhone, identity_type = :identityType, ");
        sql.append("       identity_no = :identityNo, overdue_time = :overdueTime, ");
        sql.append("       modify_op_id = :modifyOpId, modify_time = :modifyTime, ");
        sql.append("       job_number = :jobNumber, age = :age, ");
        sql.append("       address = :address, postcode = :postcode ");
        sql.append(" where op_id = :opId");

        return baseDao.executeSQL(sql.toString(), operatorDTO);
    }

    /**
     * 修改操作员状态
     * 
     * @param opId
     * @param status
     * @return
     * @throws Exception
     */
    public int updateOpStatus(long opId, long status) throws Exception {
        String sql = "update sso_operator set status = :status where op_id = :opId";
        
        Map<String, Long> params = new HashMap<String, Long>(2);
        params.put("status", status);
        params.put("opId", opId);
        
        return baseDao.executeSQL(sql.toString(), params);
    }
}
