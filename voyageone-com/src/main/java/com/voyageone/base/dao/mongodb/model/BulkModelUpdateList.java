package com.voyageone.base.dao.mongodb.model;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.common.logger.VOAbsLoggable;

import java.util.ArrayList;
import java.util.List;

/**
 * BulkUpdateModel
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class BulkModelUpdateList extends VOAbsLoggable {
    /**
     * 执行缓冲区大小
     */
    private int bufferSize = 0;
    /**
     * 执行队列
     */
    private List<Object> bulkList = null;
    /**
     * 执行Dao对象
     */
    private BaseMongoChannelDao targetDao = null;
    /**
     * 平台渠道店铺ID
     */
    private String channelId = null;

    public BulkModelUpdateList(int limitSize, BaseMongoChannelDao targetDao, String channelId) {
        if (limitSize == 0) {
            throw new IllegalArgumentException("使用BulkUpdateList时必须设置缓冲区大小.");
        }
        this.bulkList = new ArrayList<>(limitSize);
        this.bufferSize = limitSize;
        this.targetDao = targetDao;
        this.channelId = channelId;
    }

    public BulkWriteResult addBulkModel(BulkUpdateModel model) {
        bulkList.add(model);
        // 批量更新
        if (bulkList.size() > 0 && bulkList.size() % bufferSize == 0) {
            BulkWriteResult rs = targetDao.bulkUpdateWithModel(channelId, bulkList);
            bulkList.clear();
            return rs;
        }
        return null;
    }

    public BulkWriteResult execute() {
        if (bulkList.size() > 0) {
            BulkWriteResult rs = targetDao.bulkUpdateWithModel(channelId, bulkList);
            bulkList.clear();
            return rs;
        }
        return null;
    }
}
