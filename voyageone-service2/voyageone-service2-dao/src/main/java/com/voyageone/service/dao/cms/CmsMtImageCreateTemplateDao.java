/*
 * CmsMtImageCreateTemplateDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtImageCreateTemplateDao {
    List<CmsMtImageCreateTemplateModel> selectList(Map<String, Object> map);

    CmsMtImageCreateTemplateModel selectOne(Map<String, Object> map);

    CmsMtImageCreateTemplateModel select(Integer id);

    int insert(CmsMtImageCreateTemplateModel record);

    int update(CmsMtImageCreateTemplateModel record);

    int delete(Integer id);
}