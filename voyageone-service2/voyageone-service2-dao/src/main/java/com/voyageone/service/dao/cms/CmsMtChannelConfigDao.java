/*
 * CmsMtChannelConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtChannelConfigDao {
    List<CmsMtChannelConfigModel> selectList(Object map);

    CmsMtChannelConfigModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtChannelConfigModel select(Integer id);

    int insert(CmsMtChannelConfigModel record);

    int update(CmsMtChannelConfigModel record);

    int delete(Integer id);
}