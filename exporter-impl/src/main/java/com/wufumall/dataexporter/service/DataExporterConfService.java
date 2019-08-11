package com.wufumall.dataexporter.service;

import java.util.List;

import com.wufumall.dataexporter.entity.DataExporterConfDO;
import com.wufumall.dataexporter.exception.ManagerException;

public interface DataExporterConfService {

    int deleteByPrimaryKey(Long id);

    int insert(DataExporterConfDO record);

    int insertSelective(DataExporterConfDO record);

    DataExporterConfDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataExporterConfDO record);

    int updateByPrimaryKey(DataExporterConfDO record);

    List<DataExporterConfDO> selectByDynamic(DataExporterConfDO record) throws ManagerException;
}
