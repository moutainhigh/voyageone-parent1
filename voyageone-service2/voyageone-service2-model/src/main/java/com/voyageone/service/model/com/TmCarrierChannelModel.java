/*
 * TmCarrierChannelModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

/**
 * 
 */
public class TmCarrierChannelModel extends TmCarrierChannelKey {
    protected String apiKey;

    protected String apiUser;

    protected String apiPwd;

    protected String cardNumber;

    protected String cusite;

    protected String cusname;

    protected String customer;

    protected String usekd100Flg;

    protected String allowDeleteFlg;

    protected String wsdlUrl;

    protected String comments;
    
    protected Boolean active;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey == null ? null : apiKey.trim();
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser == null ? null : apiUser.trim();
    }

    public String getApiPwd() {
        return apiPwd;
    }

    public void setApiPwd(String apiPwd) {
        this.apiPwd = apiPwd == null ? null : apiPwd.trim();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber == null ? null : cardNumber.trim();
    }

    public String getCusite() {
        return cusite;
    }

    public void setCusite(String cusite) {
        this.cusite = cusite == null ? null : cusite.trim();
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname == null ? null : cusname.trim();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getUsekd100Flg() {
        return usekd100Flg;
    }

    public void setUsekd100Flg(String usekd100Flg) {
        this.usekd100Flg = usekd100Flg == null ? null : usekd100Flg.trim();
    }

    public String getAllowDeleteFlg() {
        return allowDeleteFlg;
    }

    public void setAllowDeleteFlg(String allowDeleteFlg) {
        this.allowDeleteFlg = allowDeleteFlg == null ? null : allowDeleteFlg.trim();
    }

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl == null ? null : wsdlUrl.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
}