/*
 * MssageService.java Created on 2017年10月12日 上午10:55:01
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
package com.wst.service.mem.service;

import com.hiwi.common.dao.Page;
import com.wst.service.mem.dto.MssageDTO;

/**
 * 消息接口
 * 
 * @author <a href="mailto:yangc@hiwitech.com">YangChuang</a>
 * @version 1.0
 */
public interface MssageService {

    /**
     * 发送消息
     * 
     * @param sendUser
     *            发送人
     * @param userId
     *            收件人
     * @param msgSource
     *            来源
     * @param sendType
     *            发送类型，1-学生，2-导师
     * @param messageType
     *            信息类型
     */
    public void baseSendMssage(long sendUser, long userId, String msgSource, long sendType, long messageType)
            throws Exception;

    /**
     * 学生课程消息
     * 
     * @param sendUser
     *            发送人
     * @param userId
     *            学生ID
     * @param msgSource
     *            消息来源
     * @param msgTitle
     *            消息标题
     * @param messageType
     *            消息类型
     * @throws Exception
     */
    public void memCustomMssage(long sendUser, long userId, String msgSource, String msgTitle, String msgContent,
            long messageType, long busiType) throws Exception;

    /**
     * 导师课程消息
     * 
     * @param sendUser
     *            发送人
     * @param tutorId
     *            导师ID
     * @param msgSource
     *            消息来源
     * @param msgTitle
     *            消息标题
     * @param messageType
     *            消息类型
     * @throws Exception
     */
    public void tutorCustomMssage(long sendUser, long tutorId, String msgSource, String msgTitle, String msgContent,
            long messageType, long busiType) throws Exception;

    /**
     * 分页查询消息
     * 
     * @param pageParam
     * @param mssageDTO
     * @return
     * @throws Exception
     */
    Page<MssageDTO> pageFindMssage(Page<MssageDTO> pageParam, MssageDTO mssageDTO) throws Exception;
}
