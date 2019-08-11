package com.wufumall.dataexporter.service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.wufumall.dataexporter.BaseTest;
import com.wufumall.dataexporter.api.global.GlobalStatus;
import com.wufumall.dataexporter.core.TaskParams;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExporterTaskServiceTest extends BaseTest{

      @Autowired
      private ExporterTaskService exporterTaskService;
//      @Autowired
//      private DataExporterCallBack dataExporterCallBack;
      @Test
      public void testSelectBatchTasksByDynamic() throws Exception{
            
        HashMap<String, Object> paraMap = new HashMap<String, Object>();
        List<String> stringList = new ArrayList<String>();
        paraMap.put("statusList", stringList);
        stringList.add(GlobalStatus.DB_TASK_ENABLE +"");
        stringList.add(GlobalStatus.DB_TASK_COMPLETED +"");
        stringList.add(GlobalStatus.DB_TASK_RETRY + "");
        stringList.add(GlobalStatus.DB_TASK_FAIL + "");
        
        LocalDateTime end=LocalDateTime.now().minusDays(3);
        LocalDateTime start=end.minusDays(30);
        Date endDate = Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
        Date startDate = Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
        
        paraMap.put("shardingItem", 0);
        paraMap.put("shardingCount", 2);
        paraMap.put("startDate", startDate);
        paraMap.put("ownSign", TaskParams.getOwnSign());
        paraMap.put("endDate", endDate);
        paraMap.put("limit", 500);
        PageInfo<DataExporterTaskDO> list=exporterTaskService.selectBatchTasksPageByDynamic(0, 20, paraMap);
        System.out.println(JSON.toJSONString(list));
      }
      
      @Test
      public void testSelectEffectTasks() throws Exception{
        List<DataExporterTaskDO> list=exporterTaskService.selectEffectTasks(5, 1, 2);
        System.out.println(JSON.toJSONString(list));
      }
      
      
      @Test
      public void testSelectRunningTasks() throws Exception{
        List<DataExporterTaskDO> list=exporterTaskService.selectRunningTasks(5, 1, 2);
        System.out.println(JSON.toJSONString(list));
      }

    @Test
    public void test1() throws Exception {

    }
}
