/*
 * TmChannelShopConfigKey.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

public class TmChannelShopConfigKey extends AdminBaseModel {
    protected String orderChannelId;

    protected String cartId;

    protected String cfgName;

    protected String cfgVal1;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId == null ? null : orderChannelId.trim();
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId == null ? null : cartId.trim();
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName == null ? null : cfgName.trim();
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1 == null ? null : cfgVal1.trim();
    }
}