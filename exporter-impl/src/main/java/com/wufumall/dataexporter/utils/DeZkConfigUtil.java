package com.wufumall.dataexporter.utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import com.thoughtworks.xstream.XStream;
import com.wufumall.dataexporter.api.domain.TaskRowsConf;
import com.wufumall.dataexporter.core.TaskDubboProviderCallBackBean;


/**
 * 只针对dataexporter这种形式的zk配置进行解释
 */
public class DeZkConfigUtil {

    private static Pattern linePattern = Pattern.compile("( |\\t)*\\w+( |\\t)*=(\\w|\\.|:| |\\t|~|-|,)+(\\r\\n|\\n|$)");

    
    
    /**
     * 解析配置
     * @param taskInfo
     * @return
     */
    public static List<TaskDubboProviderCallBackBean> parseDubboTaskCallBackInfo(String taskInfo) {
        XStream xstream = new XStream();
        xstream.alias("taskServices", List.class);
        xstream.alias("taskService", TaskDubboProviderCallBackBean.class);
        Object taskServiceList = xstream.fromXML(taskInfo);
        List<TaskDubboProviderCallBackBean> taskServices = ((List<TaskDubboProviderCallBackBean>) taskServiceList);
        return taskServices;
    }
    
    
    public static List<TaskRowsConf> parseTaskRowsConf(String taskInfo) {
        XStream xStream = new XStream();
        xStream.alias("taskRowsConfs", List.class);
        xStream.alias("taskRowsConf", TaskRowsConf.class);
        Object taskRowsConfList = xStream.fromXML(taskInfo);
        List<TaskRowsConf> taskRowsConfs = ((List<TaskRowsConf>) taskRowsConfList);
        return taskRowsConfs;
    }

    /**
     * 用于配置信息分析
     * key=value形式，一行一个key=value。
     * 注意key只能由数字、字母以及'_'（下划线）组成（跟java的变量名一样）
     * 示例：
     * limit=1000
     */
    public static Map<String, String> compile(String config) {
        Map<String, String> properties = new HashMap<String, String>();
        if (!StringUtils.isEmpty(config)) {
            Matcher matcher = linePattern.matcher(config);
            while (matcher.find()) {
                String line = matcher.group();
                String[] lineParts = line.split("=");
                if (lineParts.length == 2) {
                    properties.put(lineParts[0].trim(), lineParts[1].trim());
                }
            }
        }
        return properties;
    }

}
