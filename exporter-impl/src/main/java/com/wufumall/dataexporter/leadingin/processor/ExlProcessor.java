package com.wufumall.dataexporter.leadingin.processor;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wufumall.dataexporter.entity.FileUpload;
import com.wufumall.dataexporter.exception.ManagerException;
import com.wufumall.dataexporter.utils.ExcelImportHelper;



public class ExlProcessor extends FileProcessor<FileUpload>{
  
  private static Logger logger = LoggerFactory.getLogger(ExlProcessor.class);
  
    @Override
    boolean doProcess(FileUpload task) throws ManagerException {
      //检查导入excel格式，此次通过调用业务系统接口来完成格式检查,此处不清晰可以问下陈建宇
      
      
      //全部加载到内存存在一定的风险
      List<List<String>> lists = ExcelImportHelper.instance.getListAll(task.getPath());
      //去掉excel列头
      lists.remove(0);
      logger.info(" >> 读取文件成功，总行数(包含标题)为:{}", lists.size());
      // 行数不能为0
      if (!CollectionUtils.isEmpty(lists)) {
            //批量入库
      }
      return true;
    }
    
    


}
