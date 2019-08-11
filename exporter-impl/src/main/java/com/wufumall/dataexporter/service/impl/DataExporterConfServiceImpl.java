package com.wufumall.dataexporter.service.impl;
import com.wufumall.dataexporter.dao.DataExporterConfDOMapper;
import com.wufumall.dataexporter.entity.DataExporterConfDO;
import com.wufumall.dataexporter.exception.ManagerException;
import com.wufumall.dataexporter.service.DataExporterConfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class DataExporterConfServiceImpl  implements DataExporterConfService {
    
  
    private static final Logger logger = LoggerFactory.getLogger(DataExporterConfServiceImpl.class);
    @Autowired
    private DataExporterConfDOMapper  dataExporterConfDOMapper;
  
  
    @Override
    public int deleteByPrimaryKey(Long id) {
        int index;
        try {
            index = dataExporterConfDOMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("根据主键删除数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when deleteByPrimaryKey on[DataExporterConf]" + e);
        }
        return index;
    }
  
    @Override
    public int insert(DataExporterConfDO record) {
        int index;
        try {
            index = dataExporterConfDOMapper.insert(record);
        } catch (Exception e) {
            logger.error("增加数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when insert data on[DataExporterConf]" + e);
        }
  
        return index;
    }

    @Override
    public int insertSelective(DataExporterConfDO record) {
        int index;
        try {
            index = dataExporterConfDOMapper.insertSelective(record);
        } catch (Exception e) {
            logger.error("动态增加数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when insertSelective on[DataExporterConf]" + e);
        }
        return index;
    }

    @Override
    public DataExporterConfDO selectByPrimaryKey(Long id) {
        DataExporterConfDO DataExporterConfDO;
        try {
            DataExporterConfDO = dataExporterConfDOMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            logger.error("根据主键查询数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when insert selectByPrimaryKey [DataExporterConf]" + e);
        }

        return DataExporterConfDO;
    }

    @Override
    public int updateByPrimaryKeySelective(DataExporterConfDO record) {
        int index;
        try {
            index = dataExporterConfDOMapper.updateByPrimaryKeySelective(record);
        } catch (Exception e) {
            logger.error("更新数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when updateByPrimaryKeySelective data on[DataExporterConf]" + e);
        }

        return index;
    }

 

    @Override
    public int updateByPrimaryKey(DataExporterConfDO record) {
        int index;
        try {
            index = dataExporterConfDOMapper.updateByPrimaryKey(record);
        } catch (Exception e) {
            logger.error("根据主键更新数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when updateByPrimaryKey data on[DataExporterConf]" + e);
        }

        return index;
    }

    @Override
    public List<DataExporterConfDO> selectByDynamic(DataExporterConfDO record) throws ManagerException {
        List<DataExporterConfDO> list;
        try {
            Assert.notNull(record, "Query parameter can't be null~");
            list = dataExporterConfDOMapper.selectByDynamic(record);
        } catch (Exception e) {
            logger.error("动态查询数据导出模板配置报错:{}",e);
            throw new ManagerException("Exception occur when selectByDynamic data on[DataExporterConfDO]" + e);
        }
        return list;
    }
}
