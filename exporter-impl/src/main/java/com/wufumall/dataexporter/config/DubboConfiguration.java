package com.wufumall.dataexporter.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
  

@Configuration
@ConditionalOnClass(Exporter.class)
public class DubboConfiguration {

    @Value("${dubbo.application.name}")
    private String applicationName;  
  
    @Value("${dubbo.registry.protocol}")
    private String protocol;  
  
    @Value("${dubbo.registry.address}")
    private String registryAddress;  
  
    @Value("${dubbo.protocol.name}")  
    private String protocolName;  
  
    @Value("${dubbo.protocol.port}")  
    private int protocolPort;  
  
    @Value("${dubbo.provider.timeout}")  
    private int timeout;  
  
      
    /**设置dubbo服务扫描包*/
    @Bean  
    public static AnnotationBean annotationBean(@Value("${dubbo.packageName}") String packageName) {
        AnnotationBean annotationBean = new AnnotationBean();  
        annotationBean.setPackage(packageName);
        return annotationBean;  
    }  
  
    /**dubbo上下文*/  
    @Bean  
    public ApplicationConfig applicationConfig() {
        // 当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();  
        applicationConfig.setName(applicationName);
        return applicationConfig;  
    }  
  
    /** 注册zookeeper*/  
    @Bean  
    public RegistryConfig registryConfig() {
        // zookeeper注册中心配置
        RegistryConfig registry = new RegistryConfig();  
        registry.setProtocol(protocol);
        registry.setAddress(registryAddress);
        return registry;  
    }

    /**dubbo协议提供服务 */
    @Bean  
    public ProtocolConfig protocolConfig() {
        // 服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();  
        protocolConfig.setName(protocolName);
        protocolConfig.setPort(protocolPort);
        return protocolConfig;  
    }  

    /**dubbo服务提供者*/
    @Bean(name = "exporterProvider")
    public ProviderConfig providerConfig() {  
        ProviderConfig providerConfig = new ProviderConfig();  
        providerConfig.setTimeout(timeout);
        providerConfig.setApplication(this.applicationConfig());
        providerConfig.setRegistry(this.registryConfig());

        Map<String,String> parameters=new HashMap<String,String>();
        // 设置优雅停机超时时间
        parameters.put("shutdown.timeout", "10000");
        providerConfig.setParameters(parameters);
        return providerConfig;  
    }

//    @Bean
//    public DataExporterServiceImpl dataExporterService(){
//        return new DataExporterServiceImpl();
//    }
//
//    /** 创建定时任务服务接口配置 */
//    @Bean
//    public ServiceBean<DataExporterService> dataExporterCallBack(){
//        ServiceBean<DataExporterService> serviceBean = new ServiceBean<DataExporterService>();
//        serviceBean.setInterface(DataExporterService.class);
//        serviceBean.setRef(dataExporterService());
//        serviceBean.setTimeout(3000);
//        serviceBean.setVersion("0.0.1");
//        return serviceBean;
//    }

//    @Bean
//    public ReferenceBean<DataExporterCallBack> person() {
//        ReferenceBean<DataExporterCallBack> ref = new ReferenceBean<>();
//        ref.setVersion("0.0.1.daily");
//        ref.setInterface(DataExporterCallBack.class);
//        ref.setGroup("SUPPLIER_LIST_01");
//        return ref;
//    }
}  
