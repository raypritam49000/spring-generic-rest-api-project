package com.generic.rest.api.project.enumeration;

public enum NotificationType {

    SCRAPER("Scrape"),
    BOND("Bond"),
    AVANTA("Avanta"),
    ASSIGNED("Assigned"),
    MAINTENANCE("Maintenance"),
    MESSAGE("Message");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }
}
