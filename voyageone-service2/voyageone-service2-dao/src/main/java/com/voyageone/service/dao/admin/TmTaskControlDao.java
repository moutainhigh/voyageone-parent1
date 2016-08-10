/*
 * TmTaskControlDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.admin;

import com.voyageone.service.model.admin.TmTaskControlKey;
import com.voyageone.service.model.admin.TmTaskControlModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TmTaskControlDao {
    List<TmTaskControlModel> selectList(Map<String, Object> map);

    TmTaskControlModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    TmTaskControlModel select(TmTaskControlKey key);

    int insert(TmTaskControlModel record);

    int update(TmTaskControlModel record);

    int delete(TmTaskControlKey key);
}