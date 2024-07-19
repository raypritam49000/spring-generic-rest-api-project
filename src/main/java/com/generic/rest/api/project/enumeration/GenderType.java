package com.generic.rest.api.project.enumeration;

public enum GenderType {
    MALE("male"), FEMALE("female");

    private final String name;

    GenderType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
