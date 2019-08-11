package com.wufumall.dataexporter.task;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.service.ExporterTaskService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 捞取任务
 */
@Component
public class RunTaskCollection implements TaskCollection {

    private Logger logger = LoggerFactory.getLogger(RunTaskCollection.class);

    /**
     * 默认每次提取数据任务最大值
     */
    @Setter
    private Integer taskMaxSize = 10;

    @Autowired
    private ExporterTaskService exporterTaskService;

    public List<DataExporterTaskDO> dispatch(int shardingItem,int shardingCount) throws Exception {

        List<DataExporterTaskDO> tasks = Collections.emptyList();

        try {
            // 1、获取正在执行任务
            List<DataExporterTaskDO> runningTasks = exporterTaskService.selectRunningTasks(taskMaxSize,shardingItem,shardingCount);
            int left = taskMaxSize - runningTasks.size() > 0 ? taskMaxSize - runningTasks.size() : 0;

            // 2、获取任务
            if (left > 0) {
                tasks = exporterTaskService.selectEffectTasks(left,shardingItem,shardingCount);
            }
            logger.info(" >>  当前正在执行的任务数量为:{},可以接受新增任务数量:{},准备执行任务数量:{}", runningTasks.size(), left,tasks.size());
        } catch (Exception e) {
            logger.error("error@obtainDataTasksError, e={}", new Object[]{e});
        }

        return tasks;
    }
}
