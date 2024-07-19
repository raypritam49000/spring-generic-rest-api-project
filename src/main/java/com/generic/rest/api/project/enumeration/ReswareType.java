package com.generic.rest.api.project.enumeration;

public enum ReswareType {
    DOCUMENT("Document");

    private final String name;

    ReswareType(String name) { this.name = name; }

    public String getName() { return name; }

    public String toString() {
        return this.name;
    }

}