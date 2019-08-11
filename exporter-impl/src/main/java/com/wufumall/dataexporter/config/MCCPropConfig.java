package com.wufumall.dataexporter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.wufumall.mcc.MCCPropConfigurer;

@Configuration
public class MCCPropConfig {
  
  @Value("${mcc.exporter.module}")
  private String moduleName;
  
  @Value("${mcc.zk.address}")
  private String zkConnectStr;
  
  
  @Bean("cleanDataJob")
  public MCCPropConfigurer mccPropConfigurer(){
    MCCPropConfigurer mcc=new MCCPropConfigurer();
    mcc.setModuleName(moduleName);
    mcc.setZkConnectStr(zkConnectStr);
    return  mcc;
  }

}
