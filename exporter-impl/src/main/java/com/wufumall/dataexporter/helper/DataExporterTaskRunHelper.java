package com.wufumall.dataexporter.helper;
import java.io.ByteArrayInputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.api.exception.XCriterialException;
import com.wufumall.dataexporter.entity.DataExporterConfDO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.exception.DataExporterExceptionType;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.DataExporterConfService;
import com.wufumall.dataexporter.service.DataExporterTemplateService;
import com.wufumall.dataexporter.template.DataExporterTaskAction;
import com.wufumall.dataexporter.template.DataExporterTaskRunTemplate;
import com.wufumall.dataexporter.template.TaskRunContext;
import com.wufumall.dataexporter.wrapper.DataConvertWrapper;
import com.wufumall.dataexporter.wrapper.DataExporterCallBackWrapper;


@Component
public class DataExporterTaskRunHelper {

    @Autowired
    private DataExporterTaskRunTemplate dataExporterTaskRunTemplate;
    
    @Autowired
    private DataExporterCallBackWrapper dataExporterCallBackWrapper;
    
    @Autowired
    private DataConvertWrapper dataConvertWrapper;
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private DataExporterConfService dataExporterConfService;
    
    @Autowired
    private  DataExporterTemplateService  DataExporterTemplateService;


    /**
     * 开始执行文件导出
     * @param context
     * @throws XCriterialException
     * @throws RuntimeException
     * @throws Exception
     */
    public void runExport(TaskRunContext context) throws XCriterialException, RuntimeException, Exception {

        dataExporterTaskRunTemplate.runExportData(context, new DataExporterTaskAction() {
          
          
          
            public void check(TaskRunContext taskRunContext) throws XCriterialException {
              
                if (taskRunContext == null || taskRunContext.dataExporterTaskDO == null) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_IS_NULL_ILLEGAL.getCode(),
                            DataExporterExceptionType.RUN_TASK_IS_NULL_ILLEGAL.getMessage());
                }
                DataExporterTaskDO dataExporterTaskDO = taskRunContext.dataExporterTaskDO;
                if (StringUtils.isEmpty(dataExporterTaskDO.getTaskType())) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_TYPE_IS_NULL_ILLEGAL.getCode(),
                            DataExporterExceptionType.RUN_TASK_TYPE_IS_NULL_ILLEGAL.getMessage());
                    
                }
            }
            
            

            public void runQueryExportData(TaskRunContext taskRunContext) throws RuntimeException {
                TaskCallBackResponseDTO taskCallBackResponseDTO = dataExporterCallBackWrapper.doQueryData(taskRunContext.dataExporterTaskDO);
                taskRunContext.taskCallBackResponseDTO = taskCallBackResponseDTO;
            }
            
            

            public void convert(TaskRunContext taskRunContext) throws RuntimeException, Exception {
              
                TaskCallBackResponseDTO taskCallBackResponseDTO = taskRunContext.taskCallBackResponseDTO;
                DataExporterTaskDO dataExporterTaskDO = taskRunContext.dataExporterTaskDO;
                DataExporterConfDO dataExporterConfDO=dataExporterConfService.selectByPrimaryKey(dataExporterTaskDO.getConfId());
                if (dataExporterConfDO == null) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY.getCode(),
                            DataExporterExceptionType.RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY.getMessage());
                }
                Long templateId = dataExporterConfDO.getTemplateId();
                DataExporterTemplateDO templateDO=DataExporterTemplateService.selectByPrimaryKey(templateId);
                if (templateDO == null) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY.getCode(),
                            DataExporterExceptionType.RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY.getMessage());
                }

                byte[] dataBytes = dataConvertWrapper.convertData(taskCallBackResponseDTO, templateDO.getDataType(), templateId);
                taskRunContext.dataBytes = dataBytes;
            }

            
            
            
            
            public void storeFile(TaskRunContext taskRunContext) throws RuntimeException {
              
                byte[] bytes = taskRunContext.dataBytes;
                DataExporterTaskDO dataExporterTaskDO = taskRunContext.dataExporterTaskDO;
                Long confId = dataExporterTaskDO.getConfId();
                
                DataExporterConfDO dataExporterConfDO=dataExporterConfService.selectByPrimaryKey(dataExporterTaskDO.getConfId());
                if (dataExporterConfDO == null) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY.getCode(),
                            DataExporterExceptionType.RUN_TASK_QUEY_TEMPLATE_BY_CONFID_IS_EMPTY.getMessage());
                }
                
                Long templateId = dataExporterConfDO.getTemplateId();
                DataExporterTemplateDO dataExporterTemplateDO=DataExporterTemplateService.selectByPrimaryKey(templateId);
                
                if (dataExporterTemplateDO == null) {
                    throw new XCriterialException(DataExporterExceptionType.RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY.getCode(),
                            DataExporterExceptionType.RUN_TASK_QUERY_TEMPLATE_BY_ID_IS_EMPTY.getMessage());
                }
                String dataType = dataExporterTemplateDO.getDataType();
                String fileName = dataExporterTemplateDO.getTitle() + "[" + System.currentTimeMillis() + "]." + dataType;
                if (bytes != null && bytes.length > 0) {
                    taskRunContext.fileUrl = fileName;
                    fileRepository.saveInexistenceFileInGridFs(new ByteArrayInputStream(bytes), fileName);
                }
            }
            
        });

    }
    

}
