package com.devignition.alexa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class NestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testJsonSerialization() throws IOException {

        String nestJson = fixture("fixtures/basementNest.json");
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(nestJson, Nest.class));

        Nest nest = Nest.builder()
                .id(1L)
                .location("Basement")
                .build();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

}