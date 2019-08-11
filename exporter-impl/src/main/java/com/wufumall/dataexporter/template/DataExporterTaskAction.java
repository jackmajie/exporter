package com.wufumall.dataexporter.template;
import com.wufumall.dataexporter.api.exception.XCriterialException;

public interface DataExporterTaskAction {

    /**
     * 参数校验
     * @param taskRunContext
     * @throws XCriterialException
     */
    public void check(TaskRunContext taskRunContext) throws XCriterialException;

    /**
     * 查询导出的数据
     * @param taskRunContext
     * @throws RuntimeException
     */
    public void runQueryExportData(TaskRunContext taskRunContext) throws RuntimeException;

    /**
     * 转换文件类型
     * @param taskRunContext
     * @throws RuntimeException
     */
    public void convert(TaskRunContext taskRunContext) throws RuntimeException, Exception;

    /**
     * 存储TFS文件
     * @param taskRunContext
     * @throws RuntimeException
     */
    public void storeFile(TaskRunContext taskRunContext) throws RuntimeException;


}
