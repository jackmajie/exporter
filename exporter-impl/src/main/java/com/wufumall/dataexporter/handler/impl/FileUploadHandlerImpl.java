package com.wufumall.dataexporter.handler.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wufumall.core.utils.ThreadPoolManager;
import com.wufumall.dataexporter.constants.LeadinginConstants;
import com.wufumall.dataexporter.dao.FileUploadMapper;
import com.wufumall.dataexporter.entity.FileUpload;
import com.wufumall.dataexporter.exception.ManagerException;
import com.wufumall.dataexporter.handler.FileUploadHandler;
import com.wufumall.dataexporter.leadingin.processor.FileProcessor;



@Component("fileUploadHandler")
public class FileUploadHandlerImpl implements FileUploadHandler{
  
  private static Logger logger = LoggerFactory.getLogger(FileUploadHandlerImpl.class);

  @Autowired
  private FileUploadMapper fileUploadMapper;

  @Resource(name = "exlProcessor")
  private FileProcessor fileProcessor;
  

  
  public void asyncScanAndHandle() {
      ThreadPoolManager.getInstance().addExecuteTask(new Runnable() {
            @Override
            public void run() {
              List<FileUpload> scan = scan();
              List<Long> ids = syncHandleFile(scan);
            }
      });
  }

  
  
  private  List<FileUpload> scan() {
      Map map = new HashMap();
      List listPara = new ArrayList();
      listPara.add(LeadinginConstants.FILE_UPLOADED);
      listPara.add(LeadinginConstants.FILE_PROCESSED_FAIL);
      map.put("statusList", listPara);
      List<FileUpload> list = fileUploadMapper.selectFileUpload(map);
      return list;
  }

  
  
  /**
   * 功能描述: 异步处理已上传的文件
   * @author: chenjy
   * @date: 2017年9月11日 下午4:34:40 
   * @param fileUploads
   * @return
   */
  private List<Long> syncHandleFile(List<FileUpload> fileUploads) {
      if (CollectionUtils.isEmpty(fileUploads)) return Collections.emptyList();

      List<Long> successIds = new ArrayList<Long>();
      Map<String, FileUpload> actualMap = new HashMap<String, FileUpload>();
      Map<String, List<Long>> actualMapIds = new HashMap<String, List<Long>>();
      // 相同的文件只处理一次
      for (FileUpload fileUpload : fileUploads) {
          String key = fileUpload.getMd5() + "_" + fileUpload.getLength();
          actualMap.put(key, fileUpload);
          List<Long> ids = actualMapIds.get(key);
          if (ids == null) {
              ids = new ArrayList<Long>();
              actualMapIds.put(key, ids);
          }
          ids.add(fileUpload.getId());
      }

      for (FileUpload fileUpload : actualMap.values()) {
          boolean result = false;
          try {
              String key = fileUpload.getMd5() + "_" + fileUpload.getLength();
              List<Long> ids = actualMapIds.get(key);
              // 处理前设置状态
              fileUploadMapper.updateStatusByIds(LeadinginConstants.FILE_PROCESSING, ids);
              // 进行处理
              result = fileProcessor.process(fileUpload);
              // 处理后设置状态
              fileUpload.setStatus(result ? LeadinginConstants.FILE_PROCESSED : LeadinginConstants.FILE_PROCESSED_FAIL);
              
              fileUpload.setGmtModify(new Date());
              fileUploadMapper.updateByPrimaryKey(fileUpload);
              successIds.add(fileUpload.getId());
          } catch (ManagerException e) {
              // 处理失败设置状态
              // FIXME (fileUpload属性增加备注)
              fileUpload.setStatus(LeadinginConstants.FILE_PROCESSED_FAIL);
              fileUpload.setGmtModify(new Date());
              fileUploadMapper.updateByPrimaryKey(fileUpload);
              logger.error(ExceptionUtils.getStackTrace(e));
          } catch (Exception e) {
              //其它失败原因
              // 处理失败设置状态
              // FIXME (fileUpload属性增加备注)
              fileUpload.setStatus(LeadinginConstants.FILE_UPLOADED);
              fileUpload.setGmtModify(new Date());
              fileUploadMapper.updateByPrimaryKey(fileUpload);
              logger.error(ExceptionUtils.getStackTrace(e));
          }
          logger.info(">> 处理{}，文件{}", result ? "成功" : "失败", ToStringBuilder.reflectionToString(fileUpload));
      }
      return successIds;
  }

}
