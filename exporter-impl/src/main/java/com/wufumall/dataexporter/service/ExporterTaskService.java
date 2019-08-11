package com.wufumall.dataexporter.service;
import java.util.List;
import java.util.Map;
import com.github.pagehelper.PageInfo;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.exception.ManagerException;

public interface ExporterTaskService {

    /**
     * @param dataExporterTaskDO
     * @return
     * @throws ManagerException
     */
    public Long insert(DataExporterTaskDO dataExporterTaskDO) throws ManagerException;


    /**
     * Fetch获取有效的任务
     * @return
     */
    public List<DataExporterTaskDO> selectEffectTasks(int size,int shardingItem,int shardingCount);

    /**
     * select by id
     * @param
     * @return
     */
    public DataExporterTaskDO selectTaskById(Long id);

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

    
    
    /**
     * 根据ID进行批量更新
     * @param records
     * @return
     */
    public Integer updateBatchTasksByPrimaryKeys(List<DataExporterTaskDO> records);

    /**
     * 根据某个具体的ID进行更新
     * @param record
     * @return
     */
    public Integer updateTaskByPrimaryKey(DataExporterTaskDO record);

    /**
     * 分页查询
     * @param paramMap 参数集
     * @param start    开始位置，如果paramMap已有"start"条件将被覆盖
     * @param limit    查询条数，如果paramMap已有"limit"条件将被覆盖
     * @return
     */
    public List<DataExporterTaskDO> selectPageTasksByDynamic(Map<String, Object> paramMap, int start, int limit);

    /**
     * 根据时间区间选择任务
     * @param paramMap
     * @return
     */
    public List<DataExporterTaskDO> selectByDateInterval(Map<String, Object> paramMap);

    /**
     * Fetch获取正在执行的任务
     * @return
     */
    public List<DataExporterTaskDO> selectRunningTasks(int size,int shardingItem,int shardingCount);
    /**
     * 停止任务
     * @param id
     * @return
     */
    public boolean stopDataExporterTask(Long id);
    
    /**
      * @Title
      * @Description 分页查询--使用pageHelper
      * @param pageIndex
      * @param pageSize
      * @param paramMap
      * @return
     */
    public PageInfo<DataExporterTaskDO> selectBatchTasksPageByDynamic(Integer pageIndex, Integer pageSize, Map<String, Object> paramMap);
}
