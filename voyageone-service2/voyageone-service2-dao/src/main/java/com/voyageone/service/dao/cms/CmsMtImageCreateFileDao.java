/*
 * CmsMtImageCreateFileDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateFileModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtImageCreateFileDao {
    List<CmsMtImageCreateFileModel> selectList(Object map);

    CmsMtImageCreateFileModel selectOne(Object map);

    int selectCount(Object map);

    CmsMtImageCreateFileModel select(Integer id);

    int insert(CmsMtImageCreateFileModel record);

    int update(CmsMtImageCreateFileModel record);

    int delete(Integer id);
}