package com.voyageone.web2.cms.views.translation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.enums.LanguageType;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductTransDistrBean;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.web2.cms.bean.ProductTranslationBean;
import com.voyageone.web2.cms.bean.TranslateTaskBean;
import com.voyageone.web2.cms.dao.CustomWordDao;
import com.voyageone.web2.cms.dao.MongoNativeDao;
import com.voyageone.web2.cms.views.search.CmsSearchAdvanceService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class TranslationService {

    Log logger = LogFactory.getLog(TranslationService.class);
    @Autowired
    private CmsSearchAdvanceService searchIndexService;
    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Autowired
    private ProductService productService;

    @Autowired
    protected CustomWordDao customWordDao;

    @Autowired
    private MongoNativeDao mongoDao;

    private Map sortFields;

    private static String[] RET_FIELDS = {
            "prodId", "groups",
            "fields.code",
            "fields.productNameEn",
            "fields.longDesEn",
            "fields.shortDesEn",
            "fields.longTitle",
            "fields.middleTitle",
            "fields.shortTitle",
            "fields.longDesCn",
            "fields.shortDesCn",
            "fields.model",
            "fields.images1",
            "fields.translator",
            "modified"};

    /**
     * TODO 将来应该从配置数据库中读取.
     */
    public Map getSortFieldOptions(){
        if (sortFields == null){
            sortFields = new HashMap<>();
        }

        sortFields.put("quantity","库存");
        return sortFields;
    }

    /**
     * 获取当前用户未完成的任务.
     *
     * @param channelId
     * @param userName
     * @return
     * @throws BusinessException
     */
    public TranslateTaskBean getUndoneTasks(String channelId, String userName, int cartId) throws BusinessException {

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();

        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -48);
        String translateTimeStr = DateTimeUtil.format(date, null);

        String tasksQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'0','fields.translator':'%s','fields.translateTime':{'$gt':'%s'}}",userName,translateTimeStr);

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        queryObject.setProjection(RET_FIELDS);

        List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, queryObject);
        List<List<Map<String, String>>> grpImgList = searchIndexService.getGroupImageList(cmsBtProductModels, channelId, cartId);

        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(cmsBtProductModels, grpImgList);

        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setDistributeRule(1); // 缺省只查询主商品
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(channelId));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(channelId,userName));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(channelId));

        return translateTaskBean;
    }

    /**
     * 用户获取任务列表，自动分发任务.
     *
     * @param channelId
     * @param userName
     */
    public TranslateTaskBean assignTask(String channelId, String userName, int distributeRule, int distCount, String sortCondition, Boolean sortRule, int cartId) {
        ProductTransDistrBean productTransDistrBean = new ProductTransDistrBean();

        if (!StringUtils.isEmpty(sortCondition)){
            String sortField = "fields." + sortCondition;
            if (sortRule) {
                productTransDistrBean.setSortStr(sortField + ":1");
            } else {
                productTransDistrBean.setSortStr(sortField + ":-1");
            }
        }

        productTransDistrBean.setTranslator(userName);
        productTransDistrBean.setTranslateTimeHDiff(24);
        productTransDistrBean.setDistributeRule(distributeRule);
        if (distCount > 0){
            productTransDistrBean.setLimit(distCount);
        }
        //设定返回值.
        productTransDistrBean.setProjectionArr(RET_FIELDS);

        List<CmsBtProductModel> cmsBtProductModels = productService.translateDistribute(channelId, productTransDistrBean);
        List<List<Map<String, String>>> grpImgList = searchIndexService.getGroupImageList(cmsBtProductModels, channelId, cartId);

        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(cmsBtProductModels, grpImgList);

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();

        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(channelId));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(channelId,userName));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(channelId));

        return translateTaskBean;
    }

