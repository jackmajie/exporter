package com.wufumall.dataexporter.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 类解释：回调接口返回传输对象实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskCallBackRequestDTO extends BaseDTO {
    private static final long serialVersionUID = 851952620787061872L;

    /**
     * 起始条目
     */
    private Integer start;

    /**
     * 每页查询
     */
    private Integer limit;

    /**
     * 使用map装可变的参数
     */
    private Map<String, String> paramMap;

    /**
     * 任务的类型:对应导出任务
     */
    private String taskType;

    /**
     * 具体的用户ID
     */
    private String  userAccount;
}
