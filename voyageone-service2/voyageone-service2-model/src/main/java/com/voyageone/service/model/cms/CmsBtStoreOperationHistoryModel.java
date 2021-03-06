/*
 * CmsBtStoreOperationHistoryModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 用于保存全店操作的log日志
 */
public class CmsBtStoreOperationHistoryModel extends BaseModel {
    /**
     * 具体的操作类型信息
     */
    protected String operationType;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType == null ? null : operationType.trim();
    }
}