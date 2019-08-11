package com.wufumall.dataexporter.constants;

public class ModelStatus {
    /**
     * t_data_exporter_task 表状态
     * 执行状态：0=未执行 1= 执行当中 2=执行完毕 -1 失败需要重试 －2 失败不重试 -3 已经失效
     */
    public static final int DB_TASK_ENABLE = 0;
    public static final int DB_TASK_RUNNING = 1;
    public static final int DB_TASK_COMPLETED = 2;
    public static final int DB_TASK_RETRY = -1;
    public static final int DB_TASK_FAIL = -2;
    public static final int DB_TASK_DISABLED = -3;

    /**
     * ResultDTO的结果代码
     */
    public static final int RESULTDTO_CODE_SUCCESS = 0;
    public static final int RESULTDTO_CODE_ERROR_1 = -1;//待定
    public static final int RESULTDTO_CODE_ERROR_2 = -2;//待定
}
