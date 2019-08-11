package com.wufumall.dataexporter.es;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.wufumall.dataexporter.core.DataExporterInit;



public class ReloadConfigRunner implements SimpleJob{

    private static Logger logger = LoggerFactory.getLogger(ReloadConfigRunner.class);

    @Autowired
    private DataExporterInit dataExporterInit;
    

    boolean reloadConfig() {
        try {
            dataExporterInit.reload();
            return true;
        } catch (Exception e) {
            logger.error("配置数据重新加载错误：{}",e);
            return false;
        }
    }

    
    
    @Override
    public void execute(ShardingContext shardingContext) {
      if(shardingContext.getShardingItem()==0){
          // 分片服务器0时才去加载配置,防止只有一个分片实例的时候去执行两次任务
         logger.info(" >> 重新加载数据库配置文件{}", reloadConfig() ? "成功" : "失败");
      }
    }
}
