package com.phromec.management.model;

import lombok.Getter;

@Getter
public enum QuotationStatus {

    DRAFT("Draft"),
    SENT("Sent"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    EXPIRED("Expired");

    private final String value;

    QuotationStatus(String value) {
        this.value = value;
    }

}