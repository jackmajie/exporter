package com.wufumall.dataexporter.wrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.handler.CallBackHandler;


@Component
public class DataExporterCallBackWrapper {

    @Autowired
    private CallBackHandler dataExporterCallBackHandler;

    /**
     * handler链条*
     */
    private static List<CallBackHandler> dataExportHandlerChain = new ArrayList<CallBackHandler>();

    /**
     * 开始查询数据
     * 这个接口封装一下 里面又流控降级的bean
     *
     * @param dataExporterTaskDO
     * @return
     */
    public TaskCallBackResponseDTO doQueryData(DataExporterTaskDO dataExporterTaskDO) {
        //如果handel鍊条为null 则执行初始化
        if (CollectionUtils.isEmpty(dataExportHandlerChain)) {
            loadDataExportHandlerChain();
        }

        TaskCallBackResponseDTO result = new TaskCallBackResponseDTO();
        for (CallBackHandler callBackHandler : dataExportHandlerChain) {
            callBackHandler.executeQuery(dataExporterTaskDO, result);

        }

        return result;
    }

    public void init() {
        loadDataExportHandlerChain();
    }


    /**
     * 加载文件异步导出处理连路
     */
    public void loadDataExportHandlerChain() {
        dataExportHandlerChain.add(dataExporterCallBackHandler);
        Collections.sort(dataExportHandlerChain, new OrderComparator());
    }
}
