package com.voyageone.components.cn;

import com.google.gson.Gson;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.components.ComponentBase;

import java.util.Map;

/**
 * 对独立域名提供接口调用基础
 *
 * Created by Jonas on 7/8/15.
 */
public abstract class CnBase extends ComponentBase {


    protected static final String trustStore_jc = "/opt/app-shared/voyageone_web/contents/other/third_party/004/cn_key/juicycouture_store";
    protected static final String trustStore_jc_password = "voyage1#";

    //    protected static final String trustStore_liking = "/vdb/app-shared/voyageone_web/contents/other/third_party/Liking/cn_key/liking_store";
    protected static final String trustStore_liking = "/opt/app-shared/voyageone_web/contents/other/third_party/Liking/cn_key/liking_store";
    protected static final String trustStore_liking_password = "voyage1#";

    protected String post(String apiAction, Map<String, Object> jsonMap, ShopBean shopBean) throws Exception {
        return post(apiAction, new Gson().toJson(jsonMap), shopBean);
    }

    protected String post(String apiAction, Object parameter, ShopBean shopBean) throws Exception {
        return post(apiAction, parameter, 3, 1000, shopBean);
    }

    private String post(String apiAction, Object parameter, int tryCount, int tryWait, ShopBean shopBean) throws Exception {

//        String json = new Gson().toJson(parameter);

        for (int i = 0; i < tryCount; i++) {
            try {
                // JC官网需要证书认证
                if (shopBean.getOrder_channel_id().equals(ChannelConfigEnums.Channel.JC.getId())) {
//                    return HttpUtils.post(shopBean.getApp_url() + apiAction, json, trustStore_jc, trustStore_jc_password, trustStore_jc_password);
                    return HttpUtils.post(shopBean.getApp_url() + apiAction, parameter.toString(), trustStore_jc, trustStore_jc_password, trustStore_jc_password);
                } else if (shopBean.getOrder_channel_id().equals(ChannelConfigEnums.Channel.USJGJ.getId())) {
                    return HttpUtils.post(shopBean.getApp_url() + apiAction, parameter.toString(), trustStore_liking, trustStore_liking_password, trustStore_liking_password);
                } else {
//                    return HttpUtils.post(shopBean.getApp_url() + apiAction, json);
                    return HttpUtils.post(shopBean.getApp_url() + apiAction, parameter.toString());
                }

            } catch (RuntimeException e) {
                if (tryCount - i == 1) throw e;
                try {
                    Thread.sleep(tryWait);
                } catch (Exception ignore) {
                }
            }
        }

        return null;
    }
}
