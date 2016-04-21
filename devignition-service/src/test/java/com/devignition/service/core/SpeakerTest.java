package com.devignition.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class SpeakerTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void testJsonSerialization() throws IOException {

        String aliceJson = fixture("fixtures/alice.json");
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(aliceJson, Speaker.class));

        Speaker alice = Speaker.builder()
                .id(42L)
                .name("Alice Jones")
                .twitterHandle("@alice_jones")
                .bio("Alice is great at writing software")
                .talkTitle("Why I like Dropwizard")
                .talkAbstract("Stuff that makes Dropwizard cool")
                .build();
        assertThat(MAPPER.writeValueAsString(alice)).isEqualTo(expected);
    }

}