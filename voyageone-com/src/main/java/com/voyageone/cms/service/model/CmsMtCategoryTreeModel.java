package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmsMtCategoryTreeModel extends BaseMongoModel {
    private String catId = "0";
    private String catName = "";
    private String catPath = "";
    private String parentCatId = "0";
    private int isParent = 0;
    private List<CmsMtCategoryTreeModel> children = new ArrayList<>();

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    public String getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(String parentCatId) {
        this.parentCatId = parentCatId;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public List<CmsMtCategoryTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<CmsMtCategoryTreeModel> children) {
        this.children = children;
    }

}
