package com.phromec.machinery.service;

import com.phromec.machinery.dto.quotation.QuotationListResponse;
import com.phromec.machinery.dto.quotation.QuotationResponse;
import com.phromec.machinery.model.quotation.Quotation;
import com.phromec.machinery.model.quotation.QuotationStatus;

public interface QuotationService {

    QuotationListResponse getQuotations(
            int page,
            int size,
            String search,
            QuotationStatus status,
            String sortBy,
            String direction
    );

    QuotationResponse getQuotationById(
            Integer quotationId
    );

    Quotation createQuotation(
            Quotation quotation
    );

    Quotation updateQuotation(
            Integer quotationId,
            Quotation quotation
    );

    void deleteQuotation(
            Integer quotationId
    );
}