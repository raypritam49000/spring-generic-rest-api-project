package com.generic.rest.api.project.enumeration;


public enum ProductStatus {

    NEW("New"),
    PENDING("Pending"),
    SAVED("Saved"),
    COMPLETED("Completed"),
    QC("QC"),
    MARKED("Marked"),
    CANCELED("Canceled"),
    DELIVERED("Delivered");


    private final String name;

    ProductStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

}
