package com.voyageone.service.impl.com.mq.config;

/**
 * @author aooer 2016/4/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MqRoutingKey {

    public static final String CMS_BATCH_PROMOTION = "voyageone_cms_batchjob_promotion_queue";

    public static final String CMS_BATCH_JmBtPromotionImportTask = "voyageone_cms_batchjob_JmBtPromotionImportTask_queue";

    public static final String CMS_BATCH_JmBtPromotionExportTask = "voyageone_cms_batchjob_JmBtPromotionExportTask_queue";

    public static final String CMS_BATCH_JuMeiProductUpdate = "voyageone_cms_batchjob_JuMeiProductUpdate_queue";

    public static final String CMS_BATCH_JuMeiProductUpdateDealEndTimeJob = "voyageone_cms_batchjob_JuMeiProductUpdateDealEndTimeJobService_queue";

    public static final String CMS_BATCH_PlatformCategoryTreeJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategoryTreeJdJob_queue";

    public static final String CMS_BATCH_PlatformCategorySchemaJdJob = "voyageone_cms_batchjob_CmsBuildPlatformCategorySchemaJdJob_queue";

}
