/*
 * CmsMtChannelSkuConfigModel.java
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
public class CmsMtChannelSkuConfigModel extends BaseModel {
    protected String channelId;

    protected Integer customSizeId;

    protected String customPropId;

    protected String customPropValue;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCustomSizeId() {
        return customSizeId;
    }

    public void setCustomSizeId(Integer customSizeId) {
        this.customSizeId = customSizeId;
    }

    public String getCustomPropId() {
        return customPropId;
    }

    public void setCustomPropId(String customPropId) {
        this.customPropId = customPropId == null ? null : customPropId.trim();
    }

    public String getCustomPropValue() {
        return customPropValue;
    }

    public void setCustomPropValue(String customPropValue) {
        this.customPropValue = customPropValue == null ? null : customPropValue.trim();
    }
}