package com.wufumall.dataexporter.handler;
import com.wufumall.dataexporter.api.domain.ResultDTO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.exception.DataExporterException;

public interface DataTaskRunHandler {

    /**
     * 开始执行task导出任务
     * @param dataExporterTaskDO
     * @return
     */
    public ResultDTO<String> runTask(DataExporterTaskDO dataExporterTaskDO) throws DataExporterException;

}
