package com.phromec.machinery.dto.dashboard;

public record QuotationSummaryResponse(
        long totalQuotations,
        long pendingQuotations,
        long approvedQuotations,
        long rejectedQuotations
) {}
