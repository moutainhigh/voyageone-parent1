/*
 * CtUserDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.CtUserModel;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CtUserDao {
    List<CtUserModel> selectList(Object map);

    CtUserModel selectOne(Object map);

    int selectCount(Object map);

    CtUserModel select(Integer id);

    int insert(CtUserModel record);

    int update(CtUserModel record);

    int delete(Integer id);
}