/*
 * CmsBtSxCnImagesModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 上传独立域名图片一览表
 */
public class CmsBtSxCnImagesModel extends BaseModel {
    private String channelId;

    private Integer cartId;

    /**
     * 主商品code
     */
    private String code;

    /**
     * cms商品图片名
     */
    private String imageName;

    /**
     * 独立域名UrlKey,也是图片名前缀
     */
    private String urlKey;

    /**
     * 对应独立域名此商品的第几张图片,UrlKey-index是独立域名图片命名规则
     */
    private Integer index;

    /**
     * 0:等待上传, 1:已上传, 2:上传失败, 3:曾经上传过,但现在被别的图取代
     */
    private String status;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}