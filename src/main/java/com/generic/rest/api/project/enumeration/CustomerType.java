package com.generic.rest.api.project.enumeration;

public enum CustomerType {

    RETAIL("Retail"),
    WHOLESALE("Wholesale");

    private final String name;

    CustomerType(String name) { this.name = name; }

    public String getName() { return name; }

    public String toString() {
        return this.name;
    }

}
