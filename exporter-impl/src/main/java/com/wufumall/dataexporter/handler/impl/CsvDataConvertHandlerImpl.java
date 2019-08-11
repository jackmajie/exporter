package com.wufumall.dataexporter.handler.impl;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.wufumall.core.utils.ThreadPoolManager;
import com.wufumall.dataexporter.api.domain.DataFileDTO;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.api.domain.TaskRowDTO;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.handler.DataConvertHandler;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.DataExporterTemplateService;
import com.wufumall.dataexporter.template.AbnormalContext;

/**
 * @Explain:使用csv导出
 */
@Component
public class CsvDataConvertHandlerImpl implements DataConvertHandler {

    private Logger logger = LoggerFactory.getLogger(CsvDataConvertHandlerImpl.class);
    private static String COLUMNS_FIRST = ":";
    private static String COLUMNS_SECONT = ";";
    private Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d{1,5})?$");
    
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private DataExporterTemplateService dataExporterTemplateService;



    @Override
    public byte[] convertData(TaskCallBackResponseDTO taskCallBackResponseDTO, Long templateId) throws Exception {

       StringBuffer stringBuffer = new StringBuffer();
        // 设置表头
        String header = combineHeader(templateId);
        // 设置内容
        String content = combineContent(taskCallBackResponseDTO);

        // 虽然是文本但还是需要分文件方式进行追加
        // 还有待优化的CSV导出(暂时后部份文本还是全load内存)
        // 拼装
        stringBuffer.append(header).append(content);

        // 由于使用utf-8需要加bom在window打开才不乱码，但加了bom会导致在windows打开拉动格宽时会将所有格合并成一个格的bug
        byte[] a = stringBuffer.toString().getBytes(Charset.defaultCharset().name());
        byte[] bom = new byte[]{/*(byte) 0xEF, (byte) 0xBB, (byte) 0xBF*/};
        byte[] b = new byte[bom.length+a.length];
        System.arraycopy(bom,0,b,0,bom.length);
        System.arraycopy(a,0,b,bom.length,a.length);

        logger.info("处理csv格式完毕,总大小为[{}]B~", b.length);
        return b;
    }


    public String combineHeader(Long templateId) {
      
      
        DataExporterTemplateDO templateDO = dataExporterTemplateService.selectByPrimaryKey(templateId);
        String columns = templateDO.getColumns();
        List<String> titles = titles(columns, COLUMNS_FIRST, COLUMNS_SECONT);

        StringBuilder stringBuilder = new StringBuilder();

        // 设置表头
        for (String key : titles) {
            stringBuilder.append(key).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static List<String> titles(String target, String kvSep, String sectionSep) {
        List<String> titles = new ArrayList<String>();
        if (StringUtils.isBlank(target)) {
            return Collections.emptyList();
        }

        String[] strList = target.split(sectionSep);
        for (String line : strList) {
            if (StringUtils.isBlank(line))
                continue;
            StringBuilder sb = new StringBuilder(line);
            int index = sb.indexOf(kvSep);
            if (index == -1)
                continue;
            String key = sb.substring(0, index);
            String value = sb.substring(index + 1);
            titles.add(key);
        }
        return titles;
    }

    public String combineContent(TaskCallBackResponseDTO taskCallBackResponseDTO) {
        boolean useCache = taskCallBackResponseDTO.isUseCache();
        if (useCache) {
            return doFromCache(taskCallBackResponseDTO);
        } else {
            return dealWithList(taskCallBackResponseDTO.getTaskRowDTOs());
        }
    }


    // 前期还是load到内存，后续需要使用追加文件方式
    private String doFromCache(TaskCallBackResponseDTO taskCallBackResponseDTO) {
        List objectIds = taskCallBackResponseDTO.getObjectIds();
        StringBuilder stringBuilder = new StringBuilder();
        int index = 1;
        for (final Object id : objectIds) {
            GridFSDBFile file = fileRepository.findOneFileInGridFsById(id.toString());
            byte[] temp = new byte[(int) file.getLength()];
            try {
                IOUtils.readFully(file.getInputStream(), temp);
                String jason = new String(temp);
                AbnormalContext context = JSON.parseObject(jason, AbnormalContext.class);
                List<TaskRowDTO> taskRowDTOs = context.getTaskRowDTOs();
                if (!CollectionUtils.isEmpty(taskRowDTOs)) {
                    String result = dealWithList(taskRowDTOs);
                    stringBuilder.append(result);
                    logger.debug(" >> 处理第[{}]个Mongodb文件成功，其ID[{}]", index++, id.toString());
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

        return stringBuilder.toString();
    }


    private String dealWithList(List<TaskRowDTO> taskRowDTOs) {
        StringBuilder stringBuilder = new StringBuilder();
        int rownum = 1;
        for (TaskRowDTO taskRowDTO : taskRowDTOs) {
            List<DataFileDTO> dataFiles = taskRowDTO.getDataFileDTOList();
            if (CollectionUtils.isEmpty(dataFiles)) {
                continue;
            }
            short column = 1;
            for (DataFileDTO dataFileDTO : dataFiles) {
                Object columndValueObj = dataFileDTO.getColumnValue();
                String columndValue;
                if (columndValueObj == null) {
                    columndValue = " ";
                } else if (StringUtils.isEmpty(columndValueObj.toString())) {
                    columndValue = " ";
                } else {
                    columndValue = dataFileDTO.getColumnValue().toString();
                }

                Matcher matcher = pattern.matcher(columndValue);
                if (matcher.find()) {
                    stringBuilder.append(columndValue + "\t");
                } else {
                    stringBuilder.append(columndValue);
                }
                if (column != dataFiles.size()) stringBuilder.append(",");
                column++;
            }
            stringBuilder.append("\n");
            if (++rownum % 1000 == 0) logger.info(">> CSV-handled {} rows success~",rownum);
        }
        return stringBuilder.toString();
    }
}
