package com.wufumall.dataexporter.api.request;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(callSuper = false)
public class TaskRequest extends BaseRequest{

  private static final long serialVersionUID = -7847662372962261171L;
  
  
  /**
   * primary key
   */
  private Long id;

  /**
   * 创建任务的用户userId
   */
  @NotNull(message="userAccount不能为空")
  private String userAccount;

  /**
   * 任务类型
   */
  @NotBlank(message="应用任务类型不能为空")
  private String taskType;

  /**
   * 任务状态
   */
  private String status;
  
  
  /**
   * 版本号
   */
  private Integer version;
  
  
  /**
   * 关联的导出配置基本信息的id
   */
  private Long confId;
  
  

  /**
   * 当前调用的程序名称：比如pay使用则写：wf-pay
   */
  @NotBlank(message="应用名称不能为空")
  private String app;

  /**
   * 任务标题
   */
  private String title;
  
 /**
   * TFS文件url
   */
  private String fileUrl;
  
  /**
   * 扩展信息 :回调参数格式如下
   * k1:v1;k2:v2;k3:v3
   */
  @NotBlank(message="查询条件不能为空")
  private String params;

}
