package com.voyageone.task2.cms.service.platform;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.redis.CacheHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jason.jiang on 2016/07/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsPlatformActiveLogServiceTest {

    @Autowired
    CmsPlatformActiveLogService targetService;

    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString();

    @Before
    public void setUp() {
        // 准备参数
        Shops.reload();
        Map<String, ShopBean> shopBeanMap = new HashMap<>();
        // tmall
        ShopBean bean1 = new ShopBean();
        bean1.setCart_id("23");
        bean1.setPlatform_id("1");
        bean1.setOrder_channel_id("010");
        bean1.setApp_url("http://gw.api.taobao.com/router/rest");

        shopBeanMap.put(buildKey(bean1.getCart_id(), bean1.getOrder_channel_id()), bean1);
        // jingdong
        ShopBean bean2 = new ShopBean();
        bean2.setCart_id("28");
        bean2.setPlatform_id("2");
        bean2.setOrder_channel_id("928");
        bean2.setApp_url("https://api.jd.com/routerjson");

        shopBeanMap.put(buildKey(bean2.getCart_id(), bean2.getOrder_channel_id()), bean2);
        CacheHelper.reFreshSSB(KEY, shopBeanMap);
    }

    @Test
    public void testTMPlatformToOnsale() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartIdList", new ArrayList(Arrays.asList(23)));
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creater", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("CRWNRG01LBO-10","CRWNRG01LBO-9","CRWNRG01LBO-8","CRWNRG01LBO-7","CRWNRG01LBO-6","CRWNRG01LBO-5")));
        try {
            targetService.onStartup(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTMPlatformToInstock() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "010");
        logParams.put("cartIdList", new ArrayList(Arrays.asList(23)));
        logParams.put("activeStatus", "ToInStock");
        logParams.put("creater", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("CRWNRG01LBO-10","CRWNRG01LBO-9","CRWNRG01LBO-8","CRWNRG01LBO-7","CRWNRG01LBO-6","CRWNRG01LBO-5")));
        try {
            targetService.onStartup(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJDPlatformToOnsale() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "928");
        logParams.put("cartIdList", new ArrayList(Arrays.asList(28)));
        logParams.put("activeStatus", "ToOnSale");
        logParams.put("creater", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("VN-04OJJPV")));
        try {
            targetService.onStartup(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJDPlatformToInstock() {
        // 发送请求到MQ,插入操作历史记录
        Map<String, Object> logParams = new HashMap<>(6);
        logParams.put("channelId", "928");
        logParams.put("cartIdList", new ArrayList(Arrays.asList(28)));
        logParams.put("activeStatus", "ToInStock");
        logParams.put("creater", "will2");
        logParams.put("comment", "高级检索 批量上下架");
        logParams.put("codeList", new ArrayList(Arrays.asList("VN-04OJJPV")));
        try {
            targetService.onStartup(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * build redis hash Key
     */
    private static String buildKey(String cart_id, String order_channel_id) {
        return cart_id + CacheKeyEnums.SKIP + order_channel_id;
    }
}