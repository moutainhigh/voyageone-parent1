/*
 * BiReportSalesShop010Key.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

public class BiReportSalesShop010Key extends BaseModel {
    /**
     * 日期
     */
    protected Date date;

    /**
     * 渠道id
     */
    protected Integer shopId;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}