package com.wufumall.dataexporter.service.impl;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.wufumall.dataexporter.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService{



  @Override
  public HashMap<String, Object> saveUploadFile(MultipartFile file) {
    return null;
  }

}
