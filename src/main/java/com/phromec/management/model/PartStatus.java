package com.phromec.management.model;

public enum PartStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    PartStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}