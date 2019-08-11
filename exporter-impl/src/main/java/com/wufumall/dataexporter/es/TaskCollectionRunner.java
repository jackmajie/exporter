package com.wufumall.dataexporter.es;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wufumall.core.utils.ThreadPoolManager;
import com.wufumall.dataexporter.api.domain.ResultDTO;
import com.wufumall.dataexporter.api.global.GlobalStatus;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.handler.DataTaskRunHandler;
import com.wufumall.dataexporter.service.ExporterTaskService;
import com.wufumall.dataexporter.task.RunTaskCollection;



public class TaskCollectionRunner  implements SimpleJob{
  
    private static final Logger logger = LoggerFactory.getLogger(TaskCollectionRunner.class);
    
    @Autowired
    private DataTaskRunHandler dataTaskRunHandler;
    
    @Autowired
    private RunTaskCollection runTaskCollection;
    
    @Autowired
    private ExporterTaskService  exporterTaskService;
    
    /**
     * 最大运行次数（也是相当于最大重试次数据）
     */
    private Integer taskMaxRedoNum = 3;
    
    public void run0(DataExporterTaskDO dataExporterTaskDO) {
        if (dataExporterTaskDO.getRedoNum() >= taskMaxRedoNum) {
            DataExporterTaskDO para = new DataExporterTaskDO();
            para.setId(dataExporterTaskDO.getId());
            para.setStatus(GlobalStatus.DB_TASK_FAIL);
            para.setGmtModified(new Date());
            updateTask(para);
            return;
        } else {
            DataExporterTaskDO para = new DataExporterTaskDO();
            para.setId(dataExporterTaskDO.getId());
            para.setStatus(GlobalStatus.DB_TASK_RUNNING);
            para.setRedoNum(dataExporterTaskDO.getRedoNum() + 1);
            para.setGmtModified(new Date());
            updateTask(para);
            dataExporterTaskDO.setStatus(GlobalStatus.DB_TASK_RUNNING);
            dataExporterTaskDO.setRedoNum(para.getRedoNum());
        }
        ResultDTO<String> result;
        try {
            result = dataTaskRunHandler.runTask(dataExporterTaskDO);
            //如果执行成功
            if (result.isSuccess()) {
                DataExporterTaskDO updateDPT = new DataExporterTaskDO();
                updateDPT.setVersion(dataExporterTaskDO.getVersion());
                updateDPT.setStatus(GlobalStatus.DB_TASK_COMPLETED);
                updateDPT.setId(dataExporterTaskDO.getId());
                updateDPT.setFileUrl(result.getData());
                updateDPT.setGmtModified(new Date());
                updateTask(updateDPT);
                return;
            }
        }
        // 任务失败处理
        catch (Exception e) {
           // if (dataExporterTaskDO.getRedoNum() >= taskMaxRedoNum) {
                DataExporterTaskDO para = new DataExporterTaskDO();
                para.setId(dataExporterTaskDO.getId());
                para.setStatus(GlobalStatus.DB_TASK_FAIL);
                para.setGmtModified(new Date());
                updateTask(para);
           /* } else {
                DataExporterTaskDO para = dataExporterTaskDO;
                para.setStatus(GlobalStatus.DB_TASK_RETRY);
                para.setGmtModified(new Date());
                updateTask(para);
            }*/
        }
    }

    private void updateTask(DataExporterTaskDO para) {
        exporterTaskService.updateTaskByPrimaryKey(para);
    }
    
    @Override
    public void execute(ShardingContext shardingContext) {
        logger.info("导出定时任务开始执行:");
        try {
            List<DataExporterTaskDO> list = runTaskCollection.dispatch(shardingContext.getShardingItem(),shardingContext.getShardingTotalCount());
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            for (final DataExporterTaskDO dataExporterTaskDO : list) {
              
                ThreadPoolManager.getInstance().addExecuteTask(new Runnable() {
                      @Override
                      public void run() {
                          run0(dataExporterTaskDO);
                      }
                });
            }
        } catch (Exception e) {
            logger.error(" >> 执行导出任务指失败", e);
            throw new RuntimeException("执行导出任务指失败", e);
        }
        logger.info("导出定时任务结束执行:");
    }
}
