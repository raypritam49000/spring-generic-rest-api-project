package com.generic.rest.api.project.enumeration;

public enum JurisdictionCodeType {
    APPRAISAL_DISTRICT("Appraisal District"),
    COLLECTOR("Collector"),
    TAX_STATEMENT("Tax Statement");

    private final String name;

    JurisdictionCodeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
