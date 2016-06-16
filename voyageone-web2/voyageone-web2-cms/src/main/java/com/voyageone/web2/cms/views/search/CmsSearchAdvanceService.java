package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.daoext.cms.CmsMtCommonPropDaoExt;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.impl.cms.CommonPropService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.feed.FeedCustomPropService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.promotion.PromotionService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsSearchAdvanceService extends BaseAppService {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ChannelCategoryService channelCategoryService;
    @Autowired
    private CommonPropService commonPropService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private FeedCustomPropService feedCustomPropService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Autowired
    private TagService tagService;
    @Resource
    private CmsBtJmPromotionService jmPromotionService;
    @Autowired
    private CmsMtCommonPropDaoExt cmsMtCommonPropDaoExt;

    // 查询产品信息时的缺省输出列
    private final String searchItems = "channelId;prodId;catId;catPath;created;creater;modified;orgChannelId;modifier;carts;skus;freeTags;sales;" +
            "fields.longTitle;fields.productNameEn;fields.brand;fields.status;fields.code;fields.images1;fields.images2;fields.images3;fields.images4;fields.images5;fields.images6;fields.quantity;fields.productType;fields.sizeType;fields.isMasterMain;" +
            "fields.priceSaleSt;fields.priceSaleEd;fields.priceRetailSt;fields.priceRetailEd;fields.priceMsrpSt;fields.priceMsrpEd;fields.hsCodeCrop;fields.hsCodePrivate;";

    // DB检索页大小
    private final static int SELECT_PAGE_SIZE = 2000;

    // Excel 文件最大行数
    private final static int MAX_EXCEL_REC_COUNT = 10000;

    /**
     * 取得用户自定义显示列设置
     *
     */
    public void getUserCustColumns(String channelId, int userId, CmsSessionBean cmsSession, String language) {
        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userId);
        String custAttrStr;
        String commStr;
        if (rsList == null || rsList.isEmpty()) {
            $debug("该用户还未设置自定义查询列 userId=" + userId + " channelId=" + channelId);
            custAttrStr = "";
            commStr = "";
        } else {
            custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
            commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        }

        // 设置自定义查询用的属性
        List<Map<String, String>> custAttsQueryList = new ArrayList<>();

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        String[] custAttrList = custAttrStr.split(",");
        StringBuilder customPropsStr = new StringBuilder();
        if (custAttrList.length > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(channelId, "0");
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                Map<String, String> atts = new HashMap<>(2);
                atts.put("configCode", "feed.cnAtts." + propId);
                atts.put("configValue1", (String) props.get("feed_prop_translation"));
                custAttsQueryList.add(atts);

                if (ArrayUtils.contains(custAttrList, propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                    customPropsStr.append("feed.orgAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }
        List<Map<String, Object>> commonProp2 = new ArrayList<>();
        String[] commList = commStr.split(",");
        StringBuilder commonPropsStr = new StringBuilder();
        if (commList.length > 0) {
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                Map<String, String> atts = new HashMap<>(2);
                atts.put("configCode", "fields." + propId);
                atts.put("configValue1", (String) props.get("propName"));
                custAttsQueryList.add(atts);

                if (ArrayUtils.contains(commList, propId)) {
                    commonProp2.add(props);
                    commonPropsStr.append("fields.");
                    commonPropsStr.append(propId);
                    commonPropsStr.append(";");
                }
            }
        }

        cmsSession.putAttribute("_adv_search_props_custAttsQueryList", custAttsQueryList);
        cmsSession.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSession.putAttribute("_adv_search_customProps", customProps2);
        cmsSession.putAttribute("_adv_search_commonProps", commonProp2);

        List<Map<String, Object>> rsList2 = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userId);
        List<String> itemList = new ArrayList<>();
        if (!rsList2.isEmpty()) {
            String itemVal = org.apache.commons.lang3.StringUtils.trimToNull((String) rsList2.get(0).get("cfg_val2"));
            if (itemVal != null) {
                Collections.addAll(itemList, itemVal.split(","));
            }
        }
        cmsSession.putAttribute("_adv_search_selSalesType", getSalesTypeList(channelId, language, itemList));
    }

    /**
     * 获取检索页面初始化的master data数据
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, CmsSessionBean cmsSession, String language) throws IOException {

        Map<String, Object> masterData = new HashMap<>();

        // 获取product status
        masterData.put("productStatusList", TypeConfigEnums.MastType.productStatus.getList(language));

        // 获取publish status
        masterData.put("platformStatusList", TypeConfigEnums.MastType.platFormStatus.getList(language));

        // 获取label
        Map<String, Object> param = new HashMap<>(2);
        param.put("channelId", userInfo.getSelChannelId());
        param.put("tagTypeSelectValue", "4");
        masterData.put("freetagList", cmsChannelTagService.getTagInfoList(param));

        // 获取price type
        masterData.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));

        // 获取compare type
        masterData.put("compareTypeList", TypeConfigEnums.MastType.compareType.getList(language));

        // 获取brand list
        masterData.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, userInfo.getSelChannelId(), language));

        // 获取sort list
        masterData.put("sortList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SORT_ATTRIBUTES_61, userInfo.getSelChannelId(), language));

        // 获取category list
        masterData.put("categoryList", channelCategoryService.getAllCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取promotion list
        masterData.put("promotionList", promotionService.getPromotionsByChannelId(userInfo.getSelChannelId(), null));

        //add by holysky  新增一些页的聚美促销活动预加载
        masterData.put("jmPromotionList", jmPromotionService.getJMActivePromotions(userInfo.getSelChannelId()));

        // 获取自定义查询用的属性
        masterData.put("custAttsList", cmsSession.getAttribute("_adv_search_props_custAttsQueryList"));

        //标签type
        masterData.put("tagTypeList", Types.getTypeList(74, language));

        // 设置按销量排序的选择列表
        masterData.put("salesTypeList", getSalesTypeList(userInfo.getSelChannelId(), language, null));

        // 判断是否是minimall用户
        boolean isMiniMall = userInfo.getSelChannelId().equals(ChannelConfigEnums.Channel.VOYAGEONE.getId());
        masterData.put("isminimall", isMiniMall ? 1 : 0);
        if (isMiniMall) {
            masterData.put("channelList", Channels.getUsJoiChannelList());
        }

        //获取店铺列表
        masterData.put("cartList",TypeChannels.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language));

        return masterData;
    }

    /**
     * 获取当前页的product列表
     */
    public List<String> getProductCodeList(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjection("{'fields.code':1,'_id':0}");
        queryObject.setSort(setSortValue(searchValue));
        List<CmsBtProductModel> prodList = productService.getList(userInfo.getSelChannelId(), queryObject);
        if (prodList == null || prodList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductCodeList prodList为空");
            return new ArrayList<>(0);
        }

        // 取得符合条件的产品code列表
        List<String> codeList = new ArrayList<>(prodList.size());
        for (CmsBtProductModel prodObj : prodList) {
            codeList.add(prodObj.getFields().getCode());
        }
        return codeList;
    }

    /**
     * 获取当前页的product列表
     */
    public List<CmsBtProductBean> getProductInfoList(List<String> prodCodeList
            , CmsSearchInfoBean searchValue
            , UserSessionBean userInfo
            , CmsSessionBean cmsSessionBean) {
        // 最后再获取本页实际产品信息
        JomgoQuery queryObject = new JomgoQuery();
        String[] codeArr = new String[prodCodeList.size()];
        codeArr = prodCodeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "}");

        String plusStr = (String) cmsSessionBean.getAttribute("_adv_search_props_searchItems");
        if (plusStr == null) {
            plusStr = "";
        }
        String projStr = queryObject.buildProjection(searchItems.concat(plusStr).split(";"));
        queryObject.setProjection(projStr);
        queryObject.setSort(setSortValue(searchValue));

        List<CmsBtProductBean> prodInfoList = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
        if (prodInfoList == null || prodInfoList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductInfoList prodInfoList");
            return new ArrayList<>(0);
        }
        return prodInfoList;
    }

    /**
     * 检查翻译状态
     */
    public void checkProcStatus(List<CmsBtProductBean> productList, String lang) {
        if (productList == null || productList.isEmpty()) {
            return;
        }
        List<TypeBean> transStatusList = TypeConfigEnums.MastType.translationStatus.getList(lang);
        Map<String, String> transStatusMap = new HashMap<>(transStatusList.size());
        for (TypeBean beanObj : transStatusList) {
            transStatusMap.put(beanObj.getValue(), beanObj.getName());
        }
        List<TypeBean> editStatusList = TypeConfigEnums.MastType.editStatus.getList(lang);
        Map<String, String> editStatusMap = new HashMap<>(editStatusList.size());
        for (TypeBean beanObj : editStatusList) {
            editStatusMap.put(beanObj.getValue(), beanObj.getName());
        }
        List<TypeBean> lockStatusList = TypeConfigEnums.MastType.procLockStatus.getList(lang);
        Map<String, String> lockStatusMap = new HashMap<>(lockStatusList.size());
        for (TypeBean beanObj : lockStatusList) {
            lockStatusMap.put(beanObj.getValue(), beanObj.getName());
        }

        for (CmsBtProductModel prodObj : productList) {
            CmsBtProductModel_Field fieldsObj = prodObj.getFields();
            if (fieldsObj != null) {
                String stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getTranslateStatus());
                if (stsFlg != null) {
                    String stsValueStr = transStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setTranslateStatus("");
                    } else {
                        fieldsObj.setTranslateStatus(stsValueStr);
                    }
                } else {
                    fieldsObj.setTranslateStatus("");
                }

                stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(fieldsObj.getEditStatus());
                if (stsFlg != null) {
                    String stsValueStr = editStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        fieldsObj.setEditStatus("");
                    } else {
                        fieldsObj.setEditStatus(stsValueStr);
                    }
                } else {
                    fieldsObj.setEditStatus("");
                }

                stsFlg = org.apache.commons.lang3.StringUtils.trimToNull(prodObj.getLock());
                if (stsFlg != null) {
                    String stsValueStr = lockStatusMap.get(stsFlg);
                    if (stsValueStr == null) {
                        prodObj.setLock("");
                    } else {
                        prodObj.setLock(stsValueStr);
                    }
                } else {
                    prodObj.setLock("");
                }
            }
        }
    }

    /**
     * 取得当前主商品所在组的其他信息：所有商品的价格变动信息，子商品图片
     */
    public List[] getGroupExtraInfo(List<CmsBtProductBean> groupsList, String channelId, int cartId, boolean hasImgFlg) {
        List[] rslt;
        List<List<Map<String, String>>> imgList = new ArrayList<>();
        List<Integer> chgFlgList = new ArrayList<>();
        List<String> orgChaNameList = new ArrayList<>();
        List<List<Map<String, Object>>> prodIdList = new ArrayList<>();
        List<String> freeTagsList = new ArrayList<>();

        if (hasImgFlg) {
            rslt = new List[3];
            rslt[0] = chgFlgList;
            rslt[1] = imgList;
            rslt[2] = prodIdList;
        } else {
            rslt = new List[3];
            rslt[0] = chgFlgList;
            rslt[1] = orgChaNameList;
            rslt[2] = freeTagsList;
        }

        for (CmsBtProductBean groupObj : groupsList) {
            CmsBtProductModel_Field fields = groupObj.getFields();
            String prodCode = null;
            if (fields != null) {
                prodCode = fields.getCode();
            }
            // 从group表合并platforms信息
            StringBuilder resultPlatforms = new StringBuilder();
            resultPlatforms.append(MongoUtils.splicingValue("cartId", cartId));
            resultPlatforms.append(",");
            resultPlatforms.append(MongoUtils.splicingValue("productCodes", new String[]{prodCode}, "$in"));

            // 在group表中过滤platforms相关信息
            JomgoQuery qrpQuy = new JomgoQuery();
            qrpQuy.setQuery("{" + resultPlatforms.toString() + "}");
            List<CmsBtProductGroupModel> grpList = productGroupService.getList(channelId, qrpQuy);
            CmsBtProductGroupModel groupModelMap = null;
            if (grpList == null || grpList.isEmpty()) {
                $warn("CmsSearchAdvanceService.getGroupExtraInfo prodCode=" + prodCode);
                groupObj.setGroupBean(new CmsBtProductGroupModel());
            } else {
                groupModelMap = grpList.get(0);
                groupObj.setGroupBean(groupModelMap);
            }

            // 设置cart相关信息
            List<CmsBtProductModel_Carts> cartList = groupObj.getCarts();
            if (cartList != null && cartList.size() > 0) {
                for (CmsBtProductModel_Carts cart : cartList) {
                    if (cart.getCartId() == cartId) {
                        groupObj.setCartBean(cart);
                        break;
                    }
                }
            }
            if (groupObj.getCartBean() == null) {
                groupObj.setCartBean(new CmsBtProductModel_Carts());
            }

            ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(groupObj.getOrgChannelId());
            if (channel == null) {
                orgChaNameList.add("");
            } else {
                orgChaNameList.add(channel.getFullName());
            }

            boolean hasChg = false;
            List<CmsBtProductModel_Sku> skus = groupObj.getSkus();
            if (skus != null) {
                for (CmsBtProductModel_Sku skuObj : skus) {
                    String chgFlg = org.apache.commons.lang3.StringUtils.trimToEmpty((String) (skuObj).get("priceChgFlg"));
                    if (chgFlg.startsWith("U") || chgFlg.startsWith("D") || chgFlg.startsWith("X")) {
                        hasChg = true;
                        break;
                    } else {
                        hasChg = false;
                    }
                }
            }
            if (hasChg) {
                chgFlgList.add(1);
            } else {
                chgFlgList.add(0);
            }

            if (!hasImgFlg) {
                // 获取商品free tag信息
                List<String> tagPathList = groupObj.getFreeTags();
                if (tagPathList != null && tagPathList.size() > 0) {
                    // 根据tag path查询tag path name
                    List<CmsBtTagBean> tagModelList = tagService.getTagPathNameByTagPath(channelId, tagPathList);
                    if (!tagModelList.isEmpty()) {
                        tagModelList = cmsChannelTagService.convertToTree(tagModelList);
                        List<CmsBtTagModel> tagList = cmsChannelTagService.convertToList(tagModelList);
                        List<String> tagPathStrList = new ArrayList<>();
                        tagList.forEach(tag -> tagPathStrList.add(tag.getTagPathName()));
                        freeTagsList.add(org.apache.commons.lang3.StringUtils.join(tagPathStrList, "<br>"));
                    }
                }

                // 查询商品在各平台状态
                List<CmsBtProductModel_Carts> carts = groupObj.getCarts();
                if (carts != null && carts.size() > 0) {
                    for (CmsBtProductModel_Carts cartsObj : carts) {
                        StringBuilder resultStr = new StringBuilder();
                        resultStr.append(MongoUtils.splicingValue("cartId", cartsObj.getCartId()));
                        resultStr.append(",");
                        resultStr.append(MongoUtils.splicingValue("productCodes", new String[]{prodCode}, "$in"));

                        // 在group表中过滤platforms相关信息
                        JomgoQuery qrpQuyObj = new JomgoQuery();
                        qrpQuyObj.setQuery("{" + resultStr.toString() + "},{'_id':0,'numIId':1}");
                        CmsBtProductGroupModel grpItem = productGroupService.getProductGroupByQuery(channelId, qrpQuyObj);
                        if (grpItem != null) {
                            cartsObj.setAttribute("numiid", grpItem.getNumIId());
                        }
                    }
                }
            }

            List<Map<String, String>> images1Arr = new ArrayList<>();
            List<Map<String, Object>> groupProdIdList = new ArrayList<>();
            if (hasImgFlg && groupModelMap != null) {
                // 获取子商品的图片
                List pCdList = (List) groupModelMap.getProductCodes();
                if (pCdList != null && pCdList.size() > 1) {
                    for (int i = 1, leng = pCdList.size(); i < leng; i++) {
                        // 根据商品code找到其主图片
                        JomgoQuery queryObj = new JomgoQuery();
                        queryObj.setProjection("{'fields.images1':1,'prodId': 1, 'fields.code': 1,'_id':0}");
                        queryObj.setQuery("{\"fields.code\":\"" + String.valueOf(pCdList.get(i)) + "\"}");
                        CmsBtProductModel prod = productService.getProductByCondition(channelId, queryObj);
                        // 如果根据code获取不到数据就跳过
                        if (prod == null)
                            continue;
                        List<CmsBtProductModel_Field_Image> fldImgList = prod.getFields().getImages1();
                        if (fldImgList.size() > 0) {
                            Map<String, String> map = new HashMap<>(1);
                            map.put("value", fldImgList.get(0).getName());
                            images1Arr.add(map);
                        }

                        // 设定该group对应的prodId
                        Map<String, Object> proMap = new HashMap<>();
                        proMap.put("prodId", prod.getProdId());
                        proMap.put("code", prod.getFields().getCode());
                        groupProdIdList.add(proMap);
                    }
                }
            }
            imgList.add(images1Arr);
            prodIdList.add(groupProdIdList);
        }
        return rslt;
    }

    /**
     * 返回当前页的group列表
     */
    public List<String> getGroupCodeList(List<String> codeList, UserSessionBean userInfo, CmsSessionBean cmsSessionBean) {
        String[] codeArr = new String[codeList.size()];
        codeArr = codeList.toArray(codeArr);
        StringBuilder resultPlatforms = new StringBuilder();
        resultPlatforms.append(MongoUtils.splicingValue("cartId", Integer.valueOf(cmsSessionBean.getPlatformType().get("cartId").toString())));
        resultPlatforms.append(",");
        resultPlatforms.append(MongoUtils.splicingValue("productCodes", codeArr, "$in"));

        // 在group表中过滤platforms相关信息
        JomgoQuery qrpQuy = new JomgoQuery();
        qrpQuy.setQuery("{" + resultPlatforms.toString() + "}");
        qrpQuy.setProjection("{'_id':0,'mainProductCode':1}");

        List<CmsBtProductGroupModel> grpList = productGroupService.getList(userInfo.getSelChannelId(), qrpQuy);
        if (grpList == null || grpList.isEmpty()) {
            $warn("CmsSearchAdvanceService.getProductCodeList grpList");
            return new ArrayList<String>(0);
        }

        // 将上面查询的结果放到一个临时map中,以过滤重复code
        Map<String, String> codeList2 = new HashMap<String, String>();
        for (CmsBtProductGroupModel grpObj : grpList) {
            String pCd = grpObj.getMainProductCode();
            codeList2.put(pCd, pCd);
        }

        List<String> grpCodeList = new ArrayList<String>(codeList2.size());
        codeList2.keySet().forEach(grpCodeList::add);
        return grpCodeList;
    }

    /**
     * 获取数据文件内容
     */
    public byte[] getCodeExcelFile(CmsSearchInfoBean searchValue, UserSessionBean userInfo, CmsSessionBean cmsSessionBean)
            throws IOException, InvalidFormatException {

        String templatePath = Properties.readValue(CmsProperty.Props.SEARCH_ADVANCE_EXPORT_TEMPLATE);

        long recCount = productService.getCnt(userInfo.getSelChannelId(), getSearchQuery(searchValue, cmsSessionBean, false));

        int pageCount;
        if ((int) recCount % SELECT_PAGE_SIZE > 0) {
            pageCount = (int) recCount / SELECT_PAGE_SIZE + 1;
        } else {
            pageCount = (int) recCount / SELECT_PAGE_SIZE;
        }

        $info("准备生成 Item 文档 [ %s ]", recCount);
        $info("准备打开文档 [ %s ]", templatePath);
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(getSearchQuery(searchValue, cmsSessionBean, false));
        queryObject.setProjectionExt(searchItems.concat((String) cmsSessionBean.getAttribute("_adv_search_props_searchItems")).split(";"));
        queryObject.setSort(setSortValue(searchValue));

        try (InputStream inputStream = new FileInputStream(templatePath);
             Workbook book = WorkbookFactory.create(inputStream)) {
            writeHead(book,cmsSessionBean);
            for (int i = 0; i < pageCount; i++) {

                queryObject.setSkip(i * SELECT_PAGE_SIZE);
                queryObject.setLimit(SELECT_PAGE_SIZE);
                List<CmsBtProductBean> items = productService.getBeanList(userInfo.getSelChannelId(), queryObject);
                if (items.size() == 0) {
                    break;
                }
                getGroupExtraInfo(items, userInfo.getSelChannelId(), Integer.parseInt(cmsSessionBean.getPlatformType().get("cartId").toString()), false);

                List<TypeChannelBean> hscodes = TypeChannels.getTypeList("hsCodePrivate", userInfo.getSelChannelId());
                items.forEach(item -> {
                    if (!StringUtils.isEmpty(item.getFields().getHsCodePrivate())) {
                        for (TypeChannelBean hscode : hscodes) {
                            if (hscode.getValue().equalsIgnoreCase(item.getFields().getHsCodePrivate())) {
                                item.getFields().setHsCodePrivate(hscode.getName());
                                break;
                            }
                        }
                    }
                });
                // 每页开始行
                int startRowIndex = i * SELECT_PAGE_SIZE + 1;
                boolean isContinueOutput = writeRecordToFile(book, items, cmsSessionBean, startRowIndex);
                // 超过最大行的场合
                if (!isContinueOutput) {
                    break;
                }
            }

            $info("文档写入完成");

            // 返回值设定
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                book.write(outputStream);

                $info("已写入输出流");

                return outputStream.toByteArray();
            }
        }
    }

    /**
     * 根据类目路径查询已翻译的属性信息
     */
    public List<Map<String, Object>> selectAttrs(String channelId, String catPath) {
        return feedCustomPropService.getFeedCustomPropAttrs(channelId, catPath);
    }

    /**
     * 取得自定义显示列设置
     */
    public List<Map<String, Object>> getCustColumns() {
        return commonPropService.getCustColumns();
    }

    private List<Map<String, String>> getSalesTypeList(String channelId, String language, List<String> filterList) {
        List<Map<String, String>> salseSum7List = new ArrayList<>();
        List<Map<String, String>> salseSum30List = new ArrayList<>();
        List<Map<String, String>> salseSumAllList = new ArrayList<>();

        // 设置按销量排序的选择列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartList == null) {
            return salseSumAllList;
        }
        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if (cartId == 1 || cartId == -1) {
                continue;
            }
            Map<String, String> keySum7Map = new HashMap<>();
            Map<String, String> keySum30Map = new HashMap<>();
            Map<String, String> keySumAllMap = new HashMap<>();
            if (cartId == 0) {
                keySum7Map.put("name", "7Days总销量");
                keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySum30Map.put("name", "30Days总销量");
                keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySumAllMap.put("name", "总销量");
                keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            } else {
                keySum7Map.put("name", cartObj.getName() + "7Days销量");
                keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySum30Map.put("name", cartObj.getName() + "30Days销量");
                keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySumAllMap.put("name", cartObj.getName() + "总销量");
                keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            }
            salseSum7List.add(keySum7Map);
            salseSum30List.add(keySum30Map);
            salseSumAllList.add(keySumAllMap);
        }
        salseSumAllList.addAll(salseSum30List);
        salseSumAllList.addAll(salseSum7List);

        if (filterList != null) {
            List<Map<String, String>> sumAllList = new ArrayList<>();
            for (Map<String, String> sumObj : salseSumAllList) {
                if (filterList.contains(sumObj.get("value"))) {
                    sumAllList.add(sumObj);
                }
            }
            return sumAllList;
        }
        return salseSumAllList;
    }

    /**
     * 取得用户自定义显示列设置
     *
     */
    public Map<String, Object> getUserCustColumns(UserSessionBean userInfo, String language) {
        Map<String, Object> rsMap = new HashMap<>();
        rsMap.put("salesTypeList", getSalesTypeList(userInfo.getSelChannelId(), language, null));

        List<Map<String, Object>> rsList2 = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userInfo.getUserId());
        if (rsList2 == null || rsList2.isEmpty()) {
            rsMap.put("selSalesTypeList", new String[]{});
        } else {
            String selStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList2.get(0).get("cfg_val2"));
            rsMap.put("selSalesTypeList", selStr.split(","));
        }

        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userInfo.getUserId());
        if (rsList == null || rsList.isEmpty()) {
            rsMap.put("custAttrList", new String[]{});
            rsMap.put("commList", new String[]{});
            return rsMap;
        }
        String custAttrStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val1"));
        String commStr = org.apache.commons.lang3.StringUtils.trimToEmpty((String) rsList.get(0).get("cfg_val2"));
        rsMap.put("custAttrList", custAttrStr.split(","));
        rsMap.put("commList", commStr.split(","));
        return rsMap;
    }

    /**
     * 保存用户自定义显示列设置
     */
    public void saveCustColumnsInfo(UserSessionBean userInfo, CmsSessionBean cmsSessionBean, List<String> param1, List<String> param2, String language, List<String> selSalesTypeList) {
        String customStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param1, ","));
        String commonStrs = org.apache.commons.lang3.StringUtils.trimToEmpty(org.apache.commons.lang3.StringUtils.join(param2, ","));

        List<Map<String, Object>> customProps2 = new ArrayList<>();
        StringBuilder customPropsStr = new StringBuilder();
        if (param1 != null && param1.size() > 0) {
            List<Map<String, Object>> customProps = feedCustomPropService.getFeedCustomPropAttrs(userInfo.getSelChannelId(), "0");
            for (Map<String, Object> props : customProps) {
                String propId = (String) props.get("feed_prop_original");
                if (param1.contains(propId)) {
                    customProps2.add(props);
                    customPropsStr.append("feed.cnAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                    customPropsStr.append("feed.orgAtts.");
                    customPropsStr.append(propId);
                    customPropsStr.append(";");
                }
            }
        }

        List<Map<String, Object>> commonProp2 = new ArrayList<>();
        StringBuilder commonPropsStr = new StringBuilder();
        if (param2 != null && param2.size() > 0) {
            List<Map<String, Object>> commonProps = commonPropService.getCustColumns();
            for (Map<String, Object> props : commonProps) {
                String propId = (String) props.get("propId");
                if (param2.contains(propId)) {
                    commonProp2.add(props);
                    commonPropsStr.append("fields.");
                    commonPropsStr.append(propId);
                    commonPropsStr.append(";");
                }
            }
        }
        cmsSessionBean.putAttribute("_adv_search_props_searchItems", customPropsStr.toString() + commonPropsStr.toString());
        cmsSessionBean.putAttribute("_adv_search_customProps", customProps2);
        cmsSessionBean.putAttribute("_adv_search_commonProps", commonProp2);

        List<Map<String, Object>> rsList = commonPropService.getCustColumnsByUserId(userInfo.getUserId());
        int rs;
        if (rsList == null || rsList.isEmpty()) {
            rs = commonPropService.addUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        } else {
            rs = commonPropService.saveUserCustColumn(userInfo.getUserId(), userInfo.getUserName(), customStrs, commonStrs);
        }
        if (rs == 0) {
            $error("保存自定义显示列设置不成功 userid=" + userInfo.getUserId());
        }

        rsList = cmsMtCommonPropDaoExt.selectUserCustColumnsSalesType(userInfo.getUserId());
        String selStr;
        if (selSalesTypeList == null || selSalesTypeList.isEmpty()) {
            selStr = "";
        } else {
            selStr = selSalesTypeList.stream().collect(Collectors.joining(","));
        }
        if (rsList == null || rsList.isEmpty()) {
            rs = cmsMtCommonPropDaoExt.insertUserCustColumnsSalesType(userInfo.getUserId(), userInfo.getUserName(), selStr);
        } else {
            rs = cmsMtCommonPropDaoExt.updateUserCustColumnsSalesType(userInfo.getUserId(), userInfo.getUserName(), selStr);
        }
        if (selSalesTypeList == null) {
            selSalesTypeList = new ArrayList<>(0);
        }
        cmsSessionBean.putAttribute("_adv_search_selSalesType", getSalesTypeList(userInfo.getSelChannelId(), language, selSalesTypeList));
        if (rs == 0) {
            $error("保存自定义销售数据显示设置不成功 userid=" + userInfo.getUserId());
        }
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    private String getSearchQuery(CmsSearchInfoBean searchValue, CmsSessionBean cmsSessionBean, boolean isMain) {
        StringBuilder result = new StringBuilder();

        // 设置platform检索条件
        StringBuilder resultPlatforms = new StringBuilder();

        // 添加platform cart
        Integer cartId = Integer.valueOf(cmsSessionBean.getPlatformType().get("cartId").toString());

        // 只有选中具体的某个平台的时候,和platform相关的检索才有效
        if (cartId != 0 && cartId != 1) {

            resultPlatforms.append(MongoUtils.splicingValue("cartId", cartId));
            resultPlatforms.append(",");

            // 获取platform status
            if (searchValue.getPlatformStatus() != null && searchValue.getPlatformStatus().length > 0) {
                // 获取platform status
                resultPlatforms.append(MongoUtils.splicingValue("platformStatus", searchValue.getPlatformStatus()));
                resultPlatforms.append(",");
            }

            if (searchValue.getPublishTimeStart() != null || searchValue.getPublishTimeTo() != null) {
                resultPlatforms.append("\"publishTime\":{" );
                // 获取publishTime start
                if (searchValue.getPublishTimeStart() != null) {
                    resultPlatforms.append(MongoUtils.splicingValue("$gte", searchValue.getPublishTimeStart() + " 00.00.00"));
                }
                // 获取publishTime End
                if (searchValue.getPublishTimeTo() != null) {
                    if (searchValue.getPublishTimeStart() != null) {
                        resultPlatforms.append(",");
                    }
                    resultPlatforms.append(MongoUtils.splicingValue("$lte", searchValue.getPublishTimeTo() + " 23.59.59"));
                }
                resultPlatforms.append("},");
            }

            result.append(MongoUtils.splicingValue("carts"
                    , "{" + resultPlatforms.toString().substring(0, resultPlatforms.toString().length() - 1) + "}"
                    , "$elemMatch"));
            result.append(",");
        }

        // 获取其他检索条件
        result.append(getSearchValueForMongo(searchValue));

        if (!StringUtils.isEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        } else {
            return "";
        }
    }

    /**
     * 获取其他检索条件
     */
    private String getSearchValueForMongo(CmsSearchInfoBean searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取category Id
        if (searchValue.getCatPath() != null) {
            result.append(MongoUtils.splicingValue("catPath", searchValue.getCatPath(), "$regex"));
            result.append(",");
        }

        // 获取product status
        if (searchValue.getProductStatus() != null
                && searchValue.getProductStatus().length > 0) {
            result.append(MongoUtils.splicingValue("fields.status", searchValue.getProductStatus()));
            result.append(",");
        }

        // 获取price start
        if (searchValue.getPriceType() != null
                && searchValue.getPriceStart() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "St", searchValue.getPriceStart(), "$gte"));
            result.append(",");
        }

        // 获取price end
        if (searchValue.getPriceType() != null
                && searchValue.getPriceEnd() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getPriceType() + "Ed", searchValue.getPriceEnd(), "$lte"));
            result.append(",");
        }

        if (searchValue.getCreateTimeStart() != null) {
            result.append("\"created\":{");
            // 获取createdTime start
            if (searchValue.getCreateTimeStart() != null) {
                result.append(MongoUtils.splicingValue("$gte", searchValue.getCreateTimeStart() + " 00.00.00"));
            }
            // 获取createdTime End
            if (searchValue.getCreateTimeTo() != null) {
                if (searchValue.getCreateTimeStart() != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", searchValue.getCreateTimeTo() + " 23.59.59"));
            }
            result.append("},");
        }

        // 获取inventory
        if (searchValue.getCompareType() != null
                && searchValue.getInventory() != null) {
            result.append(MongoUtils.splicingValue("fields.quantity", searchValue.getInventory(), searchValue.getCompareType()));
            result.append(",");
        }

        // 获取brand
        if (searchValue.getBrand() != null) {
            result.append(MongoUtils.splicingValue("fields.brand", searchValue.getBrand()));
            result.append(",");
        }

        // 获取tag查询条件
        if (searchValue.getTagTypeSelectValue() == 2) {
            if (searchValue.getTags() != null && searchValue.getTags().length > 0) {
                result.append(MongoUtils.splicingValue("tags", searchValue.getTags()));
                result.append(",");
            }
        } else if (searchValue.getTagTypeSelectValue() == 4) {
            if (searchValue.getTags() != null && searchValue.getTags().length > 0) {
                result.append(MongoUtils.splicingValue("freeTags", searchValue.getTags()));
                result.append(",");
            }
        }
        //获取tag查询条件
        if (searchValue.getCidValue().size()>0) {
            result.append(MongoUtils.splicingValue("sellerCats.cIds", searchValue.getCidValue().toArray(new String[searchValue.getCidValue().size()])));
            result.append(",");
        }
        // 获取code list用于检索code,model,productName,longTitle
        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("fields.code", searchValue.getCodeList()));
            orSearch.add(MongoUtils.splicingValue("fields.model", searchValue.getCodeList()));
            orSearch.add(MongoUtils.splicingValue("skus.skuCode", searchValue.getCodeList()));

            if (searchValue.getCodeList().length == 1) {
                // 原文查询内容
                orSearch.add(MongoUtils.splicingValue("fields.productNameEn", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.longDesEn", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.shortDesEn", searchValue.getCodeList()[0], "$regex"));
                // 中文查询内容
                orSearch.add(MongoUtils.splicingValue("fields.longTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.shortTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.middleTitle", searchValue.getCodeList()[0], "$regex"));
                orSearch.add(MongoUtils.splicingValue("fields.longDesCn", searchValue.getCodeList()[0], "$regex"));
            }
            result.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append(",");
        }

        // 获取自定义查询条件
