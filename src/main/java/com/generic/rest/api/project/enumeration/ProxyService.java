package com.generic.rest.api.project.enumeration;

public enum ProxyService {
    NONE("None"),
    OXYLABS("Oxylabs");

    public final String name;

    ProxyService(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
