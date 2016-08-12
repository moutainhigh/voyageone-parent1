/*
 * ComRoleConfigModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.security.model;

/**
 * 
 */
public class ComRoleConfigModel extends CoreBaseModel {
    protected Integer roleId;

    protected String cfgName;

    protected String cfgVal1;

    protected String cfgVal2;

    protected String comment;

    protected Boolean active;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName == null ? null : cfgName.trim();
    }

    public String getCfgVal1() {
        return cfgVal1;
    }

    public void setCfgVal1(String cfgVal1) {
        this.cfgVal1 = cfgVal1 == null ? null : cfgVal1.trim();
    }

    public String getCfgVal2() {
        return cfgVal2;
    }

    public void setCfgVal2(String cfgVal2) {
        this.cfgVal2 = cfgVal2 == null ? null : cfgVal2.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}