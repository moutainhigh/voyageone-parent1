/*
 * CmsZzFeedGiltInventoryDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsZzFeedGiltInventoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsZzFeedGiltInventoryDaoExt {
    List<CmsZzFeedGiltInventoryModel> selectList(Object map);

    CmsZzFeedGiltInventoryModel selectOne(Object map);

    int selectCount(Object map);

    CmsZzFeedGiltInventoryModel select(String sku);

    int insert(CmsZzFeedGiltInventoryModel record);

    int update(CmsZzFeedGiltInventoryModel record);

    int delete(String sku);

    int insertList(List<Map<String,Object>> data);
}