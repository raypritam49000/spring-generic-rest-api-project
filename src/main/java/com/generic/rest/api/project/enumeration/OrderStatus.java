package com.generic.rest.api.project.enumeration;

public enum OrderStatus {

    AWAITING_PARTNER("Awaiting Partner"),
    NEW("New"),
    AWAITING_LOCATE("Awaiting Locate"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    ISSUE("Issue"),
    CANCELED("Canceled"),
    DELIVERED("Delivered");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
