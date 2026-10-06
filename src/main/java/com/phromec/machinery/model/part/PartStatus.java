package com.phromec.machinery.model.part;

import lombok.Getter;

@Getter
public enum PartStatus {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    PartStatus(String value) {
        this.value = value;
    }

}