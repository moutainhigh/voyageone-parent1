package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductVoRateUpdateMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 当产品的vo扣点变更时的处理，计算指导价，记录价格变更履历/记录商品修改历史/同步价格范围/插入上新程序
 * 参数 channelId, codeList, voRate, creater
 *
 * @author jiangjusheng on 2016/08/01
 * @version 2.0.0
 */
@Service
public class CmsProductVoRateUpdateService extends BaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CmsBtPriceLogService cmsBtPriceLogService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private SxProductService sxProductService;

    public List<CmsBtOperationLogModel_Msg> updateProductVoRate(ProductVoRateUpdateMQMessageBody messageBody) throws Exception {

        $info("CmsProductVoRateUpdateService start");

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();

        String channelId = StringUtils.trimToNull(messageBody.getChannelId());
        List<String> codeList = messageBody.getCodeList();
        String creator = StringUtils.trimToEmpty(messageBody.getSender());

        String voRate = messageBody.getVoRate();
        String msg;
        if (voRate == null) {
            msg = "高价检索 批量更新VO扣点 清空";
        } else {
            msg = "高价检索 批量更新VO扣点 " + voRate;
        }

        JongoQuery queryObj = new JongoQuery();
        JongoUpdate updObj = new JongoUpdate();

        List<TypeChannelBean> cartTypeList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
        for (TypeChannelBean cartObj : cartTypeList) {
            int cartId = NumberUtils.toInt(cartObj.getValue());
            for (String prodCode : codeList) {
                queryObj.setQuery("{'common.fields.code':#,'platforms.P#':{$exists:true}}");
                queryObj.setParameters(prodCode, cartId);
                queryObj.setProjectionExt("prodId", "channelId", "orgChannelId", "platforms.P" + cartId + ".skus", "common.fields", "common.skus");
                CmsBtProductModel prodObj = productService.getProductByCondition(channelId, queryObj);
                if (prodObj == null) {

                    $warn("CmsProductVoRateUpdateService 产品不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg("产品不存在");
                    failList.add(errorInfo);
                    continue;
                }
                List<BaseMongoMap<String, Object>> skuList = prodObj.getPlatform(cartId).getSkus();
                if (skuList == null || skuList.isEmpty()) {

                    $warn("CmsProductVoRateUpdateService 产品sku数据不存在 channelId=%s, code=%s, cartId=%d", channelId, prodCode, cartId);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg(String.format("产品sku数据不存在 cartId=%d", cartId));
                    continue;
                }

                // 计算指导价
                try {
                    priceService.setPrice(prodObj, cartId, false);
                } catch (Exception exp) {

                    $error(String.format("CmsProductVoRateUpdateService 调用共通函数计算指导价时出错 channelId=%s, code=%s, cartId=%d, errmsg=%s", channelId, prodCode, cartId, exp.getMessage()), exp);

                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(prodCode);
                    errorInfo.setMsg(String.format("调用共通函数计算指导价时出错 cartId=%d, errmsg=%s", cartId, exp.getMessage()));
                    continue;
                }

                // 保存计算结果
                updObj.setQuery("{'common.fields.code':#}");
                updObj.setQueryParameters(prodCode);
                updObj.setUpdate("{$set:{'platforms.P#.skus':#}}");
                updObj.setUpdateParameters(cartId, prodObj.getPlatform(cartId).getSkus());
                WriteResult rs = productService.updateFirstProduct(updObj, channelId);
                $debug("CmsProductVoRateUpdateService 保存计算结果 " + rs.toString());

                // 记录价格变更履历/同步价格范围
                List<CmsBtPriceLogModel> logModelList = new ArrayList<>(1);
                for (BaseMongoMap skuObj : skuList) {
                    String skuCode = skuObj.getStringAttribute("skuCode");
                    CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
                    cmsBtPriceLogModel.setChannelId(channelId);
                    cmsBtPriceLogModel.setProductId(prodObj.getProdId().intValue());
                    cmsBtPriceLogModel.setCode(prodCode);
                    cmsBtPriceLogModel.setCartId(cartId);
                    cmsBtPriceLogModel.setSku(skuCode);
                    cmsBtPriceLogModel.setSalePrice(skuObj.getDoubleAttribute("priceSale"));
                    cmsBtPriceLogModel.setMsrpPrice(skuObj.getDoubleAttribute("priceMsrp"));
                    cmsBtPriceLogModel.setRetailPrice(skuObj.getDoubleAttribute("priceRetail"));
                    CmsBtProductModel_Sku comSku = prodObj.getCommonNotNull().getSku(skuCode);
                    if (comSku == null) {
                        cmsBtPriceLogModel.setClientMsrpPrice(0d);
                        cmsBtPriceLogModel.setClientRetailPrice(0d);
                        cmsBtPriceLogModel.setClientNetPrice(0d);
                    } else {
                        cmsBtPriceLogModel.setClientMsrpPrice(comSku.getClientMsrpPrice());
                        cmsBtPriceLogModel.setClientRetailPrice(comSku.getClientRetailPrice());
                        cmsBtPriceLogModel.setClientNetPrice(comSku.getClientNetPrice());
                    }
                    cmsBtPriceLogModel.setComment("高级检索批量更新VO扣点");
                    cmsBtPriceLogModel.setCreated(new Date());
                    cmsBtPriceLogModel.setCreater(creator);
                    cmsBtPriceLogModel.setModified(new Date());
                    cmsBtPriceLogModel.setModifier(creator);
                    logModelList.add(cmsBtPriceLogModel);
                }
                int cnt = cmsBtPriceLogService.addLogListAndCallSyncPriceJob(logModelList);
                $debug("CmsProductVoRateUpdateService修改商品价格 记入价格变更履历结束 结果=" + cnt);
            }
        }

        // 记录商品修改历史
        $debug("CmsProductVoRateUpdateService 开始记入价格变更履历");
        long sta = System.currentTimeMillis();
        productStatusHistoryService.insertList(channelId, codeList, -1, EnumProductOperationType.BatchUpdate, msg, creator);
        $debug("CmsProductVoRateUpdateService 记入价格变更履历结束 耗时" + (System.currentTimeMillis() - sta));

        // 插入上新程序
        $debug("CmsProductVoRateUpdateService 开始记入SxWorkLoad表");
        sta = System.currentTimeMillis();
        sxProductService.insertSxWorkLoad(channelId, codeList, null, creator);
        $debug("CmsProductVoRateUpdateService 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));
        return failList;
    }
}