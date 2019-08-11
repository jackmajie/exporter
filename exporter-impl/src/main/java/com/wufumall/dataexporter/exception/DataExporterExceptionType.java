package com.wufumall.dataexporter.exception;

import org.apache.commons.lang.StringUtils;

public enum DataExporterExceptionType {

    RUN_TASK_IS_NULL_ILLEGAL("RUN_TASK_IS_NULL_ILLEGAL","任务执行参数校验task对象为空非法"),
    RUN_TASK_TYPE_IS_NULL_ILLEGAL("RUN_TASK_IS_NULL_ILLEGAL","任务执行参数校验taskType对象为空非法"),
    RUN_TASK_NEED_REDO("RUN_TASK_NEED_REDO","任务需要重新执行"),
    RUN_TASK_NOT_NEED_REDO("RUN_TASK_NOT_NEED_REDO", "任务不需要重新执行"),
    RUN_TASK_QUERY_DATA_ERROR("RUN_TASK_QUERY_DATA_ERROR","任务查询数据失败"),
    RUN_TASK_OBTAIN_CONVERT_BEAN_IS_EMPTY("RUN_TASK_OBTAIN_CONVERT_BEAN_IS_EMPTY","找不到数据转换的bean处理器"),
    RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY("RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY","根据confId查询temlate为空"),
    RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY("RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY","根据templateId查询template为空");


    private DataExporterExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码中文描述
     */
    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return 枚举
     */
    public static DataExporterExceptionType getByCode(String code) {
        for (DataExporterExceptionType lrcResult : values()) {
            if (StringUtils.equals(lrcResult.getCode(), code)) {
                return lrcResult;
            }
        }
        return null;
    }

    public String toString() {
        return "[" + code + ":" + message + "]";
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~ 属性方法 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
