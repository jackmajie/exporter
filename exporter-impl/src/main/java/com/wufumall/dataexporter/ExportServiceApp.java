package com.wufumall.dataexporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@ComponentScan("com.wufumall.dataexporter")
public class ExportServiceApp{
		
	public static void main(String[] args) {
		SpringApplication.run(ExportServiceApp.class, args);	
	}
	
}
