package com.voyageone.service.impl.cms;

import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.service.dao.cms.CmsBtTasksDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTasksModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tag Service
 *
 * @author chuanyu.liang 15/12/30
 * @version 2.0.0
 */
@Service
public class TaskService extends BaseService {

    @Autowired
    private CmsBtTasksDao cmsBtTaskDao;

    public CmsBtTasksModel getTaskWithPromotion(int task_id) {
        return cmsBtTaskDao.selectByIdWithPromotion(task_id);
    }

    public List<CmsBtTasksModel> getTasks(int promotionId, String taskName, String channelId, int taskType) {
        return cmsBtTaskDao.selectByName(promotionId, taskName, channelId, taskType);
    }

    public int addTask(CmsBtTasksModel cmsBtTaskModel) {
        return cmsBtTaskDao.insert(cmsBtTaskModel);
    }
}
