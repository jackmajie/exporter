package com.wufumall.dataexporter.api.global;

import lombok.Getter;
import lombok.Setter;

/**
 * 类解释：结果类型
 */
public enum DESResultType {

    SUCCESS(0, "处理成功"),
    ERROR_PARAS(1, "必填参数错误"),
    ERROR_DB_INSERT(2, "任务入库出错"),
    ERROR_OTHER(3, "未知错误"),
    ERROR_NO_TASK(4, "不存在此任务"),
    ERROR_TASK_NO_URL(5, "任务未完成，文件URL为空"),
    ERROR_DEL_TASK(6, "删除任务失败"),
    ERROR_TASK_NO_CONF(7, "不存在此任务的配置信息"),
    ERROR_TASK_NO_TEMPLATE(8, "不存在此任务的模版信息"),
    ERROR_DEL_TASK_PARAM_NULL(9, "删除任务如参非法"),
    ERROR_DEL_TASK_NO_AUTH(10, "无权删除任务"),
    ERROR_NO_EXISTS_OR_FAIL(11, "不存在或删除失败");
    
    @Setter
    @Getter
    private Integer code;
    @Setter
    @Getter
    private String msg;

    DESResultType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
