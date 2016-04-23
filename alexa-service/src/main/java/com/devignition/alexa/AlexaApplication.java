package com.devignition.alexa;

import com.devignition.alexa.db.NestDao;
import com.devignition.alexa.health.ExternalNestServiceHealthCheck;
import com.devignition.alexa.resources.ExternalNestResource;
import com.devignition.alexa.resources.NestResource;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.client.Client;

public class AlexaApplication extends Application<AlexaConfiguration> {

    // Setting to false lets us define default values in case an environment var isn't present
    private static final boolean STRICT_ENVIRONMENT_VARS = false;

    public static void main(final String[] args) throws Exception {
        new AlexaApplication().run(args);
    }

    @Override
    public String getName() {
        return "Alexa";
    }

    @Override
    public void initialize(final Bootstrap<AlexaConfiguration> bootstrap) {
        enableEnvironmentVariableSubstitution(bootstrap);
        addMigrationsBundle(bootstrap);
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(final AlexaConfiguration configuration,
                    final Environment environment) {

        DBIFactory jdbiFactory = new DBIFactory();
        DBI jdbi = jdbiFactory.build(environment, configuration.getDataSourceFactory(), "database");

        NestDao nestDao = jdbi.onDemand(NestDao.class);
        NestResource nestResource = new NestResource(nestDao);
        environment.jersey().register(nestResource);

        Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build(getName());
        String baseURI = configuration.getNestServiceBaseURI();
        ExternalNestResource externalNestResource = new ExternalNestResource(nestDao, client, baseURI);
        environment.jersey().register(externalNestResource);

        environment.healthChecks().register("Nest Service", new ExternalNestServiceHealthCheck(baseURI, client));
    }

    private void enableEnvironmentVariableSubstitution(Bootstrap<AlexaConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(STRICT_ENVIRONMENT_VARS))
        );
    }

    private void addMigrationsBundle(Bootstrap<AlexaConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<AlexaConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(AlexaConfiguration config) {
                return config.getDataSourceFactory();
            }
        });
    }

}
