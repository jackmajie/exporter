package com.wufumall.dataexporter.handler.impl;
import com.alibaba.fastjson.JSON;
import com.wufumall.dataexporter.api.domain.*;
import com.wufumall.dataexporter.api.facade.DataExporterCallBack;
import com.wufumall.dataexporter.constants.DataExporterConstants.EXPORT_DESIRED;
import com.wufumall.dataexporter.core.TaskCallBackBeanFactory;
import com.wufumall.dataexporter.entity.DataExporterConfDO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.exception.DataExporterExceptionType;
import com.wufumall.dataexporter.handler.CallBackHandler;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.DataExporterConfService;
import com.wufumall.dataexporter.template.AbnormalContext;
import com.wufumall.dataexporter.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.wufumall.dataexporter.constants.DataExporterConstants.EXPORT_DESIRED.judgeBySize;
/**
 * 数据导出处理器
 */
@Component
public class DataExporterCallBackHandlerImpl implements CallBackHandler {
  
    private static final Logger logger = LoggerFactory.getLogger(DataExporterCallBackHandlerImpl.class);
    /**
     * 默认导出的条目*
     */
    private static final int DEFAULT_MAXSIZE = 10000;
    /**
     * 分布限制数
     */
    private static final int DEFAULT_MAXLIMIT = 2000;
    private static final int SECOND = 1;

    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private DataExporterConfService dataExporterConfService;

    /**
     * 执行回调函数
     * @param dataExporterTaskDO
     * @return
     */
    public void executeQuery(DataExporterTaskDO dataExporterTaskDO, TaskCallBackResponseDTO result) {
        TaskCallBackBeanFactory taskCallBackBeanFactory = TaskCallBackBeanFactory.getInstance();
        String group = dataExporterTaskDO.getTaskType();
        Object bean = taskCallBackBeanFactory.getTaskBean(group);
        DataExporterCallBack dataExporterCallBack = (DataExporterCallBack) bean;
        startQueryDataByPage(dataExporterTaskDO, dataExporterCallBack, result);
    }

    
    
    /**
     * 开始分页查询数据 每页100条
     * @param dataExporterTaskDO
     * @return
     */
    private void startQueryDataByPage(DataExporterTaskDO dataExporterTaskDO, DataExporterCallBack dataExporterCallBack, TaskCallBackResponseDTO result) {
        
      
        List<TaskRowDTO> taskRowDTOs = new ArrayList<TaskRowDTO>();
        List<MergeInfoDTO> mergeInfoDTOs = new ArrayList<MergeInfoDTO>();
        DataExporterConfDO dataExporterConfDO =dataExporterConfService.selectByPrimaryKey(dataExporterTaskDO.getConfId());
        if (dataExporterConfDO == null) {
            logger.error("error@QueryExporterCallBackDataIsNull, dataExporterTask=null");
            return;
        }

        int maxSize = (dataExporterConfDO.getMaxSize() == null ? DEFAULT_MAXSIZE : dataExporterConfDO.getMaxSize());
        EXPORT_DESIRED desired = judgeBySize(maxSize);
        if (desired == null) {
            logger.error(" >> 导出大小不在控制范围，导出失败，模版最大值[{}]", maxSize);
            return;
        }
        List cacheIds = new ArrayList();
        buildData(dataExporterTaskDO, dataExporterCallBack, result, 
            taskRowDTOs, mergeInfoDTOs, dataExporterConfDO,desired,maxSize,cacheIds);
        
        logger.warn("info@queryDataSuccessEnd, dataExporterTaskDO={}", new Object[]{dataExporterTaskDO});
        result.setUseCache(desired.isUseCache());

        // 如果使用缓存则存在mongoDB中，后期也从中取
        if (desired.isUseCache()) {
            result.setObjectIds(cacheIds);
            logger.info(" >> 使用缓存暂存回调接口数据，子文件个数为:[{}]\n,分别为{}", cacheIds.size(), cacheIds.toString());
        }
        // 如果不使用缓存则直接放内存中
        else {
            result.setMergeInfoDTOs(mergeInfoDTOs);
            result.setTaskRowDTOs(taskRowDTOs);
        }
    }
    
    
    
    
    
