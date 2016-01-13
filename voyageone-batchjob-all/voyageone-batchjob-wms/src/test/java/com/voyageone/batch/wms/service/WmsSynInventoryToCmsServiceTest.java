package com.voyageone.batch.wms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2015/12/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-wms-test.xml")
public class WmsSynInventoryToCmsServiceTest {
    @Autowired
    private WmsSynInventoryToCmsService wmsSynInventoryToCmsService;

    @Test
    public void testOnStartup() throws Exception {
        wmsSynInventoryToCmsService.startup();
    }
}