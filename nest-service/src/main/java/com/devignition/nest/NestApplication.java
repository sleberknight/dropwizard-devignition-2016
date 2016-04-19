package com.devignition.nest;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class NestApplication extends Application<NestConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NestApplication().run(args);
    }

    @Override
    public String getName() {
        return "Nest";
    }

    @Override
    public void initialize(final Bootstrap<NestConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final NestConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
