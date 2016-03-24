package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;

import java.util.List;

@Repository
public interface CmsBtJmSkuDao {
    public List<CmsBtJmSkuModel>  selectList();
    public  CmsBtJmSkuModel select(long id);
    public int insert(CmsBtJmSkuModel entity);
    public  int update(CmsBtJmSkuModel entity);

    public  int delete(long id);
    }
