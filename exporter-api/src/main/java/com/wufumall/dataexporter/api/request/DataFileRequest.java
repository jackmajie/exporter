package com.wufumall.dataexporter.api.request;

import javax.validation.constraints.NotNull;

public class DataFileRequest extends BaseRequest{

  private static final long serialVersionUID = 6937351079394030725L;
  
  @NotNull(message="任务id不能为空")
  private Long id;
  
  @NotNull(message="用户账号不能为空")
  private String userAccount;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(String userAccount) {
    this.userAccount = userAccount;
  }
  
  
  

}
