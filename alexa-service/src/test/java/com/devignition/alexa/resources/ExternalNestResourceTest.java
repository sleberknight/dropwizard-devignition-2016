package com.devignition.alexa.resources;

import com.devignition.alexa.TestHelpers;
import com.devignition.alexa.core.ExternalNest;
import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.NestDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.PATCH;
import io.dropwizard.testing.junit.DropwizardClientRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.devignition.alexa.TestHelpers.newExternalNest;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class ExternalNestResourceTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Path("/nests")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public static class TestNestResource {
        @GET
        public List<ExternalNest> getNests() {
            return ImmutableList.of(
                    newExternalNest(1L, "Dining Room"),
                    newExternalNest(2L, "Master Bedroom")
            );
        }

        @PATCH
        @Path("/{id}/temp")
        public String updateTemperature(@PathParam("id") Long id, Integer newTemp) throws JsonProcessingException {
            Map<String, Object> nest = TestHelpers.newHashMap(
                    "id", id,
                    "location", "Dining Room",
                    "temperature", newTemp,
                    "mode", "COOL");
            return MAPPER.writeValueAsString(nest);
        }
    }

    @ClassRule
    public static final DropwizardClientRule CLIENT_RULE = new DropwizardClientRule(new TestNestResource());

    private static NestDao nestDao;
    private static Client client;
    private ExternalNestResource resource;

    @BeforeClass
    public static void beforeAll() {
        nestDao = mock(NestDao.class);
        client = new JerseyClientBuilder(CLIENT_RULE.getEnvironment()).build("test client");
    }

    @AfterClass
    public static void afterAll() {
        client.close();
    }

    @Before
    public void setUp() {
        resource = new ExternalNestResource(nestDao, client, CLIENT_RULE.baseUri().toString());
    }

    @After
    public void tearDown() {
        reset(nestDao);
    }

    @Test
    public void testGetNests() {
        List<ExternalNest> nests = resource.getNests();
        assertThat(nests).containsExactly(
                newExternalNest(1L, "Dining Room"), newExternalNest(2L, "Master Bedroom"));
    }

    @Test
    public void testUpdateTemperatore_WhenInvalidNestLocationGiven() {
        when(nestDao.getNest(anyString())).thenReturn(Optional.empty());

        Response response = resource.updateTemperature("Wherever", 73);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
    }

    @Test
    public void testUpdateTemperature_WhenValidNestLocationGiven() {
        long locationId = 12345L;
        Nest nest = new Nest(42L, "Dining Room", locationId);
        when(nestDao.getNest(nest.getLocation())).thenReturn(Optional.of(nest));

        int newTemp = 72;
        Response response = resource.updateTemperature(nest.getLocation(), newTemp);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        assertThat(response.hasEntity()).isTrue();
        assertThat(response.getEntity()).isExactlyInstanceOf(ExternalNest.class);
        ExternalNest entity = ExternalNest.class.cast(response.getEntity());
        assertThat(entity).isEqualTo(new ExternalNest(locationId, "Dining Room", newTemp));
    }

}