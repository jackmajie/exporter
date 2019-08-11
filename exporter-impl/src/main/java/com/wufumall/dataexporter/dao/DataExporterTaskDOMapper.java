package com.wufumall.dataexporter.dao;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wufumall.dataexporter.entity.DataExporterTaskDO;


@Repository
public interface DataExporterTaskDOMapper {
  
    int deleteByPrimaryKey(Long id);

    int insert(DataExporterTaskDO record);

    int insertSelective(DataExporterTaskDO record);

    DataExporterTaskDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataExporterTaskDO record);

    int updateByPrimaryKey(DataExporterTaskDO record);

    
    /**
     * 动态获取任务列表
     * @param paramMap
     * @return
     */
    public List<DataExporterTaskDO> selectBatchTasksByDynamic(Map<String, Object> paramMap);

    
    /**
     * 动态获取任务列表大小
     * @param paramMap
     * @return
     */
    public Integer selectTasksCountByDynamic(Map<String, Object> paramMap);
}
