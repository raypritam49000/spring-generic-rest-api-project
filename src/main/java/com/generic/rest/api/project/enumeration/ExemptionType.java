package com.generic.rest.api.project.enumeration;

public enum ExemptionType {
    NONE("None"),
    FLAT("Flat"),
    PERCENTAGE("Percentage"),
    FLAT_PLUS_PERCENTAGE("Flat + Percentage");

    public final String name;

    ExemptionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
