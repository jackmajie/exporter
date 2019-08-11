package com.wufumall.dataexporter.web;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.wufumall.core.ResBodyData;
import com.wufumall.dataexporter.handler.impl.FileUploadHandlerImpl;
import com.wufumall.dataexporter.service.FileUploadService;

@Controller
@RequestMapping("/exporter")
public class LeadinginController {
  
  @Autowired
  private FileUploadService fileUploadService;
  
  @Resource(name = "fileUploadHandler")
  private FileUploadHandlerImpl fileUploadHandler;
  
 
  /**
   * 功能描述: 上传文件
   * @author: chenjy
   * @date: 2017年9月11日 下午4:32:34 
   * @param file
   * @return
   */
  @RequestMapping(value = "/leadingin/upload", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
  @ResponseBody
  public ResBodyData upload(@RequestParam(value = "file", required = true) MultipartFile file) {
      ResBodyData responseText = new ResBodyData();
      if (!file.isEmpty()) {
          responseText.setData(fileUploadService.saveUploadFile(file));
          fileUploadHandler.asyncScanAndHandle();
      }
      return responseText;
  }


}
