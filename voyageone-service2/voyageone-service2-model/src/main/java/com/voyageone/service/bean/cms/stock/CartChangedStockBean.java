package com.voyageone.service.bean.cms.stock;


/**
 * 变化的店铺库存信息
 *         WMS => CMS 发送MQ
 *
 * @author Wangtd 2017/03/17
 * @version 1.0.0
 *
 * @author piao
 *
 */
public class CartChangedStockBean {

    private String channelId;

    private Integer cartId;

    private String sku;

    private String itemCode;

    private Integer qty;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

}
