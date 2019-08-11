package com.wufumall.dataexporter.service.impl;
import static com.wufumall.dataexporter.constants.ModelStatus.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wufumall.dataexporter.dao.DataExporterTaskDOMapper;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.exception.ManagerException;
import com.wufumall.dataexporter.service.ExporterTaskService;

@Service
public class ExporterTaskServiceImpl  implements ExporterTaskService {
  
    private static final Logger logger = LoggerFactory.getLogger(ExporterTaskServiceImpl.class);
  
    @Autowired
    private DataExporterTaskDOMapper  dataExporterTaskDOMapper;
  
    
    public Long insert(DataExporterTaskDO dataExporterTaskDO) throws ManagerException {
        Long index;
        try {
            dataExporterTaskDOMapper.insertSelective(dataExporterTaskDO);
            index = dataExporterTaskDO.getId();
        } catch (Exception e) {
            logger.error("插入task任务表失败:{}",e);
            throw new ManagerException("Exception occur when insert data on[" + dataExporterTaskDO.getClass().getSimpleName() +
                    "]\n" +e);
        }
        return index;
    }



    public List<DataExporterTaskDO> selectEffectTasks(int size,int shardingItem,int shardingCount) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(DB_TASK_ENABLE);
        statusList.add(DB_TASK_RETRY);
        int start = 0;
        int limit = size;
        paramMap.put("shardingItem", shardingItem);
        paramMap.put("shardingCount", shardingCount);
        paramMap.put("statusList", statusList);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("orderByField", "gmt_create");
        paramMap.put("orderByType", "desc");
        return this.selectBatchTasksByDynamic(paramMap);
    }

  
    public List<DataExporterTaskDO> selectRunningTasks(int size,int shardingItem,int shardingCount) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<Integer> statusList = new ArrayList<Integer>();
        statusList.add(DB_TASK_RUNNING);
        int start = 0;
        int limit = size;
        
        paramMap.put("shardingItem", shardingItem);
        paramMap.put("shardingCount", shardingCount);
        paramMap.put("statusList", statusList);
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        paramMap.put("orderByField", "gmt_create");
        paramMap.put("orderByType", "desc");
        return this.selectBatchTasksByDynamic(paramMap);
    }

    /**
     * select by id
     * @param
     * @return
     */
    public DataExporterTaskDO selectTaskById(Long id) {
        return dataExporterTaskDOMapper.selectByPrimaryKey(id);
    }

    /**
     * 动态获取任务列表
     * @param paramMap
     * @return
     */
    public List<DataExporterTaskDO> selectBatchTasksByDynamic(Map<String, Object> paramMap) {
        List<DataExporterTaskDO> list = Collections.emptyList();
        try {
          
            list = dataExporterTaskDOMapper.selectBatchTasksByDynamic(paramMap);
        } catch (Exception e) {
            logger.error("动态获取任务列表失败:{}",e);
        }
        return list;
    }

    public Integer selectTasksCountByDynamic(Map<String, Object> paramMap) {
        try {
            return dataExporterTaskDOMapper.selectTasksCountByDynamic(paramMap);
        } catch (Exception e) {
            logger.error("动态获取任务列表大小:{}",e);
        }
        return -1;
    }


    public List<DataExporterTaskDO> selectPageTasksByDynamic(Map<String, Object> paramMap, int start, int limit) {
        List<DataExporterTaskDO> list;
        if (start < 0 || limit < 0) {
            logger.error("The start/limit position can't be negative!");
            return Collections.EMPTY_LIST;
        }
        paramMap.put("start", start);
        paramMap.put("limit", limit);
        list = this.selectBatchTasksByDynamic(paramMap);
        return list;
    }

    public List<DataExporterTaskDO> selectByDateInterval(Map<String, Object> paramMap) {
        return null;
    }

    public boolean stopDataExporterTask(Long id) {
      
        DataExporterTaskDO data = selectTaskById(id);
        Integer status = data.getStatus();
        DataExporterTaskDO query = new DataExporterTaskDO();
        query.setId(id);
        query.setStatus(DB_TASK_DISABLED);
        if (status.equals(DB_TASK_RUNNING)) {
            return false;
        }
        // 完成状态则设置成已失效
        else if (status.equals(DB_TASK_COMPLETED)) {
            int effectSize = updateTaskByPrimaryKey(query);
            if (effectSize <= 0) return false;
            else return false;
        }

        // 失败的删除则设置成失效
        else {
            int effectSize = updateTaskByPrimaryKey(query);
            return effectSize > 0;
        }
    }

    
    /**
     * 更新表
     * @return 小于0:出错，大于或等于0表示生效条数
     */
    public Integer updateTaskByPrimaryKey(DataExporterTaskDO record) {
        return dataExporterTaskDOMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 动态批量更新Task
     * @return
     */
    public Integer updateBatchTasksByPrimaryKeys(List<DataExporterTaskDO> records) {
        Integer size = 0;
        for (DataExporterTaskDO record : records) {
            size += updateTaskByPrimaryKey(record);
        }
        return size;
    }

    
    
  	@Override
  	public PageInfo<DataExporterTaskDO> selectBatchTasksPageByDynamic(
  			Integer pageIndex, Integer pageSize, Map<String, Object> paramMap) {
  	  
    		PageHelper.startPage(pageIndex, pageSize, true);
        paramMap.put("orderByField","gmt_create desc");
        return new PageInfo<DataExporterTaskDO>(selectBatchTasksByDynamic(paramMap));
    		
  	}
}