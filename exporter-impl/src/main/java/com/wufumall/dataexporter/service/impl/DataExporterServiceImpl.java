package com.wufumall.dataexporter.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Strings;
import com.wufumall.core.ResBodyData;
import com.wufumall.dataexporter.api.facade.DataExporterService;
import com.wufumall.dataexporter.api.global.DESResultType;
import com.wufumall.dataexporter.api.request.TaskRequest;
import com.wufumall.dataexporter.constants.ModelStatus;
import com.wufumall.dataexporter.core.TaskParams;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.service.ExporterTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;



/**
  *类解释：DataExporterService接口调用结果类型实现
  */
@Service(version = "0.0.1")
public class DataExporterServiceImpl implements DataExporterService {

    private static final Logger logger = LoggerFactory.getLogger(DataExporterServiceImpl.class);
    
    @Autowired
    private ExporterTaskService exporterTaskService;
    
    public ResBodyData createDataExporterTask(TaskRequest taskRequest) {
        ResBodyData result = new ResBodyData();
        try {
            DESResultType resultType =DESResultType.SUCCESS;
            if (!Strings.isNullOrEmpty(taskRequest.check())) {
                result.setCode(DESResultType.ERROR_PARAS.getCode());
                result.setMsg(DESResultType.ERROR_PARAS.getMsg());
                return result;
            }
            DataExporterTaskDO dataExporterTaskDO=wrapper(taskRequest);
            Long id = exporterTaskService.insert(dataExporterTaskDO);
            result.setCode(resultType.getCode());
            result.setMsg(resultType.getMsg());
            result.setData(id);
            return result;
        } catch (Exception e) {
            result.setCode(DESResultType.ERROR_DB_INSERT.getCode());
            result.setMsg(DESResultType.ERROR_DB_INSERT.getMsg());
            logger.error("创建导出task失败:{}", e);
            return result;
        }
    }
    
    
    
   /**
    * 功能描述: 封装DataExporterTaskDO对象
    * @author: chenjy
    * @date: 2017年7月18日 下午2:11:12 
    * @param taskRequest
    * @return
    */
   private DataExporterTaskDO wrapper(TaskRequest taskRequest){
       DataExporterTaskDO dataExporterTaskDO = new DataExporterTaskDO();
       dataExporterTaskDO.setStatus(ModelStatus.DB_TASK_ENABLE);
       dataExporterTaskDO.setUserAccount(taskRequest.getUserAccount());
       dataExporterTaskDO.setTaskType(taskRequest.getTaskType());
       dataExporterTaskDO.setVersion(taskRequest.getVersion());
       dataExporterTaskDO.setRedoNum(0);
       dataExporterTaskDO.setConfId(taskRequest.getConfId());
       dataExporterTaskDO.setTitle(taskRequest.getTitle());
       dataExporterTaskDO.setApp(taskRequest.getApp());
       dataExporterTaskDO.setOwnSign(TaskParams.getOwnSign());
       dataExporterTaskDO.setGmtCreate(new Date());
       dataExporterTaskDO.setParams(taskRequest.getParams());
       return dataExporterTaskDO;
   }

    
}

