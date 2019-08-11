package com.wufumall.dataexporter.handler.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wufumall.dataexporter.api.domain.ResultDTO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.exception.DataExporterException;
import com.wufumall.dataexporter.exception.DataExporterExceptionType;
import com.wufumall.dataexporter.handler.DataTaskRunHandler;
import com.wufumall.dataexporter.helper.DataExporterTaskRunHelper;
import com.wufumall.dataexporter.template.TaskRunContext;



@Component
public class DataTaskRunHandlerImpl implements DataTaskRunHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataTaskRunHandlerImpl.class);

    @Autowired
    private DataExporterTaskRunHelper dataExporterTaskRunHelper;

    public ResultDTO<String> runTask(DataExporterTaskDO dataExporterTaskDO) throws DataExporterException {
        TaskRunContext context = new TaskRunContext();
        ResultDTO<String> result = new ResultDTO<String>();
        context.dataExporterTaskDO = dataExporterTaskDO;
        try {
            dataExporterTaskRunHelper.runExport(context);
            result.setData(context.fileUrl);
            
        } catch (Exception e) {
            logger.error("dataExporterExceptionError,dataExporterTaskDO={}" + dataExporterTaskDO, e);
            throw new DataExporterException(DataExporterExceptionType.RUN_TASK_NEED_REDO);
        }
        return result;
    }

}
