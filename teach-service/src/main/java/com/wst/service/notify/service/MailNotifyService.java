/*
 * SemNotifyService.java Created on 2017年8月29日 下午4:01:37
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
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
package com.wst.service.notify.service;

import com.wst.service.notify.dto.MailNotifyDTO;

/**
 * 邮件通知Service 用于邮件通知
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public interface MailNotifyService {

	/**
	 * 发送注册激活邮件
	 * 
	 * @param mailNotifyDTO
     * @return
	 * @throws Exception
	 */
	public void sendRegisterActivationMail(MailNotifyDTO mailNotifyDTO) throws Exception;

	/**
	 * 发送重置密码邮件
	 * 
	 * @param mailNotifyDTO
     * @return
	 * @throws Exception
	 */
	public void sendRestPwdMail(MailNotifyDTO mailNotifyDTO) throws Exception;
}
