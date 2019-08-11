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
import com.wufumall.dataexporter.es.CleanDataRunner;


@Configuration
public class CleanDataJobConfig {
  
    @Resource
    private ZookeeperRegistryCenter regCenter;
    
    @Resource
    private JobEventConfiguration jobEventConfiguration;
    
    @Bean
    public CleanDataRunner cleanDataRunner() {
        return new CleanDataRunner(); 
    }
   

    @Bean(initMethod = "init")
    public JobScheduler cleanDataJobScheduler(final CleanDataRunner cleanDataJob,
                                           @Value("${cleanDataJobConfig.cron}") final String cron,
                                           @Value("${cleanDataJobConfig.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${cleanDataJobConfig.shardingItemParameters}") final String shardingItemParameters) {
          
      
        return new SpringJobScheduler(cleanDataJob, regCenter,
                getLiteJobConfiguration(cleanDataJob.getClass(),
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