package com.devignition.nest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceStatusTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static String STATUS_JSON = FixtureHelpers.fixture("fixtures/serviceStatus.json");

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(STATUS_JSON, ServiceStatus.class));

        ServiceStatus nest = externalNestForTest();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(STATUS_JSON, ServiceStatus.class)).isEqualTo(externalNestForTest());
    }

    private ServiceStatus externalNestForTest() {
        return new ServiceStatus(ServiceHealth.RED, ImmutableList.of("Slowness reported", "Location services offline"));
    }

}