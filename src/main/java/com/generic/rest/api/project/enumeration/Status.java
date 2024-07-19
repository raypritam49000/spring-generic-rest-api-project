package com.generic.rest.api.project.enumeration;

public enum Status {
    OPEN("open"),
    REVIEW("review"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

}