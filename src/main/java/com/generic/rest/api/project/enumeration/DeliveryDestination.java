package com.generic.rest.api.project.enumeration;

public enum DeliveryDestination {
    PARTNER("Partner"),
    WEB("Web"),
    EMAIL("Email");

    public final String name;

    DeliveryDestination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
