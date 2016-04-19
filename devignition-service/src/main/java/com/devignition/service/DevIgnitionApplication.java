package com.devignition.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DevIgnitionApplication extends Application<DevIgnitionConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DevIgnitionApplication().run(args);
    }

    @Override
    public String getName() {
        return "Devignition Service";
    }

    @Override
    public void initialize(final Bootstrap<DevIgnitionConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DevIgnitionConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
