package com.wufumall.dataexporter.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.wufumall.dataexporter.BaseTest;


public class ExporterControllerTest extends BaseTest{
  
      @Test
      public void testGetTaskInfoList() throws Exception{
        
           ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/exporterController/task/list")
           .accept(MediaType.APPLICATION_JSON)
           .param("pageIndex","1")
           .param("pageSize","20")); //请求
            result.andDo(new ResultHandler() {
                @Override
                public void handle(MvcResult result) throws Exception {
                    System.out.println(result.getResponse().getContentAsString());
                }
            });
          
      }
      
      
      @Test
      public void testGetTaskInfo() throws Exception{
           ResultActions result = mockMvc.perform(get("/exporterController/task/4"))
          .andExpect(status().isOk()); //请求
          result.andDo(new ResultHandler() {
              @Override
              public void handle(MvcResult result) throws Exception {
                  System.out.println(result.getResponse().getContentAsString());
              }
          });
      }

}
