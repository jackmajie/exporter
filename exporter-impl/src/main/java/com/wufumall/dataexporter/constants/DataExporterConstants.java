package com.wufumall.dataexporter.constants;
import java.util.TreeSet;
import lombok.Getter;

public interface DataExporterConstants {

    public interface DATA_TYPE {
        public String EXCEL = "xlsx";
        public String CSV = "csv";
    }

    /**
     * 渴望导出量
     */
    public enum EXPORT_DESIRED {

        /**
         * 少量(1-1000)
         */
        LITTLE(1000,false),
        /**
         * 正常(1001-10000)
         */
        NORMAL(10000,false),
        /**
         * 大量(10001-100000)
         */
        LARGER(100000,false),
        /**
         * 变态(10w以上)
         */
        ABNORMAL(1000000,true);

      
        @Getter
        private int maxSize;
        
        @Getter
        private boolean useCache;
        
        

        EXPORT_DESIRED(int maxSize,boolean useCache) {
            this.maxSize = maxSize;
            this.useCache= useCache;
        }

        public static EXPORT_DESIRED judgeBySize(int size) {

            if (size < 0) return null;
            
            TreeSet<EXPORT_DESIRED> set = new TreeSet<EXPORT_DESIRED>((o1,o2)->{
                  return o1.getMaxSize() - o2.getMaxSize();
            });
            for (EXPORT_DESIRED desired : values()) {
                if (desired.getMaxSize() >= size) set.add(desired);
            }
            return (EXPORT_DESIRED) set.pollFirst();
        }

    }
}
