package com.generic.rest.api.project.enumeration;

public enum ContactStrategy {
    UNIVERSAL("Universal"),
    BRANCH("Branch"),
    INDIVIDUAL("Individual");

    private final String name;

    ContactStrategy(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

}
