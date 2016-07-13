/*
 * CmsMtFeeExchangeDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeeExchangeModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMtFeeExchangeDao {
    List<CmsMtFeeExchangeModel> selectList(Map<String, Object> map);

    CmsMtFeeExchangeModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsMtFeeExchangeModel select(Integer id);

    int insert(CmsMtFeeExchangeModel record);

    int update(CmsMtFeeExchangeModel record);

    int delete(Integer id);
}