package com.wufumall.dataexporter.handler.impl;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.wufumall.dataexporter.api.facade.DataExporterCallBack;
import com.wufumall.dataexporter.core.DynamicDubboConsumerHolder;
import com.wufumall.dataexporter.core.TaskCallBackBeanFactory;
import com.wufumall.dataexporter.core.TaskDubboProviderCallBackBean;
import com.wufumall.dataexporter.handler.CallBackBeanLoader;
import com.wufumall.dataexporter.utils.DeZkConfigUtil;


@Component
public class CallBackBeanLoaderImpl implements CallBackBeanLoader {

    private static Logger logger = LoggerFactory.getLogger(CallBackBeanLoaderImpl.class);
    
    @Autowired
    private DynamicDubboConsumerHolder dynamicDubboConsumerHolder;
    @Override
    public void loadCallBackBean(String config) {
        loadTaskConf(config);
    }

    /**
     * 加载zk的回调函数bean的配置
     * <taskServices>
     *     <taskService>
     *     <version>0.0.1.daily</version>
     *     <group>TRADE_ERP_01</group>
     *     <timeOut>3000</timeOut>
     *     </taskService>
     * </taskServices>
     */
    private void loadTaskConf(String confStr) {
        //去掉换行
        confStr = confStr.replaceAll("\\s+", "");
        List<TaskDubboProviderCallBackBean> providerCallBackBeanList = DeZkConfigUtil.parseDubboTaskCallBackInfo(confStr);
        if (CollectionUtils.isEmpty(providerCallBackBeanList)) {
            logger.warn("warn@listenerDiamondTaskBeanConfModifiedLoadTaskBeanIsNotTaskBeanInfo,diamondConf={}", new Object[]{
                    confStr
            });
            return;
        }
        loadTaskBean(providerCallBackBeanList, dynamicDubboConsumerHolder);
    }

    /**
     * 初始化taskBean
     * @param providerCallBackBeanList
     */
    private void loadTaskBean(List<TaskDubboProviderCallBackBean> providerCallBackBeanList, DynamicDubboConsumerHolder dynamicDubboConsumerHolder) {
        TaskCallBackBeanFactory taskCallBackBeanFactory = TaskCallBackBeanFactory.getInstance();
        
        for (TaskDubboProviderCallBackBean dubboProviderCallBackBean : providerCallBackBeanList) {
            try {
                if (!validateCallBackBean(dubboProviderCallBackBean)) {
                    continue;
                }
                if (taskCallBackBeanFactory.isRegisted(dubboProviderCallBackBean.getGroup())) {
                    logger.warn("TaskCallBackBeanFactory group[{}] has been registered~", dubboProviderCallBackBean.getGroup());
                } else {
                    //开始注册dubboBean
                    dynamicDubboConsumerHolder.registerNewDubboBean(dubboProviderCallBackBean);
                }
                String group = dubboProviderCallBackBean.getGroup();
                Object dubboBeanrReference = dynamicDubboConsumerHolder.getBean(group);
                if (dubboBeanrReference == null) {
                    logger.error("Register DataExporterCallBack reference error, group={}", group);
                    continue;
                }
                DataExporterCallBack callBack = taskCallBackBeanFactory.reGetProxy((ReferenceConfig<DataExporterCallBack>) dubboBeanrReference);
                if (callBack == null) {
                    dynamicDubboConsumerHolder.removeOldDubboBean(group);
                }
                
            } catch (Exception e) {
                logger.error("Register {}'s DataExporterCallBack interface service fail~ because found no service perhaps service not exists, {}" );
                continue;
            }
        }
    }

    /**
     * 校验taskBean数据是否合法
     * @param dubboProviderCallBackBean
     * @return
     */
    private static boolean validateCallBackBean(TaskDubboProviderCallBackBean dubboProviderCallBackBean) {
        if (dubboProviderCallBackBean == null
                || StringUtils.isEmpty(dubboProviderCallBackBean.getGroup())
                || dubboProviderCallBackBean.getTimeOut() == null
                || StringUtils.isEmpty(dubboProviderCallBackBean.getVersion())) {
          
            return false;
        }
        return true;
    }
}
