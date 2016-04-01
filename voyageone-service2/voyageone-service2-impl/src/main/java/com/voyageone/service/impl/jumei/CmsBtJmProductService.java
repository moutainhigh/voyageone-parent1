package com.voyageone.service.impl.jumei;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmProductService {
@Autowired
    CmsBtJmProductDao dao;

    public CmsBtJmProductModel select(int id)
    {
       return dao.select(id);
    }

    public int update(CmsBtJmProductModel entity)
    {
   return dao.update(entity);
    }
    public int create(CmsBtJmProductModel entity)
    {
                   return dao.insert(entity);
    }
    }

