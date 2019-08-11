package com.wufumall.dataexporter.template;

import lombok.Data;

/**
 * 回调函数的callback bean
 */
@Data
public class TaskDubboProviderCallBackBean {

    /**
     * 版本号*
     */
    private String version;

    /**
     * group*
     */
    private String group;

    /**
     * 超时时间
     */
    private Long timeOut;

    /**
     *应用程序名称
     */
    private String application;
}
