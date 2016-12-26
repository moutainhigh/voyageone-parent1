package com.voyageone.task2.cms.service.jumei;
import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import com.voyageone.service.impl.cms.vomessage.CmsMqRoutingKey;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * JuMei Product UpdateJob Service
 *
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_JuMeiProductUpdate)
public class JuMeiProductUpdateJobService extends BaseMQCmsService {
//    @Autowired
//    private JuMeiProductPlatformService service;

    @Autowired
    private JuMeiProductPlatform3Service service;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        int id = (int) Double.parseDouble(messageMap.get("id").toString());
        service.updateJmByPromotionId(id);
    }
}
