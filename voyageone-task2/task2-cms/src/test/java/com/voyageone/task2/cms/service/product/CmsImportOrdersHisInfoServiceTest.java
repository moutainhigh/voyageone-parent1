package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.cms.service.product.sales.CmsImportOrdersHisInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jason.jiang on 2016/05/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsImportOrdersHisInfoServiceTest {

    @Autowired
    CmsImportOrdersHisInfoService cmsImportOrdersHisInfoService;

    @Test
    public void testOnStartup() {
        cmsImportOrdersHisInfoService.startup();
    }
}