    /**
     * 功能描述: 构建导出数据 
     * @author: chenjy
     * @date: 2017年7月20日 下午1:59:20 
     * @param dataExporterTaskDO
     * @param dataExporterCallBack
     * @param result
     * @param taskRowDTOs
     * @param mergeInfoDTOs
     * @param dataExporterConfDO
     * @param desired
     * @param maxSize
     * @param cacheIds
     */
    private  void buildData(DataExporterTaskDO dataExporterTaskDO,DataExporterCallBack dataExporterCallBack,
        TaskCallBackResponseDTO result,List<TaskRowDTO> taskRowDTOs,List<MergeInfoDTO> mergeInfoDTOs,
        DataExporterConfDO dataExporterConfDO,EXPORT_DESIRED desired,int maxSize,List cacheIds){
      
            TaskCallBackBeanFactory factory = TaskCallBackBeanFactory.getInstance();
            int limit = DEFAULT_MAXLIMIT;
            if (factory.getTaskRowsConf(dataExporterConfDO.getCode()) != null) {
              limit = factory.getTaskRowsConf(dataExporterConfDO.getCode());
            }
            int pages = (maxSize % limit == 0) ? (maxSize / limit) : (maxSize / limit + 1);
            int total = 0;
            for (int index = 1; index <= pages; index++) {
              
                TaskCallBackRequestDTO taskCallBackRequestDTO = buildCallBackRequest(dataExporterTaskDO, index, limit);
                logger.info("start_obtian_dubbo_data,params={},taskId={},taskType={}", new Object[]{taskCallBackRequestDTO, dataExporterTaskDO.getId(), dataExporterTaskDO.getTaskType()});
                
                ResultDTO<TaskCallBackResponseDTO> callBackResult = dataExporterCallBack.queryExportData(taskCallBackRequestDTO);
                logger.info(">> remote call the {}th time success~", index);
                
                if (!callBackResult.isSuccess() || callBackResult.getData() == null) {
                    logger.error("error@startQueryDataByPageQueryDataError, errorCode={},errorMSG={},limit={},index={},params={}", new Object[]{callBackResult.getErrorCode(), callBackResult.getErrorMSG(), limit, index, dataExporterTaskDO});
                    throw new RuntimeException(DataExporterExceptionType.RUN_TASK_QUERY_DATA_ERROR.getCode() + DataExporterExceptionType.RUN_TASK_QUERY_DATA_ERROR.getMessage());
                }
                TaskCallBackResponseDTO responseDTO = callBackResult.getData();
                // 为空直接不处理
                if (CollectionUtils.isEmpty(responseDTO.getTaskRowDTOs())) {
                    break;
                }
                // 不为空根据设置进行表格处理
                else {
                    mergeInfos(responseDTO, total);
                }
                taskRowDTOs.addAll(responseDTO.getTaskRowDTOs());
                
                mergeInfoDTOs.addAll(responseDTO.getMergeInfoDTOs());
                
                total += responseDTO.getTaskRowDTOs().size();
                
                result.setHasMergeColumns(responseDTO.getHasMergeColumns());
                
                if (desired.isUseCache()) {
                    saveToCache(taskRowDTOs, mergeInfoDTOs, cacheIds);
                }
            }
    }
    
    /**
     * 保存到mongoDb当作缓存
     * @param taskRowDTOs   保存后会被清除
     * @param mergeInfoDTOs 保存后会被清除
     * @param cacheIds      返回保存的id
     */
    private void saveToCache(List<TaskRowDTO> taskRowDTOs, List<MergeInfoDTO> mergeInfoDTOs, List cacheIds) {
      
        AbnormalContext context = new AbnormalContext();
        context.setMergeInfoDTOs(mergeInfoDTOs);
        context.setTaskRowDTOs(taskRowDTOs);
        String json = JSON.toJSONString(context);
        Object id = fileRepository.saveInexistenceFileInGridFs(new ByteArrayInputStream(json.getBytes()), UUID.randomUUID().toString());
        // 保存成功则清除
        if (id != null) {
            mergeInfoDTOs.clear();
            taskRowDTOs.clear();
        }
        cacheIds.add(id);
        logger.info(" >> 从回调接口获取数据暂存至mongodb成功，文件id为:{}", id.toString());
    }
    
    
    private void mergeInfos(TaskCallBackResponseDTO responseDTO, int total) {
        int baseRow = total;
        for (MergeInfoDTO mergeInfoDTO : responseDTO.getMergeInfoDTOs()) {
            Integer fromRows = mergeInfoDTO.getMergeFromRow();
            Integer toRows = mergeInfoDTO.getMegeToRow();
            fromRows += baseRow;
            toRows += baseRow;
            mergeInfoDTO.setMergeFromRow(fromRows);
            mergeInfoDTO.setMegeToRow(toRows);
        }
    }
    
    /**
     * 构建request
     * @param dataExporterTaskDO
     * @return
     */
    private static TaskCallBackRequestDTO buildCallBackRequest(DataExporterTaskDO dataExporterTaskDO, int index, int limit) {
        TaskCallBackRequestDTO requestDTO = new TaskCallBackRequestDTO();
        String params = dataExporterTaskDO.getParams();
        Map<String, String> paramsMap = CommonUtil.dictToMap(params, ":", ";");
        requestDTO.setParamMap(paramsMap);
        requestDTO.setLimit(limit);
        requestDTO.setStart(index);
        requestDTO.setUserAccount(dataExporterTaskDO.getUserAccount());
        requestDTO.setTaskType(dataExporterTaskDO.getTaskType());
        return requestDTO;
    }
    
    public int getOrder() {
        return SECOND;
    }
}
