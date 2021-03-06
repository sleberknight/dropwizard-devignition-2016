package com.devignition.alexa;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AlexaConfiguration extends Configuration {

    @JsonProperty("database")
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory;

    @JsonProperty("jerseyClient")
    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClientConfiguration;

    @URL
    @NotBlank
    private String nestServiceBaseURI;
}
