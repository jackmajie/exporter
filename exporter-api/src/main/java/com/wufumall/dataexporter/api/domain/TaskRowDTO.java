package com.wufumall.dataexporter.api.domain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskRowDTO extends BaseDTO {
    private static final long serialVersionUID = 7048542423032500097L;
    private List<DataFileDTO> dataFileDTOList = new ArrayList<DataFileDTO>();

    public void addColumns(String columnsName, Object columnsValue) {
        DataFileDTO dataFileDTO = new DataFileDTO(columnsName, columnsValue);
        dataFileDTOList.add(dataFileDTO);
    }
}
