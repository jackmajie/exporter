package com.wufumall.dataexporter.template;
import java.util.HashMap;
import java.util.Map;
import com.wufumall.dataexporter.api.domain.TaskCallBackResponseDTO;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;


public class TaskRunContext {

    public DataExporterTaskDO dataExporterTaskDO;

    public TaskCallBackResponseDTO taskCallBackResponseDTO;

    public byte[] dataBytes;

    public String fileUrl;

    private Map<String, Object> params = new HashMap<String, Object>();

    public void addParams(String key, Object obj) {
        params.put(key, obj);
    }

    public Object getParams(String key) {
        return params.get(key);
    }

}
