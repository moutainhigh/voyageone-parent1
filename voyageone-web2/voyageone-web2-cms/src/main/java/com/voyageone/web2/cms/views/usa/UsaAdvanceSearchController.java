package com.voyageone.web2.cms.views.usa;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.impl.cms.PlatformService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.service.impl.cms.usa.UsaAdvanceSearchService;
import com.voyageone.service.impl.cms.usa.UsaCustomColumnService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvSearchCustColumnService;
import com.voyageone.web2.cms.views.search.CmsAdvSearchOtherService;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import com.voyageone.web2.core.bean.UserSessionBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * USA CMS 高级检索
 *
 * @Author rex.wu
 * @Create 2017-07-17 13:15
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.ADVANCE_SEARCH.ROOT)
public class UsaAdvanceSearchController extends CmsController {

    @Autowired
    private CmsProductSearchQueryService cmsProductSearchQueryService;
    @Autowired
    private CmsAdvanceSearchService searchIndexService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private CmsAdvSearchOtherService advSearchOtherService;
    @Autowired
    private CmsAdvSearchCustColumnService advSearchCustColumnService;

    @Autowired
    private UsaAdvanceSearchService usaAdvanceSearchService;
    @Autowired
    private UsaCustomColumnService usaCustomColumnService;

    /**
     * 统一的当前语言环境提供
     */
    @Override
    public String getLang() {
        return "en";
    }

    /**
     * 搜索页面数据初始化
     */
    @RequestMapping(value = UsaCmsUrlConstants.ADVANCE_SEARCH.INIT)
    public AjaxResponse init() throws IOException {
        UserSessionBean userInfo = getUser();
        CmsSessionBean cmsSession = getCmsSession();
//        usaCustomColumnService.getUserCustColumns(userInfo.getSelChannelId(), userInfo.getUserId(), cmsSession, getLang());
//        Map<String, Object> resultMap = usaCustomColumnService.getMasterData(userInfo, cmsSession, getLang());
//        resultMap.put("channelId", userInfo.getSelChannelId());
        return success(null);
    }


    /**
     * @api {post} /cms/search/advance/getCustColumnsInfo 1.6 取得自定义显示列设置
     * @apiName getCustColumnsInfo
     * @apiDescription 取得自定义显示列设置
     * @apiGroup search
     * @apiVersion 0.0.1
     * @apiPermission 认证商户
     * @apiSuccess (系统级返回字段) {String} code 处理结果代码编号
     * @apiSuccess (系统级返回字段) {String} message 处理结果描述
     * @apiSuccess (系统级返回字段) {String} displayType 消息的提示方式
     * @apiSuccess (系统级返回字段) {String} redirectTo 跳转地址
     * @apiSuccess (应用级返回字段) {Object[]} customProps 自定义显示列信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {Object[]} commonProps 共同属性显示列信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {String[]} custAttrList 用户保存的自定义显示列信息，没有数据时返回空数组
     * @apiSuccess (应用级返回字段) {String[]} commList 用户保存的共同属性显示列信息，没有数据时返回空数组
     * @apiSuccessExample 成功响应查询请求
     * {
     * "code":null, "message":null, "displayType":null, "redirectTo":null,
     * "data":{
     * "customProps": [ {"feed_prop_original":"a_b_c", "feed_prop_translation":"yourname" }...],
     * "commonProps": [ {"propId":"a_b_c", "propName":"yourname" }...],
     * "custAttrList": [ "a_b_c", "a_b_d", "a_b_e"...],
     * "commList": [ "q_b_c", "q_b_d", "q_b_e"...]
     * }
     * }
     * @apiExample 业务说明
     * 取得自定义显示列设置
     * @apiExample 使用表
     * 使用cms_bt_feed_custom_prop表, cms_mt_common_prop表
     * @apiSampleRequest off
     */
    /**
     * 获取用户USA-CMS自定义列信息
     */
    @RequestMapping(value = UsaCmsUrlConstants.ADVANCE_SEARCH.GET_CUSTOM_COLUMNS)
    public AjaxResponse getCustomColumns() {
        UserSessionBean userInfo = getUser();
        // 获取该用户自定义显示列设置
        Map<String, Object> colData = usaCustomColumnService.getCustomColumnsWithChecked(userInfo.getSelChannelId(), userInfo.getUserId(), getLang());
        // 返回用户信息
        return success(colData);
    }


    @RequestMapping(value = UsaCmsUrlConstants.ADVANCE_SEARCH.SEARCH)
    public AjaxResponse search(@RequestBody CmsSearchInfoBean2 params) {

        if ($isDebugEnabled()) {
            $debug("USA->CMS advance search parameter: " + JacksonUtil.bean2JsonNotNull(params));
        }
        // 页面传入的检索条件 ->>> SKU/Barcode/Code/Model
        String[] codeList = params.getCodeList();
        if (codeList != null && codeList.length > 0) {
            HashSet<String> codeSet = new HashSet<String>(Arrays.asList(codeList));
            if (codeSet.size() > 2000) {
                throw new BusinessException("(SKU / Barcode/ Code / Model) Number is too much, preferably no more than 2000.");
            }
            String[] codes = new String[codeSet.size()];
            params.setCodeList(codeSet.toArray(codes));
        }
        // 用户Session信息
        UserSessionBean userInfo = getUser();
        // 返回结果
        Map<String, Object> resultBean = new HashMap<>();
        CmsSessionBean cmsSession = getCmsSession();
        cmsSession.putAttribute("_adv_search_params", params);

        int endIdx = params.getProductPageSize();
        // 从Solr中统计满足条件的Product总数和当前页商品CodeList
        CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId(), userInfo.getUserId(), userInfo.getUserName());
        long productListTotal = cmsProductCodeListBean.getTotalCount();
        cmsSession.putAttribute("_adv_search_productListTotal", productListTotal);
        cmsSession.putAttribute("_adv_search_groupListTotal", null);

