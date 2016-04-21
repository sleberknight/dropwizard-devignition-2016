package com.devignition.service;

import com.devignition.service.db.SpeakerDao;
import com.devignition.service.resources.SpeakerResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class DevIgnitionApplication extends Application<DevIgnitionConfiguration> {

    // Setting to false lets us define default values in case an environment var isn't present
    private static final boolean STRICT_ENVIRONMENT_VARS = false;

    public static void main(final String[] args) throws Exception {
        new DevIgnitionApplication().run(args);
    }

    @Override
    public String getName() {
        return "DevIgnition Service";
    }

    @Override
    public void initialize(final Bootstrap<DevIgnitionConfiguration> bootstrap) {
        enableEnvironmentVariableSubstitution(bootstrap);
        addMigrationsBundle(bootstrap);
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(final DevIgnitionConfiguration configuration, final Environment environment) {

        DBIFactory jdbiFactory = new DBIFactory();
        DBI jdbi = jdbiFactory.build(environment, configuration.getDataSourceFactory(), "database");

        SpeakerDao speakerDao = jdbi.onDemand(SpeakerDao.class);
        SpeakerResource speakerResource = new SpeakerResource(speakerDao);
        environment.jersey().register(speakerResource);
    }

    private void enableEnvironmentVariableSubstitution(Bootstrap<DevIgnitionConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(STRICT_ENVIRONMENT_VARS))
        );
    }

    private void addMigrationsBundle(Bootstrap<DevIgnitionConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<DevIgnitionConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(DevIgnitionConfiguration config) {
                return config.getDataSourceFactory();
            }
        });
    }

}
