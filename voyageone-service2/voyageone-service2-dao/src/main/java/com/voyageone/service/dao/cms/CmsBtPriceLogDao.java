/*
 * CmsBtPriceLogDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtPriceLogDao {
    List<CmsBtPriceLogModel> selectList(Map<String, Object> map);

    CmsBtPriceLogModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    CmsBtPriceLogModel select(Integer id);

    int insert(CmsBtPriceLogModel record);

    int update(CmsBtPriceLogModel record);

    int delete(Integer id);
}