//        List<Map<String, String>> custList = searchValue.getCustAttrMap();
//        if (custList != null && custList.size() > 0) {
//            List<String> inputList = new ArrayList<>();
//            for (Map<String, String> item : custList) {
//                String inputVal = org.apache.commons.lang3.StringUtils.trimToNull(item.get("inputVal"));
//                if (inputVal != null) {
//                    // 字符型和数字要分开比较
//                    if (org.apache.commons.lang3.math.NumberUtils.isNumber(inputVal)) {
//                        String qStr = "{'$or':[{'{0}':{'$type':1},'{0}':{1}},{'{0}':{'$type':16},'{0}':{1}},{'{0}':{'$type':18},'{0}':{1}},{'{0}':{'$type':2},'{0}':'{1}'}]}";
//                        inputList.add(com.voyageone.common.util.StringUtils.format(qStr, item.get("inputOpts"), inputVal));
//                    } else {
//                        String qStr = "{'{0}':'{1}'}";
//                        inputList.add(com.voyageone.common.util.StringUtils.format(qStr, item.get("inputOpts"), inputVal));
//                    }
//                }
//            }
//            if (inputList.size() > 0) {
//                result.append("'$and':[");
//                for (int i = 0, leng = inputList.size(); i < leng; i++) {
//                    if (i == 0) {
//                        result.append(inputList.get(i));
//                    } else {
//                        result.append(",");
//                        result.append(inputList.get(i));
//                    }
//                }
//                result.append("],");
//            }
//        }

        // 查询价格比较（建议销售价和实际销售价）
        if (searchValue.getPriceDiffFlg() == 1) {
            // 建议销售价等于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail == skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        } else if (searchValue.getPriceDiffFlg() == 2) {
            // 建议销售价小于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail < skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        } else if (searchValue.getPriceDiffFlg() == 3) {
            // 建议销售价大于实际销售价
            result.append(MongoUtils.splicingValue("$where", "function(){ var skuArr = this.skus; for (var ind in skuArr) {if(skuArr[ind].priceRetail > skuArr[ind].priceSale){return true;}}}"));
            result.append(",");
        }

        // 查询价格变动
        if (searchValue.getPriceChgFlg() == 1) {
            // 涨价
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^U'}}},");
        } else if (searchValue.getPriceChgFlg() == 2) {
            // 降价
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^D'}}},");
        } else if (searchValue.getPriceChgFlg() == 3) {
            // 击穿
            result.append("'skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^X'}}},");
        }

        // 获取翻译状态
        String transFlg = org.apache.commons.lang3.StringUtils.trimToNull(searchValue.getTransStsFlg());
        if (transFlg != null) {
            if("0".equalsIgnoreCase(transFlg)){
                result.append("'fields.translateStatus':{'$in':[null,'','0']}");
            }else{
                result.append(MongoUtils.splicingValue("fields.translateStatus", transFlg));
            }
            result.append(",");
        }

        // MINI MALL 店铺时查询原始CHANNEL
        if (searchValue.getOrgChaId() != null && !ChannelConfigEnums.Channel.NONE.getId().equals(searchValue.getOrgChaId())) {
            result.append(MongoUtils.splicingValue("orgChannelId", searchValue.getOrgChaId()));
            result.append(",");
        }

