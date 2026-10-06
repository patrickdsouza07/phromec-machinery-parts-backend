package com.phromec.machinery.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RecordStatusConverter
        implements AttributeConverter<RecordStatus, String> {

    @Override
    public String convertToDatabaseColumn(
            RecordStatus status) {

        if (status == null) {
            return null;
        }

        return status.getValue();
    }

    @Override
    public RecordStatus convertToEntityAttribute(
            String value) {

        if (value == null) {
            return null;
        }

        for (RecordStatus status : RecordStatus.values()) {

            if (status.getValue().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Unknown record status: " + value
        );
    }
}