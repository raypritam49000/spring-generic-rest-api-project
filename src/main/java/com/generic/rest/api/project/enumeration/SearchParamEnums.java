package com.generic.rest.api.project.enumeration;

public enum SearchParamEnums {
    LATEST_GEN_DOCS("latest generated docs"),
    OLD_GEN_DOCS("old generated docs"),
    Is_MANAGER_SEARCH_PARAM("member search param"),
    Is_MEMBER_SEARCH_PARAM("manager search param"),
    Is_TURN_TIME("turn time"),
    Is_TURN_AROUND("turn around");

    private final String name;

    private SearchParamEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}