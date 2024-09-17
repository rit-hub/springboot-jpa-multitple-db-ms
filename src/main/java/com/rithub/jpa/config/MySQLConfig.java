package com.rithub.jpa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.rithub.jpa.mysql.repository", // Repository package for MySQL
    entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlTransactionManager"
)
public class MySQLConfig {

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/jooq_practice");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);  // Set to true if you want SQL queries to be logged
        vendorAdapter.setGenerateDdl(true);  // Set to true to generate DDL automatically
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");  // Set Hibernate dialect for MySQL
        return vendorAdapter;
    }

    @Bean(name = "mysqlEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, new HashMap<>(), null);  // Pass an empty HashMap instead of null
    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
        @Qualifier("mysqlEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
        @Qualifier("mysqlDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.rithub.jpa.mysql.entity") // Entity package for MySQL
            .persistenceUnit("mysql")
            .build();
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
        @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory mysqlEntityManagerFactory) {
        return new JpaTransactionManager(mysqlEntityManagerFactory);
    }
}
