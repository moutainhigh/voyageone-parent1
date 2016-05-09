/*
 * CmsMtChannelCategoryConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtChannelCategoryConfigDao {
    List<CmsMtChannelCategoryConfigModel> selectList(Map<String, Object> map);

    CmsMtChannelCategoryConfigModel selectOne(Map<String, Object> map);

    CmsMtChannelCategoryConfigModel select(Integer id);

    int insert(CmsMtChannelCategoryConfigModel record);

    int update(CmsMtChannelCategoryConfigModel record);

    int delete(Integer id);
}