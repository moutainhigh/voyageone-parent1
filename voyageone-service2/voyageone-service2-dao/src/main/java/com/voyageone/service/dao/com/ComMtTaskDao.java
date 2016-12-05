/*
 * ComMtTaskDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.com;

import com.voyageone.service.model.com.ComMtTaskModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ComMtTaskDao {
    List<ComMtTaskModel> selectList(Map<String, Object> map);

    ComMtTaskModel selectOne(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    ComMtTaskModel select(Integer taskId);

    int insert(ComMtTaskModel record);

    int update(ComMtTaskModel record);

    int delete(Integer taskId);
}