package com.devignition.nest.resources;

import com.devignition.nest.core.Mode;
import com.devignition.nest.core.NestThermostat;
import com.devignition.nest.db.NestDao;
import com.google.common.collect.Lists;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.devignition.nest.TestHelpers.newNest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NestResourceIntegrationTest {

    private static final NestDao NEST_DAO = mock(NestDao.class);

    @ClassRule
    public static final ResourceTestRule RESOURCE = ResourceTestRule.builder()
            .addResource(new NestResource(NEST_DAO))
            .build();

    @After
    public void tearDown() {
        reset(NEST_DAO);
    }

    @Test
    public void testGetAllNests() {
        List<NestThermostat> nestData = Lists.newArrayList(
                newNest(1L, "Bedroom"),
                newNest(2L, "Kitchen")
        );
        when(NEST_DAO.getAll()).thenReturn(nestData);

        assertThat(RESOURCE.client()
                .target("/nests")
                .request()
                .get(new GenericType<List<NestThermostat>>() {
                }))
                .isEqualTo(nestData);

        verify(NEST_DAO).getAll();
    }

    @Test
    public void testGetNestById() {
        long id = 42;
        NestThermostat nest = newNest(id, "Kitchen", 72, Mode.COOL);

        when(NEST_DAO.getById(id)).thenReturn(nest);

        assertThat(RESOURCE.client()
                .target("/nests/42")
                .request()
                .get(NestThermostat.class))
                .isEqualToComparingFieldByField(nest);

        verify(NEST_DAO).getById(id);
    }

    @Test
    public void testGetNestById_WhenNonNumericId() {
        Response response = RESOURCE.client()
                .target("/nests/foo")
                .request()
                .get(Response.class);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
    }

    @Test
    public void testAddNest() {
        long id = 42L;
        NestThermostat nestForPostRequest = newNest(id, "Hallway", 74, Mode.COOL);

        when(NEST_DAO.create(any(NestThermostat.class))).thenReturn(id);

        Response response = RESOURCE.client()
                .target("/nests")
                .request()
                .post(Entity.entity(nestForPostRequest, MediaType.APPLICATION_JSON));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        assertThat(response.getHeaderString("Location")).contains("/nests/42");
        NestThermostat createdNest = response.readEntity(NestThermostat.class);
        assertThat(createdNest).isEqualToComparingFieldByField(nestForPostRequest);

        verify(NEST_DAO).create(argThat(new CustomTypeSafeMatcher<NestThermostat>(nestForPostRequest.toString()) {
            @Override
            protected boolean matchesSafely(NestThermostat arg) {
                return arg.equals(nestForPostRequest);
            }
        }));
    }

    @Test
    public void testAddAnInvalidNest() {
        NestThermostat nest = new NestThermostat();

        Response response = RESOURCE.client()
                .target("/nests")
                .request()
                .post(Entity.entity(nest, MediaType.APPLICATION_JSON));

        assertThat(response.getStatus()).isEqualTo(422);
    }

}