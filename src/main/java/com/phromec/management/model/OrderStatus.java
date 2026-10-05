package com.phromec.management.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {

    PENDING("Pending"),
    PROCESSING("Processing"),
    CONFIRMED("Confirmed"),
    IN_PRODUCTION("In Production"),
    SHIPPED("Shipped"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static OrderStatus fromValue(String value) {

        for (OrderStatus status : values()) {
            if (status.displayName.equalsIgnoreCase(value)
                    || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Invalid order status: " + value
        );
    }
}