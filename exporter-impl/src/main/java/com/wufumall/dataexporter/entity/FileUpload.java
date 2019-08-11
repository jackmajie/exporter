package com.wufumall.dataexporter.entity;

import java.util.Date;

public class FileUpload {
  
  private Long id;

  private String type;

  private String name;

  private String path;

  private String md5;

  private Long length;

  private Byte status;

  private String onwer;

  private Date gmtExpire;

  private Date gmtCreate;

  private Date gmtModify;

  public Long getId() {
      return id;
  }

  public void setId(Long id) {
      this.id = id;
  }

  public String getType() {
      return type;
  }

  public void setType(String type) {
      this.type = type;
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getPath() {
      return path;
  }

  public void setPath(String path) {
      this.path = path;
  }

  public String getMd5() {
      return md5;
  }

  public void setMd5(String md5) {
      this.md5 = md5;
  }

  public Long getLength() {
      return length;
  }

  public void setLength(Long length) {
      this.length = length;
  }

  public Byte getStatus() {
      return status;
  }

  public void setStatus(Byte status) {
      this.status = status;
  }

  public String getOnwer() {
      return onwer;
  }

  public void setOnwer(String onwer) {
      this.onwer = onwer;
  }

  public Date getGmtExpire() {
      return gmtExpire;
  }

  public void setGmtExpire(Date gmtExpire) {
      this.gmtExpire = gmtExpire;
  }

  public Date getGmtCreate() {
      return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
      this.gmtCreate = gmtCreate;
  }

  public Date getGmtModify() {
      return gmtModify;
  }

  public void setGmtModify(Date gmtModify) {
      this.gmtModify = gmtModify;
  }

}
