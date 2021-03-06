/*
 * CmsMtImageCreateTaskDetailModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;
import java.util.Date;

/**
 * 
 */
public class CmsMtImageCreateTaskDetailModel extends BaseModel {
    protected Integer cmsMtImageCreateTaskId;

    protected Integer cmsMtImageCreateFileId;

    /**
     * 执行开始时间
     */
    protected Date beginTime;

    /**
     * 执行结束时间
     */
    protected Date endTime;

    /**
     * 1:完成
     */
    protected Integer status;

    public Integer getCmsMtImageCreateTaskId() {
        return cmsMtImageCreateTaskId;
    }

    public void setCmsMtImageCreateTaskId(Integer cmsMtImageCreateTaskId) {
        this.cmsMtImageCreateTaskId = cmsMtImageCreateTaskId;
    }

    public Integer getCmsMtImageCreateFileId() {
        return cmsMtImageCreateFileId;
    }

    public void setCmsMtImageCreateFileId(Integer cmsMtImageCreateFileId) {
        this.cmsMtImageCreateFileId = cmsMtImageCreateFileId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}