package com.wufumall.dataexporter.utils;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class CommonUtil {
  
    private CommonUtil() {
      
    }

    
    
    /**
     * 将map转成字典格式      K1:V1;K2:V2
     * @param map        实体map
     * @param kvSep      key 与value之前的分隔符
     * @param sectionSep kv对之间的分隔符
     * @return
     */
    public static String mapToDict(Map<?,?> map, String kvSep, String sectionSep) {
        StringBuilder sb = new StringBuilder("");
        if (map != null) {
            int index = 1;
            for (Map.Entry entry : map.entrySet()){
                sb.append(entry.getKey()).append(kvSep).append(entry.getValue());
                if (index < map.size()) {
                    sb.append(sectionSep);
                }
                index++;
            }
        }
        return sb.toString();
    }
    
    
    public static Map<String, String> dictToMap(String target, String kvSep, String sectionSep) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(target)) {
            return map;
        }
        String[] strList = target.split(sectionSep);
        for (String line : strList) {
            if (StringUtils.isBlank(line))
                continue;
            StringBuilder sb = new StringBuilder(line);
            int index = sb.indexOf(kvSep);
            if (index == -1)
                continue;
            String key = sb.substring(0, index);
            String value = sb.substring(index + 1);
            map.put(key, value);
        }
        return map;
    }


}
