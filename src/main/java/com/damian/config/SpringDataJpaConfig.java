package com.damian.config;


import com.damian.hibernateInterceptor.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.damian.config")
@PropertySource(value = {"classpath:application_${spring.profiles.active}.properties"})


@EnableJpaRepositories(basePackages = "com.damian")
@EnableTransactionManagement
public class SpringDataJpaConfig {

    @Autowired
    Environment environment;
    @Autowired
    CustomInterceptor customInterceptor ;


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));


        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        Properties props = new Properties();
        props.put("hibernate.ejb.interceptor", customInterceptor);
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        
        factory.setJpaProperties(props);
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.damian.domain");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;

    }

    
}

