package com.devignition.nest.resources;

import com.devignition.nest.core.ServiceHealth;
import com.devignition.nest.core.ServiceStatus;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatusResourceTest {

    private Random random;
    private StatusResource resource;

    @Before
    public void setUp() {
        random = mock(Random.class);
        resource = new StatusResource(random);
    }

    @Test
    public void testStatus_WhenGREEN() {
        when(random.nextInt(100)).thenReturn(7);
        ServiceStatus status = resource.status();
        assertThat(status).isEqualTo(new ServiceStatus(ServiceHealth.GREEN, ImmutableList.of()));
    }

    @Test
    public void testStatus_WhenYELLOW() {
        when(random.nextInt(100)).thenReturn(2);
        ServiceStatus status = resource.status();
        assertThat(status).isEqualTo(new ServiceStatus(ServiceHealth.YELLOW, ImmutableList.of("Slowness reported")));
    }

    @Test
    public void testStatus_WhenRED() {
        when(random.nextInt(100)).thenReturn(1);
        ServiceStatus status = resource.status();
        assertThat(status).isEqualTo(
                new ServiceStatus(ServiceHealth.RED, ImmutableList.of("Slowness reported", "Location services offline")));
    }

}