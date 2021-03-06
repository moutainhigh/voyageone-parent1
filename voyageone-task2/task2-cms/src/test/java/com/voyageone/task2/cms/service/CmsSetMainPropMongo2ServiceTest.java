package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.category.match.FeedQuery;
import com.voyageone.category.match.MatchResult;
import com.voyageone.category.match.Searcher;
import com.voyageone.category.match.Tokenizer;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.solr.bean.SolrUpdateBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by james on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSetMainPropMongo2ServiceTest {

    @Autowired
    CmsSetMainPropMongo2Service cmsSetMainPropMongo2Service;
    @Autowired
    ProductService productService;

    @Autowired
    CmsProductSearchService cmsProductSearchService;

    @Autowired
    private Searcher searcher;


    @Test
    public void onStartup() throws Exception {
        TaskControlBean taskControlBean = new TaskControlBean();
        taskControlBean.setCfg_name("order_channel_id");
        taskControlBean.setCfg_val1("001");
        cmsSetMainPropMongo2Service.onStartup(Collections.singletonList(taskControlBean));
//        getMainCatInfo("Women's Sleepwear & Robes","pajama-sets","womens","Body Frosting Womens Cotton 2PC Pajama Set Tan S","Body Frosting");

    }
    public MatchResult getMainCatInfo(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 取得查询条件
        FeedQuery query = getFeedQuery(feedCategoryPath, productType, sizeType, productNameEn, brand);

        // 调用主类目匹配接口，取得匹配度最高的一个主类目和sizeType
        MatchResult searchResult = searcher.search(query, false);
        if (searchResult == null) {
            String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
                    "[sizeType:%s] [productNameEn:%s] [brand:%s]", feedCategoryPath, productType, sizeType, productNameEn, brand);
            return null;
        }

        // 取得匹配度最高的主类目
        return searchResult;
    }
    private FeedQuery getFeedQuery(String feedCategoryPath, String productType, String sizeType, String productNameEn, String brand) {
        // 调用Feed到主数据的匹配程序匹配主类目
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedCategoryPath, null, tokenizer);
        query.setProductType(productType);
        query.setSizeType(sizeType);
        query.setProductName(productNameEn, brand);

        return query;
    }
    @Test
    public void test(){
        CmsBtProductModel cmsBtProductModel = productService.getProductByCode("001", "68220-gem");
        if (cmsBtProductModel != null) {
            SolrUpdateBean update = cmsProductSearchService.createSolrBeanForNew(cmsBtProductModel, null);

            if (update != null) {
                String response = cmsProductSearchService.saveBean(update);
                System.out.println(response);
            }
        }
    }

    @Test
    public void testOnStartup () throws Exception {
        TaskControlBean t = new TaskControlBean();
        t.setTask_id("2");
        t.setCfg_name("order_channel_id");
        t.setCfg_val1("001");
        cmsSetMainPropMongo2Service.onStartup(Collections.singletonList(t));
    }

}