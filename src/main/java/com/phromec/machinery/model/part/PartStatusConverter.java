package com.phromec.machinery.model.part;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PartStatusConverter
        implements AttributeConverter<PartStatus, String> {

    @Override
    public String convertToDatabaseColumn(PartStatus status) {
        return status != null ? status.getValue() : null;
    }

    @Override
    public PartStatus convertToEntityAttribute(String value) {

        if (value == null) {
            return null;
        }

        for (PartStatus status : PartStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Unknown part status: " + value
        );
    }
}