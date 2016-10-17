/*
 * CmsBtTagJmModuleExtensionModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * 
 */
public class CmsBtTagJmModuleExtensionModel extends BaseModel {
    protected Integer tagId;

    /**
     * 模块标题
     */
    protected String moduleTitle;

    /**
     * 模块是否隐藏：不隐藏=1，预热时隐藏=2，正式时隐藏=3，自定义显示=0
     */
    protected Integer hideFlag;

    /**
     * 显示起始时间
     */
    protected Date displayStartTime;

    /**
     * 显示结束时间
     */
    protected Date displayEndTime;

    /**
     * 货架类型：普通货架=1
     */
    protected Integer shelfType;

    /**
     * 图片类型：方图=1，竖图=2
     */
    protected Integer imageType;

    /**
     * 商品排序：按销量降序=1，填写顺序=2
     */
    protected Integer productsSortBy;

    /**
     * 无库存的自动排至最后
     */
    protected Boolean noStockToLast;

    /**
     * 是否为主推
     */
    protected Byte featured;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle == null ? null : moduleTitle.trim();
    }

    public Integer getHideFlag() {
        return hideFlag;
    }

    public void setHideFlag(Integer hideFlag) {
        this.hideFlag = hideFlag;
    }

    public Date getDisplayStartTime() {
        return displayStartTime;
    }

    public void setDisplayStartTime(Date displayStartTime) {
        this.displayStartTime = displayStartTime;
    }

    public Date getDisplayEndTime() {
        return displayEndTime;
    }

    public void setDisplayEndTime(Date displayEndTime) {
        this.displayEndTime = displayEndTime;
    }

    public Integer getShelfType() {
        return shelfType;
    }

    public void setShelfType(Integer shelfType) {
        this.shelfType = shelfType;
    }

    public Integer getImageType() {
        return imageType;
    }

    public void setImageType(Integer imageType) {
        this.imageType = imageType;
    }

    public Integer getProductsSortBy() {
        return productsSortBy;
    }

    public void setProductsSortBy(Integer productsSortBy) {
        this.productsSortBy = productsSortBy;
    }

    public Boolean getNoStockToLast() {
        return noStockToLast;
    }

    public void setNoStockToLast(Boolean noStockToLast) {
        this.noStockToLast = noStockToLast;
    }

    public Byte getFeatured() {
        return featured;
    }

    public void setFeatured(Byte featured) {
        this.featured = featured;
    }
}