package com.wufumall.dataexporter.core;


/**
 * 回调函数的callback bean
 */
public class TaskDubboProviderCallBackBean {

    /**
     * 版本号*
     */
    private String version;

    /**
     * group*
     */
    private String group;

    /**
     * 超时时间
     */
    private Long timeOut;

    /**
     *应用程序名称
     */
    private String application;

    
    
    
    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public Long getTimeOut() {
      return timeOut;
    }

    public void setTimeOut(Long timeOut) {
      this.timeOut = timeOut;
    }

    public String getApplication() {
      return application;
    }

    public void setApplication(String application) {
      this.application = application;
    }
    
}
