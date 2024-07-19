package com.generic.rest.api.project.enumeration;

public enum PropertyType {

    RESIDENTIAL ("Residential"),
    COMMERCIAL  ("Commercial"),
    UNKNOWN     ("Unknown");

    private final String name;
    PropertyType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}

