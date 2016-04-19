package com.voyageone.service.dao.jumei;
import org.springframework.stereotype.Repository;
import com.voyageone.service.model.jumei.*;
import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmProductDao {
     public List<CmsBtJmProductModel> selectList(Map<String, Object> map);
    public CmsBtJmProductModel selectOne(Map<String, Object> map);
    public CmsBtJmProductModel select(long id);
    public int insert(CmsBtJmProductModel entity);
    public int update(CmsBtJmProductModel entity);
    public int delete(long id);
    }