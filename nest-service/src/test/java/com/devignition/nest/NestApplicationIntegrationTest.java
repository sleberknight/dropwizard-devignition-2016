package com.devignition.nest;

import com.devignition.nest.core.ServiceStatus;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class NestApplicationIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<NestConfiguration> APP =
            new DropwizardAppRule<>(NestApplication.class, ResourceHelpers.resourceFilePath("test-app-config.yml"));

    private static Client client;

    @BeforeClass
    public static void beforeAll() {
        client = new JerseyClientBuilder(APP.getEnvironment()).build("app test client");
    }

    @AfterClass
    public static void afterAll() {
        client.close();
    }

    @Test
    public void testGetNest() {
        String url = String.format("http://localhost:%d/nests", APP.getLocalPort());
        Response response = client.target(url)
                .request()
                .get();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void testGetStatus() {
        String url = String.format("http://localhost:%d/status", APP.getLocalPort());
        Response response = client.target(url)
                .request()
                .get();
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        ServiceStatus serviceStatus = response.readEntity(ServiceStatus.class);
        assertThat(serviceStatus).isNotNull();
    }

}