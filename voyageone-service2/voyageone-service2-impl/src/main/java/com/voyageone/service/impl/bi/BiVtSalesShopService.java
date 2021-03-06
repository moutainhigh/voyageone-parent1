package com.voyageone.service.impl.bi;

import com.google.common.base.Joiner;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.daoext.bi.BiVmColumnDaoExt;
import com.voyageone.service.daoext.bi.BiVtSalesShopExt;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BiVtSalesShopService
 *
 * @author chuanyu.liang 2016/7/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class BiVtSalesShopService extends BaseService {

    private static final int PAGE_SIZE = 1000;

    @Autowired
    private BiVtSalesShopExt vtSalesShopExt;

    @Autowired
    private BiVmColumnDaoExt vmColumnDaoExt;

    public int saveListData(String tableName, List<String> listColumns, List<List<String>> listValues) {
        int result = 0;
        if (StringUtils.isEmpty(tableName)) {
            return result;
        }
        List<String> valueList = new ArrayList<>();
        if (listValues != null) {
            for (List<String> row : listValues) {
                if (row != null && !row.isEmpty()) {
                    valueList.add("('" + Joiner.on("','").join(row) + "')");
                }
            }
        }
        if (listColumns != null && !listColumns.isEmpty() && !valueList.isEmpty()) {
            List<List<String>> valueListSplit = new ArrayList<>();
            if (valueList.size() > PAGE_SIZE) {
                int index = 0;
                for (; index < valueList.size() / PAGE_SIZE; index++) {
                    valueListSplit.add(valueList.subList(index * PAGE_SIZE, (index+1)*PAGE_SIZE));
                }
                if (valueList.size()%PAGE_SIZE > 0) {
                    valueListSplit.add(valueList.subList(index * PAGE_SIZE, valueList.size()));
                }
            } else {
                valueListSplit.add(valueList);
            }

            for (List<String> valueSqlList : valueListSplit) {
                Map<String, Object> param = new HashMap<>();
                param.put("tableTitleName", tableName);
                param.put("column", listColumns);
                param.put("value", valueSqlList);
                result = vtSalesShopExt.insertWithList(param);
                $info("BiVtSalesShopService.saveListData:" + valueSqlList.size());
            }
        }
        return result;
    }

    public List<Map<String, Object>> getListByEcommId(int ecommId, String corTableName) {
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("ecommId", ecommId);
        mapParameter.put("corTableName", corTableName);
        return vmColumnDaoExt.selectList(mapParameter);
    }

}
