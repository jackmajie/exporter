package com.wufumall.dataexporter.core;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wufumall.dataexporter.handler.CallBackBeanLoader;
import com.wufumall.mcc.PropertyUtils;

import lombok.Setter;


/**
 * 初始化做一些事情 将数据一次性加载导内存中
 */
@Component
public class DataExporterInit {

    private static final Logger logger = LoggerFactory.getLogger(DataExporterInit.class);

    public void reload() {
        init();
    }

    public void init() {
        loadDoubboConf();
    }

    @Autowired
    private CallBackBeanLoader callBackBeanLoader;
    /**
     * 模块名
     */
    private String moduleName="defaultMultiConf";

    @Setter
    private String defaultMultiConf;
    /**
     * 原来通过zk动态配置的
     */
    public void loadDoubboConf() {
        String multiConf =PropertyUtils.getProperties(moduleName);
        
        if (StringUtils.isEmpty(multiConf)) {
            multiConf = defaultMultiConf;
        }
        if (StringUtils.isEmpty(multiConf)) {
            logger.error("回调的Dubbo服务配置不能为空串~");
        } else {
            logger.info("从zk获取的字符串[{}]",multiConf);
            callBackBeanLoader.loadCallBackBean(multiConf);
        }
    }
}
