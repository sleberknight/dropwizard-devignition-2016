package com.devignition.service.resources;

import com.devignition.service.core.Speaker;
import com.devignition.service.db.SpeakerDao;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jersey.params.LongParam;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static com.devignition.service.TestHelpers.newSpeaker;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpeakerResourceTest {

    private SpeakerResource speakerResource;
    private SpeakerDao speakerDao;

    @Before
    public void setUp() {
        speakerDao = mock(SpeakerDao.class);
        speakerResource = new SpeakerResource(speakerDao);
    }

    @Test
    public void testGetSpeakers() {
        ImmutableList<Speaker> speakerData = ImmutableList.of(
                newSpeaker("Alice Jones", "@alice_jones"),
                newSpeaker("Bob Smith", "@speakerbob")
        );
        when(speakerDao.getAllSpeakers()).thenReturn(speakerData);

        List<Speaker> speakers = speakerResource.getSpeakers();
        assertThat(speakers).isSameAs(speakerData);
    }

    @Test
    public void testGetSpeaker() {
        long id = 42L;
        Speaker alice = newSpeaker(id, "Alice", "@alice");
        when(speakerDao.getSpeaker(id)).thenReturn(Optional.of(alice));

        Optional<Speaker> speakerOptional = speakerResource.getSpeaker(new LongParam("42"));
        assertThat(speakerOptional.isPresent()).isTrue();
        Speaker speaker = speakerOptional.get();
        assertThat(speaker).isEqualToComparingFieldByField(alice);
    }

    @Test
    public void testGetSpeaker_WhenNonNumericIdSpecified() {
        Throwable thrown = catchThrowable(() -> speakerResource.getSpeaker(new LongParam("asdf")));
        assertThat(thrown).isInstanceOf(WebApplicationException.class);

        WebApplicationException webEx = (WebApplicationException) thrown;
        assertThat(webEx.getResponse().getStatus()).isEqualTo(400);
    }

    @Test
    public void testCreateSpeaker() {
        Speaker alice = newSpeaker("Alice", "@alice_jones");
        long id = 42;
        when(speakerDao.createSpeaker(alice)).thenReturn(id);

        Response response = speakerResource.createSpeaker(alice);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        assertThat(response.getLocation().toString()).isEqualTo("/speakers/42");
        assertThat(response.getEntity()).isEqualToComparingFieldByField(alice.withId(id));
    }

    @Test
    public void testUpdateSpeaker() {
        Speaker alice = newSpeaker(42L, "Alice", "@alice_jones");
        Speaker updatedAlice = speakerResource.updateSpeaker(alice);
        assertThat(updatedAlice).isEqualToComparingFieldByField(alice);
    }

    @Test
    public void testDeleteSpeaker() {
        Response response = speakerResource.deleteSpeaker(new LongParam("42"));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        assertThat(response.hasEntity()).isFalse();
        verify(speakerDao).deleteSpeaker(42);
    }

}