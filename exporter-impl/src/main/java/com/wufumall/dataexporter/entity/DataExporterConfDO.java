package com.wufumall.dataexporter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 数据导出基础配置信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DataExporterConfDO  {

    private static final long serialVersionUID = -6561845761650081276L;

    /**
     * primary key
     */
    private Long id;

    /**
     * 文件导入配置的key
     */
    private String code;

    /**
     * 对应的模版的id
     */
    private Long templateId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 最后一次修改者
     */
    private String modifier;

    /**
     * 是否删除 0=正常 -1＝删除
     */
    private Integer isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    /**
     * 文件最大导出条目
     */
    private Integer maxSize;
}
