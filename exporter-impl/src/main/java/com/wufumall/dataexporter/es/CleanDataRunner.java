package com.wufumall.dataexporter.es;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.ExporterTaskService;
import com.wufumall.dataexporter.task.CleanDataCollection;


public class CleanDataRunner implements SimpleJob{
  
    private static final Logger logger = LoggerFactory.getLogger(CleanDataRunner.class);
    
    @Autowired
    private CleanDataCollection grabCleanDataCollection; 
    
    @Autowired
    private FileRepository fileRepository; 
    
    @Autowired
    private ExporterTaskService exporterTaskService; 
    
    @Override
    public void execute(ShardingContext shardingContext) {
     
          try{
              List<DataExporterTaskDO> list = grabCleanDataCollection.dispatch(shardingContext.getShardingItem(),shardingContext.getShardingTotalCount());
              if (CollectionUtils.isEmpty(list)) {
                  return;
              }
              for (DataExporterTaskDO dataExporterTaskDO : list) {
                  exporterTaskService.stopDataExporterTask(dataExporterTaskDO.getId());
                  if(StringUtils.isNotBlank(dataExporterTaskDO.getFileUrl())){
                    fileRepository.deleteFileInGridFs(dataExporterTaskDO.getFileUrl());
                  }
                  logger.info(" >> 停止任务{}成功", dataExporterTaskDO.toString());
              }
          } catch (Exception e) {
              logger.error("Consume task error:{}", e.getMessage());
              
              throw new RuntimeException("停止导出任务失败", e);
          }
    }
    
}
