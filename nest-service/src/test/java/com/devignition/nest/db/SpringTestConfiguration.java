package com.devignition.nest.db;

import com.devignition.nest.NestConfiguration;
import com.devignition.nest.core.NestThermostat;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.validation.valuehandling.OptionalValidatedValueUnwrapper;
import org.hibernate.SessionFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class SpringTestConfiguration {

    @Bean
    public NestConfiguration nestConfiguration() {

        Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addValidatedValueHandler(new OptionalValidatedValueUnwrapper())
                .buildValidatorFactory()
                .getValidator();

        ObjectMapper objectMapper = Jackson.newObjectMapper();

        ConfigurationFactory<NestConfiguration> factory =
                new ConfigurationFactory<>(NestConfiguration.class, validator, objectMapper, "dw");

        try {
            return factory.build(new File("config.yml"));
        } catch (IOException | ConfigurationException e) {
            throw new BeanCreationException("Failed to create configuration", e);
        }
    }

    @Bean
    public DataSource dataSource() {
        DataSourceFactory dataSource = nestConfiguration().getDataSourceFactory();
        return new SingleConnectionDataSource(dataSource.getUrl(), dataSource.getUser(), "DevIg$2016", true);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    private Properties getHibernateProperties() {
        Properties props = new Properties();
        props.putAll(nestConfiguration().getDataSourceFactory().getProperties());
        return props;
    }

    @Bean
    public SessionFactory sessionFactory() {
        String entityPackage = NestThermostat.class.getPackage().getName();

        return new LocalSessionFactoryBuilder(dataSource())
                .scanPackages(entityPackage)
                .addProperties(getHibernateProperties())
                .buildSessionFactory();
    }
}
