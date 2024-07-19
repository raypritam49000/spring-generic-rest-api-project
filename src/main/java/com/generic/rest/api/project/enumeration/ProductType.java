package com.generic.rest.api.project.enumeration;


public enum ProductType {
    UNKNOWN("Unknown"),
    COMMERCIAL_TAX_CERTIFICATE("Commercial Tax Certificate"),
    BUILDER_TAX_CERTIFICATE("Builder Tax Certificate"),
    TAX_CERTIFICATE("Tax Certificate"),
    TAX_REPORT("Tax Report"),
    COMMERCIAL_HOA_FULL("Commercial HOA Full"),
    BUILDER_HOA_FULL("Builder HOA Full"),
    COMMERCIAL_HOA_CONTACT("Commercial HOA Contact"),
    BUILDER_HOA_CONTACT("Builder HOA Contact"),
    HOA_FULL("HOA Full"),
    HOA_CONTACT("HOA Contact"),
    ROLLBACK_ESTIMATE("Roll Back Estimate"),
    NTP("Notice To Purchaser");

    private final String name;

    ProductType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
