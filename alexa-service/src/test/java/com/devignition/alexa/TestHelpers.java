package com.devignition.alexa;

import com.devignition.alexa.core.ExternalNest;
import com.devignition.alexa.core.Nest;

import java.util.HashMap;
import java.util.Map;

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

    public static ExternalNest newExternalNest(Long id, String location) {
        return new ExternalNest(id, location, 72);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> newHashMap(Object... kvPairs) {
        Map<K, V> map = new HashMap<>();

        // TODO Old school...need to figure out a functional way of doing this...maybe a custom Spliterator?
        int index = 0;
        while (index < kvPairs.length) {
            K key = (K) kvPairs[index];
            V value = (V) kvPairs[index + 1];
            map.put(key, value);
            index += 2;
        }

        return map;
    }
}