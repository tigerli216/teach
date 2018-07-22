/*
 * TxVideoServiceImpl.java Created on 2017年11月2日 下午2:10:21
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
package com.wst.tencent.service;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.hiwi.common.dto.ResultCodeEnum;
import com.hiwi.common.dto.ResultDTO;
import com.wst.service.tencent.service.TxVideoService;
import com.wst.tencent.module.TxVideoModule;

/**
 * 腾讯视频服务
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Service(version = "0.0.1")
public class TxVideoServiceImpl implements TxVideoService {

    @Override
    public ResultDTO<String[]> describeRecordPlayInfo(String vid) throws Exception {
        ResultDTO<String[]> result = new ResultDTO<>();
        if (StringUtils.isBlank(vid)) {
            return result.set(ResultCodeEnum.ERROR_MISSING_PARAMS, "action is null");
        }
        String[] resultInfo = TxVideoModule.describeRecordPlayInfo(vid);
        return result.set(ResultCodeEnum.SUCCESS, "", resultInfo);
    }

    @Override
    public ResultDTO<String> getSignature() throws Exception {
        
        return new ResultDTO<String>().set(ResultCodeEnum.SUCCESS, "", TxVideoModule.getSignature());
    }

    @Override
    public boolean convertVodFile(String fileId) throws Exception {
        return TxVideoModule.convertVodFile(fileId);
    }

    @Override
    public String createGroup(String groupName, String tutorAccount) throws Exception {
		return TxVideoModule.createGroup(groupName, tutorAccount);
    }

    @Override
    public String mixStream(String mainStreamId, String streamId, String groupId) throws Exception {
        return TxVideoModule.mixStream(mainStreamId, streamId, groupId);
    }

    @Override
    public String createUserSig(String account) throws Exception {
        return TxVideoModule.createUserSig(account);
    }

}
