/*
 * CmsMtBrandsMappingDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.product.CmsMtBrandsMappingBean;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMtBrandsMappingDaoExt {
    List<CmsMtBrandsMappingBean> selectList(Object map);

    CmsMtBrandsMappingBean selectOneJM(Object map);

    CmsMtBrandsMappingBean selectOneTMJD(Object map);

    int selectCount(Object map);

    CmsMtBrandsMappingBean select(Integer id);

    int insert(CmsMtBrandsMappingBean record);

    int update(CmsMtBrandsMappingBean record);

    int delete(Integer id);
}