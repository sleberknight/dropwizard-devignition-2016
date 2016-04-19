package com.devignition.alexa;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AlexaApplication extends Application<AlexaConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AlexaApplication().run(args);
    }

    @Override
    public String getName() {
        return "Alexa";
    }

    @Override
    public void initialize(final Bootstrap<AlexaConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final AlexaConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
