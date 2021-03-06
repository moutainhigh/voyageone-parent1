package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.util.MapModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/7/12.
 */
@Repository
public interface CmsBtPromotionDaoExtCamel {

    List selectPage(Map<String, Object> param);

    long selectCount(Map<String, Object> param);

    List<MapModel> selectAddPromotionList(@Param("channelId") String channelId, @Param("cartId") int cartId, @Param("codeList") List<String> codeList, @Param("activityStart") Date activityStart, @Param("activityEnd") Date activityEnd);

}
