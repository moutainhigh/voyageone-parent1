/*
 * CmsMtPlatformConfigDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtPlatformConfigModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtPlatformConfigDao {
    List<CmsMtPlatformConfigModel> selectList(Object map);

    CmsMtPlatformConfigModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtPlatformConfigModel select(Integer id);

    int insert(CmsMtPlatformConfigModel record);

    int update(CmsMtPlatformConfigModel record);

    int delete(Integer id);
}