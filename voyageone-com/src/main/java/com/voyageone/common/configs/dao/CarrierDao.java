package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CarrierBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jonas on 4/10/2015.
 */
@Repository
public class CarrierDao extends BaseDao {
    /**
     * 根据消息类型获得类型消息Map
     */
    public List<CarrierBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_carrier_channel_getAll");
    }
}
