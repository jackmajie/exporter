package com.wufumall.dataexporter.api.request;
import java.io.Serializable;
import com.wufumall.dataexporter.api.facade.ApiParamCheckUtil;

public class BaseRequest implements Serializable{
  
  private static final long serialVersionUID = 3172829670216230729L;

  public String check(){
    return ApiParamCheckUtil.checkParam(this);
  }
  
}
