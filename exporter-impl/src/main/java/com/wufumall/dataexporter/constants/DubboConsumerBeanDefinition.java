package com.wufumall.dataexporter.constants;

public class DubboConsumerBeanDefinition {

    /**
     * Dubbo的app名称
     */
    public static final String APPLICATION = "application";
    /**
     * 注册列表
     */
    public static final String REGISTRIES = "registries";
    /**提供方接口定义**/
    public static final String INTERFACE_NAME = "interface"; // 由于ReferenceConfig只有setInterface方法
    /**版本信息*/
    public static final String VERSION = "version";
    /**组别*/
    public static final String GROUP = "group";
    /**初始化方法*/
    public static final String INIT = "init";
    public static final String TIMEOUT = "timeout";

}
