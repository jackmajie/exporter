package com.wufumall.dataexporter.entity;

import lombok.EqualsAndHashCode;
import lombok.Data;

import java.util.Date;

/**
 * 文件模版DO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataExporterTemplateDO{
    private static final long serialVersionUID = 1954583270823526432L;

    /**
     * primary key
     */
    private Long id;

    /**
     * 模版文件类型
     */
    private String DataType;

    /**
     * 文件列
     */
    private String columns;

    /**
     * 标题
     */
    private String title;

    /**
     * 模版体
     */
    private String body;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改者
     */
    private String modifier;

    /**
     * 删除状态
     * 0=正常
     * -1＝删除
     */
    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;
}
