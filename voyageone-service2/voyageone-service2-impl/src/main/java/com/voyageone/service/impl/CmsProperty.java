package com.voyageone.service.impl;

/**
 * Created by dell on 2016/4/18.
 */
public interface CmsProperty {
    interface Props {
        //  Code 文件模板文件
        String SEARCH_ADVANCE_EXPORT_TEMPLATE = "cms.search.advance.export.file";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_PRODUCT = "cms.search.advance.export.file.product";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_GROUP = "cms.search.advance.export.file.group";
        String SEARCH_ADVANCE_EXPORT_TEMPLATE_SKU = "cms.search.advance.export.file.sku";
        String PROMOTION_EXPORT_TEMPLATE = "cms.promotion.export.file";
        String CMS_JM_EXPORT_PATH="cms.jm.export.path";
        String CMS_JM_IMPORT_PATH ="cms.jm.import.path";
        String STOCK_EXPORT_TEMPLATE = "cms.stock.export.file";
        String CMS_Image_Ceate_Import_Path ="cms.image.create.import.path";
        String CMS_PROMOTION_EXPORT_JUHUASUAN = "cms.promotion.export.juhuasuan";
        String CMS_PROMOTION_EXPORT_TMALL = "cms.promotion.export.tmall";
    }
}
