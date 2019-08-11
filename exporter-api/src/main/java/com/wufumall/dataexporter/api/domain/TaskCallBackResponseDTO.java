package com.wufumall.dataexporter.api.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 类解释：任务响应传输实体
 */
@ToString
public class TaskCallBackResponseDTO extends BaseDTO {

  
  
    private static final long serialVersionUID = -310354041385545637L;

    @Getter
    @Setter
    private Boolean hasMergeColumns = false;

    /**
     * 合并的元素集合
     */
    @Setter
    @Getter
    private List<MergeInfoDTO> mergeInfoDTOs = new ArrayList<MergeInfoDTO>();

    /**
     * 任务行的传输元素
     */
    @Getter
    @Setter
    private List<TaskRowDTO> taskRowDTOs;

    
    
    public void addMergeInfo(Integer mergeFromRow, Integer megeToRow, Integer fromMergeColumns, Integer toMergeColumns) {
        MergeInfoDTO mergeInfoDTO = new MergeInfoDTO(mergeFromRow, megeToRow, fromMergeColumns, toMergeColumns);
        this.mergeInfoDTOs.add(mergeInfoDTO);
    }

    
    

    /**
     * 是否使用缓存
     */
    @Setter
    @Getter
    private boolean useCache;

    
    /**
     * 使用缓存的所有文件ID
     * 必须useCache为true的时候这list才有效
     */
    @Setter
    @Getter
    private List objectIds;
}
