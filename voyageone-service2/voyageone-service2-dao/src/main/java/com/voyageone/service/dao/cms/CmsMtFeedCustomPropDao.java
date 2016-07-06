/*
 * CmsMtFeedCustomPropDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedCustomPropModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtFeedCustomPropDao {
    List<CmsMtFeedCustomPropModel> selectList(Map<String, Object> map);

    CmsMtFeedCustomPropModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsMtFeedCustomPropModel select(Integer id);

    int insert(CmsMtFeedCustomPropModel record);

    int update(CmsMtFeedCustomPropModel record);

    int delete(Integer id);
}