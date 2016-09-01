/*
 * CmsBtFeedImportSizeDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtFeedImportSizeDao {
    List<CmsBtFeedImportSizeModel> selectList(Object map);

    CmsBtFeedImportSizeModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtFeedImportSizeModel select(Integer id);

    int insert(CmsBtFeedImportSizeModel record);

    int update(CmsBtFeedImportSizeModel record);

    int delete(Integer id);
}