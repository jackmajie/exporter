package com.wufumall.dataexporter.service;
import java.util.List;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.exception.ManagerException;


public interface DataExporterTemplateService {

    int deleteByPrimaryKey(Long id);

    int insert(DataExporterTemplateDO record);

    int insertSelective(DataExporterTemplateDO record);

    DataExporterTemplateDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataExporterTemplateDO record);

    int updateByPrimaryKey(DataExporterTemplateDO record);

    List<DataExporterTemplateDO> selectByDynamic(DataExporterTemplateDO record) throws ManagerException;
}
