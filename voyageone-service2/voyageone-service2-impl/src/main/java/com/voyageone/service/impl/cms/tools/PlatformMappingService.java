package com.voyageone.service.impl.cms.tools;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.service.dao.cms.mongo.CmsBtPlatformMappingDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询, 获取和编辑平台类目的属性匹配。根据匹配计算商品属性值
 * <p>
 * Created by jonas on 8/13/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
public class PlatformMappingService extends BaseService {

    private final static int CATEGORY_TYPE_COMMON = 1;

    private final static int CATEGORY_TYPE_SPECIFIC = 2;

    private final static String EXPRESSION_TYPE_FEED_CN = "FEED_CN";

    private final static String EXPRESSION_TYPE_FEED_ORG = "FEED_ORG";

    private final static String EXPRESSION_TYPE_MASTER = "MASTER";

    private final ProductService productService;

    private final CmsBtPlatformMappingDao platformMappingDao;

    @Autowired
    public PlatformMappingService(ProductService productService, CmsBtPlatformMappingDao platformMappingDao) {
        this.productService = productService;
        this.platformMappingDao = platformMappingDao;
    }

    boolean saveMap(CmsBtPlatformMappingModel fieldMapsModel) {

        WriteResult writeResult;

        if (platformMappingDao.exists(fieldMapsModel))
            writeResult = platformMappingDao.update(fieldMapsModel);
        else
            writeResult = platformMappingDao.insert(fieldMapsModel);

        return writeResult.getN() > 0;
    }

    public Map<String, Object> getValueMap(String channelId, Long productId, int cartId) {

        // 查询需要用到的平台类目也在商品中获取

        CmsBtProductModel product = productService.getProductById(channelId, productId);

        CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);

        CmsBtPlatformMappingModel fieldMapsModel = platformMappingDao.selectOne(cartId, CATEGORY_TYPE_SPECIFIC, cart.getpCatId(), channelId);

        CmsBtPlatformMappingModel commonFieldMapsModel = platformMappingDao.selectOne(cartId, CATEGORY_TYPE_COMMON, cart.getpCatId(), channelId);

        Map<String, Object> valueMap = new HashMap<>();

        if (fieldMapsModel != null)
            fillValueMap(valueMap, product, fieldMapsModel);

        if (commonFieldMapsModel != null)
            fillValueMap(valueMap, product, commonFieldMapsModel);

        return valueMap;
    }

    private void fillValueMap(Map<String, Object> valueMap, CmsBtProductModel product, CmsBtPlatformMappingModel fieldMapsModel) {

        // 循环所有配置
        // 如果没有表达式配置, 就简单的使用 value 作为值
        // 如果有表达式配置, 需要循环表达式, 根据配置类型去商品获取值进行拼接

        List<CmsBtPlatformMappingModel.FieldMapping> mappingList = fieldMapsModel.getMappings();

        if (mappingList == null || mappingList.isEmpty())
            return;

        // 当映射里有 Master / Feed 时, 需要使用商品信息

        CmsBtProductModel_Feed feed = product.getFeed();

        BaseMongoMap<String, Object> cnAtts = feed.getCnAtts();

        BaseMongoMap<String, Object> orgAtts = feed.getOrgAtts();

        CmsBtProductModel_Common common = product.getCommon();

        CmsBtProductModel_Field master = common.getFields();

        for (CmsBtPlatformMappingModel.FieldMapping mapping: mappingList) {

            List<CmsBtPlatformMappingModel.FieldMappingExpression> expressionList = mapping.getExpressions();

            if (expressionList == null) {
                valueMap.put(mapping.getFieldId(), mapping.getValue());
                continue;
            }

            StringBuilder valueBuilder = new StringBuilder();

            for (CmsBtPlatformMappingModel.FieldMappingExpression expression: expressionList) {

                String key = expression.getValue();

                switch (expression.getType()) {

                    case EXPRESSION_TYPE_FEED_CN:
                        valueBuilder.append(String.valueOf(cnAtts.get(key)));
                        break;

                    case EXPRESSION_TYPE_FEED_ORG:
                        valueBuilder.append(String.valueOf(orgAtts.get(key)));
                        break;

                    case EXPRESSION_TYPE_MASTER:
                        valueBuilder.append(master.getStringAttribute(key));
                        break;

                    default:
                        valueBuilder.append(expression.getValue());
                        break;
                }

                valueBuilder.append(expression.getAppend());
            }

            valueMap.put(mapping.getFieldId(), valueBuilder.toString());
        }
    }
}