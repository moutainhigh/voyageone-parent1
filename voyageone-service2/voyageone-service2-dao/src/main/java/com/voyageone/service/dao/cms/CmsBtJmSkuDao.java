/*
 * CmsBtJmSkuDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtJmSkuDao {
    List<CmsBtJmSkuModel> selectList(Object map);

    CmsBtJmSkuModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtJmSkuModel select(Integer id);

    int insert(CmsBtJmSkuModel record);

    int update(CmsBtJmSkuModel record);

    int delete(Integer id);
}