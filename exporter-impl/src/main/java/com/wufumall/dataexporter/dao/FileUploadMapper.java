package com.wufumall.dataexporter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wufumall.dataexporter.entity.FileUpload;

public interface FileUploadMapper {
  
  int deleteByPrimaryKey(Long id);

  int insert(FileUpload record);

  int insertSelective(FileUpload record);

  FileUpload selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(FileUpload record);

  int updateByPrimaryKey(FileUpload record);
  
  /**
   * 分页获取的记录
   * @return
   * @param paraMap
   */
  List<FileUpload> selectFileUpload(Map paraMap);
  
  /**
    * @Title
    * @Description 批量通过fileId更新状态
    * @param stauts
    * @param fileIds
    * @return
   */
  int updateStatusByIds(@Param("status")byte status, @Param("fileIds")List<Long> fileIds);
  
  /**
    * @Title
    * @Description 通过状态获取文件ID
    * @param stauts
    * @return
   */
  List<Long> selectFileIdByStatus(@Param("statusList")List<Byte> statusList);
  
  /**
    * @Title
    * @Description 插入记录并返回ID
    * @param record
    * @return
   */
  int insertReturnId(FileUpload record);

}
