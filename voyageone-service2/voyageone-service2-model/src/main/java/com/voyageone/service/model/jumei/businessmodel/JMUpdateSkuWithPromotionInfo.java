package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.jumei.CmsBtJmSkuModel;

/**
 * Created by dell on 2016/4/12.
 */
public class JMUpdateSkuWithPromotionInfo {
    CmsBtJmSkuModel cmsBtJmSkuModel;
    CmsBtJmPromotionSkuModel cmsBtJmPromotionSkuModel;

    public CmsBtJmSkuModel getCmsBtJmSkuModel() {
        return cmsBtJmSkuModel;
    }

    public void setCmsBtJmSkuModel(CmsBtJmSkuModel cmsBtJmSkuModel) {
        this.cmsBtJmSkuModel = cmsBtJmSkuModel;
    }

    public CmsBtJmPromotionSkuModel getCmsBtJmPromotionSkuModel() {
        return cmsBtJmPromotionSkuModel;
    }

    public void setCmsBtJmPromotionSkuModel(CmsBtJmPromotionSkuModel cmsBtJmPromotionSkuModel) {
        this.cmsBtJmPromotionSkuModel = cmsBtJmPromotionSkuModel;
    }
}
