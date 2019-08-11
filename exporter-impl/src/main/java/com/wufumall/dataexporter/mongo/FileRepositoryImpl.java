package com.wufumall.dataexporter.mongo;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Title FileRepository
 * @Description 文件存储
 */
@Repository("fileRepository")
public class FileRepositoryImpl implements FileRepository {
  
    private static final Logger logger = LoggerFactory.getLogger(FileRepositoryImpl.class);

    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * @param file
     * @return
     * @Title saveExistFileInGridFs
     * @Description 保存已存在的文件
     */
    public Object saveExistFileInGridFs(File file, String specifyName) {
        InputStream inStr = null;
        try {
            inStr = new FileInputStream(file);
            GridFSFile fs = gridFsTemplate.store(inStr, specifyName);
            return fs.getId();
        } catch (Exception e) {
            logger.error("saveExistFile failed, reason:", e);
            return null;
        } finally {
            if (inStr != null) {
                try {
                    inStr.close();
                } catch (IOException e) {
                    logger.error(">> What the fuck~ the fileInputStream can't close~ What happened?");
                }
            }
        }
    }

    /**
     * @param file
     * @return
     * @Title saveExistFileInGridFs
     * @Description 保存已存在的文件
     */
    public Object saveExistFileInGridFs(File file) {
        if (file == null) {
            logger.error(" >> Input file is null~");
            return null;
        }
        return saveExistFileInGridFs(file, file.getName());
    }

    /**
     * @param inStr    文件内容
     * @param fileName 文件对象
     * @return
     * @Title saveInexistenceFileInGridFs
     * @Description 保存未存在的文件
     */
    public Object saveInexistenceFileInGridFs(InputStream inStr, String fileName) {
        try {
        	GridFSFile fs = gridFsTemplate.store(inStr, fileName);
            logger.debug(" >> Save file named {} success~", fileName);
            return fs.getId();
        } catch (Exception e) {
            logger.error("saveInexistenceFile failed, reason:", e);
            return null;
        }
    }

    /**
     * @param fileName 文件对象
     * @return
     * @Title findFilesInGridFs
     * @Description 通过文件名获取文件列表对象
     */
    public List<GridFSDBFile> findFilesInGridFs(String fileName) {
        Query query = new Query(GridFsCriteria.whereFilename().is(fileName));
        List<GridFSDBFile> result = gridFsTemplate.find(query);
        return result;
    }

    /**
     * @param fileName 文件对象
     * @return
     * @Title findOneFilesInGridFs
     * @Description 通过文件名获取文件对象
     */
    public GridFSDBFile findOneFileInGridFs(String fileName) {
        Query query = new Query(GridFsCriteria.whereFilename().is(fileName));
        GridFSDBFile result = gridFsTemplate.findOne(query);
        return result;
    }

    /**
     * @param fileName 文件对象
     * @return
     * @Title deleteFileInGridFs
     * @Description 通过文件名删除mongodb存储的对象
     */
    public boolean deleteFileInGridFs(String fileName) {
        try {
            Query query = new Query(GridFsCriteria.whereFilename().is(fileName));
            gridFsTemplate.delete(query);
            logger.debug(" >> Delete file named {} success~", fileName);

            return true;
        } catch (Exception e) {
            logger.error("deleteFileInGridFs failed, reason:", e);
            return false;
        }
    }
    
	
	/**
	 * @Title findOneFileInGridFsById
	 * @Description 通过objectId获取文件对象
	 * @param id 文件对象id
	 * @return
	 */
	public GridFSDBFile findOneFileInGridFsById(String id) {
		Query query = new Query(GridFsCriteria.where("_id").is(id));
		GridFSDBFile result = gridFsTemplate.findOne(query);
		return result;
	}
	
	
	/**
	  * @Title deleteFileInGridFsById
	  * @Description 通过objectId删除mongodb存储的对象
	  * @param id 文件对象id
	  * @return
	 */
	public boolean deleteFileInGridFsById(String id) {
		try{
			Query query = new Query(GridFsCriteria.where("_id").is(id));
			gridFsTemplate.delete(query);
			logger.debug(" >> Delete file Id {} success~", id);
			return true;
		}catch(Exception e){
			logger.error("deleteFileInGridFsById failed, reason:",e);
			return false;
		}
	}

}
