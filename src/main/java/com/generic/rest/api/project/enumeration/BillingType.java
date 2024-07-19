package com.generic.rest.api.project.enumeration;

public enum BillingType {

    PERCLOSE("Per-Close"),
    PERORDER("Per-Order");

    private final String name;

    BillingType(String name) { this.name = name; }

    public String getName() { return name; }

    public String toString() {
        return this.name;
    }


}
