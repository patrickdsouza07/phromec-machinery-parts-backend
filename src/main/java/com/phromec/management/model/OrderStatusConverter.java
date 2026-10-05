package com.phromec.management.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class OrderStatusConverter
        implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {

        if (status == null) {
            return null;
        }

        return status.getDisplayName();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String value) {

        if (value == null) {
            return null;
        }

        return OrderStatus.fromValue(value);
    }
}