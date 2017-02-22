package com.voyageone.task2.cms.service.product;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchExportMQMessageBody;
import com.voyageone.task2.cms.service.product.batch.CmsAdvSearchExportFileService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by dell on 2016/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchExportFileServiceTest {
    @Autowired
    CmsAdvSearchExportFileService cmsAdvSearchExportFileService;

    @Test
    public void onStartup() throws Exception {

        //String msg = "{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[],\"fileType\":3,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"010\",\"_userName\":\"admin\",\"_language\":\"cn\",\"_taskId\":169,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_commonProps\":[],\"_adv_search_props_searchItems\":\"\",\"_adv_search_selSalesType\":[],\"_adv_search_selBiDataList\":null}}";

        //String msg = "{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[],\"fileType\":4,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0}";
        //String msg = "{\"compareType\":null,\"brand\":null,\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[],\"fileType\":4,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"028\",\"_userName\":\"admin\",\"_language\":\"cn\",\"_taskId\":217,\"_sessionBean\":{\"_adv_search_customProps\":[],\"_adv_search_commonProps\":[],\"_adv_search_props_searchItems\":\"\",\"_adv_search_selSalesType\":[],\"_adv_search_selBiDataList\":null}}";
        String msg = "{\"cmsBtExportTaskId\":255,\"searchValue\":{\"tags\":[],\"priceChgFlg\":\"\",\"priceDiffFlg\":\"\",\"tagTypeSelectValue\":\"0\",\"promotionList\":[],\"catgoryList\":[],\"cidValue\":[],\"promotionTagType\":1,\"freeTagType\":1,\"supplierType\":1,\"brandSelType\":1,\"productSelType\":\"1\",\"sizeSelType\":\"1\",\"salesType\":\"All\",\"custGroupType\":\"1\",\"custAttrMap\":[{\"inputVal\":\"\",\"inputOpts\":\"\",\"inputOptsKey\":\"\"}],\"_selCodeList\":[\"SJ9020SZW\"],\"fileType\":3,\"productStatus\":[],\"platformStatus\":[],\"pRealStatus\":[],\"shopCatStatus\":0,\"pCatStatus\":0,\"_channleId\":\"010\",\"_userName\":\"gump.gan\",\"_language\":\"cn\",\"_taskId\":255,\"_sessionBean\":{}},\"channelIdMap\":{\"030\":\"Wella\",\"031\":\"WoodLand\",\"010\":\"Jewelry\",\"032\":\"Frye\",\"033\":\"KitBag\",\"012\":\"BCBG\",\"034\":\"Coty\",\"013\":\"Sears\",\"035\":\"LikingBuyer\",\"014\":\"WMF\",\"015\":\"GILT\",\"016\":\"ShoeCity\",\"017\":\"Lucky Vitamin\",\"018\":\"Target\",\"019\":\"SummerGuru\",\"020\":\"EdcSkincare\",\"021\":\"BHFOX\",\"022\":\"DFO\",\"001\":\"Sneakerhead\",\"023\":\"ShoeZoo\",\"002\":\"PortAmerican\",\"024\":\"Overstock\",\"003\":\"Essuntial\",\"025\":\"FragranceNet\",\"004\":\"JuicyCouture\",\"026\":\"Lighthouse\",\"005\":\"Spalding\",\"027\":\"Yogademocracy\",\"006\":\"BHFO\",\"028\":\"ShoeMetro\",\"007\":\"Champion\",\"029\":\"Modotex\",\"008\":\"RealMadrid\",\"009\":\"SwissWatch\",\"928\":\"Liking\"},\"consumerRetryTimes\":0,\"mqId\":0,\"delaySecond\":0,\"sender\":\"gump.gan\"}";
        AdvSearchExportMQMessageBody advSearchExportMQMessageBody = JacksonUtil.json2Bean(msg, AdvSearchExportMQMessageBody.class);

        cmsAdvSearchExportFileService.export(advSearchExportMQMessageBody);

        /*Map<SkuInventoryForCmsBean, Integer> map = new HashMap<>();
        SkuInventoryForCmsBean sku1 = new SkuInventoryForCmsBean("abc", "a", "1");
        map.put(sku1, 10);
        SkuInventoryForCmsBean sku2 = new SkuInventoryForCmsBean("abc", "a", "1");
        SkuInventoryForCmsBean sku3 = new SkuInventoryForCmsBean("abc", "a", "1");
        System.out.println(map.get(sku2));*/
    }

}