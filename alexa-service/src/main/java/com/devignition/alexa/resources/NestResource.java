package com.devignition.alexa.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.NestDao;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path("/nests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NestResource {

    private NestDao nestDao;

    public NestResource(NestDao nestDao) {
        this.nestDao = nestDao;
    }

    @GET
    @Timed
    @ExceptionMetered
    public List<Nest> getNests() {
        return nestDao.getAllNests();
    }

    @GET
    @Path("/{location}")
    @Timed
    @ExceptionMetered
    public Optional<Nest> getNest(@PathParam("location") String location) {
        return nestDao.getNest(location);
    }

    @POST
    @Timed
    @ExceptionMetered
    public Response createNest(@Valid Nest nest, @Context UriInfo uriInfo) {
        long nestId = nestDao.createNest(nest);
        Nest savedNest = nest.withId(nestId);
        URI location = uriInfo.getRequestUriBuilder().path("{location}").build(nest.getLocation());
        return Response.created(location)
                .entity(savedNest)
                .build();
    }

    @DELETE
    @Path("/{location}")
    @Timed
    @ExceptionMetered
    public Response deleteNest(@PathParam("location") String location) {
        nestDao.deleteNest(location);
        return Response.noContent().build();
    }

}
