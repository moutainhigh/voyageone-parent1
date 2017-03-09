package com.voyageone.service.impl.cms.vomq.vomessage.body;

import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.annotation.VOMQQueue;
import com.voyageone.components.rabbitmq.bean.BaseMQMessageBody;
import com.voyageone.components.rabbitmq.exception.MQMessageRuleException;
import com.voyageone.service.impl.cms.feed.FeedToCms2Service;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.List;

/**
 * Created by james on 2017/3/9.
 */
@VOMQQueue(value = CmsMqRoutingKey.CMS_FEED_IMPORT_MQ_JOB)
public class CmsFeedImportMQMessageBody extends BaseMQMessageBody {

    List<CmsBtFeedInfoModel> cmsBtFeedInfoModels;

    public List<CmsBtFeedInfoModel> getCmsBtFeedInfoModels() {
        return cmsBtFeedInfoModels;
    }

    public void setCmsBtFeedInfoModels(List<CmsBtFeedInfoModel> cmsBtFeedInfoModels) {
        this.cmsBtFeedInfoModels = cmsBtFeedInfoModels;
    }

    @Override
    public void check() throws MQMessageRuleException {
        if(ListUtils.isNull(cmsBtFeedInfoModels)){
            throw new MQMessageRuleException("没有数据");
        }
    }
}
