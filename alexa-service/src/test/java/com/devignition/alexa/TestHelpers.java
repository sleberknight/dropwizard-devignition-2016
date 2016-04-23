package com.devignition.alexa;

import com.devignition.alexa.core.Nest;

public final class TestHelpers {

    private TestHelpers() {
    }

    public static Nest newNest(String location, long locationId) {
        return newNest(null, location, locationId);
    }

    public static Nest newNest(Long id, String location, long locationId) {
        return Nest.builder()
                .id(id)
                .location(location)
                .locationId(locationId)
                .build();
    }
}