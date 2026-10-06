package com.phromec.machinery.model.quotation;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QuotationStatusConverter
        implements AttributeConverter<QuotationStatus, String> {

    @Override
    public String convertToDatabaseColumn(QuotationStatus status) {
        if (status == null) {
            return null;
        }

        return status.getValue();
    }

    @Override
    public QuotationStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        for (QuotationStatus status : QuotationStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Unknown quotation status: " + value
        );
    }
}