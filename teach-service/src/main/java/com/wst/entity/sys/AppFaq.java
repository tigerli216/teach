/*
 * AppFaq.java Created on 2016年10月31日 下午4:52:23
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
package com.wst.entity.sys;

import java.io.Serializable;
import java.util.Date;

/**
 * app问题解答表
 * 
 * @author <a href="mailto:dingps@hiwitech.com">dingps</a>
 * @version 1.0
 */
public class AppFaq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * faqID
     */
    protected long faqId;

    /**
     * app操作系统，1：安卓，2：IOS，9：通用
     */
    protected long osname;

    /**
     * app类型，1：风猫商城，2：经销商，3：代理商，9-通用
     */
    protected long appType;

    /**
     * FAQ编码
     */
    protected String faqCode;

    /**
     * 标题
     */
    protected String title;

    /**
     * 答案内容
     */
    protected String content;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 更新时间
     */
    protected Date updateTime;

    /** 无参构造方法 */
    public AppFaq() {
    }

    /**
     * 属性get||set方法
     */
    public long getFaqId() {
        return faqId;
    }

    public void setFaqId(long faqId) {
        this.faqId = faqId;
    }

    public long getOsname() {
        return osname;
    }

    public void setOsname(long osname) {
        this.osname = osname;
    }

    public long getAppType() {
        return appType;
    }

    public void setAppType(long appType) {
        this.appType = appType;
    }

    public String getFaqCode() {
        return faqCode;
    }

    public void setFaqCode(String faqCode) {
        this.faqCode = faqCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
