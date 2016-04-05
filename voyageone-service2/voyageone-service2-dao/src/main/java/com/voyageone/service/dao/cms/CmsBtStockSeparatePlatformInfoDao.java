package com.voyageone.service.dao.cms;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparatePlatformInfoDao extends BaseDao {
    public List<Map<String, Object>> selectStockSeparatePlatform(Map<String, Object> param) {
        return selectList("cms_bt_stock_separate_platform_info_selectStockSeparatePlatform", param);
    }

    public int updateStockSeparatePlatform(Map<String, Object> param) {
        return update("cms_bt_stock_separate_platform_info_updateStockSeparatePlatform", param);
    }

    public int deleteStockSeparatePlatform(Map<String, Object> param) {
        return delete("cms_bt_stock_separate_platform_info_deleteStockSeparatePlatform", param);
    }
    public String selectStockSeparatePlatFormInfoById(String cart_id,String revert_time){
        return selectOne("cms_bt_stock_separate_platform_selectStockSeparatePlatFormInfoById", parameters(
                "cart_id", cart_id,
                "revert_time", revert_time
        ));
    }
    public int insert(Map stockSeparatePlatFormInfoMap) {
        return insert("cms_bt_stock_separate_platform_info_insert", stockSeparatePlatFormInfoMap);
    }
    public List<Map<String,Object>> stockSeparatePlatFormInfoMapByTaskID(String task_id){
        return selectList("cms_bt_stock_separate_platform_info_stockSeparatePlatFormInfoMapByTaskID",task_id);
    }
}
