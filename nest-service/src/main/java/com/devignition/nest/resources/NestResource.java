package com.devignition.nest.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.nest.core.NestThermostat;
import com.devignition.nest.db.NestDao;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.jersey.params.LongParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    @UnitOfWork
    public List<NestThermostat> getAllNests() {
        return nestDao.getAll();
    }

    @GET
    @Path("/{id}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public Optional<NestThermostat> getNestById(@PathParam("id") LongParam id) {
        return Optional.ofNullable(nestDao.getById(id.get()));
    }

    @POST
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public Response addNest(@Valid NestThermostat nest) {
        long id = nestDao.create(nest);
        URI location = UriBuilder.fromResource(NestResource.class).path("{id}").build(id);
        return Response.created(location)
                .entity(nest)
                .build();
    }

    @PATCH
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public NestThermostat updateNest(@Valid NestThermostat nest) {
        nestDao.update(nest);
        return nest;
    }

    @PATCH
    @Path("/{id}/temp")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public NestThermostat updateTemperature(@PathParam("id") LongParam id, @NotNull Integer newTemp) {
        NestThermostat nest = nestDao.getById(id.get());
        nest.setTemperature(newTemp);
        nestDao.update(nest);
        return nest;
    }

    @DELETE
    @Path("/{id}")
    @Timed
    @ExceptionMetered
    @UnitOfWork
    public Response deleteNest(@PathParam("id") LongParam id) {
        nestDao.delete(id.get());
        return Response.noContent().build();
    }
}
