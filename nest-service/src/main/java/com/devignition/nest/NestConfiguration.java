package com.devignition.nest;

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
public class NestConfiguration extends Configuration {

    @JsonProperty("database")
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory;

}
