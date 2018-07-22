/*
 * CustomClassVideoJob.java Created on 2017年11月25日 下午5:54:05
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
package com.wst.task;

import java.util.Date;
import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hiwi.common.dto.ResultDTO;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.SpringUtils;
import com.wst.entity.lesson.CustomClassVideo;
import com.wst.service.lesson.service.CustomClassService;
import com.wst.service.tencent.service.TxVideoService;

/**
 * 一对一定制上课视频信息拉取
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class CustomClassVideoJob implements SimpleJob {

    private static HiwiLog log = HiwiLogFactory.getLogger(CustomClassVideoJob.class);

    @Override
    public void execute(ShardingContext context) {
        try {
            CustomClassService customClassService = SpringUtils.getBean(CustomClassService.class);
            List<CustomClassVideo> classVideos = customClassService.getPendingCustomClassVideo();
            if (classVideos == null || classVideos.size() == 0) {
                log.info("当前没有待拉取地址视频信息");
                return;
            }

            
            TxVideoService txVideoService = SpringUtils.getBean(TxVideoService.class);
            for (CustomClassVideo classVideo : classVideos) {
                ResultDTO<String[]> resultInfo = txVideoService.describeRecordPlayInfo(classVideo.getVid());
                if (!resultInfo.isSuccess() || resultInfo.getResult() == null) {
                    continue;
                }
                
                String[] result = resultInfo.getResult();
                if (result.length == 1) { // 拉取地址失败
                    classVideo.setDownloadTime(new Date());
                    classVideo.setDownloadRemark(result[0]);
                    
                    customClassService.updateCustomClassVideoFail(classVideo);
                } else if (result.length == 2) { // 拉取地址成功
                    classVideo.setVideoUrl(result[1]);
                    classVideo.setDownloadTime(new Date());
                    classVideo.setDownloadRemark(result[0]);
                    classVideo.setDownloadNum(classVideo.getDownloadNum() + 1);
                    
                    customClassService.updateCustomClassVideoSuccess(classVideo);
                }
            }
        } catch (Exception e) {
            log.error("获取视频地址异常", e);
        }
    }
}
