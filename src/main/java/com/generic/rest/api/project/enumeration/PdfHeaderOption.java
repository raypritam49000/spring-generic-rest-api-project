package com.generic.rest.api.project.enumeration;

public enum PdfHeaderOption {

    TITLE("Title"),
    LOGO("Logo");

    private final String name;

    PdfHeaderOption(String name) { this.name = name; }

    public String getName() { return name; }
    public String toString() {
        return this.name;
    }

}
