/*
 * TxVideoService.java Created on 2017年11月2日 下午1:44:29
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
package com.wst.service.tencent.service;

import com.hiwi.common.dto.ResultDTO;

/**
 * 腾讯视频服务
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public interface TxVideoService {

	/**
	 * 依照 VID 查询视频信息
	 * 
	 * @param vid
	 *            直播/互动直播系统返回的video_id
	 * @return 视频URL
	 * @throws Exception
	 */
	public ResultDTO<String[]> describeRecordPlayInfo(String vid) throws Exception;

	/**
	 * 点播WEB视频上传
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResultDTO<String> getSignature() throws Exception;

	/**
	 * 视频转码
	 * 
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public boolean convertVodFile(String fileId) throws Exception;

	/**
	 * 创建群组AVChatRoom（聊天室）
	 * 
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public String createGroup(String groupName, String tutorAccount) throws Exception;

	/**
	 * 云端混流
	 * 
	 * @param mainStreamId
	 *            主播流ID
	 * @param streamId
	 *            观众流ID
	 * @param groupId
	 *            直播对应的房间ID
	 * @return
	 * @throws Exception
	 */
	public String mixStream(String mainStreamId, String streamId, String groupId) throws Exception;

	/**
	 * 创建用户签名
	 * 
	 * @param account
	 *            用户账号
	 * @return
	 * @throws Exception
	 */
	public String createUserSig(String account) throws Exception;

}
