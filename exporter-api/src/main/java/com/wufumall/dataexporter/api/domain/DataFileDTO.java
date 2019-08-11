package com.wufumall.dataexporter.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataFileDTO extends BaseDTO {
    private static final long serialVersionUID = -1287838977540729123L;
    /**
     * 列名称
     */
    private String columnName;
    /**
     * 列内容
     */
    private Object columnValue;

    public DataFileDTO() {
    }

    public DataFileDTO(String columnName, Object columnValue) {
        this.columnName = columnName;
        this.columnValue = columnValue;
    }
}
