package com.fayayo.job.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据库相关的配置
 * */
@Configuration
public class DataAccessConfiguration {

    @Autowired
    private JpaProperties jpaProperties;
    /**
     * 数据源的配置
     * */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return dataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,@Qualifier("dataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.fayayo.job.entity")
                .properties(getVendorProperties(dataSource))
                .persistenceUnit("primary")
                .build();
    }


    protected Map<String, Object> getVendorProperties(DataSource dataSource) {

        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    //配置事务
    @Bean
    public PlatformTransactionManager platformTransactionManager(@Qualifier("entityManagerFactory")
                                                                             LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory.getObject());

        return transactionManager;
    }



}
