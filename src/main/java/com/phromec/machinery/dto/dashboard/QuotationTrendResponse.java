package com.phromec.machinery.dto.dashboard;

public record QuotationTrendResponse(
        String month,
        String yearMonth,
        long quotationCount
) {}
