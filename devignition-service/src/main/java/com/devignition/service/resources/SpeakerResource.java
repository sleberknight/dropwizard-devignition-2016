package com.devignition.service.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.service.core.Speaker;
import com.devignition.service.db.SpeakerDao;
import io.dropwizard.jersey.params.LongParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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

}