//        1.  = 出现搜索出入栏 -》 完全匹配 搜索输入栏输入的内容 eg {"a": "123123"}
//        2.  != 出现搜索出入栏 -》 不等于 搜索输入栏输入的内容 eg {"a": {$ne: "123123"}}
//        3.  = null   不出现搜索输入栏 -》搜索输入栏 不可编辑，检索条件为 eg {"a":{$in:[null],$exists:true}}
//        4.  != null  不出现搜索输入栏 -》搜索输入栏 不可编辑，检索条件为 eg {"a":{$ne:[null]}}
        //inputOptsKey: "",inputOpts: "",inputVal
//        long count = dao.countByQuery("{\"imageTemplateName\":\"" + ImageTemplateName + "\"" + ",\"imageTemplateId\": { $ne:" + ImageTemplateId + "}}");
        //自定义查询  sunpt
        List<Map<String, String>> custAttrMap = searchValue.getCustAttrMap();
        if (custAttrMap != null && custAttrMap.size() > 0) {
            for (Map<String, String> map : custAttrMap) {
                String inputOptsKey = map.get("inputOptsKey");//条件字段
                String inputOpts = map.get("inputOpts");//操作符
                String inputVal = map.get("inputVal");//值
                String optsWhere=getCustAttrOptsWhere(inputOptsKey,inputOpts,inputVal);
                if(!StringUtil.isEmpty(optsWhere)) {
                    result.append(optsWhere);
                    result.append(",");
                }
            }
        }
        return result.toString();
    }

    public String getCustAttrOptsWhere( String inputOptsKey ,String inputOpts, String inputVal)
    {
        //自定义查询  sunpt
        if(StringUtil.isEmpty(inputOptsKey)) return "";
        String result="";
        switch (inputOpts) {
            case "=":
                result=MongoUtils.splicingValue(inputOptsKey, inputVal);
                break;
            case "!=":
                result="\""+inputOptsKey+"\": { $ne:\"" + inputVal + "\"}}";
                break;
            case "=null":
                result="\""+inputOptsKey+"\":{$in:[null,\"\"],$exists:true}";
                break;
            case "!=null":
                result="$and:[{\""+inputOptsKey+"\": { $ne: null }},{\""+inputOptsKey +"\": { $ne: \"\" }}]";
                break;
        }
        return  result;
    }

    private void  writeHead (Workbook book,CmsSessionBean cmsSession){
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        Sheet sheet = book.getSheetAt(0);
        Row row = FileUtils.row(sheet, 0);

        CellStyle style = row.getCell(0).getCellStyle();

        int index = 16;
        if (commonProps != null) {
            for (Map<String, String> prop : commonProps) {
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2((prop.get("propName"))));
            }
        }

        if (customProps != null) {
            for (Map<String, String> prop : customProps) {
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")));
                FileUtils.cell(row, index++, style).setCellValue(StringUtils.null2Space2(prop.get("feed_prop_translation")) + "(en)");
            }
        }
    }

    /**
     * Code单位，文件输出
     *
     * @param book          输出Excel文件对象
     * @param items         待输出DB数据
     * @param cmsSession    cmsSessionBean
     * @param startRowIndex 开始
     * @return boolean 是否终止输出
     */
    private boolean writeRecordToFile(Workbook book, List<CmsBtProductBean> items, CmsSessionBean cmsSession, int startRowIndex) {
        boolean isContinueOutput = true;
        List<Map<String, String>> customProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_customProps");
        List<Map<String, String>> commonProps = (List<Map<String, String>>) cmsSession.getAttribute("_adv_search_commonProps");
        CellStyle unlock = FileUtils.createUnLockStyle(book);

            /*
             * 现有表格的列:
             * 0: No
             * 1: productId
             * 2: num_iid
             * 3: Code
             * 4: Brand
             * 5: product_type
             * 6: size_type
             * 7: Product_Name
             * 8: Product_Name_Cn
             * 9: Qty
             * 10: msrp
             * 11: retail_price
             * 12: Sale_Price
             * 13: 类目Path
             */
        Sheet sheet = book.getSheetAt(0);

        for (CmsBtProductBean item : items) {
            Row row = FileUtils.row(sheet, startRowIndex);

            // 最大行限制
            if (startRowIndex + 1 > MAX_EXCEL_REC_COUNT - 1) {
                isContinueOutput = false;
                FileUtils.cell(row, 0, unlock).setCellValue("未完，存在未抽出数据！");
                break;
            }
            int index = 0;

            // 内容输出
            FileUtils.cell(row, index++, unlock).setCellValue(startRowIndex);

            if(item.getGroupBean() != null && item.getGroupBean().getGroupId() != null){
                FileUtils.cell(row, index++, unlock).setCellValue(item.getGroupBean().getGroupId());
            }else{
                index++;
            }


            FileUtils.cell(row, index++, unlock).setCellValue(item.getProdId());

            if(item.getGroupBean() != null && item.getGroupBean().getNumIId() != null){
                FileUtils.cell(row, index++, unlock).setCellValue(item.getGroupBean().getNumIId());
            }else{
                index++;
            }

            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getCode());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getBrand());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getProductType());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getSizeType());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getProductNameEn());
            FileUtils.cell(row, index++, unlock).setCellValue(item.getFields().getLongTitle());
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(String.valueOf(item.getFields().getQuantity())));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceMsrpSt(), item.getFields().getPriceMsrpEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceRetailSt(), item.getFields().getPriceRetailEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(getOutputPrice(item.getFields().getPriceSaleSt(), item.getFields().getPriceSaleEd()));
            FileUtils.cell(row, index++, unlock).setCellValue(item.getCatPath());
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(item.getFields().getHsCodeCrop()));
            FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(item.getFields().getHsCodePrivate()));

            if (commonProps != null) {
                for (Map<String, String> prop : commonProps) {
                    Object value = item.getFields().getAttribute(prop.get("propId"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                }
            }

            if (customProps != null) {
                for (Map<String, String> prop : customProps) {
                    Object value = item.getFeed().getCnAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                    value = item.getFeed().getOrgAtts().getAttribute(prop.get("feed_prop_original"));
                    FileUtils.cell(row, index++, unlock).setCellValue(StringUtils.null2Space2(value == null ? "" : value.toString()));
                }
            }
            startRowIndex = startRowIndex + 1;
        }

        return isContinueOutput;
    }

    /**
     * 金额输出
     *
     * @param strPrice 最小金额
     * @param endPrice 最大金额
     * @return String 输出金额
     */
    private String getOutputPrice(Double strPrice, Double endPrice) {
        String output = "";
        if (strPrice != null && endPrice != null) {
            if (strPrice.equals(endPrice)) {
                output = String.valueOf(strPrice);
            } else {
                output = strPrice + "～" + endPrice;
            }
        }
        return output;
    }

    /**
     * 获取排序规则
     */
    private String setSortValue(CmsSearchInfoBean searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取排序字段1
        if (searchValue.getSortOneName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortOneName(),
                    searchValue.getSortOneType() == null ? -1 : Integer.valueOf(searchValue.getSortOneType())));
            result.append(",");
        }

        // 获取排序字段2
        if (searchValue.getSortTwoName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortTwoName()
                    , searchValue.getSortTwoType() == null ? -1 : Integer.valueOf(searchValue.getSortTwoType())));
            result.append(",");
        }

        // 获取排序字段3
        if (searchValue.getSortThreeName() != null) {
            result.append(MongoUtils.splicingValue("fields." + searchValue.getSortThreeName(),
                    searchValue.getSortThreeType() == null ? -1 : Integer.valueOf(searchValue.getSortThreeType())));
            result.append(",");
        }

        // 获取按销量排序字段
        if (searchValue.getSortSalesType() != null && searchValue.getSortSales() != null) {
            result.append(MongoUtils.splicingValue(searchValue.getSortSalesType(), Integer.valueOf(searchValue.getSortSales())));
            result.append(",");
        }

        return result.toString().length() > 0 ? "{" + result.toString().substring(0, result.toString().length() - 1) + "}" : null;
    }
}
