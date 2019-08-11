package com.wufumall.dataexporter.config;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;


@Configuration
@EnableTransactionManagement
@MapperScan("com.wufumall.dataexporter.dao")
public class MyBatisConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.maxActive}")
    private String maxActive;
    @Value("${spring.datasource.minIdle}")
    private String minIdle;
    @Value("${spring.datasource.maxWait}")
    private String maxWait;
    @Value("${spring.datasource.initialSize}")
    private String initialSize;
    
    /** 是否自动回收超时连接 */
    @Value("${spring.datasource.removeAbandoned}")
    private Boolean removeAbandoned;
    /**  超时时间(以秒数为单位) */
    @Value("${spring.datasource.removeAbandonedTimeout}")
    private Integer removeAbandonedTimeout;
    /** 打开检查,用异步线程evict进行检查 */
    @Value("${spring.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    /** 获取连接前是否运行validationQuery,true=运行[默认],false=不运行 */
    @Value("${spring.datasource.testOnBorrow}")
    private Boolean testOnBorrow;
    /** 将连接归还连接池前是否运行validationQuery,true=运行,false=不运行[默认] */
    @Value("${spring.datasource.testOnReturn}")
    private Boolean testOnReturn;
    /** 检查连接,应用的SQL语句执行之前运行一次 */
    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;
    /** 回收资源的数量  */
    @Value("${spring.datasource.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;
    /** 资源最小空闲时间   */
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    
    
    /**
     * 可以通过这个类,详细配置mybatis
     * @return configuration
     */
    @Bean
    public org.apache.ibatis.session.Configuration mybatisSetting(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setLogPrefix("dao.");
        return configuration;
    }
    
    

    @Bean(destroyMethod = "close")
    @Primary
    public DataSource dataSource(){
        DataSource datasource = new DataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setMaxActive(Integer.parseInt(maxActive));
        datasource.setMinIdle(Integer.parseInt(minIdle));
        datasource.setMaxWait(Integer.parseInt(maxWait));
        datasource.setInitialSize(Integer.parseInt(initialSize));
        
        datasource.setRemoveAbandoned(removeAbandoned);
        datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setValidationQuery("select 1");
        datasource.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return datasource;
    }

    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setFailFast(true);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/*Mapper.xml"));
        return sessionFactory.getObject();
    }

}
