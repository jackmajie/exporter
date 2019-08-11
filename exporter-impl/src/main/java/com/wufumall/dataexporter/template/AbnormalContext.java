package com.wufumall.dataexporter.template;
import java.io.Serializable;
import java.util.List;
import com.wufumall.dataexporter.api.domain.MergeInfoDTO;
import com.wufumall.dataexporter.api.domain.TaskRowDTO;
import lombok.Data;

@Data
public class AbnormalContext implements Serializable {
    private static final long serialVersionUID = -252434231341370221L;

    private List<TaskRowDTO> taskRowDTOs;

    private List<MergeInfoDTO> mergeInfoDTOs;
    
}
