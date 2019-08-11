package com.wufumall.dataexporter.constants;

public class LeadinginConstants {
  
  /** 已上传未处理 */
  public static final Byte  FILE_UPLOADED = 0;
  /** 1：处理中 */
  public static final Byte FILE_PROCESSING= 1;
  /** 2：处理完毕 */
  public static final Byte FILE_PROCESSED= 2;
  /** 3：业务上处理完毕（额外增加（不符合单纯文件）） */
  public static final Byte FILE_BIZ_PROCESSED= 3;
  /** -1：处理失败 */
  public static final Byte FILE_PROCESSED_FAIL= -1;
  /** -2：记录删除（文件也删除） */
  public static final Byte FILE_DELETEED= -2;

}
