package com.wufumall.dataexporter.service.impl;
import com.wufumall.dataexporter.dao.DataExporterTemplateDOMapper;
import com.wufumall.dataexporter.entity.DataExporterTemplateDO;
import com.wufumall.dataexporter.exception.ManagerException;
import com.wufumall.dataexporter.service.DataExporterTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DataExporterTemplateServiceImpl  implements DataExporterTemplateService {
  
  private static final Logger logger = LoggerFactory.getLogger(DataExporterTemplateServiceImpl.class);
  @Autowired
  private DataExporterTemplateDOMapper dataExporterTemplateDOMapper;
  
    @Override
    public int deleteByPrimaryKey(Long id) {
        int index;
        try {
            index = dataExporterTemplateDOMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("删除导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when deleteByPrimaryKey on[DataExporterTemplate]" + e);
        }
        return index;
    }

    @Override
    public int insert(DataExporterTemplateDO record) {
        int index;
        try {
            index = dataExporterTemplateDOMapper.insert(record);
        } catch (Exception e) {
            logger.error("新增导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when insert data on[DataExporterTemplate]" + e);
        }
        return index;
    }

    @Override
    public int insertSelective(DataExporterTemplateDO record) {
        int index;
        try {
            index = dataExporterTemplateDOMapper.insertSelective(record);
        } catch (Exception e) {
            logger.error("新增导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when insertSelective on[DataExporterTemplate]" + e);
        }

        return index;
    }

    @Override
    public DataExporterTemplateDO selectByPrimaryKey(Long id) {
        DataExporterTemplateDO dataExporterTemplateDO;
        try {
            dataExporterTemplateDO = dataExporterTemplateDOMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("查询导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when insert selectByPrimaryKey [DataExporterTemplate]" + e);
        }
        return dataExporterTemplateDO;
    }

    @Override
    public int updateByPrimaryKeySelective(DataExporterTemplateDO record) {
        int index;
        try {
            index = dataExporterTemplateDOMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            logger.error("根据条件更新导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when updateByPrimaryKeySelective data on[DataExporterTemplate]" + e);
        }
        return index;
    }

    @Override
    public int updateByPrimaryKey(DataExporterTemplateDO record) {
        int index;
        try {
            index = dataExporterTemplateDOMapper.updateByPrimaryKey(record);
        } catch (Exception e) {
            logger.error("根据主键更新导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when updateByPrimaryKey data on[DataExporterTemplate]" + e);
        }
        return index;
    }

    
    @Override
    public List<DataExporterTemplateDO> selectByDynamic(DataExporterTemplateDO record) throws ManagerException {
        List<DataExporterTemplateDO> list;
        try {
            Assert.notNull(record, "Query parameter can't be null~");
            list = dataExporterTemplateDOMapper.selectByDynamic(record);
        } catch (Exception e) {
            logger.error("动态查询导出数据模板异常:{}",e);
            throw new ManagerException("Exception occur when selectByDynamic data on[DataExporterTemplate]" + e);
        }
        return list;
    }
}
