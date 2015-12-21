package com.voyageone.cms.service;

import com.mongodb.BulkWriteResult;
import com.mongodb.CommandResult;
import com.mongodb.WriteResult;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class CmsProductService {
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 获取商品 根据ID获
     */
    public CmsBtProductModel getProductById(String channelId, long prodId) {
        return cmsBtProductDao.selectProductById(channelId, prodId);
    }

    /**
     * 获取商品 根据ID获
     */
    public JSONObject getProductByIdWithJson(String channelId, long prodId) {
        return cmsBtProductDao.selectProductByIdWithJson(channelId, prodId);
    }

    /**
     * 获取商品 根据Code
     */
    public CmsBtProductModel getProductByCode(String channelId, String code) {
        return cmsBtProductDao.selectProductByCode(channelId, code);
    }

    /**
     * 获取商品 根据Props
     */
    public CmsBtProductModel getProductByProps(String channelId, String props) {
        if (StringUtils.isEmpty(props)) {
            return null;
        }
        props = props.trim();
        if (!props.startsWith("{")) {
            props = "{" + props;
        }

        if (!props.endsWith("}")) {
            props = props + "}" ;
        }

        return cmsBtProductDao.selectOneWithQuery(props, channelId);
    }

    /**
     * 获取商品List 根据GroupId
     */
    public List<CmsBtProductModel> getProductByGroupId(String channelId, long groupId) {
        return cmsBtProductDao.selectProductByGroupId(channelId, groupId);
    }

    /**
     * 插入商品
     */
    public WriteResult insert(CmsBtProductModel model) {
        return cmsBtProductDao.insert(model);
    }

    /**
     * 插入商品 依据FeedInfo
     */
    public WriteResult insertByFeed(CmsBtFeedInfoModel feedInfoModel) {
        //TODO 需要增加实现
        return  null;
    }

    /**
     * 插入商品
     */
    public WriteResult insert(Collection<CmsBtProductModel> models) {
        return cmsBtProductDao.insertWithList(models);
    }

    /**
     * 插入商品 依据FeedInfo List
     */
    public WriteResult insertByFeed(Collection<CmsBtFeedInfoModel> models) {
        //TODO 需要增加实现
        return  null;
    }

    /**
     * 更新商品
     */
    public WriteResult update(CmsBtProductModel model) {
        return cmsBtProductDao.update(model);
    }

    /**
     * 删除所有商品
     */
    public CommandResult removeAll(String channelId) {
        return cmsBtProductDao.deleteAll(channelId);
    }

    /**
     *更新Platform
     */
    public WriteResult updateWithPlatform(String channelId, String code, CmsBtProductModel_Group_Platform platformMode) {
        return cmsBtProductDao.updateWithPlatform(channelId, code, platformMode);
    }

    /**
     * 更新SKU
     */
    public WriteResult updateWithSku(String channelId, String code, CmsBtProductModel_Sku skuModel) {
        return cmsBtProductDao.updateWithSku(channelId, code, skuModel);
    }

    /**
     * 批量更新Fields 根据CodeList，更新为相同的值
     */
    public BulkWriteResult bathUpdateWithField(String channelId, List<String> codeList, CmsBtProductModel_Field field, String modifier) {
        return cmsBtProductDao.bathUpdateWithField(channelId, codeList, field, modifier);
    }

    /**
     * 批量更新Fields 根据CodeList
     */
    public BulkWriteResult bathUpdateWithFields(String channelId, Map<String, CmsBtProductModel_Field> codeFieldMap, String modifier) {
        return cmsBtProductDao.bathUpdateWithFields(channelId, codeFieldMap, modifier);
    }
}
