package com.wufumall.dataexporter.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MergeInfoDTO extends BaseDTO {
    private static final long serialVersionUID = 5212010419136744137L;
    private Integer mergeFromRow;
    private Integer megeToRow;
    private Integer fromMergeColumns;
    private Integer toMergeColumns;

    private MergeInfoDTO() {
    }

    public MergeInfoDTO(Integer mergeFromRow, Integer megeToRow, Integer fromMergeColumns, Integer toMergeColumns) {
        this.mergeFromRow = mergeFromRow;
        this.megeToRow = megeToRow;
        this.fromMergeColumns = fromMergeColumns;
        this.toMergeColumns = toMergeColumns;
    }

}
