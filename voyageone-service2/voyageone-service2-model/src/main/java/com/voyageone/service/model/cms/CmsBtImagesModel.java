/*
 * CmsBtImagesModel.java
 * Copyright(C) 20xx-2015 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 * 2016-05-06 Created
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 *
 */
public class CmsBtImagesModel extends BaseModel {
    private String channelId;

    private String code;

    private String originalUrl;

    private String imgName;

    private Integer updFlg;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl == null ? null : originalUrl.trim();
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName == null ? null : imgName.trim();
    }

    public Integer getUpdFlg() {
        return updFlg;
    }

    public void setUpdFlg(Integer updFlg) {
        this.updFlg = updFlg;
    }
}