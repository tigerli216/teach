/*
 * CustomClassVideo.java Created on 2017年11月25日 下午5:22:09
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
package com.wst.entity.lesson;

import java.io.Serializable;
import java.util.Date;

/**
 * 待拉取得视频地址数据
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
public class CustomClassVideoDtl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 详情ID
     */
    protected long hisId;
    
    /**
     * 记录ID
     */
    protected long videoId;
    
    /**
     * 课时ID
     */
    protected long classId;
    
    /**
     * 腾讯云返回的视频VID
     */
    protected String vid;
    
    /**
     * 视频上传时间
     */
    protected Date uploadTime;
    
    /**
     * 拉取的视频地址
     */
    protected String videoUrl;
    
    /**
     * 视频拉取时间
     */
    protected Date downloadTime;

    /**
     * 视频拉取次数
     */
    protected int downloadNum;
    
    /**
     * 视频拉取结果
     */
    protected String downloadRemark;
    
    public long getHisId() {
		return hisId;
	}

	public void setHisId(long hisId) {
		this.hisId = hisId;
	}

	public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    public int getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(int downloadNum) {
        this.downloadNum = downloadNum;
    }

    public String getDownloadRemark() {
        return downloadRemark;
    }

    public void setDownloadRemark(String downloadRemark) {
        this.downloadRemark = downloadRemark;
    }
    
}
