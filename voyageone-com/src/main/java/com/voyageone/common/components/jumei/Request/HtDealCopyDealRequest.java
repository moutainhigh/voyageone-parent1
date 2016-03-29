package com.voyageone.common.components.jumei.Request;
import com.voyageone.common.components.jumei.Bean.HtDealCopyDeal_DealInfo;
import com.voyageone.common.components.jumei.Bean.HtDealUpdate_DealInfo;
import com.voyageone.common.util.JacksonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by dell on 2016/3/29.
 */
public class HtDealCopyDealRequest {
    public String Url = "/v1/htDeal/copyDeal";

    public String getUrl() {
        return Url;
    }

    String jumei_hash_id;

    public void setUrl(String url) {
        Url = url;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    int start_time;//	Number 售卖开始时间    参数范围: 注:
    int end_time;//Number 售卖结束时间    参数范围: 注:
    HtDealCopyDeal_DealInfo update_data;

    public String getJumei_hash_id() {
        return jumei_hash_id;
    }

    public void setJumei_hash_id(String jumei_hash_id) {
        this.jumei_hash_id = jumei_hash_id;
    }

    public HtDealCopyDeal_DealInfo getUpdate_data() {
        return update_data;
    }

    public void setUpdate_data(HtDealCopyDeal_DealInfo update_data) {
        this.update_data = update_data;
    }

    public Map<String, Object> getParameter() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("jumei_hash_id", jumei_hash_id);
        params.put("start_time", start_time);
        params.put("end_time", end_time);
        params.put("update_data", JacksonUtil.bean2JsonNotNull(update_data));
        return params;
    }
}
