/*
 * CmsBtJmPromotionSpecialExtensionDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsBtJmPromotionSpecialExtensionDaoExt {


    /**
     * 查询聚美活动扩展信息
     *
     * @param jmPromotionId 聚美活动ID
     * @return
     */
    CmsBtJmPromotionSpecialExtensionModel selectOne(Integer jmPromotionId);

    int insert(CmsBtJmPromotionSpecialExtensionModel record);

    int update(CmsBtJmPromotionSpecialExtensionModel record);

    int delete(Integer id);
}