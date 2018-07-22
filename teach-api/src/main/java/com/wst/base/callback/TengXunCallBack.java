/*
 * TengXunCallBack.java Created on 2017年11月29日 下午2:03:11
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
package com.wst.base.callback;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.redis.RedisClient;
import com.hiwi.common.util.JsonUtils;
import com.hiwi.common.util.security.MD5Util;
import com.wst.constant.RedisConstant;
import com.wst.constant.SysConfigConstant;
import com.wst.constant.SysConfigConstant.BusiTypeEnum;
import com.wst.service.lesson.dto.CustomClassDTO;
import com.wst.service.lesson.dto.PublicClassDTO;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.lesson.service.PublicClassService;
import com.wst.service.sys.dto.BusiConfigDTO;
import com.wst.service.sys.service.BusiConfigService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 腾讯云回调
 * @author <a href="mailto:liqg@hiwitech.com">liqiangen</a>
 * @version 1.0
 */
@RestController
@RequestMapping("d-app/callback/")
public class TengXunCallBack {

    private static HiwiLog log = HiwiLogFactory.getLogger(TengXunCallBack.class);
    
    @Reference(version = "0.0.1")
    private BusiConfigService busiConfigService;
    @Reference(version = "0.0.1")
    private TxVideoService txVideoService;
    @Reference(version = "0.0.1")
    private CustomClassService customClassService;
    @Reference(version = "0.0.1")
    private PublicClassService publicClassService;
    
    public static void main(String[] args) {
        JSONObject groupDataObj = new JSONObject();
        System.out.println(groupDataObj.size());
    }
    /**
     * 腾讯云回调
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("tengxun")
    public Map<String, Object> callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        
        BufferedReader reader = request.getReader();
        StringBuffer content = new StringBuffer();
        String line = ""; // 一行数据
        while ((line = reader.readLine()) != null)
            content.append(line);
        
        log.debug("====腾讯云回调通知，接收内容：" + content);
        
        BusiConfigDTO busiQuery = new BusiConfigDTO();
        busiQuery.setBusiCode(SysConfigConstant.TENCENT_VIDEO_ACC);
        busiQuery.setBusiType(BusiTypeEnum.SYS_CONFIG.type);
        List<BusiConfigDTO> busiConfigList = busiConfigService.getConfigList(busiQuery);
        if (busiConfigList == null || busiConfigList.size() <= 0) {
            log.debug("====腾讯云配置为空");
            return null;
        }
        JSONObject configObject = JSONObject.parseObject(busiConfigList.get(0).getParams());
        
        JSONObject requestObj = JSONObject.parseObject(content.toString());
        String sign = requestObj.getString("sign");
        String t = requestObj.getString("t");
        if (!MD5Util.MD5(configObject.getString("liveKey") + t).equalsIgnoreCase(sign)) {
            log.error("====腾讯云回调通知，消息鉴权失败");
            return null;
        }
        
        int eventType = requestObj.getIntValue("event_type");   // 事件类型；0-断流；1-推流；100-录制文件生成
        String streamId = requestObj.getString("stream_id");    // 流ID
        // 获取groupId，对应房间ID，10开头表示定制课；20开头表示直播课；8位数字
        String streamParam = requestObj.getString("stream_param");
        int groupIdIndex = streamParam.indexOf("groupid=");
        String groupId = null;
        if (groupIdIndex > -1) {
            groupId = streamParam.substring(groupIdIndex + 8, groupIdIndex + 16);
        } else {    // 混流是无groupId的
            groupIdIndex = streamId.indexOf("_MIX_");
            groupId = streamId.substring(groupIdIndex + 5, groupIdIndex + 13);
        }
        log.debug("====腾讯云回调通知，groupId：" + groupId);
        
        if (eventType == 1) {   // 推流
            if (groupId.indexOf("10") == 0) {   // 定制课
                String groupData = RedisClient.get(RedisConstant.LIVE_GROUP + groupId);
                JSONObject groupDataObj = JSONObject.parseObject(groupData);
                if (groupDataObj == null || groupDataObj.size() < 2) {
                    if (groupDataObj == null) {
                        groupDataObj = new JSONObject();
                    }
                    if (groupDataObj.size() == 0) {
                        groupDataObj.put("streamId1", streamId);
                    } else{
                        groupDataObj.put("streamId2", streamId);
                    }
                    RedisClient.set(RedisConstant.LIVE_GROUP + groupId, JsonUtils.getJson(groupDataObj));
                    RedisClient.expire(RedisConstant.LIVE_GROUP + groupId, RedisConstant.LIVE_GROUP_EXPIRE);
                }
            } else if (groupId.indexOf("20") == 0) {    // 直播课
                RedisClient.set(RedisConstant.LIVE_GROUP + groupId, streamId);
                RedisClient.expire(RedisConstant.LIVE_GROUP + groupId, RedisConstant.LIVE_GROUP_EXPIRE);
            }
        }
        if (eventType == 100) { // 新的录制文件已生成
            String videoUrl = requestObj.getString("video_url");
            if (!videoUrl.contains("https")) {
                videoUrl = videoUrl.replace("http", "https");
            }
//            log.debug("====腾讯云回调通知，接收到新文件：" + videoUrl);
            if (groupId.indexOf("10") == 0) {   // 定制课
                log.debug("====腾讯云回调通知，定制课获取到视频文件：" + videoUrl);
                String groupData = RedisClient.get(RedisConstant.LIVE_GROUP + groupId);
                JSONObject groupDataObj = JSONObject.parseObject(groupData);
                if (groupDataObj == null) {
                    log.error("====腾讯云回调通知，定制课对应的redis信息为空，groupId：" + groupId);
                    return result;
                }
                
                CustomClassDTO customClassDTO = customClassService.getByRoomId(Integer.valueOf(groupId));
                
                String movieUrl = customClassDTO.getMovieUrl();
                String streamId1Redis = groupDataObj.getString("streamId1"); // 导师流ID
                String streamId2Redis = groupDataObj.getString("streamId2"); // 学生流ID
                
                // 数据库中的流1、流2
                String moiveUrl1 = "";
                String moiveUrl2 = "";
                if (StringUtils.isNotEmpty(movieUrl)) {
                    String[] movieUrlArr = movieUrl.split(",");
                    moiveUrl1 = movieUrlArr[0];
                    if (movieUrlArr.length > 1) {
                        moiveUrl2 = movieUrlArr[1];
                    }
                }
                
                if (streamId1Redis.equals(streamId)) {
                    moiveUrl1 = videoUrl;
                } else if (streamId2Redis.equals(streamId)){
                    moiveUrl2 = videoUrl;
                }
                customClassService.upMovieUrl(customClassDTO.getClassId(), moiveUrl1 + "," + moiveUrl2);
            } else if (groupId.indexOf("20") == 0) {    // 直播课
                log.debug("====腾讯云回调通知，直播课获取到视频文件：" + videoUrl);
                
                PublicClassDTO publicClassDTO = publicClassService.getByRoomId(Integer.valueOf(groupId));
                if (publicClassDTO != null) {
                    publicClassService.upMoveUrlById(videoUrl, "", publicClassDTO.getClassId());
                }
            }
        }
        
        return result;
    }
}
