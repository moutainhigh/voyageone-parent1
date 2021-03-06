/*
 * CmsMtChannelConditionConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelConditionConfigModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtChannelConditionConfigDao {
    List<CmsMtChannelConditionConfigModel> selectList(Object map);

    CmsMtChannelConditionConfigModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtChannelConditionConfigModel select(Integer id);

    int insert(CmsMtChannelConditionConfigModel record);

    int update(CmsMtChannelConditionConfigModel record);

    int delete(Integer id);
}