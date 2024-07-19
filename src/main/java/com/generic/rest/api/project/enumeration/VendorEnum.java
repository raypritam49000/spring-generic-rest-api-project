package com.generic.rest.api.project.enumeration;

public enum VendorEnum {

    AVANTA("Avanta"),
    TGX("TitleLogix"),
    STUB("Stub"),
    LocateIntelligence("LocateIntelligence"),
    NONE("None");


    private final String name;

    VendorEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
