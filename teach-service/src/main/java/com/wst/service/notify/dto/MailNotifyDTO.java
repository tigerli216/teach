/*
 * SemNotifyDTO.java Created on 2017年8月29日 下午3:45:44
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
package com.wst.service.notify.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 邮件发送DTO
 * 
 * @author <a href="mailto:wangdl@hiwitech.com">WangDL</a>
 * @version 1.0
 */
public class MailNotifyDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 收件人
     */
    private String toAccs;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发送内容参数
     */
    private Map<String, Object> bodyParms;

    public String getToAccs() {
        return toAccs;
    }

    public void setToAccs(String toAccs) {
        this.toAccs = toAccs;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getBodyParms() {
        return bodyParms;
    }

    public void setBodyParms(Map<String, Object> bodyParms) {
        this.bodyParms = bodyParms;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
