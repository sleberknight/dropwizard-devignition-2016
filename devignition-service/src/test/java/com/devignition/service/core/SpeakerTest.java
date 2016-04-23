package com.devignition.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class SpeakerTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static final String SPEAKER_JSON = fixture("fixtures/alice.json");

    @Test
    public void testJsonSerialization() throws IOException {
        String expected = MAPPER.writeValueAsString(MAPPER.readValue(SPEAKER_JSON, Speaker.class));

        Speaker alice = speakerForTest();
        assertThat(MAPPER.writeValueAsString(alice)).isEqualTo(expected);
    }

    @Test
    public void testJsonDeserialization() throws IOException {
        assertThat(MAPPER.readValue(SPEAKER_JSON, Speaker.class)).isEqualTo(speakerForTest());
    }

    private Speaker speakerForTest() {
        return Speaker.builder()
                .id(42L)
                .name("Alice Jones")
                .twitterHandle("@alice_jones")
                .bio("Alice is great at writing software")
                .talkTitle("Why I like Dropwizard")
                .talkAbstract("Stuff that makes Dropwizard cool")
                .build();
    }

}