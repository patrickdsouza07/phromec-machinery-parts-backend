package com.phromec.machinery.dto.quotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationSummaryResponse {

    private Long totalQuotations;

    private Long draft;

    private Long sent;

    private Long underReview;

    private Long approved;

    private Long rejected;

    private Long expired;
}