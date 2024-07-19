package com.generic.rest.api.project.search;

public enum ComparativeOperation {
    GT("Greater than"),
    GTE("Greater than or equal to"),
    E("Equal to"),
    LT("Less than"),
    LTE("Less than or equal to");

    private final String name;

    ComparativeOperation(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

}