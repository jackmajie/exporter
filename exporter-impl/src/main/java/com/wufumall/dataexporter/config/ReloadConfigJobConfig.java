package com.wufumall.dataexporter.config;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.wufumall.dataexporter.es.ReloadConfigRunner;

@Configuration
public class ReloadConfigJobConfig {
  
  @Resource
  private ZookeeperRegistryCenter regCenter;
  
  @Resource
  private JobEventConfiguration jobEventConfiguration;
  

  @Bean
  public ReloadConfigRunner reloadConfigRunner() {
      return new ReloadConfigRunner(); 
  }
  

  @Bean(initMethod = "init")
  public JobScheduler reloadJobScheduler(final ReloadConfigRunner reloadConfigJob,
                                         @Value("${reloadConfigJob.cron}") final String cron,
                                         @Value("${reloadConfigJob.shardingTotalCount}") final int shardingTotalCount,
                                         @Value("${reloadConfigJob.shardingItemParameters}") final String shardingItemParameters) {
        
    
      return new SpringJobScheduler(reloadConfigJob, regCenter,
              getLiteJobConfiguration(reloadConfigJob.getClass(),
              cron, shardingTotalCount, shardingItemParameters), jobEventConfiguration);
      
  }




  private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String shardingItemParameters) {
     
    return LiteJobConfiguration.newBuilder(
          new SimpleJobConfiguration(
              JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
              .shardingItemParameters(shardingItemParameters).build(),
              jobClass.getCanonicalName()))
              .overwrite(false).build();
  }


}
