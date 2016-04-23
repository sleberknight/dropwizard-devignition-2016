package com.devignition.alexa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ExternalNestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static String EXTERNAL_NEST_JSON = FixtureHelpers.fixture("fixtures/externalNest.json");

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(EXTERNAL_NEST_JSON, ExternalNest.class));

        ExternalNest nest = externalNestForTest();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(EXTERNAL_NEST_JSON, ExternalNest.class)).isEqualTo(externalNestForTest());
    }

    private ExternalNest externalNestForTest() {
        return new ExternalNest(88L, "Basement", 73);
    }

}