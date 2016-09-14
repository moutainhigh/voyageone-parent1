package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.service.bean.vms.channeladvisor.product.FieldModel;
import com.voyageone.service.dao.cms.mongo.CmsBtCAdProductDao;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsBtCAdProudctModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/9/13.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CA_Feed_Analysis)
public class CaAnalysisService extends BaseMQCmsService {

    @Autowired
    private CmsBtCAdProductDao cmsBtCAdProductDao;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = messageMap.get("channelId").toString();
        List<String> sellerSKUs = (List<String>) messageMap.get("sellerSKUs");
        List<CmsBtCAdProudctModel> feedList = cmsBtCAdProductDao.getProduct(channelId, sellerSKUs);

    }

    private List<CmsBtFeedInfoModel> convertToFeedInfo(String channelId, CmsBtCAdProudctModel feed) {

        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        feed.getBuyableProducts().forEach(sku -> {
            CmsBtFeedInfoModel cmsBtFeedInfoModel = new CmsBtFeedInfoModel();
            cmsBtFeedInfoModel.setModel(feed.getSellerSKU());
            cmsBtFeedInfoModel.setBrand(getFieldValueByName(feed.getFields(), "brand"));
            cmsBtFeedInfoModel.setCategory(getFieldValueByName(feed.getFields(), "category").replaceAll("-", "－").replaceAll(" : ", "-"));
            cmsBtFeedInfoModel.setColor(getFieldValueByName(sku.getFields(), "color"));
            cmsBtFeedInfoModel.setCode(feed.getSellerSKU() + (StringUtil.isEmpty(cmsBtFeedInfoModel.getCode()) ? "" : "-" + cmsBtFeedInfoModel.getCode()));
            cmsBtFeedInfoModel.setLongDescription(getFieldValueByName(feed.getFields(), "description"));
            cmsBtFeedInfoModel.setName(getFieldValueByName(feed.getFields(), "title"));
            cmsBtFeedInfoModel.setImage(getImages(feed.getFields()));

            List<CmsBtFeedInfoModel_Sku> skus = new ArrayList<CmsBtFeedInfoModel_Sku>();
            CmsBtFeedInfoModel_Sku feedSku = new CmsBtFeedInfoModel_Sku();
            Double price = 0.0;
            if (!StringUtil.isEmpty(getFieldValueByName(sku.getFields(), "price"))) {
                price = Double.parseDouble(getFieldValueByName(sku.getFields(), "price"));
            }
            feedSku.setPriceNet(price);
            feedSku.setClientSku(sku.getSellerSKU());
            feedSku.setQty(sku.getQuantity());
            feedSku.setSize(getFieldValueByName(sku.getFields(), "size"));
            feedSku.setSku(channelId + "-" + sku.getSellerSKU());
            feedSku.setImage(getImages(feed.getFields()));


            List<FieldModel> temp = feed.getFields();
            temp.addAll(sku.getFields());
            cmsBtFeedInfoModel.setAttribute(getAtt(temp));
            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(BaseAnalysisService.attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }
        });
        return modelBeans;

    }


    private String getFieldValueByName(List<FieldModel> fields, String name) {
        FieldModel field = fields.stream().filter(fieldModel -> fieldModel.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (field != null) {
            return field.getValue();
        }
        return "";
    }

    private List<String> getImages(List<FieldModel> fields) {
        List<String> images = new ArrayList<>();
        for (int i = 1; i < 20; i++) {

            String image = getFieldValueByName(fields, "productgroupimageurl" + i);
            if (!StringUtil.isEmpty(image)) {
                images.add(image);
            } else {
                break;
            }
        }
        return images;
    }

    private Map<String, List<String>> getAtt(List<FieldModel> fields) {

        Map<String, List<String>> attribute = new HashMap<>();
        fields.forEach(fieldModel -> {
            if(!StringUtil.isEmpty(fieldModel.getValue())){
                if(attribute.containsKey(fieldModel.getName())){
                    List<String> temp = attribute.get(fieldModel.getName());
                    temp.add(fieldModel.getValue());
                    attribute.put(fieldModel.getName(),temp);
                }else{
                    List<String> values = new ArrayList<>();
                    values.add(fieldModel.getValue());
                    attribute.put(fieldModel.getName(), values);
                }

            }
        });
        return attribute;
    }

}
