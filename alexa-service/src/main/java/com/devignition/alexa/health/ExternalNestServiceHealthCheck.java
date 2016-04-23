package com.devignition.alexa.health;

import com.codahale.metrics.health.HealthCheck;
import com.devignition.alexa.core.NestServiceStatus;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class ExternalNestServiceHealthCheck extends HealthCheck {

    private final WebTarget statusTarget;

    public ExternalNestServiceHealthCheck(String baseURI, Client client) {
        this.statusTarget = client.target(baseURI).path("/status");
    }

    @Override
    protected Result check() throws Exception {
        NestServiceStatus status = statusTarget.request().get(NestServiceStatus.class);
        switch (status.getState()) {
            case "GREEN":
                return Result.healthy("Nest service is GREEN");
            case "YELLOW":
                return unhealthyResultFor(status);
            case "RED":
                return unhealthyResultFor(status);
            default:
                return Result.unhealthy("Unknown Nest service state: %s. Reported issues: %s",
                        status.getState(), status.getIssues());
        }
    }

    private HealthCheck.Result unhealthyResultFor(NestServiceStatus status) {
        return Result.unhealthy("Nest service is %s. Reported issues: %s", status.getState(), status.getIssues());
    }
}
