package com.wufumall.dataexporter.template;

import org.springframework.stereotype.Component;

@Component
public class DataExporterTaskRunTemplate {

  
    public void runExportData(TaskRunContext context, DataExporterTaskAction dataExporterTaskAction) throws Exception {
        //入参校验
        dataExporterTaskAction.check(context);
        //执行数据callBack
        dataExporterTaskAction.runQueryExportData(context);
        //转换为TFS要存贮的文件
        dataExporterTaskAction.convert(context);
        //存贮TFS
        dataExporterTaskAction.storeFile(context);
    }

}
