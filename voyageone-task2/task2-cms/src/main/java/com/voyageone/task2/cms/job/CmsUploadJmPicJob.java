package com.voyageone.task2.cms.job;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.CmsUploadJmPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Component("CmsUploadJmPicJob")
public class CmsUploadJmPicJob extends BaseTaskJob {
    @Autowired
    private CmsUploadJmPicService cmsUploadJmPicService;

    @Override
    protected BaseTaskService getTaskService() {
        return cmsUploadJmPicService;
    }
}
