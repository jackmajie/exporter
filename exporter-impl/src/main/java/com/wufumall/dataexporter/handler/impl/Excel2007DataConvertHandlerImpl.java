package com.wufumall.dataexporter.handler.impl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.wufumall.core.utils.ThreadPoolManager;
import com.wufumall.dataexporter.api.domain.DataFileDTO;
import com.wufumall.dataexporter.api.domain.MergeInfoDTO;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.api.domain.TaskRowDTO;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.handler.DataConvertHandler;
import com.wufumall.dataexporter.helper.Excel2007Helper;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.DataExporterTemplateService;
import com.wufumall.dataexporter.template.AbnormalContext;

import lombok.Setter;

/**
 * excel处理
 */
@Component
public class Excel2007DataConvertHandlerImpl implements DataConvertHandler {

    private static String COLUMNS_FIRST = ":";
    private static String COLUMNS_SECONT = ";";

    private Logger logger = LoggerFactory.getLogger(CsvDataConvertHandlerImpl.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DataExporterTemplateService dataExporterTemplateService;

    @Setter
    private int flushRow = 1000;

    public byte[] convertData(TaskCallBackResponseDTO taskCallBackResponseDTO, Long templateId) throws Exception {

        DataExporterTemplateDO templateDO = dataExporterTemplateService.selectByPrimaryKey(templateId);
        String columns = templateDO.getColumns();

        // 设置表头
        Excel2007Helper excelHelper = new Excel2007Helper(templateDO.getTitle());

        // 设置列头
        setExcelHeader(excelHelper, columns);

        // 设置内容
        combineContent(excelHelper, taskCallBackResponseDTO);

        // 设置合并单元格
        mergeGrid(excelHelper, taskCallBackResponseDTO);

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        excelHelper.getWorkbook().write(byteOutputStream);
        byte[] bytes = byteOutputStream.toByteArray();
        return bytes;
    }

    private void mergeGrid(Excel2007Helper excelHelper, TaskCallBackResponseDTO taskCallBackResponseDTO) {
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

    }

    private void combineContent(Excel2007Helper excelHelper, TaskCallBackResponseDTO taskCallBackResponseDTO) {
        boolean useCache = taskCallBackResponseDTO.isUseCache();
        if (useCache) {
            doFromCache(excelHelper, taskCallBackResponseDTO);
        } else {
            dealWithList(excelHelper, taskCallBackResponseDTO.getTaskRowDTOs(), 1);
        }
    }

    private void dealWithList(Excel2007Helper excelHelper, List<TaskRowDTO> taskRowDTOs, int rowIndex) {

        for (TaskRowDTO taskRowDTO : taskRowDTOs) {
            List<DataFileDTO> dataFiles = taskRowDTO.getDataFileDTOList();
            if (CollectionUtils.isEmpty(dataFiles)) {
                continue;
            }
            short column = 0;
            for (DataFileDTO dataFileDTO : dataFiles) {
                Object columndValueObj = dataFileDTO.getColumnValue();
                Object columndValue;
                try {
                    if (columndValueObj == null) {
                        columndValue = " ";
                    } else if (StringUtils.isEmpty(columndValueObj.toString())) {
                        columndValue = " ";
                    } else {
                        columndValue = dataFileDTO.getColumnValue();
                    }
                }catch (Exception e){
                    logger.error(" >> 数据转换出错:{}",e.getMessage());
                    columndValue= "error";
                }
                excelHelper.setCellValue(rowIndex, column++, columndValue);
            }
            //每当行数达到设置的值就刷新数据到硬盘,以清理内存
            if (rowIndex % flushRow == 0) excelHelper.flushToTemp();

            rowIndex++;
        }
    }

    private void doFromCache(Excel2007Helper excelHelper, TaskCallBackResponseDTO taskCallBackResponseDTO) {
        List objectIds = taskCallBackResponseDTO.getObjectIds();
        int index = 1;
        int rowIndex = 1;
        for (final Object id : objectIds) {
            GridFSDBFile file = fileRepository.findOneFileInGridFsById(id.toString());
            byte[] temp = new byte[(int) file.getLength()];
            try {
                IOUtils.readFully(file.getInputStream(), temp);
                String jason = new String(temp);
                AbnormalContext context = JSON.parseObject(jason, AbnormalContext.class);
                List<TaskRowDTO> taskRowDTOs = context.getTaskRowDTOs();
                if (!CollectionUtils.isEmpty(taskRowDTOs)) {
                    dealWithList(excelHelper, taskRowDTOs, rowIndex);
                    rowIndex += taskRowDTOs.size();
                    logger.debug(" >> 处理第[{}]个Mongodb文件成功，其ID[{}]，目前excel行数为[{}]", index++, id.toString(), rowIndex);
                } else {
                    logger.error(" >> 处理第[{}]个Mongodb文件失败,获取的context为空串~", index++);
                }
            } catch (IOException e) {
                logger.error(" >> 从mongoDB读取文件失败~");
            } finally {
                ThreadPoolManager.getInstance().addExecuteTask(()->{
                  fileRepository.deleteFileInGridFsById(id.toString());
                });
            }
        }
    }

    /**
     * 设置excel2007表头
     *
     * @param excelHelper
     * @param columns
     */
    private static void setExcelHeader(Excel2007Helper excelHelper, String columns) throws Exception {
        short column = 0;
        String[] strList = columns.split(COLUMNS_SECONT);
        for (String line : strList) {
            if (StringUtils.isBlank(line))
                continue;
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
