/*
 * SemNotifyServiceImpl.java Created on 2017年8月29日 下午4:05:14
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
package com.wst.notify.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.util.email.SendMailUtils;
import com.wst.service.notify.dto.MailNotifyDTO;
import com.wst.service.notify.service.MailNotifyService;

/**
 * 邮件通知接口实现类 用于邮件通知
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class MailNotifyServiceImpl implements MailNotifyService {

    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public void sendRegisterActivationMail(MailNotifyDTO mailNotifyDTO) throws Exception {
        Map<String, String> mailParams = this.getEmailConfig();

        mailParams.put(SendMailUtils.MAIL_TO_ACCS, mailNotifyDTO.getToAccs()); // 收件人
        mailParams.put(SendMailUtils.MAIL_SUBJECT, "[教育网] 邮箱激活通知"); // 邮件主题

        // 邮件内容
        String mailBody = VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, "email/registerEmail.html",
                "UTF-8", mailNotifyDTO.getBodyParms());

        mailParams.put(SendMailUtils.MAIL_BODY, mailBody); // 邮件内容

        SendMailUtils.sendHtmlMail(mailParams, null);
    }

    @Override
    public void sendRestPwdMail(MailNotifyDTO mailNotifyDTO) throws Exception {
        Map<String, String> mailParams = this.getEmailConfig();

        mailParams.put(SendMailUtils.MAIL_TO_ACCS, mailNotifyDTO.getToAccs()); // 收件人
        mailParams.put(SendMailUtils.MAIL_SUBJECT, "[教育网] 找回密码通知"); // 邮件主题

        // 邮件内容
        String mailBody = VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, "email/restPwdEmail.html",
                "UTF-8", mailNotifyDTO.getBodyParms());

        mailParams.put(SendMailUtils.MAIL_BODY, mailBody); // 邮件内容

        SendMailUtils.sendHtmlMail(mailParams, null);
    }
    
    /**
     * 发送邮件基础配置信息
     * 
     * @return
     * @throws Exception
     */
    private Map<String, String> getEmailConfig() throws Exception {
        Map<String, String> mailParams = new HashMap<String, String>(10);

        // 邮件基础配置信息
        mailParams.put(SendMailUtils.MAIL_SMTP_HOST, "smtp.hiwitech.com");// 服务器地址
        mailParams.put(SendMailUtils.MAIL_SMTP_PORT, "25");// 服务器端口
        mailParams.put(SendMailUtils.MAIL_SMTP_AUTH, "true");// 是否权限验证
        mailParams.put(SendMailUtils.MAIL_FROM_ACC, "hiwi@hiwitech.com");// 邮件发送者邮箱地址
        mailParams.put(SendMailUtils.MAIL_FROM_PWD, "QweAsd!@#123Zxc");// 邮件发送者邮箱密码
        mailParams.put(SendMailUtils.MAIL_FROM_NAME, "教育网");// 邮件发送者名称

        return mailParams;
    }
}
