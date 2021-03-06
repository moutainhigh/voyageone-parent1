package com.voyageone.service.bean.vms.channeladvisor.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;

import java.math.BigDecimal;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderItemModel extends CABaseModel {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("SellerSku")
    private String sellerSku;

    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("UnitPrice")
    private BigDecimal unitPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerSku() {
        return sellerSku;
    }

    public void setSellerSku(String sellerSku) {
        this.sellerSku = sellerSku;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
