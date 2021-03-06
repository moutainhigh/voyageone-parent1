package com.voyageone.task2.cms.service.product;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.service.impl.cms.product.PlatformForcedInStockProduct_AutoOnSaleService;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PlatformForcedInStockProduct_AutoOnSaleJob Job Service
 *被迫下架的产品，自动上架 job
 * @author peitao 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */

@Service
public class PlatformForcedInStockProduct_AutoOnSaleJobService extends BaseCronTaskService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PlatformForcedInStockProduct_AutoOnSaleService service;

    @Override
    protected String getTaskName() {
        return "PlatformForcedInStockProduct_AutoOnSaleJobService";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("PlatformForcedInStockProduct_AutoOnSaleJobService", "begin");
        // 获取允许运行的渠道
        Set<String> colList = mongoTemplate.getCollectionNames();
        List<String> orderChannelIdList = colList.stream().filter(s -> s.indexOf("cms_bt_product_c") != -1 && s.length() == 19).map(s1 -> s1.substring(16)).collect(Collectors.toList());
        $info("orderChannelIdList=" + orderChannelIdList.size());
        // 根据订单渠道运行
        for (String channelId : orderChannelIdList) {
            $info("onSaleByChannelId:%s begin", channelId);
            service.onSaleByChannelId(channelId);
            $info("sumByChannelId:%s end", channelId);

        }

        $info("PlatformForcedInStockProduct_AutoOnSaleJobService", "end");
    }
}
