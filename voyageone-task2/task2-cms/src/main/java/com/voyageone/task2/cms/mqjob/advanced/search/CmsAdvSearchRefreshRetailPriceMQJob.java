package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanNameAll;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 高级检索-重新计算指导价Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 16:17
 */
@Service
@VOSubRabbitListener
public class CmsAdvSearchRefreshRetailPriceMQJob extends TBaseMQCmsSubService<AdvSearchRefreshRetailPriceMQMessageBody> {

    @Autowired
    private PlatformPriceService cmsProductPriceUpdateService;

    @Override
    public void onStartup(AdvSearchRefreshRetailPriceMQMessageBody messageBody) throws Exception {

        super.count = messageBody.getCodeList().size();
        List<CmsBtOperationLogModel_Msg> failList = cmsProductPriceUpdateService.updateProductRetailPrice(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getCodeList().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
