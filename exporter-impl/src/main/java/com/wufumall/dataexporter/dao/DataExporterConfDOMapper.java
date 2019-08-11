package com.wufumall.dataexporter.dao;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.wufumall.dataexporter.entity.DataExporterConfDO;
import com.wufumall.dataexporter.exception.ManagerException;


@Repository
public interface DataExporterConfDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataExporterConfDO record);

    int insertSelective(DataExporterConfDO record);

    DataExporterConfDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataExporterConfDO record);

    int updateByPrimaryKey(DataExporterConfDO record);

    List<DataExporterConfDO> selectByDynamic(DataExporterConfDO record) throws ManagerException;
}