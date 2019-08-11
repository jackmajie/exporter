package com.wufumall.dataexporter.core;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.google.common.collect.Lists;
import com.wufumall.dataexporter.api.facade.DataExporterCallBack;
import com.wufumall.dataexporter.constants.DubboConsumerBeanDefinition;
import lombok.Setter;

/**
 * 动态注入
 */

@Component
public class DynamicDubboConsumerHolder implements ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    @Setter
    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private RegistryConfig registryConfig;

    
    private transient boolean initialize;
    
    
    @PostConstruct
    public void init() {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        initialize = true;
    }

    
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }
    
    
    public void registerNewDubboBean(TaskDubboProviderCallBackBean taskDubboProviderCallBackBean) {
        if (beanFactory == null) {
            init();
        }
        String beanName = taskDubboProviderCallBackBean.getGroup();
        //如果这个bean在spring ioc中存在 则什么也不做了
        if (applicationContext.containsBean(beanName)) {
            return;
        }
        BeanDefinitionBuilder beanDefinitionBuilder 
        = buildDubboBeanDefinition(taskDubboProviderCallBackBean.getGroup(), taskDubboProviderCallBackBean.getVersion(), taskDubboProviderCallBackBean.getTimeOut(), taskDubboProviderCallBackBean.getApplication());
        //开始注册bean
        startResDubboBean(beanName, beanDefinitionBuilder);
    }

    
    public void removeOldDubboBean(String beanName) {
        if(!initialize) return;
        if (applicationContext.containsBean(beanName)) {
            beanFactory.removeBeanDefinition(beanName);
        }
    }

    /**
     * 正式将hsfConsumerbean 注册到spring IOC容器中
     * @param beanDefinitionBuilder
     * @param beanName
     */
    public void startResDubboBean(String beanName, BeanDefinitionBuilder beanDefinitionBuilder) {
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    
    
    /**
     * @param group
     * @param version
     * @param target
     */
    private BeanDefinitionBuilder buildDubboBeanDefinition(String group, String version, Long timeOut, String application) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ReferenceConfig.class);
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.INTERFACE_NAME, DataExporterCallBack.class.getName());
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.APPLICATION, new ApplicationConfig(application));
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.REGISTRIES, Lists.newArrayList(registryConfig));
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.VERSION, version);
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.GROUP, group);
        beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.INIT, true);
        if (timeOut != null) {
            beanDefinitionBuilder.addPropertyValue(DubboConsumerBeanDefinition.TIMEOUT, timeOut);
        }
        return beanDefinitionBuilder;
    }
}
