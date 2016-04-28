package com.devignition.nest.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class NestThermostatTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final String NEST_JSON = fixture("fixtures/kitchenNest.json");
    public static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(NEST_JSON, NestThermostat.class));

        NestThermostat nest = nestForTest();
        assertThat(MAPPER.writeValueAsString(nest)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(NEST_JSON, NestThermostat.class)).isEqualTo(nestForTest());
    }

    @Test
    public void testValidation_ForValidNest() {
        Set<ConstraintViolation<NestThermostat>> violations = VALIDATOR.validate(nestForTest());
        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void testValidation_ForInvalidNest() {
        NestThermostat nest = new NestThermostat();
        Set<ConstraintViolation<NestThermostat>> violations = VALIDATOR.validate(nest);
        assertThat(violations.isEmpty()).isFalse();
        assertThat(violations.stream()
                .map(violation -> violation.getPropertyPath().toString())
                .collect(toList()))
                .containsOnly("location", "temperature", "mode");
    }

    private NestThermostat nestForTest() {
        return NestThermostat.builder()
                .id(84L)
                .location("Kitchen")
                .temperature(68)
                .mode(Mode.HEAT)
                .build();
    }

}