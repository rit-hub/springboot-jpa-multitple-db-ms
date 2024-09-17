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
    basePackages = "com.rithub.jpa.postgres.repository", // Repository package for PostgreSQL
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager"
)
public class PostgresConfig {

    @Bean(name = "postgresDataSource")
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/jooq_postgres");
        dataSource.setUsername("postgres");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter postgresJpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");  // Set PostgreSQL Dialect
        return vendorAdapter;
    }

    @Bean(name = "postgresEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, new HashMap<>(), null);
    }

    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
        @Qualifier("postgresEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
        @Qualifier("postgresDataSource") DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.rithub.jpa.postgres.entity") // Entity package for PostgreSQL
            .persistenceUnit("postgres")
            .build();
    }

    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
        @Qualifier("postgresEntityManagerFactory") EntityManagerFactory postgresEntityManagerFactory) {
        return new JpaTransactionManager(postgresEntityManagerFactory);
    }
}
