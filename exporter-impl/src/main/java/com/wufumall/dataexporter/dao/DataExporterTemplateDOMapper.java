package com.wufumall.dataexporter.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.wufumall.dataexporter.entity.DataExporterTemplateDO;


@Repository
public interface DataExporterTemplateDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DataExporterTemplateDO record);

    int insertSelective(DataExporterTemplateDO record);

    DataExporterTemplateDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DataExporterTemplateDO record);

    int updateByPrimaryKey(DataExporterTemplateDO record);

    List<DataExporterTemplateDO> selectByDynamic(DataExporterTemplateDO record);
}