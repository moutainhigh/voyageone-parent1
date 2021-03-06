package com.voyageone.service.daoext.report;

import com.voyageone.service.model.report.BiReportDownloadTaskModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/1/16.
 */

@Repository
public interface BiReportDownloadTaskDaoExt {
    List<BiReportDownloadTaskModel> selectTasksByCreatorId(Map<String,Object>  map);
    long selectCount(Map<String,Object>  map);
    List<BiReportDownloadTaskModel> selectPage(Map<String,Object>  map);
    List<BiReportDownloadTaskModel> selectNoTasksByCreatorId(Map<String,Object>  map);
    int softDel(int id);
}
