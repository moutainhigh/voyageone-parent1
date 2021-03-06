package com.voyageone.service.impl.cms.feed;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.service.dao.cms.mongo.CmsMtFeedCategoryTreeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by DELL on 2016/4/5.
 */

@Service
public class FeedCategoryTreeService extends BaseService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

//    // 取得Top类目路径数据
//    public CmsMtFeedCategoryTreeModelx getFeedCategory(String channelId) {
//        return cmsMtFeedCategoryTreeDao.selectFeedCategoryx(channelId);
//    }

    public List<CmsMtFeedCategoryTreeModel> getOnlyTopFeedCategories(String channelId) {
        return cmsMtFeedCategoryTreeDao.selectTopCategories(channelId);
    }

    /**
     * 获取channel下所有的类目树
     *
     * @param channelId channelId
     * @return channel下所有的类目树
     */
    public List<CmsMtFeedCategoryTreeModel> getFeedAllCategoryTree(String channelId) {
        return cmsMtFeedCategoryTreeDao.selectFeedAllCategory(channelId);
    }

    /**
     * 取出该channel下所有的类目 以list方式取得（拍平）
     *
     * @param channelId channelId
     * @return channel下所有的类目list
     */
    public List<CmsMtFeedCategoryTreeModel> getFeedAllCategoryList(String channelId) {
        List<CmsMtFeedCategoryTreeModel> result = new ArrayList<>();
        List<CmsMtFeedCategoryTreeModel> topCategorys = getFeedAllCategoryTree(channelId);
        topCategorys.forEach(item -> {
            result.addAll(treeToList(item));
        });
        return result;
    }

    /**
     * 对一个channelid下的类目追加一个Category
     *
     * @param channelId    渠道
     * @param categoryPath 类目
     */
    public void addCategory(String channelId, String categoryPath, String modifier) {
        List<String> categorys = Arrays.asList(categoryPath.split("-"));
        // 取得一级类目树
        CmsMtFeedCategoryTreeModel categoryTree = getFeedCategoryByCategory(channelId, categorys.get(0));

        if (categoryTree == null) {
            categoryTree = new CmsMtFeedCategoryTreeModel();
            categoryTree.setCreated(DateTimeUtil.getNow());
            categoryTree.setCreater(modifier);
            categoryTree.setChannelId(channelId);
            categoryTree.setCatPath(categorys.get(0));
            categoryTree.setCatName(categorys.get(0));
            categoryTree.setCatId(MD5.getMD5(categorys.get(0)));
            categoryTree.setParentCatId("0");
            categoryTree.setIsParent(categorys.size() > 1 ? 1 : 0);
            categoryTree.setChildren(new ArrayList<>());
            if (categorys.size() == 1) {
                categoryTree.setModified(DateTimeUtil.getNow());
                categoryTree.setModifier(modifier);
                cmsMtFeedCategoryTreeDao.update(categoryTree);
                return;
            }
        }

//        if (addCategory(categoryTree, category) != null) {
//            return;
//        }
        if (addSubCategory(categoryTree, categoryPath)) {
            categoryTree.setModified(DateTimeUtil.getNow());
            categoryTree.setModifier(modifier);
            cmsMtFeedCategoryTreeDao.update(categoryTree);
        }
    }

    /**
     * 追加子类目
     */
    private boolean addSubCategory(CmsMtFeedCategoryTreeModel tree, String category) {
        boolean chgFlg = false;
        String[] c = category.split("-");
        String temp = c[0] + "-";
        CmsMtFeedCategoryTreeModel befNode = tree;
        for (int i = 1; i < c.length; i++) {
            temp += c[i];
            CmsMtFeedCategoryTreeModel node = findCategory(tree, temp);
            if (node == null) {
                node = new CmsMtFeedCategoryTreeModel();
                node.setModifier(null);
                node.setModified(null);
                node.setCreater(null);
                node.setCreated(null);
                node.setCatPath(temp);
                node.setCatName(c[i]);
                node.setCatId(MD5.getMD5(temp));
                node.setParentCatId(befNode == null ? "0" : befNode.getCatId());
                node.setIsParent(i < c.length - 1 ? 1 : 0);
                node.setChildren(new ArrayList<>());
                if (befNode != null) {
                    befNode.getChildren().add(node);
                }
                befNode = node;
                chgFlg = true;
            } else {
                befNode = node;
            }
            temp += "-";
        }
        return chgFlg;
    }

    /**
     * 根据一级类目获取一级类目下所有的类目树
     *
     * @param channelId 渠道ID
     * @return CmsMtFeedCategoryTreeModel
     */
    public CmsMtFeedCategoryTreeModel getFeedCategoryByCategory(String channelId, String topCategory) {
        return cmsMtFeedCategoryTreeDao.selectFeedCategoryByCategory(channelId, topCategory);
    }

    /**
     * 根据一级类目获取一级类目下所有的类目树
     *
     * @param channelId 渠道ID
     * @return CmsMtFeedCategoryTreeModel
     */
    public CmsMtFeedCategoryTreeModel getFeedCategoryByCategoryId(String channelId, String topCategoryId) {
        return cmsMtFeedCategoryTreeDao.selectFeedCategoryByCategoryId(channelId, topCategoryId);
    }

    /**
     * 根据category从tree中找到节点
     */
    public CmsMtFeedCategoryTreeModel findCategory(CmsMtFeedCategoryTreeModel tree, String catPath) {

        for (CmsMtFeedCategoryTreeModel cmsMtFeedCategoryTreeModel : tree.getChildren()) {
            if (cmsMtFeedCategoryTreeModel.getCatPath().equalsIgnoreCase(catPath)) {
                return cmsMtFeedCategoryTreeModel;
            }
            if (!cmsMtFeedCategoryTreeModel.getChildren().isEmpty()) {
                CmsMtFeedCategoryTreeModel category = findCategory(cmsMtFeedCategoryTreeModel, catPath);
                if (category != null) return category;
            }
        }
        return null;
    }

    /**
     * 一棵树转成list（拍平）
     *
     * @param cmsMtFeedCategoryTree 一棵树
     * @return 类目list
     */
    public List<CmsMtFeedCategoryTreeModel> treeToList(CmsMtFeedCategoryTreeModel cmsMtFeedCategoryTree) {
        List<CmsMtFeedCategoryTreeModel> result = new ArrayList<>();
        result.add(cmsMtFeedCategoryTree);
        cmsMtFeedCategoryTree.getChildren().forEach(item -> {
            result.addAll(treeToList(item));
        });
        cmsMtFeedCategoryTree.setChildren(null);
        return result;
    }

    /**
     * 根据类目路径返回改路径的类目节点
     *
     * @param channelId channelId
     * @param catPath   类目路径
     */
    public CmsMtFeedCategoryTreeModel getCategoryNote(String channelId, String catPath) {
        List<String> categorys = Arrays.asList(catPath.split("-"));
        if (categorys.isEmpty()) return null;
        CmsMtFeedCategoryTreeModel tree = getFeedCategoryByCategory(channelId, categorys.get(0));
        if (categorys.size() == 1) return tree;
        return findCategory(tree, catPath);
    }

}
