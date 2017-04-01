/*
 * CmsBtSizeChartImageGroupModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 尺码表和尺码图片组关联表 一一对应
 */
public class CmsBtSizeChartImageGroupModel extends BaseModel {
    protected String channelId;

    protected Integer cartId;

    /**
     * 尺码表
     */
    protected Integer cmsBtSizeChartId;

    /**
     * 尺码图片组
     */
    protected Long cmsBtImageGroupId;

    /**
     * 尺码图片组 - APP
     */
    protected Long cmsBtImageGroupIdApp;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getCmsBtSizeChartId() {
        return cmsBtSizeChartId;
    }

    public void setCmsBtSizeChartId(Integer cmsBtSizeChartId) {
        this.cmsBtSizeChartId = cmsBtSizeChartId;
    }

    public Long getCmsBtImageGroupId() {
        return cmsBtImageGroupId;
    }

    public void setCmsBtImageGroupId(Long cmsBtImageGroupId) {
        this.cmsBtImageGroupId = cmsBtImageGroupId;
    }

    public Long getCmsBtImageGroupIdApp() {
        return cmsBtImageGroupIdApp;
    }

    public void setCmsBtImageGroupIdApp(Long cmsBtImageGroupIdApp) {
        this.cmsBtImageGroupIdApp = cmsBtImageGroupIdApp;
    }
}