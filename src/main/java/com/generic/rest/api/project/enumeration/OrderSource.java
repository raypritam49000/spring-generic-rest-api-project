package com.generic.rest.api.project.enumeration;

public enum OrderSource {

    UNKNOWN("Unknown"),
    CM("CM"),
    RW("Resware"),
    SOFTPRO("SoftPro"),
    QUALIA("Qualia"),
    LOC("Loc"),
    TGX("Tgx"),
    HISTORICAL("Historical");

    private final String name;

    OrderSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}

