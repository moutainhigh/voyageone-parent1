/*
 * ComLoginLogDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.user;

import java.util.List;

import com.voyageone.service.model.user.ComLoginLogModel;
import org.springframework.stereotype.Repository;

@Repository
public interface ComLoginLogDao {
    List<ComLoginLogModel> selectList(Object map);

    ComLoginLogModel selectOne(Object map);

    int selectCount(Object map);

    ComLoginLogModel select(Integer id);

    int insert(ComLoginLogModel record);

    int update(ComLoginLogModel record);

    int delete(Integer id);
}