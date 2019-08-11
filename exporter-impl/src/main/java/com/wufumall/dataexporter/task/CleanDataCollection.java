package com.wufumall.dataexporter.task;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wufumall.dataexporter.api.global.GlobalStatus;
import com.wufumall.dataexporter.core.TaskParams;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.service.ExporterTaskService;



@Component
public class CleanDataCollection implements TaskCollection {

    private static final Logger logger = LoggerFactory.getLogger(CleanDataCollection.class);

    @Autowired
    private ExporterTaskService exporterTaskService;

    public List<DataExporterTaskDO> dispatch(int shardingItem,int shardingCount) throws Exception {

        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        List<String> stringList = new ArrayList<String>();
        paraMap.put("statusList", stringList);
        stringList.add(GlobalStatus.DB_TASK_ENABLE + "");
        stringList.add(GlobalStatus.DB_TASK_COMPLETED + "");
        stringList.add(GlobalStatus.DB_TASK_RETRY + "");
        stringList.add(GlobalStatus.DB_TASK_FAIL + "");
        
        LocalDateTime end=LocalDateTime.now().minusDays(3);
        LocalDateTime start=end.minusDays(30);
        Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        
        paraMap.put("shardingItem", shardingItem);
        paraMap.put("shardingCount", shardingCount);
        paraMap.put("startDate", startDate);
        paraMap.put("ownSign", TaskParams.getOwnSign());
        paraMap.put("endDate", endDate);
        paraMap.put("limit", 500);
        List<DataExporterTaskDO> resultList = Collections.emptyList();
        try {
            resultList = exporterTaskService.selectBatchTasksByDynamic(paraMap);
        } catch (Exception e) {
            logger.error("Query the over time task error,{}", e.getMessage());
        }
        return resultList;
    }
}