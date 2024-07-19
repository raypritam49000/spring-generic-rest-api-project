package com.generic.rest.api.project.enumeration;

public enum Type {
    INTERNAL("internal"),
    EXTERNAL("external");

    public final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}