package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by jeff.duan on 2016/03/07.
 * @version 2.0.0
 */
@Repository
public class CmsBtStockSeparateIncrementItemDao extends BaseDao {

    public List<Map<String, Object>> selectStockSeparateIncrement(Map<String, Object> param) {
        return selectList("select_stock_separate_increment", param);
    }

    public Integer selectStockSeparateIncrementSuccessQty(Map<String, Object> param) {
        return selectOne("select_stock_separate_increment_success_qty", param);
    }

    public List<Object> selectStockSeparateIncrementItemByStatus(Map<String, Object> param) {
        return selectOne("select_stock_separate_increment_item_by_status", param);
    }

    public int updateStockSeparateIncrementItem(Map<String, Object> param) {
        return update("update_stock_separate_increment_item", param);
    }

    public int deleteStockSeparateIncrementItem(Map<String, Object> param) {
        return delete("delete_stock_separate_increment_item", param);
    }
}
