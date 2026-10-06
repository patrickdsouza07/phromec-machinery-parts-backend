package com.phromec.machinery.dto.quotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationListResponse {

    private QuotationSummaryResponse summary;

    private List<QuotationResponse> quotations;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}