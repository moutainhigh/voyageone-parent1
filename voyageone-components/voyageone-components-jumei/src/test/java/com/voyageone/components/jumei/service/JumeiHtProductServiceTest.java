package com.voyageone.components.jumei.service;


import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.bean.HtProductUpdate_ProductInfo;
import com.voyageone.components.jumei.JumeiHtProductService;
import com.voyageone.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.components.jumei.Request.HtProductUpdateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class JumeiHtProductServiceTest {
    @Autowired
    JumeiHtProductService service;
    @Test
   public void copyDealTest() throws Exception {
       ShopBean shopBean = new ShopBean();
       shopBean.setAppKey("72");
       shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
       shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
       shopBean.setApp_url("http://182.138.102.82:8868/");
       HtProductUpdateRequest request = new HtProductUpdateRequest();
       request.setJumei_product_id("222550619");
       request.setJumei_product_name("aa");
       HtProductUpdate_ProductInfo productInfo=new HtProductUpdate_ProductInfo();
       request.setUpdate_data(productInfo);

      // HtProductUpdateResponse response = service.copyDeal(shopBean, request);
   }
}
