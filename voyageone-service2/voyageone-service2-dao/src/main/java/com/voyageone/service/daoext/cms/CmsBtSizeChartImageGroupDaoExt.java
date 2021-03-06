/*
 * CmsBtSizeChartImageGroupDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtSizeChartImageGroupModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtSizeChartImageGroupDaoExt {
    int deleteByCmsBtSizeChartId(@Param("channelId") String channelId,@Param("cmsBtSizeChartId") int cmsBtSizeChartId);
    int deleteByCmsBtImageGroupId(@Param("channelId") String channelId,@Param("cmsBtImageGroupId") long cmsBtImageGroupId);
}