/*
 * VmsBtFinancialReportDao.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.dao.vms;

import com.voyageone.service.model.vms.VmsBtFinancialReportModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface VmsBtFinancialReportDao {
    List<VmsBtFinancialReportModel> selectList(Map<String, Object> map);

    VmsBtFinancialReportModel selectOne(Map<String, Object> map);

    VmsBtFinancialReportModel select(Integer id);

    int insert(VmsBtFinancialReportModel record);

    int update(VmsBtFinancialReportModel record);

    int delete(Integer id);
}