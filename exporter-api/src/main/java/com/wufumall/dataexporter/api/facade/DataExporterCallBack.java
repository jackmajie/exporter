package com.wufumall.dataexporter.api.facade;
import com.wufumall.dataexporter.api.domain.ResultDTO;
import com.wufumall.dataexporter.api.domain.TaskCallBackRequestDTO;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;

/**
 * 类解释：第三方系统需要实现的回调接口，如何获取具体的数据需要自己实现并以远程服务方式发布出来
 */
public interface DataExporterCallBack {

    /**
     * 业务方取数callBack接口
     * @param taskCallBackRequestDTO
     * @return
     */
    public ResultDTO<TaskCallBackResponseDTO> queryExportData(TaskCallBackRequestDTO taskCallBackRequestDTO);

}
