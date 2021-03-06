package com.voyageone.service.dao.cms;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/27.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtPromotionSkusDaoTest {

    @Autowired
    private CmsBtPromotionSkusDao cmsBtPromotionSkusDao;

    @Test
    public void testSelect() throws Exception {
        System.out.println(JacksonUtil.bean2Json(cmsBtPromotionSkusDao.select(447)));
    }
}