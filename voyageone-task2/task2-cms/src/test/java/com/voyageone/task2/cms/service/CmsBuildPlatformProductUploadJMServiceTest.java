package com.voyageone.task2.cms.service;

import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2016/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBuildPlatformProductUploadJMServiceTest {


    @Autowired
    CmsBuildPlatformProductUploadJMService cmsBuildPlatformProductUploadJMService;


    @Autowired
    CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;


    @Test
    public void testUpdateProduct() throws Exception {

        List<CmsBtSxWorkloadModel> workloadList = cmsBtSxWorkloadDaoExt.selectSxWorkloadModelWithChannelIdCartId(10, "018", 27);

        for (CmsBtSxWorkloadModel work : workloadList) {
            cmsBuildPlatformProductUploadJMService.updateProduct(work);
        }

    }
}