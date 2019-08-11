package com.wufumall.dataexporter.api.facade;
import com.wufumall.core.ResBodyData;
import com.wufumall.dataexporter.api.request.TaskRequest;

/**
 * 类解释：此方法由DataExporter实现以远程接口方工发布出去以供第三方程序直接调用
 */
public interface DataExporterService {

    /**
     * 用户提交异步导出任务（插表操作）
     * @param taskRequestDTO
     * @return
     */
    public ResBodyData createDataExporterTask(TaskRequest taskRequest);
}
