/*
 * TxVideoReqDTO.java Created on 2017年11月1日 下午8:09:43
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
package com.wst.service.tencent.dto;

/**
 * 腾讯视频上传请求信息
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class TxVideoReqDTO extends TencentBaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 视频文件地址
     */
    private String videoFile;

    /**
     * 视频封面地址
     */
    private String videoCover;

    public String getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    @Override
    public String toString() {
        return "TxVideoReqDTO [videoFile=" + videoFile + ", videoCover=" + videoCover + "]";
    }

}
