package com.voyageone.service.bean.cms.producttop;

import java.util.List;

/**
 * Created by dell on 2016/11/29.
 */
public class SaveTopProductParameter {

    int cartId;//平台id
    String pCatId;//商品分类
    List<String> codeList;//   /Code

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getpCatId() {
        return pCatId;
    }

    public void setpCatId(String pCatId) {
        this.pCatId = pCatId;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
    }

}
