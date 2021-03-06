package com.voyageone.service.impl.cms.feed;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author james.li on 2016/4/18.
 * @version 2.0.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class FeedCategoryTreeServiceTest {

    @Autowired
    FeedCategoryTreeService feedCategoryTreeService;
    @Test
    public void testGetFeedAllCategory() throws Exception {
        List<CmsMtFeedCategoryTreeModel> tree =  feedCategoryTreeService.getFeedAllCategoryTree("010");
        System.out.println(JacksonUtil.bean2Json(tree));
    }

    @Test
    public void testGetFeedCategoryByCategoryList() {
        List<CmsMtFeedCategoryTreeModel> list = feedCategoryTreeService.getFeedAllCategoryList("010");
        System.out.println(JacksonUtil.bean2JsonNotNull(list));
    }

    @Test
    public void testTreeToList() throws Exception {

    }
}