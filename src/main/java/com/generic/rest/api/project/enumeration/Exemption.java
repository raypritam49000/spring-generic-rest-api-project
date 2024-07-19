package com.generic.rest.api.project.enumeration;

public enum Exemption {
    AGRICULTURE("AG"),
    HOMESTEAD("HOM"),
    HOMESTEAD_CAP("CAP"),
    OVER_65("OV65"),
    DISABLED_PERSON("DP"),
    DISABLED_VETERAN("DV"),
    DISABLED_VETERAN_10_30("DV1030"),
    DISABLED_VETERAN_31_50("DV3150"),
    DISABLED_VETERAN_51_70("DV5170"),
    DISABLED_VETERAN_71_100("DV71100"),
    TOTAL("TOT"),
    CHARITABLE("CHAR"),
    RELIGIOUS("RELIG"),
    GOVERNMENT("GOV"),
    SOLAR("SOL"),
    OTHER("OTHER");

    private final String name;

    Exemption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
