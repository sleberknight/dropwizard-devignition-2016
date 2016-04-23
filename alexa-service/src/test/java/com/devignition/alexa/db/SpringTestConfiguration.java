package com.devignition.alexa.db;

import com.devignition.alexa.AlexaConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.ImmutableListContainerFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.hibernate.validator.HibernateValidator;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.logging.SLF4JLog;
import org.skife.jdbi.v2.spring.DBIFactoryBean;
import org.skife.jdbi.v2.tweak.SQLLog;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;

@Configuration
public class SpringTestConfiguration {

    private SLF4JLog slf4jLog = new SLF4JLog();

    @Bean
    public AlexaConfiguration devIgnitionConfiguration() {

        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
                .buildValidatorFactory()
                .getValidator();

        ObjectMapper objectMapper = Jackson.newObjectMapper();

        ConfigurationFactory<AlexaConfiguration> factory =
                new ConfigurationFactory<>(AlexaConfiguration.class, validator, objectMapper, "dw");

        try {
            return factory.build(new File("config.yml"));
        } catch (IOException | ConfigurationException e) {
            throw new BeanCreationException("Failed to create configuration", e);
        }
    }

    @Bean
    public DataSource dataSource() {
        DataSourceFactory dataSource = devIgnitionConfiguration().getDataSourceFactory();
        return new SingleConnectionDataSource(dataSource.getUrl(), dataSource.getUser(), "DevIg$2016", true);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DBIFactoryBean dbi() throws Exception {
        DBIFactoryBean dbiFactory = new DBIFactoryBean();
        dbiFactory.setDataSource(new TransactionAwareDataSourceProxy(dataSource()));
        return dbiFactory;
    }

    @Bean
    public SQLLog sqlLog() {
        return slf4jLog;
    }

    @Bean
    public NestDao nestDao() throws Exception {
        DBI dbi = (DBI) dbi().getObject();

        dbi.setSQLLog(sqlLog());

        // Enable various JDBI features
        // See io.dropwizard.jdbi.DBIFactory#build methods
        dbi.registerContainerFactory(new OptionalContainerFactory());
        dbi.registerContainerFactory(new ImmutableListContainerFactory());

        return dbi.onDemand(NestDao.class);
    }

}
