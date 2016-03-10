package com.voyageone.web2.cms.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import com.voyageone.web2.cms.model.CustomWord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * for voyageone_ims.ims_mt_custom_word and voyageone_ims.ims_mt_custom_word_param
 *
 * Created by Jonas on 9/11/15.
 */
@Repository
public class CustomWordDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public List<CustomWord> selectWithParam() {
        return selectList("cms_mt_custom_word_selectWithParam");
    }

    public List<Map<String, Object>> selectCustAttrs(String chnId, String language) {
        return selectList("cms2_mt_channel_config_getCustAttr", parameters("channelId", chnId, "langId", language));
    }
}
