/*
 * CmsMtFeedCustomOptionModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsMtFeedCustomOptionModel extends BaseModel {
    protected String channelId;

    /**
     * 如果该店是渠道级属性一致，那么就设置为0
     */
    protected Integer propId;

    protected String feedValueOriginal;

    protected String feedValueTranslation;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public String getFeedValueOriginal() {
        return feedValueOriginal;
    }

    public void setFeedValueOriginal(String feedValueOriginal) {
        this.feedValueOriginal = feedValueOriginal == null ? null : feedValueOriginal.trim();
    }

    public String getFeedValueTranslation() {
        return feedValueTranslation;
    }

    public void setFeedValueTranslation(String feedValueTranslation) {
        this.feedValueTranslation = feedValueTranslation == null ? null : feedValueTranslation.trim();
    }
}