package com.voyageone.service.bean.cms.businessmodel;


import com.voyageone.service.model.cms.CmsBtJmSkuModel;

public class CmsBtJmImportSku extends CmsBtJmSkuModel {
    double dealPrice;
    double marketPrice;
    public double getMarketPrice() {
        return marketPrice;
    }
    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }
    public double getDealPrice() {
        return dealPrice;
    }
    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }
}