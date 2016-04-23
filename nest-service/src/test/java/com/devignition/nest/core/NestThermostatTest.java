package com.devignition.nest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class NestThermostatTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testJsonSerialization() throws IOException {
        String nestJson = fixture("fixtures/kitchenNest.json");
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(nestJson, NestThermostat.class));

        NestThermostat nest = NestThermostat.builder()
                .id(84L)
                .location("Kitchen")
                .temperature(68)
                .mode(Mode.HEAT)
                .build();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

}