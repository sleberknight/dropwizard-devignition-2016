package com.devignition.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DevIgnitionConfiguration extends Configuration {

    @JsonProperty("database")
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory;

}
