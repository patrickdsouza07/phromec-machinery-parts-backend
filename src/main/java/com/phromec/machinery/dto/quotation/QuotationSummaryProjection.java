package com.phromec.machinery.dto.quotation;

public interface QuotationSummaryProjection {

    Long getTotalQuotations();

    Long getDraft();

    Long getSent();

    Long getUnderReview();

    Long getApproved();

    Long getRejected();

    Long getExpired();
}