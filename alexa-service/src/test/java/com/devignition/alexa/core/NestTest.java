package com.devignition.alexa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class NestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static String NEST_JSON = FixtureHelpers.fixture("fixtures/basementNest.json");

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(NEST_JSON, Nest.class));

        Nest nest = nestForTest();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(NEST_JSON, Nest.class)).isEqualTo(nestForTest());
    }

    private Nest nestForTest() {
        return Nest.builder()
                .id(1L)
                .location("Basement")
                .locationId(88L)
                .build();
    }

}