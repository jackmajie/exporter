package com.wufumall.dataexporter.core;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.wufumall.dataexporter.api.domain.TaskRowsConf;
import com.wufumall.dataexporter.api.facade.DataExporterCallBack;

import lombok.Setter;

public class TaskCallBackBeanFactory implements ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    private static Logger logger = LoggerFactory.getLogger(TaskCallBackBeanFactory.class);

    private static TaskCallBackBeanFactory taskCallBackBeanFactory = new TaskCallBackBeanFactory();

    private static Map<String, Object> taskBeanMap = new HashMap<String, Object>();

    private Map<String, Integer> taskRowsConfMap = new HashMap<String, Integer>();

    /**
     * 私有化构造方法*
     */
    private TaskCallBackBeanFactory() {
    }

    public static TaskCallBackBeanFactory getInstance() {
        return taskCallBackBeanFactory;
    }

    public Object getTaskBean(String taskType) {
        return taskBeanMap.get(taskType);
    }

    public void addTaskBean(String name, Object taskBean) {
        taskBeanMap.put(name, taskBean);
    }

    public void removeTaskBean(String name) {
        taskBeanMap.remove(name);
    }

    public void flushTaskRowsConf(List<TaskRowsConf> confs) {
        taskRowsConfMap = new HashMap<String, Integer>();
        for (TaskRowsConf conf : confs) {
            taskRowsConfMap.put(conf.getTaskName(), conf.getRows());
        }
    }

    public Integer getTaskRowsConf(String taskName) {
        return taskRowsConfMap.get(taskName);
    }

    /**
     * 判断这个task是否已经注册
     *
     * @param taskType
     * @return
     */
    public boolean isRegisted(String taskType) {
        return taskBeanMap.get(taskType) != null;
    }

    /**
     * 根据ReferenceConfig重置服务
     * @param dubboBeanrReference
     * @return
     */
    public DataExporterCallBack reGetProxy(ReferenceConfig<DataExporterCallBack> dubboBeanrReference) {

        DataExporterCallBack dubboBean = null;
        try {
            //注册完成之后开始将这个bean 放入到Factory中
            dubboBean = dubboBeanrReference.get();
            Optional.ofNullable(dubboBean).orElseThrow(()->new RuntimeException("Initialization of DataExporterCallBack consumer bean fail"));
            //if(dubboBean == null) throw new RuntimeException("Initialization of DataExporterCallBack consumer bean fail");

        } catch (Exception ie) {
            logger.error(" >> DataExporterCallBack service dynamic bean has register fail~ taskType={},cause by:[{}]", dubboBeanrReference.getGroup(), (ie.getMessage()));
            taskCallBackBeanFactory.removeTaskBean(dubboBeanrReference.getGroup());
            return null;
        }

        logger.warn(" >> DataExporterCallBack service dynamic bean has register success, taskType={}, bean={}", dubboBeanrReference.getGroup(), dubboBean);
        addTaskBean(dubboBeanrReference.getGroup(), dubboBean);
        return dubboBean;
    }
}
