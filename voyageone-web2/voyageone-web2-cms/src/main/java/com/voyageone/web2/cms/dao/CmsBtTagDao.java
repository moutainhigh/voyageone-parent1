package com.voyageone.web2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.cms.model.CmsBtTagModel;
import org.springframework.stereotype.Repository;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */

@Repository
public class CmsBtTagDao extends BaseDao{

    public int insertCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return updateTemplate.insert("insert_cms_bt_tag",cmsBtTagModel);
    }

    public int updateCmsBtTag(CmsBtTagModel cmsBtTagModel){
        return updateTemplate.update("update_cms_bt_tag",cmsBtTagModel);
    }
}
