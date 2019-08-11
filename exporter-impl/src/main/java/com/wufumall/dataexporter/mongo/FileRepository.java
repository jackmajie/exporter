package com.wufumall.dataexporter.mongo;

import com.mongodb.gridfs.GridFSDBFile;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface FileRepository {

	/**
	 * @param file
	 * @return
	 * @Title saveExistFileInGridFs
	 * @Description 保存已存在的文件
	 */
	public Object saveExistFileInGridFs(File file);

	/**
	 * @param inStr
	 *            文件内容
	 * @param fileName
	 *            文件对象
	 * @return
	 * @Title saveInexistenceFileInGridFs
	 * @Description 保存未存在的文件
	 */
	public Object saveInexistenceFileInGridFs(InputStream inStr,
			String fileName);

	/**
	 * @param fileName
	 *            文件对象
	 * @return
	 * @Title findFilesInGridFs
	 * @Description 通过文件名获取文件列表对象
	 */
	public List<GridFSDBFile> findFilesInGridFs(String fileName);

	/**
	 * @param fileName
	 *            文件对象
	 * @return
	 * @Title findOneFilesInGridFs
	 * @Description 通过文件名获取文件对象
	 */
	public GridFSDBFile findOneFileInGridFs(String fileName);

	/**
	 * @param fileName
	 *            文件对象
	 * @return
	 * @author kdl
	 * @Title deleteFileInGridFs
	 * @Description 通过文件名删除mongodb存储的对象
	 */
	public boolean deleteFileInGridFs(String fileName);

	/**
	  * @Title saveExistFileInGridFs
	  * @Description 保存已存在的文件
	  * @param file
	  * @return
	 */
	public Object saveExistFileInGridFs(File file, String specifyName);

	/**
	 * @author kdl
	 * @Title findOneFileInGridFsById
	 * @Description 通过objectId获取文件对象
	 * @param 文件对象id
	 * @return
	 */
	public GridFSDBFile findOneFileInGridFsById(String id);
	
	/**
	  * @Title deleteFileInGridFsById
	  * @Description 通过objectId删除mongodb存储的对象
	  * @param id 文件对象id
	  * @return
	 */
	public boolean deleteFileInGridFsById(String id);
}
