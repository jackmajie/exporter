package com.wufumall.dataexporter.handler.impl;
import java.io.ByteArrayOutputStream;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wufumall.dataexporter.api.domain.DataFileDTO;
import com.wufumall.dataexporter.api.domain.MergeInfoDTO;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.api.domain.TaskRowDTO;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.handler.DataConvertHandler;
import com.wufumall.dataexporter.helper.ExcelHelper;
import com.wufumall.dataexporter.service.DataExporterTemplateService;
import com.wufumall.dataexporter.utils.CommonUtil;

/**
 * excel处理
 */
@Component
public class ExcelDataConvertHandlerImpl implements DataConvertHandler {

    private static String COLUMNS_FIRST = ":";
    private static String COLUMNS_SECONT = ";";

    @Autowired
    private DataExporterTemplateService dataExporterTemplateService;
    
    
    public byte[] convertData(TaskCallBackResponseDTO taskCallBackResponseDTO, Long templateId) throws Exception {

        if(taskCallBackResponseDTO.isUseCache()) {
          throw new RuntimeException(" >> excel导出暂时不支持使用缓存导出~");
        }
        DataExporterTemplateDO templateDO = dataExporterTemplateService.selectByPrimaryKey(templateId);
        
        
        String columns = templateDO.getColumns();
        Map<String, String> columnsMap = CommonUtil.dictToMap(columns, COLUMNS_FIRST, COLUMNS_SECONT);
        // 设置表头
        ExcelHelper excelHelper = new ExcelHelper(templateDO.getTitle());
        // 设置列头
        setExcelHeader(excelHelper, columns);
        int rownum = 1;
        List<TaskRowDTO> taskRowDTOs = taskCallBackResponseDTO.getTaskRowDTOs();
        for (TaskRowDTO taskRowDTO : taskRowDTOs) {
            List<DataFileDTO> dataFiles = taskRowDTO.getDataFileDTOList();
            if (CollectionUtils.isEmpty(dataFiles)) {
                continue;
            }
            short column = 0;
            for (DataFileDTO dataFileDTO : dataFiles) {

                Object columndValueObj = dataFileDTO.getColumnValue();
                String columndValue = null;
                if (columndValueObj == null) {
                    columndValue = " ";
                } else if (StringUtils.isEmpty(columndValueObj.toString())) {
                    columndValue = " ";
                } else {
                    columndValue = dataFileDTO.getColumnValue().toString();
                }

                excelHelper.setCellValue(rownum, column++, columndValue);
            }
            rownum++;
        }
        boolean isMerge = taskCallBackResponseDTO.getHasMergeColumns();
        //需要合并单元格
        if (isMerge && CollectionUtils.isNotEmpty(taskCallBackResponseDTO.getMergeInfoDTOs())) {
            List<MergeInfoDTO> mergeInfoDTOs = taskCallBackResponseDTO.getMergeInfoDTOs();
            for (MergeInfoDTO mergeInfoDTO : mergeInfoDTOs) {
                Integer rowTo = mergeInfoDTO.getMegeToRow();
                Integer rowFrom = mergeInfoDTO.getMergeFromRow();
                Integer columnFrom = mergeInfoDTO.getFromMergeColumns();
                Integer columnTo = mergeInfoDTO.getToMergeColumns();
                if (rowTo != null && rowFrom != null && columnFrom != null && columnTo != null) {
                    int _toRow = rowTo + 1;
                    int _fromRow = rowFrom + 1;
                    if (_toRow > _fromRow) {
                        for (short i = columnFrom.shortValue(); i < columnTo.shortValue(); i++) {
                            excelHelper.addMergedRegion(_fromRow, i, _toRow, i);
                        }
                    }
                }
            }

        }
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        excelHelper.getWorkbook().write(byteOutputStream);
        byte[] bytes = byteOutputStream.toByteArray();
        return bytes;
    }


    /**
     * 设置excel表头
     * @param excelHelper
     * @param columns
     */
    private static void setExcelHeader(ExcelHelper excelHelper, String columns) throws Exception {
        short column = 0;
        String[] strList = columns.split(COLUMNS_SECONT);
        for (String line : strList) {
            if (StringUtils.isBlank(line)){
                continue;
            }    
            StringBuilder sb = new StringBuilder(line);
            int index = sb.indexOf(COLUMNS_FIRST);
            if (index == -1)
                continue;
            String columnsName = sb.substring(0, index);
            String value = sb.substring(index + 1);
            Integer columnsSize = Integer.parseInt(value);
            excelHelper.setHeader(column++, columnsSize, columnsName);
        }
    }
}
