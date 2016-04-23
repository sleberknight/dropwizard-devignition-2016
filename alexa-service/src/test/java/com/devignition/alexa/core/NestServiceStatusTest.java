package com.devignition.alexa.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class NestServiceStatusTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static String STATUS_JSON = FixtureHelpers.fixture("fixtures/nestServiceStatus.json");

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(STATUS_JSON, NestServiceStatus.class));

        NestServiceStatus nest = externalNestForTest();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(STATUS_JSON, NestServiceStatus.class)).isEqualTo(externalNestForTest());
    }

    private NestServiceStatus externalNestForTest() {
        return new NestServiceStatus("RED", ImmutableList.of("Slowness reported", "Location services offline"));
    }

}