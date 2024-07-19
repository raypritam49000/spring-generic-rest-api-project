package com.generic.rest.api.project.enumeration;

public enum EmailNotificationMode {

    OFF("Off"),
    NOTIFICATION_ONLY("Notification Only"),
    NOTIFICATION_WITH_ATTACHMENTS("Notification with Attachments");

    private final String name;

    EmailNotificationMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

}
