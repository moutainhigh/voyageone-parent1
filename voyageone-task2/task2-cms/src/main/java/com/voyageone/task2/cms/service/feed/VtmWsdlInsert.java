package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.SuperFeed2Dao;
import com.voyageone.task2.cms.dao.feed.VtmDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoVtmModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/8
 */
@Service
public class VtmWsdlInsert extends BaseCronTaskService {
    private static final String INSERT_FLG = " UpdateFlag = 1 ";

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    protected SuperFeed2Dao superFeedDao;

    @Autowired
    private VtmDao vtmDao;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "Vitamin";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("该 Service 为子 Service. 无可用启动逻辑.");
    }

    class Context {

        protected final ChannelConfigEnums.Channel channel;
        protected final String table;

        protected Context(ChannelConfigEnums.Channel channel) {
            this.channel = channel;
            // 主表
            this.table = Feeds.getVal1(channel, FeedEnums.Name.table_id);
        }

        private List<CmsBtFeedInfoModel> getModels(String category) throws Exception {

            Map<String, Object> column = getColumns();
            Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();


            // 条件则根据类目筛选
            String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                    category.replace("'", "\\\'"));

            List<CmsBtFeedInfoVtmModel> vtmModelBeans = vtmDao.selectSuperfeedModel(where, column, table);
            List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
            for (CmsBtFeedInfoVtmModel vtmModelBean : vtmModelBeans) {
                Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
                Map<String, List<String>> attribute = new HashMap<>();
                for (int i = 1; i <= 50; i++) {
                    String attributeName = Feeds.getVal1(channel, FeedEnums.Name.valueOf("attribute" + i));
                    if (StringUtil.isEmpty(attributeName)) {
                        break;
                    }
                    if (temp.get(attributeName) != null) {
                        List<String> values = new ArrayList<>();
                        values.add((String) temp.get(attributeName));
                        attribute.put(attributeName, values);
                    }
                }

                CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(channel);
                cmsBtFeedInfoModel.setAttribute(attribute);

                //设置重量
                List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
                for (CmsBtFeedInfoModel_Sku sku : skus) {
                    String Weight = sku.getWeightOrg().trim();
                    Pattern pattern = Pattern.compile("[^0-9.]");
                    Matcher matcher = pattern.matcher(Weight);
                    if (matcher.find()) {
                        int index = Weight.indexOf(matcher.group());
                        if (index != -1) {
                            String weightOrg = Weight.substring(0, index);
                            sku.setWeightOrg(weightOrg);
                        }
                    }
                    sku.setWeightOrgUnit(sku.getWeightOrgUnit());
                }
                cmsBtFeedInfoModel.setSkus(skus);
                //设置重量结束

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
            }

            $info("取得 [ %s ] 的 Product 数 %s", category, modelBeans.size());

            return modelBeans;
        }

        private HashMap<String, Object> getColumns() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("category", Feeds.getVal1(channel, FeedEnums.Name.category_column));
            map.put("channel_id", channel.getId());
            map.put("m_product_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_product_type));
            map.put("m_brand", Feeds.getVal1(channel, FeedEnums.Name.model_m_brand));
            map.put("m_model", Feeds.getVal1(channel, FeedEnums.Name.model_m_model));
            map.put("m_name", Feeds.getVal1(channel, FeedEnums.Name.model_m_name));
            map.put("m_size_type", Feeds.getVal1(channel, FeedEnums.Name.model_m_size_type));
            map.put("m_weight", Feeds.getVal1(channel, FeedEnums.Name.model_m_weight));
            map.put("p_code", (Feeds.getVal1(channel, FeedEnums.Name.product_p_code)));
            map.put("p_name", (Feeds.getVal1(channel, FeedEnums.Name.product_p_name)));
            map.put("p_color", (Feeds.getVal1(channel, FeedEnums.Name.product_p_color)));
            map.put("p_made_in_country", (Feeds.getVal1(channel, FeedEnums.Name.product_p_made_in_country)));
            map.put("pe_short_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_short_description)));
            map.put("pe_long_description", (Feeds.getVal1(channel, FeedEnums.Name.product_pe_long_description)));
            map.put("i_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_sku)));
            map.put("i_itemcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_itemcode)));
            map.put("i_size", (Feeds.getVal1(channel, FeedEnums.Name.item_i_size)));
            map.put("i_barcode", (Feeds.getVal1(channel, FeedEnums.Name.item_i_barcode)));
            map.put("image", (Feeds.getVal1(channel, FeedEnums.Name.images)));
            map.put("i_client_sku", (Feeds.getVal1(channel, FeedEnums.Name.item_i_client_sku)));
            map.put("client_product_url", (Feeds.getVal1(channel, FeedEnums.Name.client_product_url)));
            map.put("product_type", (Feeds.getVal1(channel, FeedEnums.Name.product_type)));


            map.put("price_client_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_client_msrp)));
            map.put("price_client_retail", (Feeds.getVal1(channel, FeedEnums.Name.price_client_retail)));
            map.put("price_net", (Feeds.getVal1(channel, FeedEnums.Name.price_net)));
            map.put("price_current", (Feeds.getVal1(channel, FeedEnums.Name.price_current)));
            map.put("price_msrp", (Feeds.getVal1(channel, FeedEnums.Name.price_msrp)));

            map.put("quantity", (Feeds.getVal1(channel, FeedEnums.Name.quantity)));
            map.put("material", (Feeds.getVal1(channel, FeedEnums.Name.material)));
            map.put("usage_en", (Feeds.getVal1(channel, FeedEnums.Name.usage_en)));
            map.put("weight_org", (Feeds.getVal1(channel, FeedEnums.Name.weight_org)));
            map.put("weight_org_unit", (Feeds.getVal1(channel, FeedEnums.Name.weight_org_unit)));
            map.put("weight_calc", (Feeds.getVal1(channel, FeedEnums.Name.weight_calc)));

            map.put("translationName1", (Feeds.getVal1(channel, FeedEnums.Name.translationName1)));
            map.put("translationValue1", (Feeds.getVal1(channel, FeedEnums.Name.translationValue1)));
            map.put("translationName2", (Feeds.getVal1(channel, FeedEnums.Name.translationName2)));
            map.put("translationValue2", (Feeds.getVal1(channel, FeedEnums.Name.translationValue2)));

            return map;
        }

        /**
         * 查询一共有多少类目
         *
         * @return 类目清单
         */
        private List<String> getCategories() {

            // 先从数据表中获取所有商品的类目路径,经过去重复的
            // update flg 标记, 只获取哪些即将进行新增的商品的类目
            List<String> categoryPaths = superFeedDao.selectSuperfeedCategory(
                    Feeds.getVal1(channel, FeedEnums.Name.category_column), table, " AND " + INSERT_FLG);
            $info("获取类目路径数 %s , 准备拆分继续处理", categoryPaths.size());

            return categoryPaths;
        }

        /**
         * 生成类目数据包含model product数据
         */
        protected List<CmsBtFeedInfoModel> getCategoryInfo(String categoryPath) throws Exception {

            return getModels(categoryPath);
        }

        /**
         * 调用 WsdlProductService 提交新商品
         *
         * @throws Exception
         */
        protected void postNewProduct() throws Exception {
            $info("准备 <构造> 类目树");
            List<String> categoryPaths = getCategories();

            List<CmsBtFeedInfoModel> productSuccessList = new ArrayList<>();
            // 准备接收失败内容
            List<CmsBtFeedInfoModel> productFailAllList = new ArrayList<>();
            List<CmsBtFeedInfoModel> productAll = new ArrayList<>();

            for (String categoryPath : categoryPaths) {

                // 每棵树的信息取得
                $info("每棵树的信息取得开始" + categoryPath);
                List<CmsBtFeedInfoModel> product;
                try {
                    product = getCategoryInfo(categoryPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                $info("每棵树的信息取得结束");

                product.forEach(cmsBtFeedInfoModel -> {
                    List<String> categories = java.util.Arrays.asList(cmsBtFeedInfoModel.getCategory().split(":"));
                    cmsBtFeedInfoModel.setCategory(categories.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-")));
                });
                productAll.addAll(product);
                if (productAll.size() > 500) {
                    executeMongoDB(productAll, productSuccessList, productFailAllList);
                }
            }
            executeMongoDB(productAll, productSuccessList, productFailAllList);

            $info("总共~ 失败的 Product: %s", productFailAllList.size());

        }

        /**
         * MongoDB插入更新
         *
         * @param productAll         全部更新对象
         * @param productSucceeList  接收成功更新对象
         * @param productFailAllList 全部更新失败对象
         */
        private void executeMongoDB(List<CmsBtFeedInfoModel> productAll, List<CmsBtFeedInfoModel> productSucceeList, List<CmsBtFeedInfoModel> productFailAllList) {
            try {
                $info("插入mongodb开始");
                Map response = feedToCmsService.updateProduct(channel.getId(), productAll, getTaskName());
                $info("插入mongodb结束");
                List<String> itemIds = new ArrayList<>();
                productSucceeList = (List<CmsBtFeedInfoModel>) response.get("succeed");
                productSucceeList.forEach(feedProductModel -> feedProductModel.getSkus().forEach(feedSkuModel -> itemIds.add(feedSkuModel.getClientSku())));
                updateFull(itemIds);
                productFailAllList.addAll((List<CmsBtFeedInfoModel>) response.get("fail"));
            } catch (Exception e) {
                $error(e);
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            } finally {
                productSucceeList.clear();
                productAll.clear();
            }
        }

        /**
         * 导入成功的FEED数据保存起来
         */
        @Transactional
        private void updateFull(List<String> itemIds) {
            if (itemIds.size() > 0) {
                vtmDao.delFull(itemIds);
                vtmDao.insertFull(itemIds);
                vtmDao.updateFeetStatus(itemIds);
            }
        }
    }
}