//    /**
//     *
//     * @param productsGetRequest
//     */
//    private void setReturnValue(VoApiRequest productsGetRequest) {
//
//        productsGetRequest.addField("prodId");
//        productsGetRequest.addField("fields.code");
//        productsGetRequest.addField("fields.productNameEn");
//        productsGetRequest.addField("fields.longDesEn");
//        productsGetRequest.addField("fields.shortDesEn");
//
//        productsGetRequest.addField("fields.longTitle");
//        productsGetRequest.addField("fields.middleTitle");
//        productsGetRequest.addField("fields.shortTitle");
//        productsGetRequest.addField("fields.longDesCn");
//        productsGetRequest.addField("fields.shortDesCn");
//        productsGetRequest.addField("fields.model");
//        productsGetRequest.addField("fields.images1");
//        productsGetRequest.addField("fields.translator");
//        productsGetRequest.addField("modified");
//    }

    /**
     * 暂时保存翻译任务.
     * @param channelId
     * @param userName
     * @param taskBean
     * @return
     */
    public TranslateTaskBean saveTask(String channelId, String userName, ProductTranslationBean taskBean, String transSts, int cartId) {
        // 先查询该商品对应的group信息
        logger.debug("TranslationService.saveTask() 商品ProdId=" + taskBean.getProdId());
        DBObject excObj = new BasicDBObject();
        excObj.put("groups.platforms.cartId", 1);
        excObj.put("groups.platforms.groupId", 1);
        excObj.put("groups.platforms.isMain", 1);

        List<DBObject> rslt = mongoDao.find("cms_bt_product_c" + channelId, new BasicDBObject("prodId", taskBean.getProdId()), excObj);
        int groupId = 0;
        for (DBObject obj : rslt) {
            DBObject groupObj = (DBObject) obj.get("groups");
            if (groupObj != null) {
                Object platObj = groupObj.get("platforms");
                if (platObj != null) {
                    for (Object obj2 : (List) platObj) {
                        int cartId2 = (Integer) ((DBObject) obj2).get("cartId");
                        int isMain = (Integer) ((DBObject) obj2).get("isMain");
                        if (cartId == cartId2 && isMain == 1) {
                            groupId = (Integer) ((DBObject) obj2).get("groupId");
                            break;
                        }
                    }
                }
            }
        }

        DBObject params = new BasicDBObject();
        if (groupId == 0) {
            logger.warn("TranslationService.saveTask() 无group信息 商品ProdId=" + taskBean.getProdId());
            // 只更新该商品信息
            params.put("prodId", taskBean.getProdId());
        } else {
            // 更新该group下的商品信息
            DBObject pal4 = new BasicDBObject();
            pal4.put("cartId", cartId);
            pal4.put("groupId", groupId);
            DBObject pal3 = new BasicDBObject();
            pal3.put("$elemMatch", pal4);
            params.put("groups.platforms", pal3);
        }

        DBObject updObj = new BasicDBObject();
        updObj.put("fields.translator", userName);
        updObj.put("fields.translateStatus", transSts);
        updObj.put("fields.translateTime", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        updObj.put("fields.longTitle", taskBean.getLongTitle());
        updObj.put("fields.middleTitle", taskBean.getMiddleTitle());
        updObj.put("fields.shortTitle", taskBean.getShortTitle());
        updObj.put("fields.longDesCn", taskBean.getLongDesCn());
        updObj.put("fields.shortDesCn", taskBean.getShortDesCn());
        // 保存翻译后的信息
        mongoDao.update("cms_bt_product_c" + channelId, params, updObj);

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setModifiedTime(DateTimeUtil.getNowTimeStamp());
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(channelId));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(channelId, userName));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(channelId));
        return translateTaskBean;
    }

    /**
     * 从当前产品所在分组中拷贝已经翻译好的信息到当前类目.
     *
     * @return
     * @throws BusinessException
     */
    public ProductTranslationBean copyFormMainProduct(String channelId,ProductTranslationBean translationBean) {

        String tasksQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.model':'%s'}",translationBean.getModel());

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        queryObject.setProjection("fields.longTitle", "fields.middleTitle", "fields.shortTitle", "fields.longDesCn", "fields.shortDesCn");

        CmsBtProductModel productModel = productService.getProductWithQuery(channelId, queryObject);

        if (productModel == null){

           throw new BusinessException("没用可参照的数据，请直接翻译！");
        }

        CmsBtProductModel_Field productFields = productModel.getFields();

        translationBean.setLongTitle(productFields.getLongTitle());
        translationBean.setMiddleTitle(productFields.getMiddleTitle());
        translationBean.setShortTitle(productFields.getShortTitle());
        translationBean.setLongDesCn(productFields.getLongDesCn());
        translationBean.setShortDesCn(productFields.getShortDesCn());

        return translationBean;
    }

    /**
     * 获取当前用户未完成的任务.
     *
     * @param channelId
     * @param userName
     * @return
     * @throws BusinessException
     */
    public TranslateTaskBean searchUserTasks(String channelId, String userName,String condition, int cartId) throws BusinessException {
        if (StringUtils.isEmpty(condition)) {
            return this.getUndoneTasks(channelId, userName, cartId);
        }

        String tasksQueryStr = String.format("{'fields.translator':'%s',$or:[{'fields.code':{$regex:'%s'}},{'fields.productNameEn':{$regex:'%s'}},{'fields.longDesEn':{$regex:'%s'}},{'fields.shortDesEn':{$regex:'%s'}},{'fields.longTitle':{$regex:'%s'}},{'fields.middleTitle':{$regex:'%s'}},{'fields.shortTitle':{$regex:'%s'}},{'fields.longDesCn':{$regex:'%s'}},{'fields.shortDesCn':{$regex:'%s'}}]}",userName,condition,condition,condition,condition,condition,condition,condition,condition,condition);

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery(tasksQueryStr);
        //设定返回值.
        queryObject.setProjection(RET_FIELDS);

        List<CmsBtProductModel> cmsBtProductModels = productService.getList(channelId, queryObject);
        List<List<Map<String, String>>> grpImgList = searchIndexService.getGroupImageList(cmsBtProductModels, channelId, cartId);

        List<ProductTranslationBean> translateTaskBeanList = buildTranslateTaskBeen(cmsBtProductModels, grpImgList);

        TranslateTaskBean translateTaskBean = new TranslateTaskBean();
        translateTaskBean.setProductTranslationBeanList(translateTaskBeanList);
        translateTaskBean.setTotalDoneCount(this.getTotalDoneCount(channelId));
        translateTaskBean.setUserDoneCount(this.getDoneTaskCount(channelId,userName));
        translateTaskBean.setTotalUndoneCount(this.getTotalUndoneCount(channelId));

        return translateTaskBean;
    }

    /**
     * 组装task beans。
     * @param cmsBtProductModels
     * @return
     */
    private List<ProductTranslationBean> buildTranslateTaskBeen(List<CmsBtProductModel> cmsBtProductModels, List<List<Map<String, String>>> grpImgList) {
        List<ProductTranslationBean> translateTaskBeanList = new ArrayList<>(cmsBtProductModels.size());
        int idx = 0;
        for (CmsBtProductModel productModel : cmsBtProductModels) {
            ProductTranslationBean translationBean = new ProductTranslationBean();

            translationBean.setLongDescription(productModel.getFields().getLongDesEn());
            translationBean.setProductName(productModel.getFields().getProductNameEn());
            translationBean.setShortDescription(productModel.getFields().getShortDesEn());

            translationBean.setLongTitle(productModel.getFields().getLongTitle());
            translationBean.setMiddleTitle(productModel.getFields().getMiddleTitle());
            translationBean.setShortTitle(productModel.getFields().getShortTitle());
            translationBean.setLongDesCn(productModel.getFields().getLongDesCn());
            translationBean.setShortDesCn(productModel.getFields().getShortDesCn());
            translationBean.setModel(productModel.getFields().getModel());
            translationBean.setProductCode(productModel.getFields().getCode());
            translationBean.setProductImage(productModel.getFields().getImages1().get(0).getName());
            translationBean.setModifiedTime(productModel.getModified());
            translationBean.setProdId(productModel.getProdId());
            translationBean.setTranslator(productModel.getFields().getTranslator());

            translationBean.setGroupImgList(grpImgList.get(idx));
            idx ++;
            translateTaskBeanList.add(translationBean);
        }
        return translateTaskBeanList;
    }

    /**
     * 获取个人完成翻译数.
     * @param channelId
     * @param userName
     */
    private int getDoneTaskCount(String channelId, String userName) {
        String doneTaskCountQueryStr = String.format("{'fields.status':{'$nin':['New']},'fields.translateStatus':'1','fields.translator':'%s'}", userName);
        return ((Long)productService.getCnt(channelId, doneTaskCountQueryStr)).intValue();
    }

    /**
     * 获取所有完成的数量.
     * @param channelId
     */
    private int getTotalDoneCount(String channelId) {
        String totalDoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':'1'}";
        return ((Long)productService.getCnt(channelId, totalDoneCountQueryStr)).intValue();
    }

    /**
     * 设定个人完成翻译数、完成翻译总数、待翻译总数.
     * @param channelId
     */
    private int getTotalUndoneCount(String channelId) {
        String totalUndoneCountQueryStr = "{'fields.status':{'$nin':['New']},'fields.translateStatus':{'$in':[null,'','0']}}";
        return ((Long)productService.getCnt(channelId, totalUndoneCountQueryStr)).intValue();
    }

    /**
     * 获取 feed info model.
     *
     * @param channelId
     * @param productCode
     * @return
     */
    public Map<String, String> getFeedAttributes(String channelId, String productCode) {
        CmsBtFeedInfoModel feedInfoModel = cmsBtFeedInfoDao.selectProductByCode(channelId, productCode);

        if (feedInfoModel == null) {
            //feed 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId + " product code: " + productCode + " 对应的品牌方信息不存在！";

            logger.warn(errMsg);

        }

        Map<String, String> feedAttributes = new HashMap<>();

        if (!StringUtils.isEmpty(feedInfoModel.getCategory())) {
            feedAttributes.put("category", feedInfoModel.getCategory());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getCode())) {
            feedAttributes.put("code", feedInfoModel.getCode());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getName())) {
            feedAttributes.put("name", feedInfoModel.getName());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getModel())) {
            feedAttributes.put("model", feedInfoModel.getModel());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getColor())) {
            feedAttributes.put("color", feedInfoModel.getColor());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getOrigin())) {
            feedAttributes.put("origin", feedInfoModel.getOrigin());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getSizeType())) {
            feedAttributes.put("sizeType", feedInfoModel.getSizeType());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getBrand())) {
            feedAttributes.put("brand", feedInfoModel.getBrand());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getWeight())) {
            feedAttributes.put("weight", feedInfoModel.getWeight());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getShort_description())) {
            feedAttributes.put("short_description", feedInfoModel.getShort_description());
        }

        if (!StringUtils.isEmpty(feedInfoModel.getLong_description())) {
            feedAttributes.put("long_description", feedInfoModel.getLong_description());
        }

        if (!StringUtils.isEmpty(String.valueOf(feedInfoModel.getUpdFlg()))) {
            feedAttributes.put("updFlg", String.valueOf(feedInfoModel.getUpdFlg()));
        }

        Map<String, List<String>> attributes = feedInfoModel.getAttribute();

        Map<String, String> attributesMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {

            StringBuilder valueStr = new StringBuilder();

            List<String> values = entry.getValue();

            if (values != null) {
                for (int i = 0; i < values.size(); i++) {
                    if (i < values.size() - 1) {
                        valueStr.append(values.get(i)).append("/");
                    } else {
                        valueStr.append(values.get(i));
                    }
                }
            }

            attributesMap.put(entry.getKey(), valueStr.toString());
        }

        feedAttributes.putAll(attributesMap);
        return feedAttributes;
    }

    /**
     * 校验输入文字长度
     * @param requestBean
     */
    public void verifyParameter(ProductTranslationBean requestBean, String channelId) {
        if (StringUtils.isEmpty(requestBean.getLongTitle())){
            throw new BusinessException("长标题不能为空");
        }
        if (StringUtils.isEmpty(requestBean.getMiddleTitle())){
            throw new BusinessException("中标题不能为空");
        }
        if (StringUtils.isEmpty(requestBean.getShortTitle())){
            throw new BusinessException("短标题不能为空");
        }
        if (StringUtils.isEmpty(requestBean.getLongDesCn())){
            throw new BusinessException("长描述不能为空");
        }
        if (StringUtils.isEmpty(requestBean.getShortDesCn())){
            throw new BusinessException("短描述不能为空");
        }

        Map<String, Object> setInfo = getTransLenSet(channelId);
        Map<String, Object> lenColl = (Map<String, Object>) setInfo.get("long_title");
        int minLen = NumberUtils.toInt((String) lenColl.get("minLen"));
        int maxLen = NumberUtils.toInt((String) lenColl.get("maxLen"));
        int strLen = requestBean.getLongTitle().length();
        if (strLen < minLen) {
            throw new BusinessException("长标题必须至少输入" + minLen + "个文字");
        }
        if (strLen > maxLen) {
            throw new BusinessException("长标题不能超过" + maxLen + "个文字");
        }

        lenColl = (Map<String, Object>) setInfo.get("middle_title");
        minLen = NumberUtils.toInt((String) lenColl.get("minLen"));
        maxLen = NumberUtils.toInt((String) lenColl.get("maxLen"));
        strLen = requestBean.getMiddleTitle().length();
        if (strLen < minLen) {
            throw new BusinessException("中标题必须至少输入" + minLen + "个文字");
        }
        if (strLen > maxLen) {
            throw new BusinessException("中标题不能超过" + maxLen + "个文字");
        }

        lenColl = (Map<String, Object>) setInfo.get("short_title");
        minLen = NumberUtils.toInt((String) lenColl.get("minLen"));
        maxLen = NumberUtils.toInt((String) lenColl.get("maxLen"));
        strLen = requestBean.getShortTitle().length();
        if (strLen < minLen) {
            throw new BusinessException("短标题必须至少输入" + minLen + "个文字");
        }
        if (strLen > maxLen) {
            throw new BusinessException("短标题不能超过" + maxLen + "个文字");
        }

        lenColl = (Map<String, Object>) setInfo.get("long_desc");
        minLen = NumberUtils.toInt((String) lenColl.get("minLen"));
        maxLen = NumberUtils.toInt((String) lenColl.get("maxLen"));
        strLen = requestBean.getLongDesCn().length();
        if (strLen < minLen) {
            throw new BusinessException("长描述必须至少输入" + minLen + "个文字");
        }
        if (strLen > maxLen) {
            throw new BusinessException("长描述不能超过" + maxLen + "个文字");
        }

        lenColl = (Map<String, Object>) setInfo.get("short_desc");
        minLen = NumberUtils.toInt((String) lenColl.get("minLen"));
        maxLen = NumberUtils.toInt((String) lenColl.get("maxLen"));
        strLen = requestBean.getShortDesCn().length();
        if (strLen < minLen) {
            throw new BusinessException("短描述必须至少输入" + minLen + "个文字");
        }
        if (strLen > maxLen) {
            throw new BusinessException("短描述不能超过" + maxLen + "个文字");
        }
    }

    // 获取翻译时标题和描述的长度设置
    public Map<String, Object> getTransLenSet(String chnId) {
        Map<String, Object> setInfo = new HashMap<String, Object>();
        List<Map<String, Object>> rslt = customWordDao.getTransLenSet(chnId);
        for (Map<String, Object> item : rslt) {
            String lenType = (String) item.get("lenType");
            item.remove("lenType");
            setInfo.put(lenType, item);
        }
        return setInfo;
    }
}
