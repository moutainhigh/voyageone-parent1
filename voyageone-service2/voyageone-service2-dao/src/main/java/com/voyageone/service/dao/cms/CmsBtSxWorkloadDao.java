package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-12-7.
 */
@Repository
public class CmsBtSxWorkloadDao extends ServiceBaseDao {
    public List<CmsBtSxWorkloadModel> getSxWorkloadModelWithChannel(int recordCount, String channelId) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount, "channel_id", channelId));
    }

    public List<CmsBtSxWorkloadModel> getSxWorkloadModel(int recordCount) {
        return selectList("cms_select_sx_workload", parameters("record_count", recordCount));
    }

    public void updateSxWorkloadModel(CmsBtSxWorkloadModel model) {
        update("cms_update_sx_workload", parameters("seq", model.getSeq(), "publish_status", model.getPublishStatus()));
    }

    public void insertSxWorkloadModel(CmsBtSxWorkloadModel model) {
        insert("cms_insert_sx_workload", model);
    }

    public void insertSxWorkloadModels(List<CmsBtSxWorkloadModel> models) {
        insert("cms_insert_sx_workloads", models);
    }
}