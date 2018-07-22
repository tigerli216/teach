/*
 * @(#)LoginService.java 2016年9月6日下午9:32:34
 * Copyright 2016 HIWIsoft, Inc. All rights reserved.
 */
package com.wst.service.sm.service;

import java.util.List;

import com.hiwi.common.dto.ResultDTO;
import com.wst.service.sm.dto.OperatorDTO;
import com.wst.service.sm.dto.ResourceDTO;

/**
 * 登陆、下线处理Service
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public interface AuthService {

	/**
	 * 登陆
	 * 
	 * @param dto
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<OperatorDTO> login(OperatorDTO dto, String sessionId) throws Exception;
	
	/**
	 * 根据用户ID获取用户有权限范围的菜单
	 * 
	 * @param opId 用户ID
	 * @return
	 * @throws Exception
	 */
	 public List<ResourceDTO> getOperatorMenus(Long opId) throws Exception;

	 /**
	 * 下线
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> logout(String sessionId) throws Exception;
}
