package com.wufumall.dataexporter.handler;
import org.springframework.core.Ordered;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;

public interface CallBackHandler extends Ordered {

    /**
     * 开始查询
     *
     * @param dataExporterTaskDO
     * @return
     */
    public void executeQuery(DataExporterTaskDO dataExporterTaskDO, TaskCallBackResponseDTO result);
}
