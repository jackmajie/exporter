package com.wufumall.dataexporter.leadingin.processor;
import java.io.File;
import java.util.List;

import com.wufumall.dataexporter.entity.FileUpload;
import com.wufumall.dataexporter.exception.ManagerException;

import lombok.Setter;

public abstract class FileProcessor<J extends FileUpload> {
  
      // 能处理的类型
      @Setter
      private List<String> types;
    
      // 下一个人责任人是谁
      @Setter
      private FileProcessor nextHandler;
    
      /**
       * 功能描述: 资源前置判断
       * @author: chenjy
       * @date: 2017年9月11日 下午1:56:28 
       * @param task
       * @return
       * @throws ManagerException
       */
      boolean beforeProcess(J task) throws ManagerException {
          String path = task.getPath();
          try {
              File file = new File(path);
              if (file.exists()) return true;
          } catch (Exception e) {
              return false;
          }
          return false;
      }
    
      /**
       * 功能描述: 资源释放
       * @author: chenjy
       * @date: 2017年9月11日 下午1:56:45 
       * @param task
       */
      void afterProcess(J task) {
      }
    
      
      /**
       * 功能描述: 处理对外统一方法
       * @author: chenjy
       * @date: 2017年9月11日 下午4:32:12 
       * @param task
       * @return
       * @throws ManagerException
       */
      public boolean process(J task) throws ManagerException {
          try {
              if (beforeProcess(task)) {
                  if (types.contains(task.getType())) {
                      return doProcess(task);
                  } else {
                      if (this.nextHandler != null) {
                          this.nextHandler.process(task);
                      } else {
                          throw new ManagerException();
                      }
                  }
              } else {
                  throw new ManagerException();
              }
          } finally {
              afterProcess(task);
          }
          return false;
      }
      
      
      
      /**
       * 功能描述: 抽象方法，各子类需要自我实现不同的处理逻辑
       * @author: chenjy
       * @date: 2017年9月11日 下午1:57:07 
       * @param task
       * @return
       * @throws ManagerException
       */
      abstract boolean doProcess(J task) throws ManagerException;

}
