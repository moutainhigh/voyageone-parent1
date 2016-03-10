package com.voyageone.task2.cms.job.promotion.beat;

import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.promotion.beat.BeatJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jonasvlag on 16/3/10.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@Component("CmsBeatJob")
public class CmsBeatJob extends BaseTaskJob {

    @Autowired
    private BeatJobService beatJobService;

    @Override
    protected BaseTaskService getTaskService() {
        return beatJobService;
    }
}
