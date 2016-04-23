package com.devignition.nest.resources;

import com.devignition.nest.core.Mode;
import com.devignition.nest.core.NestThermostat;
import com.devignition.nest.db.NestDao;
import com.google.common.collect.Lists;
import io.dropwizard.jersey.params.LongParam;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static com.devignition.nest.TestHelpers.newNest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NestResourceTest {

    private NestResource nestResource;
    private NestDao nestDao;

    @Before
    public void setUp() {
        nestDao = mock(NestDao.class);
        nestResource = new NestResource(nestDao);
    }

    @Test
    public void testGetAllNests() {
        List<NestThermostat> nestData = Lists.newArrayList(
                newNest(1L, "Bedroom"),
                newNest(2L, "Kitchen")
        );
        when(nestDao.getAll()).thenReturn(nestData);

        List<NestThermostat> nests = nestResource.getAllNests();
        assertThat(nests).isSameAs(nestData);
    }

    @Test
    public void testGetNestById() {
        NestThermostat nest = newNest(42L, "Kitchen", 72, Mode.COOL);

        when(nestDao.getById(42)).thenReturn(nest);

        Optional<NestThermostat> nestOptional = nestResource.getNestById(new LongParam("42"));
        assertThat(nestOptional.isPresent()).isTrue();
        assertThat(nestOptional.get()).isSameAs(nest);
    }

    @Test
    public void testGetNestById_WhenNotFound() {
        when(nestDao.getById(42)).thenReturn(null);

        Optional<NestThermostat> nestOptional = nestResource.getNestById(new LongParam("42"));
        assertThat(nestOptional.isPresent()).isFalse();
    }

    @Test
    public void testAddNest() {
        long id = 42;

        NestThermostat nest = newNest("Bedroom", 72, Mode.HEAT);
        when(nestDao.create(nest)).thenReturn(id);

        Response response = nestResource.addNest(nest);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        assertThat(response.getLocation().toString()).isEqualTo("/nests/42");
        assertThat(response.getEntity()).isEqualToIgnoringGivenFields(nest, "id");
    }

    @Test
    public void testUpdateNest() {
        NestThermostat nest = newNest(42L, "Bedroom", 72, Mode.HEAT);
        NestThermostat updatedNest = nestResource.updateNest(nest);
        assertThat(updatedNest).isSameAs(nest);
        verify(nestDao).update(nest);
    }

    @Test
    public void testUpdateTemperature() {
        NestThermostat nest = newNest(42L, "Bedroom", 72, Mode.HEAT);
        when(nestDao.getById(nest.getId())).thenReturn(nest);

        int newTemp = 70;
        NestThermostat updatedNest = nestResource.updateTemperature(new LongParam(nest.getId().toString()), newTemp);
        assertThat(updatedNest.getTemperature()).isEqualTo(newTemp);
    }

    @Test
    public void testDeleteNest() {
        Response response = nestResource.deleteNest(new LongParam("42"));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        assertThat(response.hasEntity()).isFalse();
        verify(nestDao).delete(42);
    }
}