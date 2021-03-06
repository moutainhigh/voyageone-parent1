/*
 * CmsBtPromotionCodesTagDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtPromotionCodesTagModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsBtPromotionCodesTagDaoExt {

    int deleteByTagIdPromotionCodesId(@Param("promotionCodesId") int promotionCodesId,@Param("tagId") int tagId);

    int batchDeleteByCodes(@Param("codeList") List<String> codeList,@Param("promotionId") int promotionId);
}