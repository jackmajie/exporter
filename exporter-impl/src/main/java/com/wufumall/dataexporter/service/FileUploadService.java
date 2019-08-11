package com.wufumall.dataexporter.service;
import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;


public interface FileUploadService {
  
     /**
       * @Title
       * @Description 保存上传文件历史记录
       * @param file
       * @return
      */
      public HashMap<String, Object> saveUploadFile(MultipartFile file);
      

}
