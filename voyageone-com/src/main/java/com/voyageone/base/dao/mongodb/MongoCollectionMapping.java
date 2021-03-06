package com.voyageone.base.dao.mongodb;

import java.util.HashMap;
import java.util.Map;

/**
 * MongoCollectionMapping
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
@Deprecated
public class MongoCollectionMapping {
    private Map<String, String> collectionNameMap;
    private Map<String, String> _collectionNameMap = new HashMap<>();
    public Map<String, String> getCollectionNameMap() {
        return collectionNameMap;
    }

    public void setCollectionNameMap(Map<String, String> collectionNameMap) {
        this.collectionNameMap = collectionNameMap;
        for (Map.Entry<String, String> entry : collectionNameMap.entrySet()) {
            _collectionNameMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public String getCollectionName(Class<?> entityClass) {
        String collectionName = entityClass.getSimpleName().toLowerCase();
        if (_collectionNameMap.containsKey(collectionName)) {
            return _collectionNameMap.get(collectionName);
        }
        return null;
    }

}
