package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james 15/12/11
 * @version 2.0.0
 */
@Repository
public class CmsPromotionDao extends BaseDao{

    public List<CmsBtPromotionModel> getPromotionList(Map<String,Object> params){
        List<CmsBtPromotionModel> ret = selectList("select_promotion_list",params);
        if (ret == null){
            ret = new ArrayList<>();
        }
        return ret;
    }
}
