package com.voyageone.service.impl.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.JMProductUpdateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductUpdateService {
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmProductImagesDao daoCmsBtJmProductImages;
    @Autowired
    CmsMtMasterInfoDao daoCmsMtMasterInfo;

    public CmsBtJmPromotionModel getCmsBtJmPromotion(int id)
    {
       return daoCmsBtJmPromotion.select(id);
    }
    public  int getShippingSystemId(String ChannelId)
    {
             return  2121;
    }
    public  List<CmsBtJmPromotionProductModel> getListPromotionProduct() {
        Map<String, Object> parameterPromotionProduct = new HashMap<>();
        List<CmsBtJmPromotionProductModel> listPromotionProduct = daoCmsBtJmPromotionProduct.selectList(parameterPromotionProduct);
        return  listPromotionProduct;
    }
    public JMProductUpdateInfo getJMProductUpdateInfo(CmsBtJmPromotionProductModel promotionProductModel) {
        JMProductUpdateInfo info = new JMProductUpdateInfo();
        info.setModelCmsBtJmPromotionProduct(promotionProductModel);

        //CmsBtJmProduct
        CmsBtJmProductModel productModel = daoCmsBtJmProduct.select(promotionProductModel.getId());
        info.setModelCmsBtJmProduct(productModel);

        //CmsBtJmPromotionSku   list
        Map<String, Object> parameterPromotionSku = new HashMap<>();
        parameterPromotionSku.put("cmsBtJmPromotionId", promotionProductModel.getCmsBtJmPromotionId());
        parameterPromotionSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        List<CmsBtJmPromotionSkuModel> listPromotionSku = daoCmsBtJmPromotionSku.selectList(parameterPromotionSku);
        info.setListCmsBtJmPromotionSku(listPromotionSku);



        //CmsBtJmSku   list
        Map<String, Object> parameterSku = new HashMap<>();
        parameterSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        List<CmsBtJmSkuModel> listSkuModel = daoCmsBtJmSku.selectList(parameterSku);
        info.setListCmsBtJmSku(listSkuModel);

        //tCmsBtJmProductImages  list
        Map<String, Object> parameterJmProductImages = new HashMap<>();
        parameterJmProductImages.put("channelId", promotionProductModel.getChannelId());
        parameterJmProductImages.put("productCode", promotionProductModel.getProductCode());
        List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel = daoCmsBtJmProductImages.selectList(parameterJmProductImages);
        info.setListCmsBtJmProductImages(listCmsBtJmProductImagesModel);


        //CmsMtMasterInfo  list
        Map<String, Object> parameterCmsMtMasterInfo = new HashMap<>();
        parameterCmsMtMasterInfo.put("platformId", 27);
        parameterCmsMtMasterInfo.put("channelId", promotionProductModel.getChannelId());
        parameterCmsMtMasterInfo.put("brandName", productModel.getBrandName());
        parameterCmsMtMasterInfo.put("productType", productModel.getProductType());
        List<CmsMtMasterInfoModel> listCmsMtMasterInfoModel = daoCmsMtMasterInfo.selectList(parameterCmsMtMasterInfo);
        info.setListCmsMtMasterInfo(listCmsMtMasterInfoModel);

        return info;
    }
}
