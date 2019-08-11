package com.wufumall.dataexporter.entity;

import lombok.EqualsAndHashCode;
import lombok.Data;

import java.util.Date;

/**
 * 异步导出任务DO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataExporterTaskDO {
  
    private static final long serialVersionUID = -5563705181386870516L;

    /**
     * primary key
     */
    private Long id;

    /**
     * 任务创建者商家id
     */
    private String userAccount;

    /**
     * 关联的导出配置基本信息的id
     */
    private Long confId;

  
    private Integer status;

    /**
     * 扩展信息 :回调参数格式如下
     * k1:v1;k2:v2;k3:v3
     */
    private String params;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 执行的次数
     */
    private Integer redoNum;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * TFS文件url
     */
    private String fileUrl;

    /**
     * 任务类型对应dubbo的group
     */
    private String taskType;

    /**
     * 来源应用
     */
    private String app;

    /**
     * 预隔离
     */
    private String ownSign;
    

    

}
