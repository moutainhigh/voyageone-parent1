/*
 * CmsMtChannelValuesDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtChannelValuesDaoExt{
    int insertIgnore(CmsMtChannelValuesModel record);
}