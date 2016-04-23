package com.devignition.nest;

import com.devignition.nest.core.Mode;
import com.devignition.nest.core.NestThermostat;

public final class TestHelpers {

    private TestHelpers() {
    }

    public static NestThermostat newNest(Long id, String location) {
        return newNest(id, location, 70, Mode.HEAT);
    }

    public static NestThermostat newNest(String location, Integer temp, Mode mode) {
        return newNest(null, location, temp, mode);
    }

    public static NestThermostat newNest(Long id, String location, Integer temp, Mode mode) {
        return NestThermostat.builder()
                .id(id)
                .location(location)
                .temperature(temp)
                .mode(mode)
                .build();
    }
}
