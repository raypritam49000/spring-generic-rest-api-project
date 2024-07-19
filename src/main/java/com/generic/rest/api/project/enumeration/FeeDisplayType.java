package com.generic.rest.api.project.enumeration;

public enum FeeDisplayType {

    FEE("FEE"),
    PLACEHOLDER("PLACEHOLDER"),
    NO_FEE("NO_FEE");

    private final String name;


    FeeDisplayType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
