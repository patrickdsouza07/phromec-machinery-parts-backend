package com.phromec.machinery.model;

import lombok.Getter;

@Getter
public enum RecordStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    RecordStatus(String value) {
        this.value = value;
    }
}