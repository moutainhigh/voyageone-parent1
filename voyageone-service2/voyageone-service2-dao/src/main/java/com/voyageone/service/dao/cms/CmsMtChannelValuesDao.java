/*
 * CmsMtChannelValuesDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtChannelValuesDao {
    List<CmsMtChannelValuesModel> selectList(Map<String, Object> map);

    CmsMtChannelValuesModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsMtChannelValuesModel select(Integer id);

    int insert(CmsMtChannelValuesModel record);

    int update(CmsMtChannelValuesModel record);

    int delete(Integer id);
}