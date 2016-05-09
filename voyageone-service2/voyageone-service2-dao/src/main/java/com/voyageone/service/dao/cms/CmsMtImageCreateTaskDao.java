/*
 * CmsMtImageCreateTaskDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtImageCreateTaskDao {
    List<CmsMtImageCreateTaskModel> selectList(Map<String, Object> map);

    CmsMtImageCreateTaskModel selectOne(Map<String, Object> map);

    CmsMtImageCreateTaskModel select(Integer id);

    int insert(CmsMtImageCreateTaskModel record);

    int update(CmsMtImageCreateTaskModel record);

    int delete(Integer id);
}