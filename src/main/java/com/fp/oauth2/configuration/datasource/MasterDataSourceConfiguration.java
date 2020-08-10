package com.fp.oauth2.configuration.datasource;

import com.fp.oauth2.configuration.datasource.domain.master.MobileAppUsers;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//@Configuration
//@EnableJpaRepositories(basePackages = "com.fp.oauth2.configuration.datasource.repository.master",
//        entityManagerFactoryRef = "masterEntityManagerFactory",
//        transactionManagerRef= "masterTransactionManager")
public class MasterDataSourceConfiguration {
//    @Bean
//    @ConfigurationProperties("master.datasource")
//    public DataSourceProperties masterDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @ConfigurationProperties("master.datasource.configuration")
//    public DataSource masterDataSource() {
//        return masterDataSourceProperties().initializeDataSourceBuilder()
//                .type(BasicDataSource.class).build();
//    }
//
//    @Bean(name = "masterEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(masterDataSource())
//                .packages(MobileAppUsers.class)
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager masterTransactionManager(
//            final @Qualifier("masterEntityManagerFactory") LocalContainerEntityManagerFactoryBean cardEntityManagerFactory) {
//        return new JpaTransactionManager(cardEntityManagerFactory.getObject());
//    }
}
