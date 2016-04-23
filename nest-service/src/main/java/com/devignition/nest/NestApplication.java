package com.devignition.nest;

import com.devignition.nest.core.NestThermostat;
import com.devignition.nest.db.NestDao;
import com.devignition.nest.resources.NestResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class NestApplication extends Application<NestConfiguration> {

    // Setting to false lets us define default values in case an environment var isn't present
    private static final boolean STRICT_ENVIRONMENT_VARS = false;

    private HibernateBundle<NestConfiguration> hibernateBundle;

    public static void main(final String[] args) throws Exception {
        new NestApplication().run(args);
    }

    @Override
    public String getName() {
        return "Nest";
    }

    @Override
    public void initialize(final Bootstrap<NestConfiguration> bootstrap) {
        enableEnvironmentVariableSubstitution(bootstrap);
        addMigrationsBundle(bootstrap);

        String entityPackage = NestThermostat.class.getPackage().getName();
        ;
        hibernateBundle = newHibernateBundle(entityPackage);
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final NestConfiguration configuration,
                    final Environment environment) {

        NestDao nestDao = new NestDao(hibernateBundle.getSessionFactory());
        NestResource nestResource = new NestResource(nestDao);
        environment.jersey().register(nestResource);
    }

    private void enableEnvironmentVariableSubstitution(Bootstrap<NestConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(STRICT_ENVIRONMENT_VARS))
        );
    }

    private void addMigrationsBundle(Bootstrap<NestConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<NestConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(NestConfiguration config) {
                return config.getDataSourceFactory();
            }
        });
    }

    private HibernateBundle<NestConfiguration> newHibernateBundle(final String entityPackage) {
        return new ScanningHibernateBundle<NestConfiguration>(entityPackage) {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(NestConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };
    }

}
