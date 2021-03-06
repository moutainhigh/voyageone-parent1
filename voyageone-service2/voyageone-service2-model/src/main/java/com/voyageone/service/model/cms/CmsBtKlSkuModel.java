/*
 * CmsBtKlSkuModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 考拉商品表
 */
public class CmsBtKlSkuModel extends BaseModel {
    protected String channelId;

    /**
     * 渠道id
     */
    protected String orgChannelId;

    /**
     * 商品Code
     */
    protected String productCode;

    /**
     * SKU
     */
    protected String sku;

    /**
     * 考拉的商品的key
     */
    protected String klNumiid;

    /**
     * 考拉的sku的id
     */
    protected String klSkuKey;

    /**
     * 商品条形码
     */
    protected String upc;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId == null ? null : orgChannelId.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getKlNumiid() {
        return klNumiid;
    }

    public void setKlNumiid(String klNumiid) {
        this.klNumiid = klNumiid == null ? null : klNumiid.trim();
    }

    public String getKlSkuKey() {
        return klSkuKey;
    }

    public void setKlSkuKey(String klSkuKey) {
        this.klSkuKey = klSkuKey == null ? null : klSkuKey.trim();
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc == null ? null : upc.trim();
    }
}