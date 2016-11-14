/*
 * CmsBtShelvesTemplateModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 
 */
public class CmsBtShelvesTemplateModel extends BaseModel {
    /**
     * 模板名称
     */
    protected String templateName;

    /**
     * 模板类型
     */
    protected Integer templateType;

    /**
     * 模板客户端类型
     */
    protected Integer clientType;

    /**
     * 平台ID
     */
    protected Integer cartId;

    /**
     * 品牌ID
     */
    protected String channelId;

    /**
     * 备注
     */
    protected String remark;

    /**
     * 每行几个
     */
    protected Integer numPerLine;

    /**
     * html头部
     */
    protected String htmlHead;

    /**
     * 标题模块
     */
    protected String htmlModuleTitle;

    /**
     * 搜索模块
     */
    protected String htmlModuleSearch;

    /**
     * 大图模块
     */
    protected String htmlBigImage;

    /**
     * 小图模块
     */
    protected String htmlSmallImage;

    /**
     * HTML尾部
     */
    protected String htmlFoot;

    /**
     * 清除浮动
     */
    protected String htmlClearfix1;

    /**
     * 清除浮动
     */
    protected String htmlClearfix2;

    /**
     * 最后一个小图模块
     */
    protected String htmlLastImage;

    /**
     * 图片模板
     */
    protected String htmlImageTemplate;

    /**
     * 逻辑删除：0未被删除，1被删除
     */
    protected Integer deleted;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName == null ? null : templateName.trim();
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getNumPerLine() {
        return numPerLine;
    }

    public void setNumPerLine(Integer numPerLine) {
        this.numPerLine = numPerLine;
    }

    public String getHtmlHead() {
        return htmlHead;
    }

    public void setHtmlHead(String htmlHead) {
        this.htmlHead = htmlHead == null ? null : htmlHead.trim();
    }

    public String getHtmlModuleTitle() {
        return htmlModuleTitle;
    }

    public void setHtmlModuleTitle(String htmlModuleTitle) {
        this.htmlModuleTitle = htmlModuleTitle == null ? null : htmlModuleTitle.trim();
    }

    public String getHtmlModuleSearch() {
        return htmlModuleSearch;
    }

    public void setHtmlModuleSearch(String htmlModuleSearch) {
        this.htmlModuleSearch = htmlModuleSearch == null ? null : htmlModuleSearch.trim();
    }

    public String getHtmlBigImage() {
        return htmlBigImage;
    }

    public void setHtmlBigImage(String htmlBigImage) {
        this.htmlBigImage = htmlBigImage == null ? null : htmlBigImage.trim();
    }

    public String getHtmlSmallImage() {
        return htmlSmallImage;
    }

    public void setHtmlSmallImage(String htmlSmallImage) {
        this.htmlSmallImage = htmlSmallImage == null ? null : htmlSmallImage.trim();
    }

    public String getHtmlFoot() {
        return htmlFoot;
    }

    public void setHtmlFoot(String htmlFoot) {
        this.htmlFoot = htmlFoot == null ? null : htmlFoot.trim();
    }

    public String getHtmlClearfix1() {
        return htmlClearfix1;
    }

    public void setHtmlClearfix1(String htmlClearfix1) {
        this.htmlClearfix1 = htmlClearfix1 == null ? null : htmlClearfix1.trim();
    }

    public String getHtmlClearfix2() {
        return htmlClearfix2;
    }

    public void setHtmlClearfix2(String htmlClearfix2) {
        this.htmlClearfix2 = htmlClearfix2 == null ? null : htmlClearfix2.trim();
    }

    public String getHtmlLastImage() {
        return htmlLastImage;
    }

    public void setHtmlLastImage(String htmlLastImage) {
        this.htmlLastImage = htmlLastImage == null ? null : htmlLastImage.trim();
    }

    public String getHtmlImageTemplate() {
        return htmlImageTemplate;
    }

    public void setHtmlImageTemplate(String htmlImageTemplate) {
        this.htmlImageTemplate = htmlImageTemplate == null ? null : htmlImageTemplate.trim();
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}