package com.devignition.service.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.service.core.Speaker;
import com.devignition.service.db.SpeakerDao;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/speakers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpeakerResource {

    private final SpeakerDao speakerDao;

    public SpeakerResource(SpeakerDao speakerDao) {
        this.speakerDao = speakerDao;
    }

    @GET
    @Timed
    @ExceptionMetered
    public List<Speaker> getSpeakers() {
        return speakerDao.getAllSpeakers();
    }

    @GET
    @Path("/{id}")
    @Timed
    @ExceptionMetered
    public Optional<Speaker> getSpeaker(@PathParam("id") LongParam id) {
        return speakerDao.getSpeaker(id.get());
    }

    @POST
    @Timed
    @ExceptionMetered
    public Response createSpeaker(@Valid Speaker speaker) {
        long id = speakerDao.createSpeaker(speaker);
        Speaker savedSpeaker = speaker.withId(id);
        URI location = UriBuilder.fromResource(SpeakerResource.class).path("{id}").build(id);
        return Response.created(location)
                .entity(savedSpeaker)
                .build();
    }

    @PATCH
    @Timed
    @ExceptionMetered
    public Speaker updateSpeaker(@Valid Speaker speaker) {
        speakerDao.updateSpeaker(speaker);
        return speaker;
    }

    @DELETE
    @Path("/{id}")
    @Timed
    @ExceptionMetered
    public Response deleteSpeaker(@PathParam("id") LongParam id) {
        speakerDao.deleteSpeaker(id.get());
        return Response.noContent().build();
    }

}
