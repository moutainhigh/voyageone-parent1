package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionSkuService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by james on 2016/10/19.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_JmSynPromotionDealPrice)
public class CmsSynJmPromotionDealPriceService extends BaseMQCmsService {

    @Autowired
    private CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;

    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        Integer jmPromotionId = (Integer) messageMap.get("jmPromotionId");

        Map<String, Object> param = new HashedMap();
        param.put("cmsBtJmPromotionId", jmPromotionId);
        List<CmsBtJmPromotionSkuModel> skus = cmsBtJmPromotionSkuService.selectList(param);
        skus.sort((o1, o2) -> o1.getProductCode().compareToIgnoreCase(o2.getProductCode()));

        CmsBtProductModel cacheProduct = null;
        for (CmsBtJmPromotionSkuModel sku : skus) {
            if (cacheProduct == null || sku.getProductCode().compareToIgnoreCase(cacheProduct.getCommon().getFields().getCode()) != 0) {
                cacheProduct = productService.getProductByCode(sku.getChannelId(), sku.getProductCode());
            }
            BaseMongoMap<String, Object> productSku = cacheProduct.getPlatform(CartEnums.Cart.JM).getSkus()
                    .stream().filter(pSku -> sku.getSkuCode().equalsIgnoreCase(pSku.getStringAttribute("skuCode")))
                    .findFirst().orElse(null);


            CmsBtProductModel_Sku cmsBtProductModelSku = cacheProduct.getCommon().getSkus().stream().filter(cmsBtProductModel_sku -> sku.getSkuCode().equalsIgnoreCase(cmsBtProductModel_sku.getSkuCode()))
                    .findFirst().orElse(null);
            boolean editFlg = false;
            if(productSku != null){
                if (sku.getMsrpRmb().doubleValue() != productSku.getDoubleAttribute("priceMsrp")){
                    sku.setMsrpRmb(new BigDecimal(productSku.getDoubleAttribute("priceMsrp")));
                    editFlg = true;
                }
                if (sku.getRetailPrice().doubleValue() != productSku.getDoubleAttribute("priceRetail")){
                    sku.setRetailPrice(new BigDecimal(productSku.getDoubleAttribute("priceRetail")));
                    editFlg = true;
                }
                if (sku.getSalePrice().doubleValue() != productSku.getDoubleAttribute("priceSale")){
                    sku.setSalePrice(new BigDecimal(productSku.getDoubleAttribute("priceSale")));
                    editFlg = true;
                }
            }
            if(cmsBtProductModelSku != null){
                if (sku.getMsrpUsd().doubleValue() != cmsBtProductModelSku.getClientMsrpPrice()){
                    sku.setSalePrice(new BigDecimal(cmsBtProductModelSku.getClientMsrpPrice()));
                    editFlg = true;
                }
            }
            if(editFlg){
                $info(String.format("sku:%s 价格有变化 MsrpUsd:%d MsrpRmb:%d RetailPrice:%d SalePrice:%d",sku.getSkuCode(), sku.getMsrpUsd().doubleValue(), sku.getMsrpRmb().doubleValue(), sku.getRetailPrice().doubleValue(), sku.getSalePrice().doubleValue()));
                cmsBtJmPromotionSkuService.updateWithDiscount(sku, sku.getChannelId(),getTaskName());
            }

        }


    }

}
