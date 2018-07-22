/**
 * @{#} Ad.java Created on 2017-09-25 21:54:17
 *
 * Copyright (c) 2017 by HIWI software.
 */
package com.wst.entity.operate;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告营销表
 * 
 * @author masb 对应表(OPERATE_AD)
 */
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 广告id
     */
    protected long adId;

    /**
     * 广告位（1-首页banner）
     */
    protected long adPosition;

    /**
     * 广告图片
     */
    protected String adPic;

    /**
     * 广告链接
     */
    protected String adUrl;

    /**
     * 广告名称
     */
    protected String adName;

    /**
     * 权重
     */
    protected long weight;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 修改时间
     */
    protected Date modifyTime;

    /**
     * 有效期
     */
    protected Date validTime;

    /**
     * 状态（1-有效；2-无效）
     */
    protected long adStatus;

    /** 无参构造方法 */
    public Ad() {
    }

    /**
     * 属性get||set方法
     */
    public long getAdId() {
        return this.adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public long getAdPosition() {
        return this.adPosition;
    }

    public void setAdPosition(long adPosition) {
        this.adPosition = adPosition;
    }

    public String getAdPic() {
        return this.adPic;
    }

    public void setAdPic(String adPic) {
        this.adPic = adPic;
    }

    public String getAdUrl() {
        return this.adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdName() {
        return this.adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public long getWeight() {
        return this.weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getValidTime() {
        return this.validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public long getAdStatus() {
        return this.adStatus;
    }

    public void setAdStatus(long adStatus) {
        this.adStatus = adStatus;
    }

}