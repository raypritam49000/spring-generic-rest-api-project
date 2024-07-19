package com.generic.rest.api.project.enumeration;

public enum CustomerLevel {

    SYS("Sys"),
    TENANT("Tenant"),
    CUSTOMER("Customer");

    private final String name;

    CustomerLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

}
