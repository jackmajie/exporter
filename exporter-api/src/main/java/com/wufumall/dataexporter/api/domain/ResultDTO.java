package com.wufumall.dataexporter.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class ResultDTO<T> extends BaseDTO {

    private static final long serialVersionUID = 445888198851737582L;

    /**
     * 是否成功
     */
    private boolean isSuccess = true;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息描述
     */
    private String errorMSG;

    /**
     * 具体的数据
     */
    private T data;


}
