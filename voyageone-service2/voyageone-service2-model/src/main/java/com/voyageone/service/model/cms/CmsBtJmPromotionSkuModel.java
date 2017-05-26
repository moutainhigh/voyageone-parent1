/*
 * CmsBtJmPromotionSkuModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.math.BigDecimal;

/**
 * JMBTPromotionSku|| 聚美推广活动商品规格表
 */
public class CmsBtJmPromotionSkuModel extends BaseModel {
    /**
     * 渠道商id
     */
    protected String channelId;

    /**
     * 聚美活动ID
     */
    protected Integer cmsBtJmPromotionId;

    protected Integer cmsBtJmPromotionProductId;

    protected String productModel;

    protected String productCode;

    /**
     * 规格代码
     */
    protected String skuCode;

    /**
     * 市场价格 大于等于团购价
     */
    protected BigDecimal marketPrice;

    /**
     * 折扣
     */
    protected BigDecimal discount;

    /**
     * 团购价格 至少大于15元
     */
    protected BigDecimal dealPrice;

    /**
     * 同步更新异常信息
     */
    protected String errorMsg;

    /**
     * 更新聚美平台的状态:1:待更新2:同步更新完成 3:同步更新失败
     */
    protected Integer synchStatus;

    /**
     * 更新状态
     */
    protected Integer updateState;

    /**
     * 海外官网价格
     */
    protected BigDecimal msrpUsd;

    /**
     * 中国官网价格
     */
    protected BigDecimal msrpRmb;

    /**
     * 中国指导价格
     */
    protected BigDecimal retailPrice;

    /**
     * 中国最终售价
     */
    protected BigDecimal salePrice;

    /**
     * 团购价/最终售价
     */
    protected BigDecimal discount2;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCmsBtJmPromotionId() {
        return cmsBtJmPromotionId;
    }

    public void setCmsBtJmPromotionId(Integer cmsBtJmPromotionId) {
        this.cmsBtJmPromotionId = cmsBtJmPromotionId;
    }

    public Integer getCmsBtJmPromotionProductId() {
        return cmsBtJmPromotionProductId;
    }

    public void setCmsBtJmPromotionProductId(Integer cmsBtJmPromotionProductId) {
        this.cmsBtJmPromotionProductId = cmsBtJmPromotionProductId;
    }

    public String getProductModel() {
        return productModel;
    }

    public void setProductModel(String productModel) {
        this.productModel = productModel == null ? null : productModel.trim();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode == null ? null : productCode.trim();
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode == null ? null : skuCode.trim();
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Integer getSynchStatus() {
        return synchStatus;
    }

    public void setSynchStatus(Integer synchStatus) {
        this.synchStatus = synchStatus;
    }

    public Integer getUpdateState() {
        return updateState;
    }

    public void setUpdateState(Integer updateState) {
        this.updateState = updateState;
    }

    public BigDecimal getMsrpUsd() {
        return msrpUsd;
    }

    public void setMsrpUsd(BigDecimal msrpUsd) {
        this.msrpUsd = msrpUsd;
    }

    public BigDecimal getMsrpRmb() {
        return msrpRmb;
    }

    public void setMsrpRmb(BigDecimal msrpRmb) {
        this.msrpRmb = msrpRmb;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getDiscount2() {
        return discount2;
    }

    public void setDiscount2(BigDecimal discount2) {
        this.discount2 = discount2;
    }
}