        // 从Solr中获取本页实际Code List后再从MongoDB中查询Product实际信息
        List<String> currCodeList = cmsProductCodeListBean.getProductCodeList();
        List<CmsBtProductBean> prodInfoList = searchIndexService.getProductInfoList(currCodeList, params, userInfo, cmsSession);
        prodInfoList.sort((o1, o2) -> Integer.compare(currCodeList.indexOf(o1.getCommon().getFields().getCode()), currCodeList.indexOf(o2.getCommon().getFields().getCode())));

        Map<String, TypeChannelBean> productTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, userInfo.getSelChannelId(), getLang());
        Map<String, TypeChannelBean> sizeTypes = TypeChannels.getTypeMapWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, userInfo.getSelChannelId(), getLang());
        Map<String, Map<String, Integer>> codeMap = new HashMap<>();
        prodInfoList.forEach(cmsBtProductBean -> {
            String productType = cmsBtProductBean.getCommon().getFields().getProductType();
            if (StringUtils.isNotBlank(productType)) {
                TypeChannelBean productTypeBean = productTypes.get(productType);
                if (productTypeBean != null) {
                    cmsBtProductBean.getCommon().getFields().setProductTypeCn(productTypeBean.getName());
                }
            }

            String sizeType = cmsBtProductBean.getCommon().getFields().getSizeType();
            if (StringUtils.isNotBlank(sizeType)) {
                TypeChannelBean sizeTypeBean = sizeTypes.get(sizeType);
                if (sizeTypeBean != null) {
                    cmsBtProductBean.getCommon().getFields().setSizeTypeCn(sizeTypeBean.getName());
                }
            }
            // TODO: 2017/7/17 rex.wu  库存统计逻辑 ???
            codeMap.putAll(getCodeQty(cmsBtProductBean, userInfo));
        });
        // TODO: 2017/7/17 rex.wu Check Produt Status ???
        searchIndexService.checkProcStatus(prodInfoList, getLang());
        resultBean.put("codeMap", codeMap);
        resultBean.put("productList", prodInfoList);
        resultBean.put("productListTotal", productListTotal);

        // 查询平台显示商品URL
        Integer cartId = params.getCartId();
        resultBean.put("productUrl", platformService.getPlatformProductUrl(cartId.toString()));

        // 查询商品其它画面显示用的信息
        List[] infoArr = advSearchOtherService.getProductExtraInfo(prodInfoList, userInfo.getSelChannelId(), cartId);
        resultBean.put("prodOrgChaNameList", infoArr[0]);
        resultBean.put("freeTagsList", infoArr[1]);

        // 获取该用户自定义显示列设置
        resultBean.put("customProps", cmsSession.getAttribute("_adv_search_customProps"));
        resultBean.put("commonProps", cmsSession.getAttribute("_adv_search_commonProps"));
        resultBean.put("selSalesType", cmsSession.getAttribute("_adv_search_selSalesType"));
        resultBean.put("selBiDataList", cmsSession.getAttribute("_adv_search_selBiDataList"));

        // 返回用户信息
        return success(resultBean);
    }


    public Map<String, Map<String, Integer>> getCodeQty(CmsBtProductBean cmsBtProductBean, UserSessionBean userInfo) {
        Map<String, Map<String, Integer>> codeMap = new HashMap<>();
        // 店铺(cart/平台)列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang());

        //sku取得库存
//        Map<String, String> codesMap = new HashMap<>();
//        if (StringUtils.isNotBlank(cmsBtProductBean.getCommon().getFields().getOriginalCode())) {
//            codesMap.put("channelId", cmsBtProductBean.getOrgChannelId());
//            codesMap.put("code", cmsBtProductBean.getCommon().getFields().getOriginalCode());
//        }
//        List<WmsBtInventoryCenterLogicModel> inventoryList = wmsBtInventoryCenterLogicDao.selectItemDetailByCode(codesMap);
        //code取得库存
//        int codeQty = 0;
//        for (WmsBtInventoryCenterLogicModel inventoryInfo : inventoryList) {
//            codeQty = codeQty + inventoryInfo.getQtyChina();
//        }
//        cmsBtProductBean.getCommon().getFields().setQuantity(codeQty);

        Map<String, Integer> cartIdMap = new HashMap();
        for (TypeChannelBean cartObj : cartList) {
            CmsBtProductModel_Platform_Cart ptfObj = cmsBtProductBean.getPlatform(Integer.parseInt(cartObj.getValue()));
            if (ptfObj != null && !ListUtils.isNull(ptfObj.getSkus())) {
                int qty = 0;
                for (BaseMongoMap<String, Object> map : ptfObj.getSkus()) {
                    String sku = (String) map.get("skuCode");
                    Boolean isSale = (Boolean) map.get("isSale");
                    if (isSale != null && isSale) {
                        for (CmsBtProductModel_Sku skus : cmsBtProductBean.getCommonNotNull().getSkus()) {
//                        for (WmsBtInventoryCenterLogicModel inventoryInfo : inventoryList) {
                            if (skus.getSkuCode().equals(sku)) {
                                qty = qty + skus.getQty();
                            }
                        }
                    }
                }
                cartIdMap.put(cartObj.getAdd_name2(), qty);
            } else {
                cartIdMap.put(cartObj.getAdd_name2(), 0);
            }
        }
        codeMap.put(cmsBtProductBean.getCommon().getFields().getCode(), cartIdMap);
        return codeMap;
    }

}
