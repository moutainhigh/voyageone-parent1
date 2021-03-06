package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.daoext.cms.CmsMtPlatformDictDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典（表达式）的增删改查管理
 *
 * Created by Jonas on 9/11/15.
 */
@Service
public class DictService extends BaseService {

    @Autowired
    private CmsMtPlatformDictDaoExt cmsMtPlatformDictDaoExt;

    /**
     * 获取渠道所有字典的简单信息
     */
    public CmsMtPlatformDictModel getDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        return cmsMtPlatformDictDaoExt.selectById(cmsMtPlatformDictModel);
    }

    public List<CmsMtPlatformDictModel> getModesByChannel(CmsDictionaryIndexBean params) {
        return cmsMtPlatformDictDaoExt.selectByChannel(params);
    }

    public int getCountByChannel(CmsDictionaryIndexBean params) {
        return cmsMtPlatformDictDaoExt.selectAllCount(params);
    }

    public Map<String, Object> getModesAndTotalCountByChannel(CmsDictionaryIndexBean params) {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("dictionaryList", getModesByChannel(params));
        resultInfo.put("dictionaryListCnt", getCountByChannel(params));
        return resultInfo;
    }

    public List<CmsMtPlatformDictModel> getModesByChannelCartId(String order_channel_id, int cartId) {
        return cmsMtPlatformDictDaoExt.selectByChannelCartId(order_channel_id, cartId);
    }

    /**
     * 检测现有数据是否符合
     *
     * @param cmsMtPlatformDictModel CmsMtDictModel
     */
    private void checkDict(CmsMtPlatformDictModel cmsMtPlatformDictModel, boolean isNameCheck) {

        if (StringUtils.isEmpty(cmsMtPlatformDictModel.getName()))
            throw new BusinessException("字典名称不存在!");

        if (StringUtils.isEmpty(cmsMtPlatformDictModel.getValue()))
            throw new BusinessException("字典定义内容不存在!");

        if (isNameCheck) {
            if (cmsMtPlatformDictDaoExt.selectByName(cmsMtPlatformDictModel).size() > 1) {
                throw new BusinessException("该字典名称已经存在,请重新设定字典名称!");
            }
        }
    }

    /**
     * 添加一个字典项
     */
    @VOTransactional
    public int addDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        // 检测新字典项数据
        checkDict(cmsMtPlatformDictModel, true);
        return cmsMtPlatformDictDaoExt.insertDict(cmsMtPlatformDictModel);
    }

    /**
     * 删除一个字典项
     */
    @VOTransactional
    public int removeDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
//        cmsMtPlatformDictDaoExt.insertDictLog(cmsMtPlatFormDictModel);
        return cmsMtPlatformDictDaoExt.deleteDict(cmsMtPlatformDictModel);
    }

    /**
     * 更新一个字典项
     */
    @VOTransactional
    public int saveDict(CmsMtPlatformDictModel cmsMtPlatformDictModel) {
        checkDict(cmsMtPlatformDictModel, false);

        CmsMtPlatformDictModel oldCmsMtPlatformDictModel = getDict(cmsMtPlatformDictModel);
        if (!oldCmsMtPlatformDictModel.getModified().equals(cmsMtPlatformDictModel.getModified())) {
            throw new BusinessException("该条数据已经被其他人更新过了,请确认!");
        }

//        cmsMtPlatformDictDaoExt.insertDictLog(oldCmsMtPlatFormDictModel);
        return cmsMtPlatformDictDaoExt.updateDict(cmsMtPlatformDictModel);
    }
}
