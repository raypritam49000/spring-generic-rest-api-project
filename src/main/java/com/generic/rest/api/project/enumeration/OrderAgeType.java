package com.generic.rest.api.project.enumeration;

public enum OrderAgeType {

    MINUTES("Minutes"),
    HOURS("Hours"),
    DAYS("Days");

    private final String name;

    OrderAgeType(String name) { this.name = name; }

    public String getName() { return name; }

    public String toString() {
        return this.name;
    }

}
