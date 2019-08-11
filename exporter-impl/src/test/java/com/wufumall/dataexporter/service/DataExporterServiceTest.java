package com.wufumall.dataexporter.service;
import com.alibaba.dubbo.common.json.JSON;
import com.wufumall.core.ResBodyData;
import com.wufumall.dataexporter.BaseTest;
import com.wufumall.dataexporter.api.facade.DataExporterService;
import com.wufumall.dataexporter.api.request.TaskRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class DataExporterServiceTest extends BaseTest{
  
      @Autowired
      private DataExporterService dataExporterService;
      
      @Test
      public void  createDataExporterTask() throws IOException{
          TaskRequest request=new TaskRequest();
          request.setApp("test");
          request.setConfId(0l);
          request.setFileUrl("");
          request.setStatus("0");
          request.setTaskType("test");
          request.setTitle("");
          request.setUserAccount("0");
          request.setVersion(1);
          ResBodyData res=dataExporterService.createDataExporterTask(request);
          System.out.println(JSON.json(res));
      }

}
