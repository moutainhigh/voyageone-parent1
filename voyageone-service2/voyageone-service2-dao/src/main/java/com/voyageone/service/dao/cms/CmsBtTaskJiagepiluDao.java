/*
 * CmsBtTaskJiagepiluDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsBtTaskJiagepiluModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsBtTaskJiagepiluDao {
    List<CmsBtTaskJiagepiluModel> selectList(Object map);

    CmsBtTaskJiagepiluModel selectOne(Object map);

    int selectCount(Object map);

    CmsBtTaskJiagepiluModel select(Integer id);

    int insert(CmsBtTaskJiagepiluModel record);

    int update(CmsBtTaskJiagepiluModel record);

    int delete(Integer id);
}