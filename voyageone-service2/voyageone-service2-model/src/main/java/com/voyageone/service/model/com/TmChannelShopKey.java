/*
 * TmChannelShopKey.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.com;

public class TmChannelShopKey extends AdminBaseModel {
    protected String orderChannelId;

    protected Integer cartId;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId == null ? null : orderChannelId.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}