package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author jerry 15/12/14
 * @version 2.0.0
 */

@Repository
public class CmsBtTagDaoExt extends ServiceBaseDao {

    public int insertCmsBtTag(CmsBtTagModel cmsBtTagModel) {
        return insert("insert_cms_bt_tag", cmsBtTagModel);
    }

    public int updateCmsBtTag(CmsBtTagModel cmsBtTagModel) {
        return update("update_cms_bt_tag", cmsBtTagModel);
    }

    public int deleteCmsBtTagByTagId(CmsBtTagModel cmsBtTagModel) {
        return delete("delete_cms_bt_tag_by_tag_id", cmsBtTagModel);
    }

    public int deleteCmsBtTagByParentTagId(CmsBtTagModel cmsBtTagModel) {
        return delete("delete_cms_bt_tag_by_parent_tag_id", cmsBtTagModel);
    }

    public CmsBtTagModel selectCmsBtTagByTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("tagId", tagId);
        return selectOne("select_one_by_tag_id", paraIn);
    }

    public CmsBtTagModel selectCmsBtTagByParentTagId(int tagId) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("tagId", tagId);
        return selectOne("select_one_by_parent_tag_id", paraIn);
    }

    public List<CmsBtTagModel> selectListByParentTagId(int parentTagId) {
        return selectList("select_list_by_parent_tag_id", parentTagId);
    }

    public List<CmsBtTagModel> selectListByChannelId(String channelId) {
        return selectList("select_list_by_channel_id", channelId);
    }

    public List<CmsBtTagModel> selectListByParentTagId(String channelId, int parentTagId, String tagName) {
        HashMap<String, Object> paraIn = new HashMap<>();
        paraIn.put("channelId", channelId);
        paraIn.put("parentTagId", parentTagId);
        paraIn.put("tagName", tagName);

        return selectList("select_one_by_tag_name", paraIn);
    }
    public List<CmsBtTagBean> selectListByChannelIdAndTagType(String channelId,String tagType) {
        return selectList("select_list_by_channel_id_and_tag_type", parameters("channelId", channelId, "tagType", tagType));
    }
}