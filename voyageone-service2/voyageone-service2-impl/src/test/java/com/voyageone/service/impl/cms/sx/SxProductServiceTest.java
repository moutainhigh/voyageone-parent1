package com.voyageone.service.impl.cms.sx;

import com.taobao.api.domain.Picture;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.product.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/5/12.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class SxProductServiceTest {

    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductService productService;

    @Test
    public void insertSxWorkLoadTest_WithProductModel() throws Exception {
        sxProductService.insertSxWorkLoad(productService.getProductById("010", 5944), "tester");
    }

    @Test
    public void insertSxWorkLoadTest() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, null, "tester");
    }

    @Test
    public void insertSxWorkLoadTest_WithCart() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        sxProductService.insertSxWorkLoad("018", codeList, 23, "tester");
    }

    @Test
    public void getSxProductDataByGroupIdTest() {
        SxData sxData = sxProductService.getSxProductDataByGroupId("010", 3L);
        System.out.println(sxData == null);
    }

    @Test
    public void testSortSkuInfo() {

        List<BaseMongoMap<String, Object>> skuList = new ArrayList<>();
        BaseMongoMap<String, Object> sku1 = new BaseMongoMap<>();
        sku1.put("skuCode", "A001");
        sku1.put("sizeSx", "43");  // 尺码排序只支持SkuSort枚举变量中定义的"XXL"等排序，不支持英文，汉字等排序
        skuList.add(sku1);
        BaseMongoMap<String, Object> sku2 = new BaseMongoMap<>();
        sku2.put("skuCode", "A002");
        sku2.put("sizeSx", "42");
        skuList.add(sku2);
        BaseMongoMap<String, Object> sku3 = new BaseMongoMap<>();
        sku3.put("skuCode", "A003");
        sku3.put("sizeSx", "41");
        skuList.add(sku3);
        sxProductService.sortSkuInfo(skuList);

//        for (BaseMongoMap<String, Object> sku:skuList) {
//            System.out.println(sku.get("skuCode")+":"+sku.get("sizeSx"));
//        }
        skuList.forEach(s -> System.out.println(s.get("skuCode")+":"+s.get("sizeSx")));
    }

    @Test
    public void testUploadImageByUrl() {
        // 上传图片到天猫同购测试店铺图片空间（因为没有后台账号，所以只能调用API上传图片到天猫店铺图片空间）
        String channelId = "010";
        int cartId = 30;

        // 天猫同购测试店铺，没给我们后台管理员账户，天猫的人管理的一个官网同购测试店铺
        ShopBean shopProp = new ShopBean();
        shopProp.setOrder_channel_id(channelId);
        shopProp.setCart_id(String.valueOf(cartId));
        shopProp.setApp_url("http://gw.api.taobao.com/router/rest");
        shopProp.setAppKey("23239809");
        shopProp.setAppSecret("");
        shopProp.setSessionKey("");
        // platformid默认为天猫（1），expressionParser.parse里面会上传照片到天猫空间
        shopProp.setPlatform_id("1");
        // for test only==============================================================

        // 保存返回的天猫图片空间URL地址
        Map<String, String> retUrls = new HashMap<>();
        // 需要上传的图片原始URL
        List<String> srcUrls = new ArrayList<>();
        // 天猫同购PC端详情页用固定图片原始URL（只传需要上传到店铺空间的固定图片，商品图片等动态图片会自动上传的）
        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB27cR8aMCN.eBjSZFoXXXj0FXa_!!2183719539.jpg"); // img1
        srcUrls.add("https://img.alicdn.com/imgextra/i3/2183719539/TB2gaJ8aRaM.eBjSZFMXXcypVXa_!!2183719539.jpg"); // img2
        srcUrls.add("https://img.alicdn.com/imgextra/i1/2183719539/TB2TOt7aRyN.eBjSZFkXXb8YFXa_!!2183719539.jpg"); // img3
        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB2V1R2aRyN.eBjSZFIXXXbUVXa_!!2183719539.jpg"); // img4
        srcUrls.add("https://img.alicdn.com/imgextra/i2/2183719539/TB2IP1Waw1J.eBjy0FpXXaMoXXa_!!2183719539.jpg"); // img9
        srcUrls.add("https://img.alicdn.com/imgextra/i2/2183719539/TB2AeWQaCOI.eBjy1zkXXadxFXa_!!2183719539.jpg"); // img10
        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB2NqCMaraI.eBjSspaXXXIKpXa_!!2183719539.jpg"); // img11

        // 天猫同购无线描述用固定图片原始URL
//        srcUrls.add("https://img.alicdn.com/imgextra/i3/2183719539/TB2CfX1aLOM.eBjSZFqXXculVXa_!!2183719539.jpg"); // img1
//        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB2llGRaraI.eBjSszdXXaB6XXa_!!2183719539.jpg"); // img2
//        srcUrls.add("https://img.alicdn.com/imgextra/i3/2183719539/TB2lgt4aOGO.eBjSZFjXXcU9FXa_!!2183719539.jpg"); // img3
////        srcUrls.add("http://img.alicdn.com/imgextra/i1/2640015666/TB2bKAQar1J.eBjSspnXXbUeXXa_!!2640015666.jpg"); // img4 产品图片不用上传(会自动上传)，只传固定图片到该店铺的图片空间
//        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB2wymOaq9I.eBjy0FeXXXqwFXa_!!2183719539.jpg"); // img9
//        srcUrls.add("https://img.alicdn.com/imgextra/i3/2183719539/TB2kACQaseJ.eBjy0FiXXXqapXa_!!2183719539.jpg"); // img10
//        srcUrls.add("https://img.alicdn.com/imgextra/i4/2183719539/TB2W_uSar1J.eBjy1zeXXX9kVXa_!!2183719539.jpg"); // img11

        int i = 1;
        for (String srcUrl : srcUrls) {
            try {
                Picture picture = sxProductService.uploadImageByUrl(srcUrl, shopProp);
                if (picture != null) {
                    String destUrl = picture.getPicturePath();
                    String pictureId = String.valueOf(picture.getPictureId());
                    retUrls.put(String.valueOf(i), destUrl);
                }
            } catch (Exception e) {
                System.out.println("uploadImageByUrl error");
            }

            i++;
        }

        retUrls.entrySet().stream()
        .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
        .forEach(p ->  System.out.println(p.getKey() + " : " + p.getValue()));

    }

}