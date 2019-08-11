package com.wufumall.dataexporter.task;




/**
 * 类解释：任务收集器
 */
public interface TaskCollection {

    /**
     * 功能描述: 可以实现任务分机分发任务
     * @author: chenjy
     * @date: 2017年7月12日 下午3:28:58 
     * @param var1
     * @param var2
     * @return
     * @throws Exception
     */
    <T> T dispatch(int shardingItem,int shardingCount) throws Exception;
}
