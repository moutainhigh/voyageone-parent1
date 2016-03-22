package com.voyageone.service.impl.jumei;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.JmBtApiLogDaoExt;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class JmBtApiLogService {
    @Autowired
    JmBtApiLogDao dao;
    @Autowired
    JmBtApiLogDaoExt daoext;

    public JmBtApiLogModel get(int id) {
        return dao.get(id);
    }

    public List<JmBtApiLogModel> getList() {
        return dao.getList();
    }

    public int update(JmBtApiLogModel entity) {
        return dao.update(entity);
    }

    public int create(JmBtApiLogModel entity) {
        return dao.create(entity);
    }

    public List<Map<String,Object>> getPage() {
        return daoext.getPage();
    }

}

