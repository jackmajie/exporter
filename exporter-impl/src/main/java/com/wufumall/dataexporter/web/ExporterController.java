package com.wufumall.dataexporter.web;
import com.github.pagehelper.PageInfo;
import com.wufumall.core.ResBodyData;
import com.wufumall.dataexporter.api.global.DESResultType;
import com.wufumall.dataexporter.api.global.GlobalStatus;
import com.wufumall.dataexporter.entity.DataExporterTaskDO;
import com.wufumall.dataexporter.mongo.FileRepository;
import com.wufumall.dataexporter.service.ExporterTaskService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/exporter")
public class ExporterController {
    @Autowired
    private FileRepository fileRepository;
    
    @Autowired
    private ExporterTaskService exporterTaskService;
    
    
    /**
     * 功能描述: 根据文件名下载导出的文件
     * @author: chenjy
     * @date:   2017年7月12日 上午10:19:43 
     * @param   fileName
     * @return
     * @throws  IOException
     */
    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> download(@RequestParam(value = "fileName", required = true) String fileName /*   ,HttpServletResponse response. */) throws IOException {

        InputStream inputStream = fileRepository.findOneFileInGridFs(fileName).getInputStream();
        if (inputStream == null) {
            return null;
        }
        byte[] bytes = IOUtils.toByteArray(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(fileName.getBytes(Charset.defaultCharset()),"ISO8859-1"));
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }

    
    /**
     * 功能描述: 根据taskId下载导出文件
     * @author: chenjy
     * @date: 2017年7月12日 上午10:20:19 
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download/{taskId:[\\S]{1,30}}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> download(@PathVariable("taskId") final Long id) throws IOException {
      
        DataExporterTaskDO dataExporterTaskDO = exporterTaskService.selectTaskById(id);
        if (dataExporterTaskDO == null || StringUtils.isEmpty(dataExporterTaskDO.getFileUrl())) {
            return null;
        }
        return download(dataExporterTaskDO.getFileUrl());
    }
    
    
    
    /**
     * 功能描述: 删除文件
     * @author: chenjy
     * @date: 2017年7月12日 上午10:20:55 
     * @param id
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/delFile/{taskId:[\\S]{1,30}}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResBodyData delFile(@PathVariable("taskId") final Long id) throws IOException {
    	 DataExporterTaskDO dataExporterTaskDO = exporterTaskService.selectTaskById(id);
    	 ResBodyData result = new ResBodyData();
       if (dataExporterTaskDO == null || StringUtils.isEmpty(dataExporterTaskDO.getFileUrl())) {
           result.setCode(0);
           result.setMsg("任务不存在或者文件不存在！");
           return result;
       }else{
        	 if (fileRepository.deleteFileInGridFs(dataExporterTaskDO.getFileUrl())){
               result.setCode(DESResultType.SUCCESS.getCode());
               result.setMsg(DESResultType.SUCCESS.getMsg());	
        	 }else{
               result.setCode(DESResultType.ERROR_NO_EXISTS_OR_FAIL.getCode());
               result.setMsg(DESResultType.ERROR_NO_EXISTS_OR_FAIL.getMsg()); 
        	 }
        	 dataExporterTaskDO.setStatus(GlobalStatus.DB_TASK_DISABLED);
        	 exporterTaskService.updateTaskByPrimaryKey(dataExporterTaskDO);
       }
       return result;
    	
    }
    
    
   /**
    * 功能描述: 获取任务信息
    * @author: chenjy
    * @date: 2017年7月12日 上午10:21:24 
    * @param id
    * @return
    * @throws IOException
    */
   @RequestMapping(value = "/task/{taskId:[\\S]{1,30}}", method = RequestMethod.GET)
   @ResponseBody
   public ResBodyData getTaskInfo(@PathVariable("taskId") final Long id) throws IOException {
      ResBodyData result = new ResBodyData();
       DataExporterTaskDO dataExporterTaskDO = exporterTaskService.selectTaskById(id);
      result.setCode(DESResultType.SUCCESS.getCode());
      result.setMsg(DESResultType.SUCCESS.getMsg()); 
     	result.setData(dataExporterTaskDO);
      return result;
   }

   
    /**
     * 功能描述: 获取任务列表
     * @author: chenjy
     * @date: 2017年7月12日 上午10:26:13 
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/task/list", method = RequestMethod.GET)
    @ResponseBody
    public ResBodyData getTaskInfoList(@RequestParam(value = "pageIndex",defaultValue="1") Integer pageIndex,
			@RequestParam(value = "pageSize",defaultValue="20") Integer pageSize) throws IOException {
        ResBodyData result = new ResBodyData();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //获取用户信息
        //paramMap.put("userAccount", user.getUserId());
      	PageInfo<DataExporterTaskDO> info  = exporterTaskService.selectBatchTasksPageByDynamic(pageIndex, pageSize,paramMap);
      	result.setCode(DESResultType.SUCCESS.getCode());
        result.setMsg(DESResultType.SUCCESS.getMsg()); 
      	result.setData(info);
        return result;
    }
    
    
}
