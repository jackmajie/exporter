package com.wufumall.dataexporter.handler;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;

/**
 * 数据转换处理器
 */
public interface DataConvertHandler {

    /**
     * 传唤数据为输出流
     * @param taskCallBackResponseDTO
     * @return
     */
    public byte[] convertData(TaskCallBackResponseDTO taskCallBackResponseDTO, Long templateId) throws Exception;

}
