package com.wufumall.dataexporter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.grid-fs-database}")
    private String database;
    
    @Value("${mongodb.username}")
    private String username;
    
    @Value("${mongodb.password}")
    private String password;
    
    @Value("${mongodb.host}")
    private String host;
    
    @Value("${mongodb.port}")
    private int port;
    
    @Value("${mongodb.connectionsPerHost}")
    private int connectionsPerHost;
    
    @Value("${mongodb.threads-allowed-to-block-for-connection-multiplier}")
    private int threadsAllowedToBlockForConnectionMultiplier;
    
    @Value("${mongodb.connect-timeout}")
    private int connectTimeout;
    
    @Value("${mongodb.max-wait-time}")
    private int maxWaitTime;

    
    @Value("${mongodb.socket-keep-alive}")
    private boolean socketKeepAlive;
    
    
    @Value("${mongodb.socket-timeout}")
    private int socketTimeout;
    
    
    @Value("${mongodb.write-number}")
    private int writeNumber;
    
    @Value("${mongodb.write-timeout}")
    private int writeTimeout;
    
    
    @Override
    protected String getDatabaseName() {
        return database;
    }

  
    @Override
    public Mongo mongo() throws Exception {
      MongoClientOptions mongoClientOptions= MongoClientOptions.builder().connectionsPerHost(connectionsPerHost)
        .threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(threadsAllowedToBlockForConnectionMultiplier))
        .connectTimeout(connectTimeout).maxWaitTime(maxWaitTime).socketKeepAlive(socketKeepAlive)
        .socketTimeout(socketTimeout).writeConcern(WriteConcern.W1) //写入日志文件确认成功
        //.readPreference(ReadPreference.secondaryPreferred())//可以在从节点读操作
        .build();
        return new MongoClient(new ServerAddress(host,port) ,
            Lists.newArrayList(MongoCredential.createCredential(username, database, password.toCharArray())),
            mongoClientOptions);
    }

    @Bean("gridFsTemplate")
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}
