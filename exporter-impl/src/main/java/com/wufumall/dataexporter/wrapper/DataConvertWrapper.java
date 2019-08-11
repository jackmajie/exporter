package com.wufumall.dataexporter.wrapper;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.api.exception.XCriterialException;
import com.wufumall.dataexporter.constants.DataExporterConstants;
import com.wufumall.dataexporter.exception.DataExporterExceptionType;
import com.wufumall.dataexporter.handler.DataConvertHandler;
import com.wufumall.dataexporter.handler.impl.CsvDataConvertHandlerImpl;
import com.wufumall.dataexporter.handler.impl.ExcelDataConvertHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;



@Component
public class DataConvertWrapper {
  
    private static final Logger logger = LoggerFactory.getLogger(DataConvertWrapper.class);
    
    public static Map<String, DataConvertHandler> dataConvertHandlerMap = new HashMap<String, DataConvertHandler>();
    
    @Autowired
    private ExcelDataConvertHandlerImpl excelDataConvertHandler;

    @Autowired
    private CsvDataConvertHandlerImpl csvDataConvertHandler;

    /**
     * 转换数据对象
     * @param taskCallBackResponseDTO
     * @param dataType
     * @return
     */
    public byte[] convertData(TaskCallBackResponseDTO taskCallBackResponseDTO, String dataType, Long templateId) throws Exception {
        
        if (CollectionUtils.isEmpty(dataConvertHandlerMap)) {
            loadDataConvertHandlerMap();
        }
        DataConvertHandler dataConvertHandler = dataConvertHandlerMap.get(dataType);
        
        
        if (dataConvertHandler == null) {
          
            logger.error("error@DataConvertWarpperQueryNotDataConvertHandler, dataType={}", new Object[]{dataType});
            throw new XCriterialException(DataExporterExceptionType.RUN_TASK_OBTAIN_CONVERT_BEAN_IS_EMPTY.getCode(),
                    DataExporterExceptionType.RUN_TASK_OBTAIN_CONVERT_BEAN_IS_EMPTY.getMessage());
            
        }
        return dataConvertHandler.convertData(taskCallBackResponseDTO, templateId);
    }

    
    public void load() {
        loadDataConvertHandlerMap();
    }

    private void loadDataConvertHandlerMap() {
        dataConvertHandlerMap.put(DataExporterConstants.DATA_TYPE.EXCEL, excelDataConvertHandler);
        dataConvertHandlerMap.put(DataExporterConstants.DATA_TYPE.CSV, csvDataConvertHandler);
    }
}
