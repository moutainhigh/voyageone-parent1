package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateCategoryMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 2017/7/17.
 */
@Service
@RabbitListener()
public class CmsBtProductUpdateCategoryMQJob extends TBaseMQCmsService<CmsBtProductUpdateCategoryMQMessageBody> {

    private final ProductService productService;

    @Autowired
    public CmsBtProductUpdateCategoryMQJob(SxProductService sxProductService, ProductService productService) {
        this.productService = productService;
    }
    @Override
    public void onStartup(CmsBtProductUpdateCategoryMQMessageBody messageBody) throws Exception {
        if ( messageBody != null){
            List<String> productCodes = messageBody.getProductCodes();
            if (ListUtils.notNull(productCodes)){
                super.count = productCodes.size();
                for (String productCode : productCodes) {
                    update(productCode,messageBody);
                }
            }
        }
    }
    private void update(String productCode, CmsBtProductUpdateCategoryMQMessageBody messageBody){
        Integer cartId = messageBody.getCartId();
        List<BulkUpdateModel> bulkList = new ArrayList<>(1);
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("usPlatforms.P" + cartId + ".pCatPath", messageBody.getpCatPath());
        updateMap.put("usPlatforms.P" + cartId + ".pCatId", messageBody.getpCatId());
        updateMap.put("usPlatforms.P" + cartId + ".pCatStatus", 1);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", productCode);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        productService.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, messageBody.getSender(), "$set");
    }
}
