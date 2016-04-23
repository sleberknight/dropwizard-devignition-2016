package com.devignition.alexa.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.devignition.alexa.core.ExternalNest;
import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.NestDao;
import io.dropwizard.jersey.PATCH;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/external/nests")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class ExternalNestResource {

    private final NestDao nestDao;
    private final Client client;
    private final URI baseURI;

    public ExternalNestResource(NestDao nestDao, Client client, String baseURI) {
        this.nestDao = nestDao;
        this.client = client;
        this.baseURI = URI.create(baseURI);
    }

    @GET
    @Timed
    @ExceptionMetered
    public List<ExternalNest> getNests() {
        URI nestsURI = UriBuilder.fromUri(baseURI).path("/nests").build();
        return client.target(nestsURI)
                .request()
                .get(new GenericType<List<ExternalNest>>() {
                });
    }

    @PATCH
    @Path("/{location}/temp")
    public Response updateTemperature(@PathParam("location") String location,
                                      @NotNull Integer newTemp) {

        Optional<Nest> nest = nestDao.getNest(location);
        if (isEmpty(nest)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // TODO Validate temp before sending request? What do we get back for bad values?

        UriBuilder updateTempURI = UriBuilder.fromUri(baseURI).path("/nests/{id}/temp");
        ExternalNest externalNest = client.target(updateTempURI)
                .resolveTemplate("id", nest.get().getLocationId())
                .request()
                .method("PATCH", entity(newTemp, APPLICATION_JSON), ExternalNest.class);
        return Response.ok(externalNest).build();
    }

    private <T> boolean isEmpty(Optional<T> optional) {
        return !optional.isPresent();
    }

}
