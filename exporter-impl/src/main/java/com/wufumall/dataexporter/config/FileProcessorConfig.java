package com.wufumall.dataexporter.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.collect.Lists;
import com.wufumall.dataexporter.leadingin.processor.ExlProcessor;

@Configuration
public class FileProcessorConfig {
  
  

  
  
  @Bean("exlProcessor")
  public ExlProcessor exlProcessor() {
    ExlProcessor processor=new ExlProcessor();
    processor.setTypes(Lists.newArrayList("xlsx"));
    return processor;
  }

  
  
  
}
