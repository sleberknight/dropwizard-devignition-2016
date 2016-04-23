package com.devignition.nest.core;

import lombok.Getter;

@Getter
public enum Mode {

    HEAT("Heat"), COOL("Cool"), HEAT_COOL("Heat-Cool"), OFF("Off");

    private String label;

    Mode(String label) {
        this.label = label;
    }

}
