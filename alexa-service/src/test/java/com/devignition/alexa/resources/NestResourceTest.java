package com.devignition.alexa.resources;

import com.devignition.alexa.core.Nest;
import com.devignition.alexa.db.NestDao;
import com.google.common.collect.ImmutableList;
import com.google.common.net.UrlEscapers;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;

import static com.devignition.alexa.TestHelpers.newNest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
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
    public void testGetNests() {
        ImmutableList<Nest> nestData = ImmutableList.of(
                newNest("Dining Room", 12345),
                newNest("Master Bedroom", 67890)
        );
        when(nestDao.getAllNests()).thenReturn(nestData);

        List<Nest> nests = nestResource.getNests();
        assertThat(nests).isSameAs(nestData);
    }

    @Test
    public void testGetNest() {
        String location = "Basement";
        Nest nest = newNest(42L, location, 12345);
        when(nestDao.getNest(location)).thenReturn(Optional.of(nest));

        Optional<Nest> nestOptional = nestResource.getNest(location);
        assertThat(nestOptional.isPresent()).isTrue();
        Nest foundNest = nestOptional.get();
        assertThat(foundNest).isEqualToComparingFieldByField(nest);
    }

    @Test
    public void testGetNest_WhenNotFound() {
        when(nestDao.getNest(anyString())).thenReturn(Optional.empty());
        Optional<Nest> nestOptional = nestResource.getNest("Dining Room");
        assertThat(nestOptional.isPresent()).isFalse();
    }

    @Test
    public void testCreateNest() {
        String location = "Master Bedroom";
        Nest nest = newNest(location, 12345);
        long id = 42;
        when(nestDao.createNest(nest)).thenReturn(id);

        UriInfo uriInfo = mock(UriInfo.class);
        when(uriInfo.getRequestUriBuilder()).thenReturn(UriBuilder.fromResource(NestResource.class));
        Response response = nestResource.createNest(nest, uriInfo);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        String urlEscapedLocation = UrlEscapers.urlPathSegmentEscaper().escape(location);
        assertThat(response.getLocation().toString()).isEqualTo("/nests/" + urlEscapedLocation);
        assertThat(response.getEntity()).isEqualToComparingFieldByField(nest.withId(id));
    }

    @Test
    public void testDeleteNest() {
        String location = "Master Bedroom";
        Response response = nestResource.deleteNest(location);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        assertThat(response.hasEntity()).isFalse();
        verify(nestDao).deleteNest(location);
    }

}