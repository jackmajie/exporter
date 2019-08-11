package com.wufumall.dataexporter.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskRowsConf {
  private String taskName;
  private Integer rows;
}
