package com.devignition.nest.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.nest.core.ServiceHealth;
import com.devignition.nest.core.ServiceStatus;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Random;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Resource that reports the current status of the Nest service.
 */
@Path("/status")
@Produces(APPLICATION_JSON)
public class StatusResource {

    private final Random random;

    public StatusResource() {
        random = new Random();
    }

    @VisibleForTesting
    StatusResource(Random random) {
        this.random = random;
    }

    @GET
    @Timed
    @ExceptionMetered
    public ServiceStatus status() {
        return randomStatus();
    }

    // For demo purposes randomly return a random health status.
    // About 93% of the time we should be GREEN.
    // About 5% of the time we should be YELLOW.
    // About 2% of the time we should be RED.
    private ServiceStatus randomStatus() {
        int value = random.nextInt(100);

        if (value < 2) {
            return new ServiceStatus(ServiceHealth.RED, ImmutableList.of("Slowness reported", "Location services offline"));
        } else if (value >= 2 && value < 7) {
            return new ServiceStatus(ServiceHealth.YELLOW, ImmutableList.of("Slowness reported"));
        } else {
            return new ServiceStatus(ServiceHealth.GREEN, ImmutableList.of());
        }
    }

}
